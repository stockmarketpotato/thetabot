package com.stockmarketpotato.feeds;

public interface QuoteReceiver {
	void handleQuoteNotification(AggregatedQuote q);
}
