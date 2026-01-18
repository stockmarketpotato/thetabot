package com.stockmarketpotato.integration.tastytrade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockmarketpotato.broker.AccountStreamer.AccountUpdateListener;
import com.stockmarketpotato.broker.ApiUtilities;
import com.stockmarketpotato.broker.SessionManager;
import com.stockmarketpotato.broker.TastytradeSettings;
import com.stockmarketpotato.broker.TastytradeSettingsRepository;
import com.stockmarketpotato.integration.tastytrade.model.orders.ActionEnum;
import com.stockmarketpotato.integration.tastytrade.model.orders.InstrumentTypeEnum;
import com.stockmarketpotato.integration.tastytrade.model.orders.Order;
import com.stockmarketpotato.integration.tastytrade.model.orders.OrderTypeEnum;
import com.stockmarketpotato.integration.tastytrade.model.orders.OrdersFactory;
import com.stockmarketpotato.integration.tastytrade.model.orders.PlacedOrderResponse;
import com.stockmarketpotato.integration.tastytrade.model.orders.PostAccountsAccountNumberOrders;
import com.stockmarketpotato.integration.tastytrade.model.orders.PostAccountsAccountNumberOrdersDryRunLegsInner;
import com.stockmarketpotato.integration.tastytrade.model.orders.PriceEffectEnum;
import com.stockmarketpotato.integration.tastytrade.model.orders.PutAccountsAccountNumberOrdersId;
import com.stockmarketpotato.integration.tastytrade.model.orders.TimeInForceEnum;

import jakarta.json.JsonObject;

@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan(basePackages = { "com.stockmarketpotato.broker", "com.stockmarketpotato.feeds.dxfeed" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class OrdersApiTest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final Double LIMIT_PRICE_FILL = 2.49;
	private static final Double LIMIT_PRICE_NO_FILL = 3.01;

	@Autowired
	private SessionManager tokenManager;

	@Autowired
	private TastytradeSettingsRepository tastytradeSettings;

	@Value("${tastytrade.api.prod.login}")
	private String prodApiLogin;
	@Value("${tastytrade.api.prod.password}")
	private String prodApiPassword;

	@Value("${tastytrade.api.cert01.login}")
//  @Value("${tastytrade.api.cert02.login}")
//  @Value("${tastytrade.api.cert03.login}")
	private String certApiLogin;

	@Value("${tastytrade.api.cert01.password}")
//  @Value("${tastytrade.api.cert02.password}")
//  @Value("${tastytrade.api.cert03.password}")
	private String certApiPassword;

	@Value("${tastytrade.api.cert01.account}")
//  @Value("${tastytrade.api.cert02.account}")
//  @Value("${tastytrade.api.cert03.account}")
	private String CERT_ACCOUNT;

	private CountDownLatch sellToOpenFilled;
	private CountDownLatch buyToCloseFilled;
	private CountDownLatch filled;

	@BeforeEach
	void saveTastytradeSetting() {
		logger.info("Save TastytradeSettings");
		TastytradeSettings s = new TastytradeSettings();
		s.setApiLogin(prodApiLogin);
		s.setApiPassword(prodApiPassword);
		s.setCertApiLogin(certApiLogin);
		s.setCertApiPassword(certApiPassword);
		tastytradeSettings.save(s);
		logger.info("Check not null");
		assertThat(tastytradeSettings).isNotNull();
		assertThat(tokenManager).isNotNull();

		logger.info("Connect to Account Streamer in Certification Environment");
		assertThat(TastytradeSettingsRepository.certIsInvalid(tastytradeSettings)).isNotEqualTo(true);
	}

	/**
	 * Cancel all cancellable orders that are Received, Routed, In Flight, or Live
	 */
	@Test
	void test01cancelAllOrdersInCertAccount() {
		String format = "%-10s | %-10s | %-10s | %-10s | %-10s | %-10s | %-10s | %-10s";
		logger.info(" ## LIVE ORDERS ## ");
		logger.info("#    |  timeInForce |  orderType |  size |  price |  Symbol |  Status |  Stop");
		Orders cert = new Orders(TastytradeApi.BASE.CERTIFICATION);
		for (Order ox : cert.getOrdersLive(CERT_ACCOUNT, tokenManager.getCertSessionToken())) {
			logger.info(String.format(format, ox.getId(), ox.getTimeInForce(), ox.getOrderType(), ox.getSize(),
					ox.getPrice(), ox.getUnderlyingSymbol(), ox.getStatus(), ox.getStopTrigger()));

			if (ox.getCancellable() && (ox.getStatus().equals("Received") || ox.getStatus().equals("Routed")
					|| ox.getStatus().equals("In Flight") || ox.getStatus().equals("Live"))) {
				logger.info("Cancel #" + ox.getId());
				Order order = cert.deleteOrder(CERT_ACCOUNT, tokenManager.getCertSessionToken(), ox.getId());
				assertThat(order.getStatus().equals("Cancel Requested") || order.getStatus().equals("Cancelled"));
				assertThat(order.getCancellable()).isFalse();
			}
		}
	}

	@Test
	void test02basicCertGetOrdersId() {
		Orders cert = new Orders(TastytradeApi.BASE.CERTIFICATION);
		for (Order ox : cert.getOrdersLive(CERT_ACCOUNT, tokenManager.getCertSessionToken())) {
			Order oy = cert.getOrdersId(CERT_ACCOUNT, tokenManager.getCertSessionToken(), ox.getId());
			assertThat(ox).isEqualTo(oy);
			break;
		}
	}

	@Test
	void test03basicCertPostOrder() throws Exception {
		final String sessionToken = tokenManager.getCertSessionToken();
		assertThat(sessionToken).isNotBlank();

		filled = new CountDownLatch(1);
		AccountStreamerSocketClient certAccountStreamer = new AccountStreamerSocketClient(
				tokenManager.getCertSessionToken(), new ArrayList<String>() {
					{
						add(CERT_ACCOUNT);
					}
				});
		certAccountStreamer.addUserMessageSubscribeListener(new AccountUpdateListener() {
			CountDownLatch filled = new CountDownLatch(1);

			public AccountUpdateListener setFilled(CountDownLatch L) {
				this.filled = L;
				return this;
			}

			@Override
			public void onUpdate(JsonObject object) {
				if (object.containsKey("type") && object.getString("type").equals("Order")) {
					ObjectMapper objectMapper = ApiUtilities.createObjectMapper();
					try {
						Order order = objectMapper.readValue(object.getJsonObject("data").toString(), Order.class);
						logger.info(order.getOrderType() + " Order #" + order.getId() + " " + order.getStatus());
						if (order.getLegs().get(0).getAction().equals(ActionEnum.BUY_TO_OPEN.toString())
								&& order.getStatus().equals("Filled"))
//                          wrapper.filled.countDown();
							filled.countDown();
					} catch (JsonProcessingException e) {
						logger.error(e.getMessage());
						logger.error(e.getStackTrace().toString());
					}
				} else
					logger.info("User Message Listener: " + object.toString());
			}
		}.setFilled(filled));

		WebSocket certWebSocket = HttpClient.newHttpClient().newWebSocketBuilder()
				.buildAsync(URI.create(TastytradeStreamer.STREAMER.CERTIFICATION.getURL()), certAccountStreamer).join();
		certAccountStreamer.getConnected().await(30, TimeUnit.SECONDS);
		assertThat(certAccountStreamer.getConnected().getCount()).isEqualTo(0);
		certAccountStreamer.getUserMessageReady().await(30, TimeUnit.SECONDS);

		// Build Dry Run Limit Order
		PostAccountsAccountNumberOrders testOrder = new PostAccountsAccountNumberOrders()
				.timeInForce(TimeInForceEnum.DAY).orderType(OrderTypeEnum.LIMIT).price(LIMIT_PRICE_FILL)
				.priceEffect(PriceEffectEnum.DEBIT).source("Thetabot Test");

		PostAccountsAccountNumberOrdersDryRunLegsInner leg = new PostAccountsAccountNumberOrdersDryRunLegsInner()
				.instrumentType(InstrumentTypeEnum.EQUITY).symbol("AAPL").quantity(1.).action(ActionEnum.BUY_TO_OPEN);
		testOrder.addLegsItem(leg);

		Orders cert = new Orders(TastytradeApi.BASE.CERTIFICATION);
		// Post Order Dry Run
		logger.info("Place DryRun Order on Certification Environment");
		PlacedOrderResponse postOrderResponse = cert.postOrderDryRun(CERT_ACCOUNT, tokenManager.getCertSessionToken(),
				testOrder);
		assertThat(postOrderResponse).isNotNull();
		assertThat(postOrderResponse.getErrors()).isNull();

		// Post Order Live
		logger.info("Place Limit Order on Certification Environment");
		postOrderResponse = null;
		postOrderResponse = cert.postOrder(CERT_ACCOUNT, tokenManager.getCertSessionToken(), testOrder);
		assertThat(postOrderResponse).isNotNull();
		assertThat(postOrderResponse.getErrors()).isNull();
		assertThat(postOrderResponse.getOrder()).isNotNull();
		assertThat(postOrderResponse.getOrder().getAccountNumber().equals(CERT_ACCOUNT)).isTrue();
		assertThat(postOrderResponse.getOrder().getPrice().equals(LIMIT_PRICE_FILL)).isTrue();
		assertThat(cert.getOrdersId(CERT_ACCOUNT, tokenManager.getCertSessionToken(),
				postOrderResponse.getOrder().getId()));

		logger.info("Wait for Buy-to-Open Order to be filled.");
		boolean awaitSuccess = this.filled.await(30, TimeUnit.SECONDS);
		if (!awaitSuccess)
			logger.info(cert
					.getOrdersId(CERT_ACCOUNT, tokenManager.getCertSessionToken(), postOrderResponse.getOrder().getId())
					.getStatus());
		assertTrue(awaitSuccess);
		logger.info("Buy-to-Open filled.");

		certWebSocket.sendClose(WebSocket.NORMAL_CLOSURE, "");
	}

	/**
	 * BTO Place STC that stays Live Replace STC that fills
	 * 
	 * @throws InterruptedException
	 */
	@Test
	void test04basicReplaceSellToClose() throws InterruptedException {
		final String stock = "AAPL";

		var socketResponseWrapper = new Object() {
			CountDownLatch buyToOpenFilled = new CountDownLatch(1);
			CountDownLatch sellToCloseLive = new CountDownLatch(1);
			CountDownLatch sellToCloseFilled = new CountDownLatch(1);
		};
		AccountStreamerSocketClient certAccountStreamer = new AccountStreamerSocketClient(
				tokenManager.getCertSessionToken(), new ArrayList<String>() {
					{
						add(CERT_ACCOUNT);
					}
				});
		certAccountStreamer.addUserMessageSubscribeListener(new AccountUpdateListener() {
			@Override
			public void onUpdate(JsonObject object) {
				if (object.containsKey("type") && object.getString("type").equals("Order")) {
					ObjectMapper objectMapper = ApiUtilities.createObjectMapper();
					try {
						Order order = objectMapper.readValue(object.getJsonObject("data").toString(), Order.class);
						logger.info(order.getOrderType() + " Order #" + order.getId() + " " + order.getStatus());
						if (order.getLegs().get(0).getAction().equals(ActionEnum.BUY_TO_OPEN.toString())
								&& order.getStatus().equals("Filled"))
							socketResponseWrapper.buyToOpenFilled.countDown();
						if (order.getLegs().get(0).getAction().equals(ActionEnum.SELL_TO_CLOSE.toString())
								&& order.getStatus().equals("Live"))
							socketResponseWrapper.sellToCloseLive.countDown();
						if (order.getLegs().get(0).getAction().equals(ActionEnum.SELL_TO_CLOSE.toString())
								&& order.getStatus().equals("Filled"))
							socketResponseWrapper.sellToCloseFilled.countDown();
					} catch (JsonProcessingException e) {
						logger.error(e.getMessage());
						logger.error(e.getStackTrace().toString());
					}
				} else
					logger.info("User Message Listener: " + object.toString());
			}
		});

		WebSocket certWebSocket = HttpClient.newHttpClient().newWebSocketBuilder()
				.buildAsync(URI.create(TastytradeStreamer.STREAMER.CERTIFICATION.getURL()), certAccountStreamer).join();
		certAccountStreamer.getConnected().await(30, TimeUnit.SECONDS);
		assertThat(certAccountStreamer.getConnected().getCount()).isEqualTo(0);
		certAccountStreamer.getUserMessageReady().await(30, TimeUnit.SECONDS);

		// ================================================================================
		// Sell To Open Limit
		// ================================================================================
		PostAccountsAccountNumberOrders buyToOpen = new PostAccountsAccountNumberOrders()
				.timeInForce(TimeInForceEnum.DAY).orderType(OrderTypeEnum.LIMIT).price(LIMIT_PRICE_FILL)
				.priceEffect(PriceEffectEnum.DEBIT);

		PostAccountsAccountNumberOrdersDryRunLegsInner btoLeg = new PostAccountsAccountNumberOrdersDryRunLegsInner()
				.instrumentType(InstrumentTypeEnum.EQUITY).symbol(stock).quantity(1.).action(ActionEnum.BUY_TO_OPEN);
		buyToOpen.addLegsItem(btoLeg);

		Orders cert = new Orders(TastytradeApi.BASE.CERTIFICATION);
		// Post Order Dry
		logger.info("Post Order Dry #1");
		PlacedOrderResponse orderResponse = cert.postOrderDryRun(CERT_ACCOUNT, tokenManager.getCertSessionToken(),
				buyToOpen);
		logger.info(orderResponse.toString());
		assertThat(orderResponse).isNotNull();
		assertThat(orderResponse.getErrors()).isNull();

		// Post Order
		logger.info("Post Order #1");
		orderResponse = cert.postOrder(CERT_ACCOUNT, tokenManager.getCertSessionToken(), buyToOpen);
		logger.info(orderResponse.toString());
		assertThat(orderResponse).isNotNull();
		assertThat(orderResponse.getErrors()).isNull();

		logger.info("Wait for Buy-to-Open Order to be filled.");
		boolean awaitSuccess = socketResponseWrapper.buyToOpenFilled.await(30, TimeUnit.SECONDS);
		assertTrue(awaitSuccess);
		logger.info("Buy-to-Open filled.");

		// ================================================================================
		// Buy To Close Limit (Profit Taker) that will never fill
		// ================================================================================
		PostAccountsAccountNumberOrders sellToCloseOrder = new PostAccountsAccountNumberOrders()
				.timeInForce(TimeInForceEnum.GTC).orderType(OrderTypeEnum.LIMIT).price(LIMIT_PRICE_NO_FILL) // profit
																											// target
																											// irrelevant
																											// here
				.priceEffect(PriceEffectEnum.CREDIT);

		PostAccountsAccountNumberOrdersDryRunLegsInner stcLeg = new PostAccountsAccountNumberOrdersDryRunLegsInner()
				.instrumentType(InstrumentTypeEnum.EQUITY).symbol(stock).quantity(1.).action(ActionEnum.SELL_TO_CLOSE);
		sellToCloseOrder.addLegsItem(stcLeg);

		// Dry Run Order
		logger.info("Post Order Dry #2");
		orderResponse = cert.postOrderDryRun(CERT_ACCOUNT, tokenManager.getCertSessionToken(), sellToCloseOrder);
		assertThat(orderResponse).isNotNull();
		assertThat(orderResponse.getErrors()).isNull();

		// Place Order
		logger.info("Post Order #2");
		orderResponse = cert.postOrder(CERT_ACCOUNT, tokenManager.getCertSessionToken(), sellToCloseOrder);
		assertThat(orderResponse).isNotNull();
		assertThat(orderResponse.getErrors()).isNull();

		// wait order live
		logger.info("Wait for Sell-to-Close Order to be live.");
		awaitSuccess = socketResponseWrapper.sellToCloseLive.await(30, TimeUnit.SECONDS);
		assertTrue(awaitSuccess);
		logger.info("Sell-to-Close live.");

		// ================================================================================
		// Replace Order
		// ================================================================================
		String id = orderResponse.getOrder().getId();
		logger.info("Replace Sell-to-Close Order");

		Order liveSellToCloseOrder = cert.getOrdersId(CERT_ACCOUNT, tokenManager.getCertSessionToken(), id);
		PostAccountsAccountNumberOrders updatedSellToCloseOrderDryRun = OrdersFactory
				.createPostAccountsAccountNumberOrders(liveSellToCloseOrder);
		updatedSellToCloseOrderDryRun.setPrice(LIMIT_PRICE_FILL);

		logger.info("Post Order Dry #3");
		orderResponse = cert.postOrdersIdDryRun(CERT_ACCOUNT, tokenManager.getCertSessionToken(), id,
				updatedSellToCloseOrderDryRun);
		assertThat(orderResponse).isNotNull();
		assertThat(orderResponse.getErrors()).isNull();
		logger.info(orderResponse.toString());

		PutAccountsAccountNumberOrdersId updatedSellToCloseOrder = OrdersFactory
				.createPutAccountsAccountNumberOrdersId(updatedSellToCloseOrderDryRun);
		logger.info("Put Order #3");
		cert.putOrdersId(CERT_ACCOUNT, tokenManager.getCertSessionToken(), id, updatedSellToCloseOrder);
		assertThat(orderResponse).isNotNull();
		assertThat(orderResponse.getErrors()).isNull();
		logger.info(orderResponse.toString());

		// wait order filled
		logger.info("Wait for Sell-to-Close Order to be filled.");
		awaitSuccess = socketResponseWrapper.sellToCloseFilled.await(30, TimeUnit.SECONDS);
		assertTrue(awaitSuccess);
		logger.info("Replaced Sell-to-Close filled.");

		certWebSocket.sendClose(WebSocket.NORMAL_CLOSURE, "");
	}

	/**
	 * BTO Place STC that stays Live Replace STC that fills
	 * 
	 * @throws InterruptedException
	 */
	@Test
	void test04a_basicReplaceSellToClose_usesPolling() throws InterruptedException {
		final String stock = "AAPL";

		// ================================================================================
		// Sell To Open Limit
		// ================================================================================
		PostAccountsAccountNumberOrders buyToOpen = new PostAccountsAccountNumberOrders()
				.timeInForce(TimeInForceEnum.DAY).orderType(OrderTypeEnum.LIMIT).price(LIMIT_PRICE_FILL)
				.priceEffect(PriceEffectEnum.DEBIT);

		PostAccountsAccountNumberOrdersDryRunLegsInner btoLeg = new PostAccountsAccountNumberOrdersDryRunLegsInner()
				.instrumentType(InstrumentTypeEnum.EQUITY).symbol(stock).quantity(1.).action(ActionEnum.BUY_TO_OPEN);
		buyToOpen.addLegsItem(btoLeg);

		Orders cert = new Orders(TastytradeApi.BASE.CERTIFICATION);
		// Post Order Dry
		logger.info("Post Order Dry #1");
		PlacedOrderResponse orderResponse = cert.postOrderDryRun(CERT_ACCOUNT, tokenManager.getCertSessionToken(),
				buyToOpen);
		logger.info(orderResponse.toString());
		assertThat(orderResponse).isNotNull();
		assertThat(orderResponse.getErrors()).isNull();

		// Post Order
		logger.info("Post Order #1");
		orderResponse = cert.postOrder(CERT_ACCOUNT, tokenManager.getCertSessionToken(), buyToOpen);
		logger.info(orderResponse.toString());
		assertThat(orderResponse).isNotNull();
		assertThat(orderResponse.getErrors()).isNull();

		logger.info("Wait for Buy-to-Open Order to be filled.");
		Order awaitOrderLive = cert.getOrdersId(CERT_ACCOUNT, tokenManager.getCertSessionToken(),
				orderResponse.getOrder().getId());
		do {
			awaitOrderLive = cert.getOrdersId(CERT_ACCOUNT, tokenManager.getCertSessionToken(),
					orderResponse.getOrder().getId());
		} while (!awaitOrderLive.getSize().equals("Filled"));
		logger.info("Buy-to-Open filled.");

		// ================================================================================
		// Buy To Close Limit (Profit Taker) that will never fill
		// ================================================================================
		PostAccountsAccountNumberOrders sellToCloseOrder = new PostAccountsAccountNumberOrders()
				.timeInForce(TimeInForceEnum.GTC).orderType(OrderTypeEnum.LIMIT).price(LIMIT_PRICE_NO_FILL) // profit
																											// target
																											// irrelevant
																											// here
				.priceEffect(PriceEffectEnum.CREDIT);

		PostAccountsAccountNumberOrdersDryRunLegsInner stcLeg = new PostAccountsAccountNumberOrdersDryRunLegsInner()
				.instrumentType(InstrumentTypeEnum.EQUITY).symbol(stock).quantity(1.).action(ActionEnum.SELL_TO_CLOSE);
		sellToCloseOrder.addLegsItem(stcLeg);

		// Dry Run Order
		logger.info("Post Order Dry #2");
		orderResponse = cert.postOrderDryRun(CERT_ACCOUNT, tokenManager.getCertSessionToken(), sellToCloseOrder);
		assertThat(orderResponse).isNotNull();
		assertThat(orderResponse.getErrors()).isNull();

		// Place Order
		logger.info("Post Order #2");
		orderResponse = cert.postOrder(CERT_ACCOUNT, tokenManager.getCertSessionToken(), sellToCloseOrder);
		assertThat(orderResponse).isNotNull();
		assertThat(orderResponse.getErrors()).isNull();

		// wait order live
		logger.info("Wait for Sell-to-Close Order to be live.");
		do {
			awaitOrderLive = cert.getOrdersId(CERT_ACCOUNT, tokenManager.getCertSessionToken(),
					orderResponse.getOrder().getId());
		} while (!awaitOrderLive.getSize().equals("Filled"));

		logger.info("Sell-to-Close live.");

		// ================================================================================
		// Replace Order
		// ================================================================================
		String id = orderResponse.getOrder().getId();
		logger.info("Replace Sell-to-Close Order");

		Order liveSellToCloseOrder = cert.getOrdersId(CERT_ACCOUNT, tokenManager.getCertSessionToken(), id);
		PostAccountsAccountNumberOrders updatedSellToCloseOrderDryRun = OrdersFactory
				.createPostAccountsAccountNumberOrders(liveSellToCloseOrder);
		updatedSellToCloseOrderDryRun.setPrice(LIMIT_PRICE_FILL);

		logger.info("Post Order Dry #3");
		orderResponse = cert.postOrdersIdDryRun(CERT_ACCOUNT, tokenManager.getCertSessionToken(), id,
				updatedSellToCloseOrderDryRun);
		assertThat(orderResponse).isNotNull();
		assertThat(orderResponse.getErrors()).isNull();
		logger.info(orderResponse.toString());

		PutAccountsAccountNumberOrdersId updatedSellToCloseOrder = OrdersFactory
				.createPutAccountsAccountNumberOrdersId(updatedSellToCloseOrderDryRun);
		logger.info("Put Order #3");
		cert.putOrdersId(CERT_ACCOUNT, tokenManager.getCertSessionToken(), id, updatedSellToCloseOrder);
		assertThat(orderResponse).isNotNull();
		assertThat(orderResponse.getErrors()).isNull();
		logger.info(orderResponse.toString());

		// wait order filled
		logger.info("Wait for Sell-to-Close Order to be filled.");
		do {
			awaitOrderLive = cert.getOrdersId(CERT_ACCOUNT, tokenManager.getCertSessionToken(),
					orderResponse.getOrder().getId());
		} while (!awaitOrderLive.getSize().equals("Filled"));
		logger.info("Replaced Sell-to-Close filled.");

	}

	/**
	 * Test: This test case closes an open short position. There is a BTC order live
	 * that is replaced to force immediate fill and close. This approach is used
	 * when - the max DTE for the Trade has been reached and it must be closed on
	 * that day - the trade broke through the Stop Loss and must be closed
	 * immediately (Ask Price) Close DTE: Wenn maximales Alter des Trades erreicht
	 * ist, dann für aktuellen Preis (Ask?) zurück kaufen, also Update Buy To Close
	 * Limit order mit neuem Limit
	 * 
	 * @throws InterruptedException
	 */
	@Test
	void test05thetaTradeReplaceBuyToClose() throws InterruptedException {
		final String put = "AAPL  261218P00150000";

		var socketResponseWrapper = new Object() {
			CountDownLatch sellToOpenFilled = new CountDownLatch(1);
			CountDownLatch buyToCloseLive = new CountDownLatch(1);
			CountDownLatch buyToCloseFilled = new CountDownLatch(1);
		};
		AccountStreamerSocketClient certAccountStreamer = new AccountStreamerSocketClient(
				tokenManager.getCertSessionToken(), new ArrayList<String>() {
					{
						add(CERT_ACCOUNT);
					}
				});
		certAccountStreamer.addUserMessageSubscribeListener(new AccountUpdateListener() {
			@Override
			public void onUpdate(JsonObject object) {
				if (object.containsKey("type") && object.getString("type").equals("Order")) {
					ObjectMapper objectMapper = ApiUtilities.createObjectMapper();
					try {
						Order order = objectMapper.readValue(object.getJsonObject("data").toString(), Order.class);
						logger.info(order.getOrderType() + " Order #" + order.getId() + " " + order.getStatus());
						if (order.getLegs().get(0).getAction().equals(ActionEnum.SELL_TO_OPEN.toString())
								&& order.getStatus().equals("Filled"))
							socketResponseWrapper.sellToOpenFilled.countDown();
						if (order.getLegs().get(0).getAction().equals(ActionEnum.BUY_TO_CLOSE.toString())
								&& order.getStatus().equals("Filled"))
							socketResponseWrapper.buyToCloseFilled.countDown();
						if (order.getLegs().get(0).getAction().equals(ActionEnum.BUY_TO_CLOSE.toString())
								&& order.getStatus().equals("Live"))
							socketResponseWrapper.buyToCloseLive.countDown();

					} catch (JsonProcessingException e) {
						logger.error(e.getMessage());
						logger.error(e.getStackTrace().toString());
					}
				} else
					logger.info("User Message Listener: " + object.toString());
			}
		});

		WebSocket certWebSocket = HttpClient.newHttpClient().newWebSocketBuilder()
				.buildAsync(URI.create(TastytradeStreamer.STREAMER.CERTIFICATION.getURL()), certAccountStreamer).join();
		certAccountStreamer.getConnected().await(30, TimeUnit.SECONDS);
		assertThat(certAccountStreamer.getConnected().getCount()).isEqualTo(0);
		certAccountStreamer.getUserMessageReady().await(30, TimeUnit.SECONDS);

		// ================================================================================
		// Sell To Open Limit
		// ================================================================================
		PostAccountsAccountNumberOrders sellToOpen = new PostAccountsAccountNumberOrders();
		sellToOpen.setTimeInForce(TimeInForceEnum.DAY);
		sellToOpen.setOrderType(OrderTypeEnum.LIMIT);
		sellToOpen.setPrice(LIMIT_PRICE_FILL);
		sellToOpen.setPriceEffect(PriceEffectEnum.CREDIT);

		PostAccountsAccountNumberOrdersDryRunLegsInner stoLeg = new PostAccountsAccountNumberOrdersDryRunLegsInner();
		stoLeg.setInstrumentType(InstrumentTypeEnum.EQUITY_OPTION);
		stoLeg.symbol(put);
		stoLeg.quantity(1.);
		stoLeg.action(ActionEnum.SELL_TO_OPEN);
		sellToOpen.addLegsItem(stoLeg);

		Orders cert = new Orders(TastytradeApi.BASE.CERTIFICATION);
		// Post Order
		PlacedOrderResponse orderResponse = cert.postOrderDryRun(CERT_ACCOUNT, tokenManager.getCertSessionToken(),
				sellToOpen);
		logger.info(orderResponse.toString());
		assertThat(orderResponse).isNotNull();
		assertThat(orderResponse.getErrors()).isNull();

		logger.info("Wait for Sell-to-Open Order to be filled.");
		boolean awaitSuccess = socketResponseWrapper.sellToOpenFilled.await(30, TimeUnit.SECONDS);
		assertTrue(awaitSuccess);
		logger.info("Sell-to-Open filled.");

		// ================================================================================
		// Buy To Close Limit (Profit Taker)
		// ================================================================================
		PostAccountsAccountNumberOrders buyToCloseOrder = new PostAccountsAccountNumberOrders();
		buyToCloseOrder.setTimeInForce(TimeInForceEnum.GTC);
		buyToCloseOrder.setOrderType(OrderTypeEnum.LIMIT);
		buyToCloseOrder.setPrice(LIMIT_PRICE_NO_FILL); // profit target irrelevant here
		buyToCloseOrder.setPriceEffect(PriceEffectEnum.DEBIT);

		PostAccountsAccountNumberOrdersDryRunLegsInner btcLeg = new PostAccountsAccountNumberOrdersDryRunLegsInner();
		btcLeg.setInstrumentType(InstrumentTypeEnum.EQUITY_OPTION);
		btcLeg.symbol(put);
		btcLeg.quantity(1.);
		btcLeg.action(ActionEnum.BUY_TO_CLOSE);
		buyToCloseOrder.addLegsItem(btcLeg);

		// Dry Run Order
		orderResponse = cert.postOrderDryRun(CERT_ACCOUNT, tokenManager.getCertSessionToken(), buyToCloseOrder);
		assertThat(orderResponse).isNotNull();
		assertThat(orderResponse.getErrors()).isNull();

		// Place Order
		orderResponse = cert.postOrder(CERT_ACCOUNT, tokenManager.getCertSessionToken(), buyToCloseOrder);
		assertThat(orderResponse).isNotNull();
		assertThat(orderResponse.getErrors()).isNull();
		// wait order live
		logger.info("Wait for Buy-to-Close Order to be live.");
		awaitSuccess = socketResponseWrapper.buyToCloseLive.await(30, TimeUnit.SECONDS);
		assertTrue(awaitSuccess);
		logger.info("Buy-to-Close live.");

		// ================================================================================
		// Replace Order
		// ================================================================================
		String id = orderResponse.getOrder().getId();
		logger.info("Replace Buy-to-Close Order");
		// POST /accounts/{account_number}/orders/{id}/dry-run
		// PostAccountsAccountNumberOrdersIdDryRun
		PostAccountsAccountNumberOrders updatedBuyToCloseOrderDryRun = buyToCloseOrder;
		updatedBuyToCloseOrderDryRun.setPrice(LIMIT_PRICE_FILL); // will be ask price in final implementation
		orderResponse = cert.postOrdersIdDryRun(CERT_ACCOUNT, tokenManager.getCertSessionToken(), id,
				updatedBuyToCloseOrderDryRun);
		assertThat(orderResponse).isNotNull();
		assertThat(orderResponse.getErrors()).isNull();
		logger.info(orderResponse.toString());

		// PUT /accounts/{account_number}/orders/{id} PutAccountsAccountNumberOrdersId
		PutAccountsAccountNumberOrdersId updatedBuyToCloseOrder = OrdersFactory
				.createPutAccountsAccountNumberOrdersId(updatedBuyToCloseOrderDryRun);
		cert.putOrdersId(CERT_ACCOUNT, tokenManager.getCertSessionToken(), id, updatedBuyToCloseOrder);
		assertThat(orderResponse).isNotNull();
		assertThat(orderResponse.getErrors()).isNull();
		logger.info(orderResponse.toString());

		// wait order filled
		logger.info("Wait for Buy-to-Close Order to be filled.");
		awaitSuccess = socketResponseWrapper.buyToCloseFilled.await(30, TimeUnit.SECONDS);
		assertTrue(awaitSuccess);
		logger.info("Replaced Buy-to-Close filled.");

		certWebSocket.sendClose(WebSocket.NORMAL_CLOSURE, "");
	}

	/**
	 * Open a new position with Theta Engine NOTE: Complex Orders are not supported
	 * by the Certification Environment. So they are not used atm. { "strike-price":
	 * "5425.0", "call": "./ESU4 EWM4 240628C5425", "call-streamer-symbol":
	 * "./EWM24C5425:XCME", "put": "./ESU4 EWM4 240628P5425", "put-streamer-symbol":
	 * "./EWM24P5425:XCME" }
	 * 
	 * @throws InterruptedException
	 */
	@Test
	void test06thetaTradeSellToOpenAndBuyToClose() throws InterruptedException {
		final String put = "AAPL  261218P00150000";

		var socketResponseWrapper = new Object() {
			CountDownLatch sellToOpenFilled = new CountDownLatch(1);
			CountDownLatch buyToCloseFilled = new CountDownLatch(1);
		};
		AccountStreamerSocketClient certAccountStreamer = new AccountStreamerSocketClient(
				tokenManager.getCertSessionToken(), new ArrayList<String>() {
					{
						add(CERT_ACCOUNT);
					}
				});
		certAccountStreamer.addUserMessageSubscribeListener(new AccountUpdateListener() {
			@Override
			public void onUpdate(JsonObject object) {
				if (ApiUtilities.containsOrder(object)) {
					ObjectMapper objectMapper = ApiUtilities.createObjectMapper();
					try {
						Order order = objectMapper.readValue(object.getJsonObject("data").toString(), Order.class);
						if (order.getLegs().get(0).getAction().equals(ActionEnum.SELL_TO_OPEN.toString())
								&& order.getStatus().equals("Filled"))
							socketResponseWrapper.sellToOpenFilled.countDown();
						if (order.getLegs().get(0).getAction().equals(ActionEnum.BUY_TO_CLOSE.toString())
								&& order.getStatus().equals("Filled"))
							socketResponseWrapper.buyToCloseFilled.countDown();
					} catch (JsonProcessingException e) {
						logger.error(e.getMessage());
					}
				}
			}
		});

		WebSocket certWebSocket = HttpClient.newHttpClient().newWebSocketBuilder()
				.buildAsync(URI.create(TastytradeStreamer.STREAMER.CERTIFICATION.getURL()), certAccountStreamer).join();
		certAccountStreamer.getConnected().await(30, TimeUnit.SECONDS);
		assertThat(certAccountStreamer.getConnected().getCount()).isEqualTo(0);
		certAccountStreamer.getUserMessageReady().await(30, TimeUnit.SECONDS);

		/**
		 * Sell To Open Limit
		 */
		PostAccountsAccountNumberOrders sellToOpen = new PostAccountsAccountNumberOrders()
				.timeInForce(TimeInForceEnum.DAY).orderType(OrderTypeEnum.LIMIT).price(2.5)
				.priceEffect(PriceEffectEnum.CREDIT);

		PostAccountsAccountNumberOrdersDryRunLegsInner stoLeg = new PostAccountsAccountNumberOrdersDryRunLegsInner()
				.instrumentType(InstrumentTypeEnum.EQUITY_OPTION).symbol(put).quantity(1.)
				.action(ActionEnum.SELL_TO_OPEN);
		sellToOpen.addLegsItem(stoLeg);

		Orders cert = new Orders(TastytradeApi.BASE.CERTIFICATION);
		// Post Order
		PlacedOrderResponse orderResponse = cert.postOrderDryRun(CERT_ACCOUNT, tokenManager.getCertSessionToken(),
				sellToOpen);
		logger.info(orderResponse.toString());
		assertThat(orderResponse).isNotNull();
		assertThat(orderResponse.getErrors()).isNull();

		// valid assertion means: go on.
		orderResponse = cert.postOrder(CERT_ACCOUNT, tokenManager.getCertSessionToken(), sellToOpen);
		logger.info(orderResponse.toString());
		assertThat(orderResponse).isNotNull();
		assertThat(orderResponse.getErrors()).isNull();

		logger.info("Wait for Sell to Open Order to be filled.");
		boolean awaitSuccess = socketResponseWrapper.sellToOpenFilled.await(30, TimeUnit.SECONDS);
		assertTrue(awaitSuccess);
		logger.info("Sell to Open filled.");

		/**
		 * Buy To Close Limit (Profit Taker)
		 */
		PostAccountsAccountNumberOrders buyToCloseOrder = new PostAccountsAccountNumberOrders()
				.timeInForce(TimeInForceEnum.GTC).orderType(OrderTypeEnum.LIMIT).price(1.) // 60% profit
				.priceEffect(PriceEffectEnum.DEBIT);

		PostAccountsAccountNumberOrdersDryRunLegsInner btcLeg = new PostAccountsAccountNumberOrdersDryRunLegsInner()
				.instrumentType(InstrumentTypeEnum.EQUITY_OPTION).symbol(put).quantity(1.)
				.action(ActionEnum.BUY_TO_CLOSE);
		buyToCloseOrder.addLegsItem(btcLeg);

		orderResponse = cert.postOrderDryRun(CERT_ACCOUNT, tokenManager.getCertSessionToken(), buyToCloseOrder);
		assertThat(orderResponse).isNotNull();
		assertThat(orderResponse.getErrors()).isNull();
		logger.info(orderResponse.toString());

		orderResponse = cert.postOrder(CERT_ACCOUNT, tokenManager.getCertSessionToken(), buyToCloseOrder);
		assertThat(orderResponse).isNotNull();
		assertThat(orderResponse.getErrors()).isNull();
		logger.info(orderResponse.toString());

		logger.info("Wait for Buy to Close Order to be filled.");
		awaitSuccess = socketResponseWrapper.buyToCloseFilled.await(30, TimeUnit.SECONDS);
		assertTrue(awaitSuccess);
		logger.info("Buy to Close filled.");

		certWebSocket.sendClose(WebSocket.NORMAL_CLOSURE, "");
	}
}
