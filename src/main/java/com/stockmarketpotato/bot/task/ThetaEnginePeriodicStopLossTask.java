package com.stockmarketpotato.bot.task;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stockmarketpotato.bot.BotConfiguration;
import com.stockmarketpotato.bot.strategy.BaseStrategy;

import com.stockmarketpotato.integration.tastytrade.model.accounts.Account;
import com.stockmarketpotato.integration.tastytrade.model.accounts.AccountBalances;
import com.stockmarketpotato.integration.tastytrade.model.accounts.AccountPosition;
import com.stockmarketpotato.integration.tastytrade.model.orders.InstrumentTypeEnum;
import com.stockmarketpotato.integration.tradingcalendar.TradingCalendar;

/**
 * Periodic task that checks existing positions against stop-loss criteria.
 * Runs frequently during market hours to limit losses on open positions.
 */
public class ThetaEnginePeriodicStopLossTask extends ThetaEngineClosePositionTask {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * Constructor for ThetaEnginePeriodicStopLossTask.
	 * 
	 * @param baseStrategy The parent strategy.
	 * @param name The name of the task.
	 */
	public ThetaEnginePeriodicStopLossTask(BaseStrategy baseStrategy, String name) {
		super(baseStrategy, name);
	}

	@Override
	protected boolean isExecutable(final BotConfiguration configuration) {
		if (!configuration.isClosePositions())
			return false;
    	String marketStatus = TradingCalendar.getMarketStatus();
		if (marketStatus == null) {
			log.info("Market Status unknown");
		} else if (marketStatus.equals("Open")) {
			log.info("Market Open: run " + this.name);
		} else if (marketStatus.equals("Closed")) {
			log.info("Market Closed; Skip " + this.name);
			return false;
		}
		
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
				if (!a.instrument_type.equals(InstrumentTypeEnum.FUTURE_OPTION.getValue()) &&
						!a.instrument_type.equals(InstrumentTypeEnum.EQUITY_OPTION.getValue())) {
					continue;
				}
				if (a.pnl == null) {
					log.warn("  Skip " + a.getName() + ". Cannot compute PNL!");
					continue;
				}
				log.info(a.getName());
				log.info("   PNL: " + a.pnl.toString() + " (" + a.pnl.divide(new BigDecimal("100.0")).toString() + ")");
				log.info("   Stop Loss: " + configuration.getStopLoss().toString());
				log.info("" + a.pnl.divide(new BigDecimal("100.0")).compareTo(configuration.getStopLoss()));
				if (a.pnl.divide(new BigDecimal("100.0")).compareTo(configuration.getStopLoss()) <= 0) {
					findOrderAndClosePosition(a, accountNumber);
				}
			}
		}
	}

	/**
	 * Attempts to execute the stop loss check task.
	 * 
	 * @param botConfiguration The current bot configuration.
	 */
	@Override
	public void tryExecute(final BotConfiguration botConfiguration) {
		if (isExecutable(botConfiguration)) {
			runStrategy(botConfiguration);
		}
	}
	
}
