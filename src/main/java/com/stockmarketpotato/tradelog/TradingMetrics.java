package com.stockmarketpotato.tradelog;

import java.math.BigDecimal;
import java.util.Objects;

public class TradingMetrics {
    private BigDecimal avgcredit;
    private BigDecimal avgdit;
    private BigDecimal avgloss;
    private BigDecimal avgwin;
    private BigDecimal lostrate;
    private BigDecimal occurences;
    private BigDecimal plpercx;
    private BigDecimal theopcr;
    private BigDecimal totalcredit;
    private BigDecimal wincount;
    private BigDecimal winrate;

    // Default constructor
    public TradingMetrics() {
        this(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
             BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
             BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
             BigDecimal.ZERO, BigDecimal.ZERO);
    }

    // All-args constructor
    public TradingMetrics(BigDecimal avgcredit,
                        BigDecimal avgdit,
                        BigDecimal avgloss,
                        BigDecimal avgwin,
                        BigDecimal lostrate,
                        BigDecimal occurences,
                        BigDecimal plpercx,
                        BigDecimal theopcr,
                        BigDecimal totalcredit,
                        BigDecimal wincount,
                        BigDecimal winrate) {
        this.avgcredit = Objects.requireNonNullElse(avgcredit, BigDecimal.ZERO);
        this.avgdit = Objects.requireNonNullElse(avgdit, BigDecimal.ZERO);
        this.avgloss = Objects.requireNonNullElse(avgloss, BigDecimal.ZERO);
        this.avgwin = Objects.requireNonNullElse(avgwin, BigDecimal.ZERO);
        this.lostrate = Objects.requireNonNullElse(lostrate, BigDecimal.ZERO);
        this.occurences = Objects.requireNonNullElse(occurences, BigDecimal.ZERO);
        this.plpercx = Objects.requireNonNullElse(plpercx, BigDecimal.ZERO);
        this.theopcr = Objects.requireNonNullElse(theopcr, BigDecimal.ZERO);
        this.totalcredit = Objects.requireNonNullElse(totalcredit, BigDecimal.ZERO);
        this.wincount = Objects.requireNonNullElse(wincount, BigDecimal.ZERO);
        this.winrate = Objects.requireNonNullElse(winrate, BigDecimal.ZERO);
    }

    // Getters
    public BigDecimal getAvgcredit() { return avgcredit; }
    public BigDecimal getAvgdit() { return avgdit; }
    public BigDecimal getAvgloss() { return avgloss; }
    public BigDecimal getAvgwin() { return avgwin; }
    public BigDecimal getLostrate() { return lostrate; }
    public BigDecimal getOccurences() { return occurences; }
    public BigDecimal getPlpercx() { return plpercx; }
    public BigDecimal getTheopcr() { return theopcr; }
    public BigDecimal getTotalcredit() { return totalcredit; }
    public BigDecimal getWincount() { return wincount; }
    public BigDecimal getWinrate() { return winrate; }

    // Setters
    public void setAvgcredit(BigDecimal avgcredit) {
        this.avgcredit = Objects.requireNonNullElse(avgcredit, BigDecimal.ZERO);
    }
    public void setAvgdit(BigDecimal avgdit) {
        this.avgdit = Objects.requireNonNullElse(avgdit, BigDecimal.ZERO);
    }
    public void setAvgloss(BigDecimal avgloss) {
        this.avgloss = Objects.requireNonNullElse(avgloss, BigDecimal.ZERO);
    }
    public void setAvgwin(BigDecimal avgwin) {
        this.avgwin = Objects.requireNonNullElse(avgwin, BigDecimal.ZERO);
    }
    public void setLostrate(BigDecimal lostrate) {
        this.lostrate = Objects.requireNonNullElse(lostrate, BigDecimal.ZERO);
    }
    public void setOccurences(BigDecimal occurences) {
        this.occurences = Objects.requireNonNullElse(occurences, BigDecimal.ZERO);
    }
    public void setPlpercx(BigDecimal plpercx) {
        this.plpercx = Objects.requireNonNullElse(plpercx, BigDecimal.ZERO);
    }
    public void setTheopcr(BigDecimal theopcr) {
        this.theopcr = Objects.requireNonNullElse(theopcr, BigDecimal.ZERO);
    }
    public void setTotalcredit(BigDecimal totalcredit) {
        this.totalcredit = Objects.requireNonNullElse(totalcredit, BigDecimal.ZERO);
    }
    public void setWincount(BigDecimal wincount) {
        this.wincount = Objects.requireNonNullElse(wincount, BigDecimal.ZERO);
    }
    public void setWinrate(BigDecimal winrate) {
        this.winrate = Objects.requireNonNullElse(winrate, BigDecimal.ZERO);
    }

    // equals, hashCode, and toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TradingMetrics that = (TradingMetrics) o;
        return compareBigDecimals(avgcredit, that.avgcredit) &&
               compareBigDecimals(avgdit, that.avgdit) &&
               compareBigDecimals(avgloss, that.avgloss) &&
               compareBigDecimals(avgwin, that.avgwin) &&
               compareBigDecimals(lostrate, that.lostrate) &&
               compareBigDecimals(occurences, that.occurences) &&
               compareBigDecimals(plpercx, that.plpercx) &&
               compareBigDecimals(theopcr, that.theopcr) &&
               compareBigDecimals(totalcredit, that.totalcredit) &&
               compareBigDecimals(wincount, that.wincount) &&
               compareBigDecimals(winrate, that.winrate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(avgcredit, avgdit, avgloss, avgwin, lostrate,
                          occurences, plpercx, theopcr, totalcredit,
                          wincount, winrate);
    }

    @Override
    public String toString() {
        return "TradingMetrics{" +
               "avgcredit=" + avgcredit +
               ", avgdit=" + avgdit +
               ", avgloss=" + avgloss +
               ", avgwin=" + avgwin +
               ", lostrate=" + lostrate +
               ", occurences=" + occurences +
               ", plpercx=" + plpercx +
               ", theopcr=" + theopcr +
               ", totalcredit=" + totalcredit +
               ", wincount=" + wincount +
               ", winrate=" + winrate +
               '}';
    }

    // Helper method for BigDecimal comparison
    private boolean compareBigDecimals(BigDecimal bd1, BigDecimal bd2) {
        if (bd1 == bd2) return true;
        if (bd1 == null || bd2 == null) return false;
        return bd1.compareTo(bd2) == 0;
    }
}