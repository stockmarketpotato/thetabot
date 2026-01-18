package com.stockmarketpotato.bot.task;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.stockmarketpotato.bot.BotConfiguration;
import com.stockmarketpotato.bot.strategy.BaseStrategy;
import com.stockmarketpotato.bot.strategy.ThetaEngine45DTE;
import com.stockmarketpotato.broker.ApiUtilities;
import com.stockmarketpotato.broker.AccountStreamer.AccountUpdateListener;
import com.stockmarketpotato.feeds.AggregatedQuote;
import com.stockmarketpotato.feeds.FeedUtilities;

import jakarta.json.JsonObject;
import com.stockmarketpotato.integration.tastytrade.AccountStreamerSocketClient;
import com.stockmarketpotato.integration.tastytrade.Orders;
import com.stockmarketpotato.integration.tastytrade.TastytradeStreamer;
import com.stockmarketpotato.integration.tastytrade.model.accounts.Account;
import com.stockmarketpotato.integration.tastytrade.model.accounts.AccountBalances;
import com.stockmarketpotato.integration.tastytrade.model.instruments.FuturesOptionChain;
import com.stockmarketpotato.integration.tastytrade.model.instruments.FuturesOptionChainsExpirationsStrikes;
import com.stockmarketpotato.integration.tastytrade.model.orders.ActionEnum;
import com.stockmarketpotato.integration.tastytrade.model.orders.InstrumentTypeEnum;
import com.stockmarketpotato.integration.tastytrade.model.orders.Order;
import com.stockmarketpotato.integration.tastytrade.model.orders.OrderTypeEnum;
import com.stockmarketpotato.integration.tastytrade.model.orders.PlacedOrderResponse;
import com.stockmarketpotato.integration.tastytrade.model.orders.PostAccountsAccountNumberOrders;
import com.stockmarketpotato.integration.tastytrade.model.orders.PostAccountsAccountNumberOrdersDryRunLegsInner;
import com.stockmarketpotato.integration.tastytrade.model.orders.PriceEffectEnum;
import com.stockmarketpotato.integration.tastytrade.model.orders.TimeInForceEnum;

/**
 * Task responsible for scanning the market and opening new positions.
 * Filters option chains, checks buying power, places opening orders, and immediately sets up closing orders (take profit).
 */
public class ThetaEngineOpenPositionTask extends BaseStrategyTimedTask {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private final BigDecimal TICK_SIZE = BigDecimal.valueOf(0.05);
	private AccountStreamerSocketClient accountStreamer;
	private WebSocket webSocket;
	private CountDownLatch sellToOpenFilled;
	private CountDownLatch buyToCloseLive;
	private Order filledSellToOpenOrder;

	/**
	 * Constructor for ThetaEngineOpenPositionTask.
	 * 
	 * @param baseStrategy The parent strategy.
	 * @param name The name of the task.
	 */
	public ThetaEngineOpenPositionTask(BaseStrategy baseStrategy, String name) {
		super(baseStrategy, name);
	}

	@Override
	protected boolean isExecutable(final BotConfiguration configuration) {
		if (!configuration.isOpenPositions())
			return false;
		String accountNumber = configuration.getAccountNumber();
		Account a = baseStrategy.getBrokerManager().getAccount(accountNumber);
		if (a == null) {
			log.error("Won't open new positions, account not found");
			return false;
		}
		if (a.is_closed) {
			log.error("Won't open new positions, account is closed");
			return false;
		}
		if (!a.is_futures_approved) {
			log.error("Won't open new positions, futures not approved in account");
			return false;
		}
		if (a.is_firm_error) {
			log.error("Won't open new positions, account has firm error.");
			return false;
		}
		if (a.margin_or_cash.equals("Cash")) {
			log.error("Won't open new positions, selected account is not a margin account");
			return false;
		}
		AccountBalances b = baseStrategy.getBrokerManager().getBalances(accountNumber);
		if (b.cash_balance < 10000.) {
			log.error("Won't open new positions, Balances below 10,000 USD.");
			return false;
		}
		if (configuration.getMaxBuyingPower().compareTo(BigDecimal.valueOf(b.maintenance_requirement / b.margin_equity)) < 0) {
			log.warn("Won't open new positions, insufficient buying power.");
			return false;
		}
		return true;
	}

	@Override
	protected void runStrategy(final BotConfiguration configuration) {
		boolean success = false;
		try {
			success = connectAccountStreamer(configuration.getAccountNumber());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Preconditions.checkNotNull(baseStrategy.getQuoteRepository(), "Invalid Quote Repository");
		String symbol = configuration.getUnderlying().getDisplayValue();
		symbol = symbol.replace("/", "");
		final String account = configuration.getAccountNumber();
		final Integer minDte = configuration.getOpenDte().intValue();
		final Integer maxDte = minDte + 7;
		final BigDecimal maxDelta = configuration.getMaxDelta();
		final BigDecimal minDelta = maxDelta.subtract(new BigDecimal("0.01")).max(new BigDecimal("0.00"));
		FuturesOptionChain chain = ((ThetaEngine45DTE) baseStrategy).getFilteredOptionChain(account, symbol, minDte, maxDte, minDelta,
				maxDelta);
		if (chain.getExpirations().size() == 0) {
			log.warn("Futures Options chain is empty.");
			return;
		}
		
		FuturesOptionChainsExpirationsStrikes strike = chain.getExpirations().get(0).getStrikes()
				.get(chain.getExpirations().get(0).getStrikes().size() - 1);
		final String putSymbol = strike.getPut();
		final String accountNumber = configuration.getAccountNumber();
		try {
			success = sellToOpen(putSymbol, accountNumber, configuration.getMaxBuyingPower());
		} catch (InterruptedException e) {
			e.printStackTrace();
			success = false;
		}
		if (success)
			try {
				success = takeProfit(putSymbol, accountNumber, configuration.getTakeProfit());
			} catch (InterruptedException e) {
				e.printStackTrace();
				success = false;
			}
		
		disconnectAccountStreamer();
	}

	private boolean connectAccountStreamer(final String accountNumber) throws InterruptedException {
		sellToOpenFilled = new CountDownLatch(1);
		buyToCloseLive = new CountDownLatch(1);
		accountStreamer = new AccountStreamerSocketClient(baseStrategy.getTokenManager().getSessionToken(),
				new ArrayList<String>() {
					{
						add(accountNumber);
					}
				});
		accountStreamer.addUserMessageSubscribeListener(new AccountUpdateListener() {
			private CountDownLatch sellToOpenFilled;
			private CountDownLatch buyToCloseLive;
			public AccountUpdateListener setLatches(CountDownLatch sellToOpenFilled, CountDownLatch buyToCloseLive) {
				this.sellToOpenFilled = sellToOpenFilled;
				this.buyToCloseLive = buyToCloseLive;
				return this;
			}
			@Override
			public void onUpdate(JsonObject object) {
				if (ApiUtilities.containsOrder(object)) {
					ObjectMapper objectMapper = ApiUtilities.createObjectMapper();
					try {
						Order order = objectMapper.readValue(object.getJsonObject("data").toString(), Order.class);
						if (order.getLegs().get(0).getAction().equals(
								ActionEnum.SELL_TO_OPEN.toString())
								&& order.getStatus().equals("Filled"))
							this.sellToOpenFilled.countDown();
							
						if (order.getLegs().get(0).getAction().equals(
								ActionEnum.BUY_TO_CLOSE.toString())
								&& order.getStatus().equals("Live"))
							this.buyToCloseLive.countDown();
					} catch (JsonProcessingException e) {
						log.error(e.getMessage());
					}
				}
			}
		}.setLatches(sellToOpenFilled, buyToCloseLive));
		this.webSocket = HttpClient.newHttpClient().newWebSocketBuilder()
				.buildAsync(URI.create(TastytradeStreamer.STREAMER.PRODUCTION.getURL()), this.accountStreamer)
				.join();
		this.accountStreamer.getConnected().await(30, TimeUnit.SECONDS);
		if (this.accountStreamer.getConnected().getCount() != 0) {
			disconnectAccountStreamer();
			return false;
		}
		this.accountStreamer.getUserMessageReady().await(30, TimeUnit.SECONDS);
		return true;
	}

	private void disconnectAccountStreamer() {
		this.webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "");
		this.accountStreamer = null;
	}

	private boolean sellToOpen(final String symbol, final String accountNumber, final BigDecimal maxBuyingPower) throws InterruptedException {
		Preconditions.checkNotNull(symbol);
		Optional<AggregatedQuote> q = baseStrategy.getQuoteRepository().findById(symbol);
		if (q.isEmpty()) {
			log.warn("No Quote for " + symbol + " found. Abort.");
			return false;
		}
		
		boolean good = FeedUtilities.notOlderThanSeconds(q.get().getBidTime(), 20);
		if (!good) {
			log.error("Bid Quote is too old. " + q.get().getBidTime().toString());
			return false;
		}
		/**
		 * Sell To Open Limit
		 */
		PostAccountsAccountNumberOrders sellToOpen = new PostAccountsAccountNumberOrders()
				.timeInForce(TimeInForceEnum.DAY)
				.orderType(OrderTypeEnum.LIMIT)
				.price(q.get().getBidPrice().doubleValue())
				.priceEffect(PriceEffectEnum.CREDIT)
				.source("ThetaEngine45DTE/ThetaEngineOpenPositionTask");

		PostAccountsAccountNumberOrdersDryRunLegsInner stoLeg = new PostAccountsAccountNumberOrdersDryRunLegsInner()
				.instrumentType(InstrumentTypeEnum.FUTURE_OPTION)
				.symbol(symbol).quantity(1.)
				.action(ActionEnum.SELL_TO_OPEN);
		sellToOpen.addLegsItem(stoLeg);

		// Post Order
		Orders o = new Orders();
		PlacedOrderResponse orderResponse = o.postOrderDryRun(accountNumber,
				baseStrategy.getTokenManager().getSessionToken(), sellToOpen);
		good = o.isGoodResponse(orderResponse);
		if (!good) {
			log.info("Failed Order was: " + sellToOpen.toString());
			return false;
		}
		
		if (!sufficientBuyingPower(orderResponse, accountNumber, maxBuyingPower))
			return false;

		// valid assertion means: go on.
		orderResponse = o.postOrder(accountNumber, baseStrategy.getTokenManager().getSessionToken(), sellToOpen);
		log.info("PLACE ORDER: Sell-To-Open => " + orderResponse.toString());
		good = o.isGoodResponse(orderResponse);
		if (!good) {
			log.info("Failed Order was: " + sellToOpen.toString());
			return false;
		}

		log.info("Wait for Sell-To-Open Order to be filled.");
		boolean awaitSuccess = sellToOpenFilled.await(30, TimeUnit.SECONDS);
		if (!awaitSuccess) {
			log.error("Fill for Sell-To-Open timed out.");
			cancelOrder(orderResponse.getOrder().getId(), accountNumber);
			return false;
		}	
		log.info("Sell to Open filled.");
		this.filledSellToOpenOrder = o.getOrdersId(accountNumber, baseStrategy.getTokenManager().getSessionToken(), orderResponse.getOrder().getId());
		return true;
	}

	private void cancelOrder(final String id, final String accountNumber) {
		Orders o = new Orders();
		Order ox = o.getOrdersId(accountNumber, baseStrategy.getTokenManager().getSessionToken(), id);
		if (ox.getCancellable() && (ox.getStatus().equals("Received") || ox.getStatus().equals("Routed")
				|| ox.getStatus().equals("In Flight") || ox.getStatus().equals("Live"))) {
			log.info("Cancel #" + ox.getId());
			Order cancelledOrder = o.deleteOrder(accountNumber,
					baseStrategy.getTokenManager().getSessionToken(), id);
			if (cancelledOrder != null && (cancelledOrder.getStatus().equals("Cancel Requested")
					|| cancelledOrder.getStatus().equals("Cancelled")) && !cancelledOrder.getCancellable())
				log.info("Order successfully cancelled.");
			else
				log.error("Delete Order failed");
		}
	}

	private boolean sufficientBuyingPower(final PlacedOrderResponse orderResponse, final String accountNumber, final BigDecimal maxBuyingPower) {
		// 1. - (orderResponse.getBuyingPowerEffect().getNewBuyingPower() / b.margin_equity)
		AccountBalances b = baseStrategy.getBrokerManager().getBalances(accountNumber);
		if (maxBuyingPower.compareTo(BigDecimal
				.valueOf((b.maintenance_requirement + orderResponse.getBuyingPowerEffect().getChangeInBuyingPower())
						/ b.margin_equity)) < 0) {
			log.warn("insufficient buying power");
			return false;
		}
		return true;
	}

	private boolean takeProfit(final String symbol, final String accountNumber, final BigDecimal takeProfit) throws InterruptedException {
		/**
		 * Buy To Close Limit (Profit Taker)
		 */
		Double filledAt = filledSellToOpenOrder.getLegs().get(0).getFills().get(0).getFillPrice();
		BigDecimal takeProfitPrice = ApiUtilities.roundToTickSize(TICK_SIZE, BigDecimal.valueOf(filledAt).multiply(BigDecimal.valueOf(1.0).subtract(takeProfit)));
		
		PostAccountsAccountNumberOrders buyToCloseOrder = new PostAccountsAccountNumberOrders()
				.timeInForce(TimeInForceEnum.GTC)
				.orderType(OrderTypeEnum.LIMIT)
				.price(takeProfitPrice.doubleValue()) // 60% profit
				.priceEffect(PriceEffectEnum.DEBIT);

		PostAccountsAccountNumberOrdersDryRunLegsInner btcLeg = new PostAccountsAccountNumberOrdersDryRunLegsInner()
				.instrumentType(InstrumentTypeEnum.FUTURE_OPTION)
				.symbol(symbol).quantity(1.)
				.action(ActionEnum.BUY_TO_CLOSE);
		buyToCloseOrder.addLegsItem(btcLeg);

		Orders o = new Orders();
		PlacedOrderResponse orderResponse = o.postOrderDryRun(accountNumber, baseStrategy.getTokenManager().getSessionToken(),
				buyToCloseOrder);
		boolean good = o.isGoodResponse(orderResponse);
		if (!good) {
			log.info("Failed Order was:" + buyToCloseOrder.toString());
			return false;
		}

		orderResponse = o.postOrder(accountNumber, baseStrategy.getTokenManager().getSessionToken(), buyToCloseOrder);
		log.info("PLACE ORDER: Buy-To-Close => " + orderResponse.toString());
		good = o.isGoodResponse(orderResponse);
		if (!good) {
			log.info("Failed Order was:" + buyToCloseOrder.toString());
			return false;
		}
		log.info("Wait for Buy-to-Close Order to be LIVE.");
		boolean awaitSuccess = buyToCloseLive.await(30, TimeUnit.SECONDS);
		if (!awaitSuccess) {
			log.error("Live for Buy-to-Close timed out.");
			cancelOrder(orderResponse.getOrder().getId(), accountNumber);
			return false;
		}	
		log.info("Buy-to-Close is Live.");
		o.getOrdersId(accountNumber, baseStrategy.getTokenManager().getSessionToken(), orderResponse.getOrder().getId());
		return true;

	}
}
