package com.stockmarketpotato.bot.task;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stockmarketpotato.bot.BotConfiguration;
import com.stockmarketpotato.bot.strategy.BaseStrategy;
import com.stockmarketpotato.feeds.AggregatedQuote;

import com.stockmarketpotato.integration.tastytrade.Orders;
import com.stockmarketpotato.integration.tastytrade.model.accounts.Account;
import com.stockmarketpotato.integration.tastytrade.model.accounts.AccountBalances;
import com.stockmarketpotato.integration.tastytrade.model.accounts.AccountPosition;
import com.stockmarketpotato.integration.tastytrade.model.orders.InstrumentTypeEnum;
import com.stockmarketpotato.integration.tastytrade.model.orders.Order;

/**
 * Task responsible for identifying and closing positions that meet exit criteria.
 * Checks for DTE (Days to Expiration) limits and manages the closing order process.
 */
public class ThetaEngineClosePositionTask extends BaseStrategyTimedTask {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private OrderOperations orderOperations;

	/**
	 * Constructor for ThetaEngineClosePositionTask.
	 * 
	 * @param baseStrategy The parent strategy.
	 * @param name The name of the task.
	 */
	public ThetaEngineClosePositionTask(BaseStrategy baseStrategy, String name) {
		super(baseStrategy, name);
		this.orderOperations = OrderOperations.getInstance();
	}

	@Override
	protected boolean isExecutable(final BotConfiguration configuration) {
		if (!configuration.isClosePositions())
			return false;
		String accountNumber = configuration.getAccountNumber();
		Account a = baseStrategy.getBrokerManager().getAccount(accountNumber);
		if (a == null) {
			log.error("Account not found");
			return false;
		}
		if (a.is_closed) {
			log.error("Account closed");
			return false;
		}
		if (!a.is_futures_approved) {
			log.error("Futures not prroved in account");
			return false;
		}
		if (a.is_firm_error) {
			log.error("is firm error");
			return false;
		}
		if (a.margin_or_cash.equals("Cash")) {
			log.error("Not a Margin account");
			return false;
		}
		AccountBalances b = baseStrategy.getBrokerManager().getBalances(accountNumber);
		if (b.cash_balance < 2500.) {
			log.error("Balances below 2500 USD");
			return false;
		}
		return true;
	}

	@Override
	protected void runStrategy(final BotConfiguration configuration) {
		String accountNumber = configuration.getAccountNumber();
		List<AccountPosition> positions = baseStrategy.getBrokerManager().prodGetPositionsWithQuote(accountNumber);
		if (positions != null) {
			for (AccountPosition a : positions) {
				if (!a.instrument_type.equals(InstrumentTypeEnum.FUTURE_OPTION.getValue()))
					continue;
				if (!a.instrument_type.equals(InstrumentTypeEnum.EQUITY_OPTION.getValue()))
					continue;
				if (a.getDte() <= configuration.getExitDte()) {
					findOrderAndClosePosition(a, accountNumber);
				}
			}
		}
	}

	protected void findOrderAndClosePosition(AccountPosition a, final String accountNumber) {
		for (Order ox : new Orders().getOrdersLive(accountNumber,
				baseStrategy.getTokenManager().getSessionToken())) {
			if (ox.getCancellable() && (ox.getStatus().equals("Received") || ox.getStatus().equals("Routed")
					|| ox.getStatus().equals("In Flight") || ox.getStatus().equals("Live"))) {
				if (ox.getLegs().get(0).getSymbol().equals(a.symbol)) {
					AggregatedQuote quote = baseStrategy.getQuoteRepository().findByStreamerSymbol(a.streamerSymbol);
					Double askPrice = quote.getAskPrice().doubleValue();
					boolean success = orderOperations.replaceBuyToCloseOrder(ox.getId(), accountNumber,
							baseStrategy.getTokenManager().getSessionToken(), askPrice);
					if (success) {
						log.info("Successfully replaced Order #" + ox.getId());
					} else {
						log.error("Failed to replace Order #" + ox.getId());
					}
				}
			}
		}
	}

}
