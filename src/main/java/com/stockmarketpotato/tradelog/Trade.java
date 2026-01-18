package com.stockmarketpotato.tradelog;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneId;

import com.stockmarketpotato.broker.OccOsiSymbology;

import com.stockmarketpotato.integration.tastytrade.model.transactions.Transaction;

public class Trade implements Comparable<Trade> {
	@Override
	public int compareTo(Trade o) {
		return sellToOpenTransaction.getExecutedAt().compareTo(o.sellToOpenTransaction.getExecutedAt());
	}

	private Transaction sellToOpenTransaction;
	private Transaction buyToCloseTransaction;
	private BigDecimal totalCost;
	public Trade() { }
	
	public Transaction getBuyToCloseTransaction() {
		return buyToCloseTransaction;
	}
	public void setBuyToCloseTransaction(Transaction buyToCloseTransaction) {
		this.buyToCloseTransaction = buyToCloseTransaction;
	}
	public Transaction getSellToOpenTransaction() {
		return sellToOpenTransaction;
	}
	public void setSellToOpenTransaction(Transaction sellToOpenTransaction) {
		this.sellToOpenTransaction = sellToOpenTransaction;
	}
	public void evaluate() {
		if (sellToOpenTransaction == null)
			return;
		OccOsiSymbology occSymbol = OccOsiSymbology.fromFuturesOptionSymbol(sellToOpenTransaction.getSymbol());
		strike = occSymbol.getStrike();
		tradeDate = sellToOpenTransaction.getExecutedAt();
		expDate = occSymbol.getExpiration().toInstant().atZone(ZoneId.of("America/New_York", ZoneId.SHORT_IDS)).toOffsetDateTime();
		dte = Duration.between(tradeDate, expDate).toDays();
		if (buyToCloseTransaction != null) {
			closeDate = buyToCloseTransaction.getExecutedAt();
			dit = Duration.between(tradeDate, closeDate).toDays();
		} else {
			dit = Duration.between(tradeDate, OffsetDateTime.now()).toDays();
		}
		dit = Math.max(dit, 1);
		credit = BigDecimal.valueOf(sellToOpenTransaction.getPrice());
		totalCredit = BigDecimal.valueOf(sellToOpenTransaction.getValue());
		if (buyToCloseTransaction == null)
			return;
		sumPl = BigDecimal.valueOf(sellToOpenTransaction.getNetValue()).subtract(BigDecimal.valueOf(buyToCloseTransaction.getNetValue()));
		costToClose = BigDecimal.valueOf(buyToCloseTransaction.getPrice());
		totalCost = BigDecimal.valueOf(buyToCloseTransaction.getValue());
		tradePl = totalCredit.subtract(totalCost);
		plPerDay = tradePl.divide(BigDecimal.valueOf(dit), 2, RoundingMode.HALF_UP);
		plMultiple = tradePl.divide(totalCredit, 2, RoundingMode.HALF_UP);
		commission = BigDecimal.valueOf(buyToCloseTransaction.getRegulatoryFees() + 
				buyToCloseTransaction.getClearingFees()+
				buyToCloseTransaction.getCommission() +
				sellToOpenTransaction.getRegulatoryFees() + 
				sellToOpenTransaction.getClearingFees()+
				sellToOpenTransaction.getCommission());
	}
	public String getUnderlying() {
		return underlying;
	}

	public BigDecimal getStrike() {
		return strike;
	}

	public OffsetDateTime getTradeDate() {
		return tradeDate;
	}

	public OffsetDateTime getExpDate() {
		return expDate;
	}

	public OffsetDateTime getCloseDate() {
		return closeDate;
	}

	public BigDecimal getCredit() {
		return credit;
	}

	public BigDecimal getTotalCredit() {
		return totalCredit;
	}

	public BigDecimal getCostToClose() {
		return costToClose;
	}

	public BigDecimal getTradePl() {
		return tradePl;
	}

	public Long getDte() {
		return dte;
	}

	public Long getDit() {
		return dit;
	}

	public BigDecimal getPlPerDay() {
		return plPerDay;
	}

	public BigDecimal getPlMultiple() {
		return plMultiple;
	}

	public BigDecimal getCommission() {
		return commission;
	}

	public BigDecimal getSumPl() {
		return sumPl;
	}
	private String underlying;
	private BigDecimal strike;
	private OffsetDateTime tradeDate;
	private OffsetDateTime expDate;
	private OffsetDateTime closeDate = null;
	private BigDecimal credit;
	private BigDecimal totalCredit;
	private BigDecimal costToClose;
	private BigDecimal tradePl = BigDecimal.ZERO;
	private Long dte = null;
	private Long dit = null;
	private BigDecimal plPerDay;
	private BigDecimal sumPl;
	private BigDecimal plMultiple;
	private BigDecimal commission;
}