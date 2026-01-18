package com.stockmarketpotato.broker;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stockmarketpotato.feeds.AggregatedQuote;
import com.stockmarketpotato.feeds.FeedUtilities;
import com.stockmarketpotato.feeds.QuoteRepository;
import com.stockmarketpotato.feeds.dxfeed.DxFeedManager;

import jakarta.annotation.PostConstruct;
import com.stockmarketpotato.integration.tastytrade.AccountsAndCustomers;
import com.stockmarketpotato.integration.tastytrade.BalancesAndPositions;
import com.stockmarketpotato.integration.tastytrade.Instruments;
import com.stockmarketpotato.integration.tastytrade.NetLiqHistory;
import com.stockmarketpotato.integration.tastytrade.Orders;
import com.stockmarketpotato.integration.tastytrade.TastytradeApi.BASE;
import com.stockmarketpotato.integration.tastytrade.model.accounts.Account;
import com.stockmarketpotato.integration.tastytrade.model.accounts.AccountBalances;
import com.stockmarketpotato.integration.tastytrade.model.accounts.AccountPosition;
import com.stockmarketpotato.integration.tastytrade.model.netliq.NetLiqOhlc;
import com.stockmarketpotato.integration.tastytrade.model.netliq.NetLiqOhlc.TimeBackEnum;
import com.stockmarketpotato.integration.tastytrade.model.orders.Order;
import com.stockmarketpotato.integration.tastytrade.model.positions.AccountBalanceSnapshot;

/**
 * Provides access to a Tastytrade trading account, its positions and
 * transactions.
 */
@Component
public class BrokerManager {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SessionManager tokenManager;
	
	@Autowired
	private DxFeedManager feedManager;
	
	@Autowired
	private QuoteRepository quotes;

	public List<AccountPosition> certGetPositions(final String accountNo) {
		String token = tokenManager.getCertSessionToken();
		BalancesAndPositions positionsApi = new BalancesAndPositions(BASE.CERTIFICATION);
		Instruments instrumentsApi = new Instruments(BASE.CERTIFICATION);
			List<AccountPosition> positions = positionsApi.getPositions(token, accountNo);
		for (AccountPosition ap : positions)
			ap.streamerSymbol = instrumentsApi.getStreamerSymbol(token, ap);
		return positions;
	}
	
	public List<AccountPosition> prodGetPositions(final String accountNo) {
		String token = tokenManager.getSessionToken();
		BalancesAndPositions positionsApi = new BalancesAndPositions();
		Instruments instrumentsApi = new Instruments();
		List<AccountPosition> positions = positionsApi.getPositions(token, accountNo);
		for (AccountPosition ap : positions)
			ap.streamerSymbol = instrumentsApi.getStreamerSymbol(token, ap);
		return positions;
	}
	
	/**
	 * Returns the positions in the given account at tastytrade. It also receives
	 * the latest Quote via DxLink and calculates the PNL for every position before
	 * returning.
	 * 
	 * @param accountNo The identification number of the of the account (a String).
	 * @return List of AccountPosition including the latest Quote and PNL
	 *         calculated.
	 */
	public List<AccountPosition> certGetPositionsWithQuote(final String accountNo) {
		List<AccountPosition> positions = certGetPositions(accountNo);
		for (AccountPosition ap : positions) {
			Optional<AggregatedQuote> q = quotes.findById(ap.symbol);
			if (q.isPresent())
				ap.setPnl(q.get());
		}
		return positions;
	}

	/**
	 * Returns the positions in the given account at tastytrade. It also receives
	 * the latest Quote via DxLink and calculates the PNL for every position before
	 * returning.
	 * 
	 * @param accountNo The identification number of the of the account (a String).
	 * @return List of AccountPosition including the latest Quote and PNL
	 *         calculated.
	 */
	public List<AccountPosition> prodGetPositionsWithQuote(final String accountNo) {
		List<AccountPosition> positions = prodGetPositions(accountNo);
		for (AccountPosition ap : positions) {
			Optional<AggregatedQuote> q = quotes.findById(ap.symbol);
			if (q.isPresent())
				ap.setPnl(q.get());
		}
		return positions;
	}

	/**
	 * subscribe this instance to Feed Manager to receive updates for Quotes
	 */
	@PostConstruct
	private void postConstruct() { }

	public List<NetLiqOhlc> getNetLiqActiveAccount(String accountNo) {
		String token = tokenManager.getSessionToken();
		if (token == null)
			return new ArrayList<>();
		if (accountNo == null)
			return new ArrayList<>();
		NetLiqHistory history = new NetLiqHistory();
		return history.getNetLiqHistory(token, accountNo, TimeBackEnum.ALL, null);
	}

	public void reconnect() {
		tokenManager.reconnect();
		feedManager.reconnectWithNewApiQuoteToken();
		postConstruct();
	}

	public List<Order> getLiveOrders(String accountNumber) {
		return new Orders().getOrdersLive(accountNumber, tokenManager.getSessionToken());
	}
	
	public List<Order> getOrders(final String accountNumber) {
		return new Orders().getOrders(accountNumber, tokenManager.getSessionToken(), null, null, null, null, null, null, null, null, null, null, null);
	}

	public List<Account> getAccounts() {
		AccountsAndCustomers accountsApi = new AccountsAndCustomers();
		return accountsApi.getAccounts(tokenManager.getSessionToken());
	}

	public Account getAccount(final String accountNumber) {
		for (Account a : getAccounts())
			if (a.account_number.equals(accountNumber))
				return a;
		return null;
	}
	
	public AccountBalances getBalances(String account_number) {
		BalancesAndPositions positionsApi = new BalancesAndPositions();
		return positionsApi.getBalances(tokenManager.getSessionToken(), account_number);
	}

	public List<AccountBalanceSnapshot> getAccountAllBalanceSnapshotsEodUsd(String accountNumber, LocalDate startDate) {
		BalancesAndPositions positionsApi = new BalancesAndPositions();
		return positionsApi.getAccountBalanceSnapshots(tokenManager.getSessionToken(), accountNumber, "EOD", startDate);
	}
	
	public List<AccountBalanceSnapshot> convertAccountBalanceSnapshotsUsdToEur(List<AccountBalanceSnapshot> absUsd) {
		List<AccountBalanceSnapshot> absEur = new ArrayList<AccountBalanceSnapshot>();
		if (absUsd.size() > 0) {
			HashMap<LocalDate, Double> eurUsd = FeedUtilities.getForexEurUsd(absUsd.get(0).getSnapshotDate());
			for (AccountBalanceSnapshot day : absUsd) {
				LocalDate snapshotDate = day.getSnapshotDate();
				Double eurUsdDay = eurUsd.containsKey(snapshotDate) ? eurUsd.get(snapshotDate): Double.NaN;
				if (eurUsdDay == Double.NaN)
					logger.error("No EUR/USD exchange rate for " + snapshotDate + " found");
				if (day.getAvailableTradingFunds() != null)
					day.setAvailableTradingFunds(day.getAvailableTradingFunds() / eurUsdDay);
				if (day.getBondMarginRequirement() != null)
					day.setBondMarginRequirement(day.getBondMarginRequirement() / eurUsdDay);
				if (day.getCashAvailableToWithdraw() != null)
					day.setCashAvailableToWithdraw(day.getCashAvailableToWithdraw() / eurUsdDay);
				if (day.getCashBalance() != null)
					day.setCashBalance(day.getCashBalance() / eurUsdDay);
				if (day.getCashSettleBalance() != null)
					day.setCashSettleBalance(day.getCashSettleBalance() / eurUsdDay);
				if (day.getClosedLoopAvailableBalance() != null)
					day.setClosedLoopAvailableBalance(day.getClosedLoopAvailableBalance() / eurUsdDay);
				if (day.getCryptocurrencyMarginRequirement() != null)
					day.setCryptocurrencyMarginRequirement(day.getCryptocurrencyMarginRequirement() / eurUsdDay);
				if (day.getDayEquityCallValue() != null)
					day.setDayEquityCallValue(day.getDayEquityCallValue() / eurUsdDay);
				if (day.getDayTradeExcess() != null)
					day.setDayTradeExcess(day.getDayTradeExcess() / eurUsdDay);
				if (day.getDayTradingBuyingPower() != null)
					day.setDayTradingBuyingPower(day.getDayTradingBuyingPower() / eurUsdDay);
				if (day.getDayTradingCallValue() != null)
					day.setDayTradingCallValue(day.getDayTradingCallValue() / eurUsdDay);
				if (day.getDerivativeBuyingPower() != null)
					day.setDerivativeBuyingPower(day.getDerivativeBuyingPower() / eurUsdDay);
				if (day.getEquityBuyingPower() != null)
					day.setEquityBuyingPower(day.getEquityBuyingPower() / eurUsdDay);
				if (day.getEquityOfferingMarginRequirement() != null)
					day.setEquityOfferingMarginRequirement(day.getEquityOfferingMarginRequirement() / eurUsdDay);
				if (day.getFixedIncomeSecurityMarginRequirement() != null)
					day.setFixedIncomeSecurityMarginRequirement(
							day.getFixedIncomeSecurityMarginRequirement() / eurUsdDay);
				if (day.getFuturesMarginRequirement() != null)
					day.setFuturesMarginRequirement(day.getFuturesMarginRequirement() / eurUsdDay);
				if (day.getLongBondValue() != null)
					day.setLongBondValue(day.getLongBondValue() / eurUsdDay);
				if (day.getLongCryptocurrencyValue() != null)
					day.setLongCryptocurrencyValue(day.getLongCryptocurrencyValue() / eurUsdDay);
				if (day.getLongDerivativeValue() != null)
					day.setLongDerivativeValue(day.getLongDerivativeValue() / eurUsdDay);
				if (day.getLongEquityValue() != null)
					day.setLongEquityValue(day.getLongEquityValue() / eurUsdDay);
				if (day.getLongFuturesDerivativeValue() != null)
					day.setLongFuturesDerivativeValue(day.getLongFuturesDerivativeValue() / eurUsdDay);
				if (day.getLongFuturesValue() != null)
					day.setLongFuturesValue(day.getLongFuturesValue() / eurUsdDay);
				if (day.getLongMargineableValue() != null)
					day.setLongMargineableValue(day.getLongMargineableValue() / eurUsdDay);
				if (day.getMaintenanceCallValue() != null)
					day.setMaintenanceCallValue(day.getMaintenanceCallValue() / eurUsdDay);
				if (day.getMaintenanceRequirement() != null)
					day.setMaintenanceRequirement(day.getMaintenanceRequirement() / eurUsdDay);
				if (day.getMarginEquity() != null)
					day.setMarginEquity(day.getMarginEquity() / eurUsdDay);
				if (day.getMarginSettleBalance() != null)
					day.setMarginSettleBalance(day.getMarginSettleBalance() / eurUsdDay);
				if (day.getNetLiquidatingValue() != null)
					day.setNetLiquidatingValue(day.getNetLiquidatingValue() / eurUsdDay);
				if (day.getPendingCash() != null)
					day.setPendingCash(day.getPendingCash() / eurUsdDay);
				if (day.getRegTCallValue() != null)
					day.setRegTCallValue(day.getRegTCallValue() / eurUsdDay);
				if (day.getShortCryptocurrencyValue() != null)
					day.setShortCryptocurrencyValue(day.getShortCryptocurrencyValue() / eurUsdDay);
				if (day.getShortDerivativeValue() != null)
					day.setShortDerivativeValue(day.getShortDerivativeValue() / eurUsdDay);
				if (day.getShortEquityValue() != null)
					day.setShortEquityValue(day.getShortEquityValue() / eurUsdDay);
				if (day.getShortFuturesDerivativeValue() != null)
					day.setShortFuturesDerivativeValue(day.getShortFuturesDerivativeValue() / eurUsdDay);
				if (day.getShortFuturesValue() != null)
					day.setShortFuturesValue(day.getShortFuturesValue() / eurUsdDay);
				if (day.getShortMargineableValue() != null)
					day.setShortMargineableValue(day.getShortMargineableValue() / eurUsdDay);
				if (day.getSmaEquityOptionBuyingPower() != null)
					day.setSmaEquityOptionBuyingPower(day.getSmaEquityOptionBuyingPower() / eurUsdDay);
				if (day.getSpecialMemorandumAccountApexAdjustment() != null)
					day.setSpecialMemorandumAccountApexAdjustment(
							day.getSpecialMemorandumAccountApexAdjustment() / eurUsdDay);
				if (day.getSpecialMemorandumAccountValue() != null)
					day.setSpecialMemorandumAccountValue(day.getSpecialMemorandumAccountValue() / eurUsdDay);
				if (day.getTotalSettleBalance() != null)
					day.setTotalSettleBalance(day.getTotalSettleBalance() / eurUsdDay);
				if (day.getUnsettledCryptocurrencyFiatAmount() != null)
					day.setUnsettledCryptocurrencyFiatAmount(day.getUnsettledCryptocurrencyFiatAmount() / eurUsdDay);
				if (day.getUsedDerivativeBuyingPower() != null)
					day.setUsedDerivativeBuyingPower(day.getUsedDerivativeBuyingPower() / eurUsdDay);
				absEur.add(day);
			}
		}
		return absEur;
	}
}