package com.stockmarketpotato.tradelog;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stockmarketpotato.bot.BotConfiguration;
import com.stockmarketpotato.bot.BotManager;
import com.stockmarketpotato.broker.SessionManager;

import com.stockmarketpotato.integration.tastytrade.Transactions;
import com.stockmarketpotato.integration.tastytrade.model.orders.ActionEnum;
import com.stockmarketpotato.integration.tastytrade.model.transactions.Transaction;

@Component
public class TradeManager {
	@Autowired
	private BotManager botManager;

	@Autowired
	private SessionManager tokenManager;

	public TradingStatistics getTradingStatistics() {
		var performance = new Object() {
			public BigDecimal pcr = BigDecimal.ZERO;
			public BigDecimal totalpnl = BigDecimal.ZERO;
			public BigDecimal totalpnl_prel = BigDecimal.ZERO;
		};

		var metrics = new Object() {
			public BigDecimal avgcredit = BigDecimal.ZERO;
			public BigDecimal avgdit = BigDecimal.ZERO;
			public BigDecimal avgloss = BigDecimal.ZERO;
			public BigDecimal avgwin = BigDecimal.ZERO;
			public BigDecimal lostrate = BigDecimal.ZERO;
			public BigDecimal occurences = BigDecimal.ZERO;
			public BigDecimal plpercx = BigDecimal.ZERO;
			public BigDecimal theopcr = BigDecimal.ZERO;
			public BigDecimal totalcredit = BigDecimal.ZERO;
			public BigDecimal wincount = BigDecimal.ZERO;
			public BigDecimal winrate = BigDecimal.ZERO;
		};
		BotConfiguration cfg = botManager.getActiveConfiguration();
		TradingStatistics stats = new TradingStatistics();
		if (cfg != null) {
			final String accountNumber = cfg.getAccountNumber();
			List<Trade> trades = new ArrayList<>();
			List<Transaction> transactions = getTransactions(accountNumber);
			Map<String, List<Transaction>> transactionsPerSymbol = new HashMap<>();
			for (Transaction t : transactions) {
				// remove everything but Option trades
				if (t.getUnderlyingSymbol() == null)
					continue;
				// Filter for trades with current bot configuration
				if (t.getUnderlyingSymbol().startsWith(cfg.getUnderlying().getDisplayValue()) == false)
					continue;
				final String symbol = t.getSymbol();
				if (!transactionsPerSymbol.containsKey(symbol))
					transactionsPerSymbol.put(symbol, new ArrayList<>());
				transactionsPerSymbol.get(symbol).add(t);
			}

			transactionsPerSymbol.forEach((symbol, transactionList) -> {
				trades.addAll(getTrades(transactionList));
			});

			if (trades.isEmpty())
				return stats;

			List<BigDecimal> plMultipleWin = new ArrayList<>();
			List<BigDecimal> plMultipleLose = new ArrayList<>();
			List<Long> dit = new ArrayList<>();
			trades.forEach(trade -> {
				trade.evaluate();
				if (trade.getCloseDate() != null) {
					performance.totalpnl = performance.totalpnl.add(trade.getSumPl());
					performance.totalpnl_prel = performance.totalpnl_prel.add(trade.getSumPl());
					metrics.totalcredit = metrics.totalcredit.add(trade.getTotalCredit());
					if (trade.getTradePl().compareTo(BigDecimal.ZERO) > 0) {
						metrics.wincount = metrics.wincount.add(BigDecimal.ONE);
						plMultipleWin.add(trade.getPlMultiple());
					} else {
						plMultipleLose.add(trade.getPlMultiple());
					}
					metrics.occurences = metrics.occurences.add(BigDecimal.ONE);
					dit.add(trade.getDit());
				} else {
					performance.totalpnl_prel = performance.totalpnl_prel.add(trade.getTotalCredit());
				}
			});
			performance.pcr = performance.totalpnl.divide(metrics.totalcredit, 2, RoundingMode.HALF_UP);
			metrics.winrate = metrics.wincount.divide(metrics.occurences, 2, RoundingMode.HALF_UP);
			metrics.avgwin = plMultipleWin.stream().reduce(BigDecimal.ZERO, BigDecimal::add)
					.divide(BigDecimal.valueOf(plMultipleWin.size()), 2, RoundingMode.HALF_UP);
			metrics.avgdit = BigDecimal.valueOf(dit.stream().collect(Collectors.summingLong(Long::longValue)))
					.divide(BigDecimal.valueOf(dit.size()), 2, RoundingMode.HALF_UP);
			metrics.lostrate = metrics.occurences.subtract(metrics.wincount).divide(metrics.wincount, 2,
					RoundingMode.HALF_UP);
			metrics.avgcredit = metrics.totalcredit.divide(metrics.occurences, 2, RoundingMode.HALF_UP);
			metrics.theopcr = metrics.winrate.multiply(metrics.avgwin).add(metrics.lostrate.multiply(metrics.avgloss));
			BigDecimal lots = metrics.occurences;
			metrics.plpercx = performance.totalpnl.divide(lots, 2, RoundingMode.HALF_UP);
			if (!plMultipleLose.isEmpty())
				metrics.avgloss = plMultipleLose.stream().reduce(BigDecimal.ZERO, BigDecimal::add)
						.divide(BigDecimal.valueOf(plMultipleLose.size()), 2, RoundingMode.HALF_UP);
			Collections.sort(trades, Collections.reverseOrder());

			stats.setPerformance(new TradingPerformance(performance.totalpnl, performance.totalpnl_prel, performance.pcr));
			stats.setMetrics(new TradingMetrics(metrics.avgcredit, metrics.avgdit, metrics.avgloss, metrics.avgwin,
					metrics.lostrate, metrics.occurences, metrics.plpercx, metrics.theopcr, metrics.totalcredit,
					metrics.wincount, metrics.winrate));
			stats.setTrades(trades);
			stats.setTransactions(transactions);
		}
		return stats;
	}

	private List<Transaction> getTransactions(String accountNumber) {
		Transactions transactionsApi = new Transactions();
		Integer perPage = 250;
		Integer pageOffset = null;
		String sort = "Desc";
		String type = null;
		List<String> types = null;
		List<String> subType = null;
		LocalDate startDate = null;
		LocalDate endDate = LocalDate.now();
		String instrumentType = null;
		String symbol = null;
		String underlyingSymbol = null;
		String action = null;
		String partitionKey = null;
		String futuresSymbol = null;
		OffsetDateTime startAt = null;
		OffsetDateTime endAt = null;
		List<Transaction> transactions = transactionsApi.getTransactions(tokenManager.getSessionToken(), accountNumber,
				perPage, pageOffset, sort, type, types, subType, startDate, endDate, instrumentType, symbol,
				underlyingSymbol, action, partitionKey, futuresSymbol, startAt, endAt);
		Collections.sort(transactions, (t1, t2) -> {
			return t1.getExecutedAt().compareTo(t2.getExecutedAt());
		});
		return transactions;
	}

	private List<Trade> getTrades(List<Transaction> transactions) {
		List<Trade> trades = new ArrayList<>();
		LinkedList<Trade> openTrades = new LinkedList<>();
		for (Transaction t : transactions) {
			if (t.getAction().equals(ActionEnum.BUY_TO_CLOSE.getValue())) {
				Trade closedTrade = openTrades.removeFirst();
				closedTrade.setBuyToCloseTransaction(t);
				trades.add(closedTrade);
			} else if (t.getAction().equals(ActionEnum.SELL_TO_OPEN.getValue())) {
				Trade newTrade = new Trade();
				newTrade.setSellToOpenTransaction(t);
				openTrades.add(newTrade);
			}
		}
		trades.addAll(openTrades);
		return trades;
	}
}
