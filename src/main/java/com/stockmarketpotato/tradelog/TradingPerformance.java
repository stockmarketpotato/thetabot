package com.stockmarketpotato.tradelog;

import java.math.BigDecimal;
import java.util.Objects;

public class TradingPerformance {
    private BigDecimal totalpnl;
    private BigDecimal totalpnl_prel;
    private BigDecimal pcr;

    // Default constructor
    public TradingPerformance() {
        this(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
    }

    // All-args constructor
    public TradingPerformance(BigDecimal totalpnl, BigDecimal totalpnl_prel, BigDecimal pcr) {
        this.totalpnl = Objects.requireNonNullElse(totalpnl, BigDecimal.ZERO);
        this.totalpnl_prel = Objects.requireNonNullElse(totalpnl_prel, BigDecimal.ZERO);
        this.pcr = Objects.requireNonNullElse(pcr, BigDecimal.ZERO);
    }

    // Getters
    public BigDecimal getTotalpnl() {
        return totalpnl;
    }

    public BigDecimal getTotalpnl_prel() {
        return totalpnl_prel;
    }

    public BigDecimal getPcr() {
        return pcr;
    }

    // Setters
    public void setTotalpnl(BigDecimal totalpnl) {
        this.totalpnl = Objects.requireNonNullElse(totalpnl, BigDecimal.ZERO);
    }

    public void setTotalpnl_prel(BigDecimal totalpnl_prel) {
        this.totalpnl_prel = Objects.requireNonNullElse(totalpnl_prel, BigDecimal.ZERO);
    }

    public void setPcr(BigDecimal pcr) {
        this.pcr = Objects.requireNonNullElse(pcr, BigDecimal.ZERO);
    }

    // equals, hashCode, and toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TradingPerformance that = (TradingPerformance) o;
        return totalpnl.compareTo(that.totalpnl) == 0 &&
               totalpnl_prel.compareTo(that.totalpnl_prel) == 0 &&
               pcr.compareTo(that.pcr) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalpnl, totalpnl_prel, pcr);
    }

    @Override
    public String toString() {
        return "TradingPerformance{" +
               "totalpnl=" + totalpnl +
               ", totalpnl_prel=" + totalpnl_prel +
               ", pcr=" + pcr +
               '}';
    }
}