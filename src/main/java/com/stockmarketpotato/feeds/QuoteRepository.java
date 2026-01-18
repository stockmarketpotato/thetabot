package com.stockmarketpotato.feeds;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface QuoteRepository extends CrudRepository<AggregatedQuote, String> {
	List<AggregatedQuote> findAll();
	AggregatedQuote findBySymbol(String symbol);
	AggregatedQuote findByStreamerSymbol(String streamerSymbol);
}
