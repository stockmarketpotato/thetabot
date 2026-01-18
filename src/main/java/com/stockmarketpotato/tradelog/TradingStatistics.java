package com.stockmarketpotato.tradelog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.stockmarketpotato.integration.tastytrade.model.transactions.Transaction;

public class TradingStatistics {
    private List<Trade> trades;
    private List<Transaction> transactions;
    private TradingPerformance performance;
    private TradingMetrics metrics;

    // Default constructor
    public TradingStatistics() {
        this.trades = new ArrayList<>();
        this.transactions = new ArrayList<>();
        this.performance = new TradingPerformance();
        this.metrics = new TradingMetrics();
    }

    // All-args constructor
    public TradingStatistics(List<Trade> trades, 
                           List<Transaction> transactions,
                           TradingPerformance performance,
                           TradingMetrics metrics) {
        this.trades = trades != null ? new ArrayList<>(trades) : new ArrayList<>();
        this.transactions = transactions != null ? new ArrayList<>(transactions) : new ArrayList<>();
        this.performance = performance != null ? performance : new TradingPerformance();
        this.metrics = metrics != null ? metrics : new TradingMetrics();
    }

    // Getters (return immutable views of the lists)
    public List<Trade> getTrades() {
        return Collections.unmodifiableList(trades);
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    public TradingPerformance getPerformance() {
        return performance;
    }

    public TradingMetrics getMetrics() {
        return metrics;
    }

    // Setters (with null checks and defensive copies)
    public void setTrades(List<Trade> trades) {
        this.trades = trades != null ? new ArrayList<>(trades) : new ArrayList<>();
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions != null ? new ArrayList<>(transactions) : new ArrayList<>();
    }

    public void setPerformance(TradingPerformance performance) {
        this.performance = performance != null ? performance : new TradingPerformance();
    }

    public void setMetrics(TradingMetrics metrics) {
        this.metrics = metrics != null ? metrics : new TradingMetrics();
    }

    // Utility methods to modify collections
    public void addTrade(Trade trade) {
        if (trade != null) {
            trades.add(trade);
        }
    }

    public void addTransaction(Transaction transaction) {
        if (transaction != null) {
            transactions.add(transaction);
        }
    }

    // equals, hashCode, and toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TradingStatistics that = (TradingStatistics) o;
        return Objects.equals(trades, that.trades) &&
               Objects.equals(transactions, that.transactions) &&
               Objects.equals(performance, that.performance) &&
               Objects.equals(metrics, that.metrics);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trades, transactions, performance, metrics);
    }

    @Override
    public String toString() {
        return "TradingStatistics{" +
               "trades=" + trades +
               ", transactions=" + transactions +
               ", performance=" + performance +
               ", metrics=" + metrics +
               '}';
    }
}