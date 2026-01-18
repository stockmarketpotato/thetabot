package com.stockmarketpotato.feeds;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dxfeed.event.market.Quote;
import com.stockmarketpotato.feeds.dxfeed.DxFeedManager;

@Controller
public class FeedsView {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private DxFeedManager feedManager;

	@Autowired
	private QuoteRepository quotes;

	@GetMapping("/feeds")
	public String feeds(Model model) {
		Map<String, AggregatedQuote> subscriptions = new TreeMap<>();
		for (String symbol : feedManager.getActiveSubscriptions()) {
			Optional<AggregatedQuote> q = Optional.ofNullable(quotes.findByStreamerSymbol(symbol));
			if (q.isPresent())
				subscriptions.put(symbol, q.get());
			else
				subscriptions.put(symbol, null);
		}
		model.addAttribute("subscriptions", subscriptions);
		model.addAttribute("webSocketState", feedManager.getWebSocketState());
		return "feeds";
	}

	@GetMapping("/feeds/reconnect")
	public String feedReconnect(Model model) {
		feedManager.reconnectWithNewApiQuoteToken();
		return "redirect:/feeds";
	}

	
	@GetMapping("/feeds/sendquote")
	public String sendQuote(@RequestParam("symbol") String symbol, @RequestParam("streamerSymbol") String streamerSymbol, @RequestParam("askPice") Double askPice,
			@RequestParam("bidPrice") Double bidPrice) {
		logger.info("Saved Manual Quote for " + symbol);
		Quote q = new Quote();
		q.setBidPrice(bidPrice);
		q.setAskPrice(askPice);
		q.setEventSymbol(streamerSymbol);
		feedManager.handleQuote(q, symbol);
		return "redirect:/feeds";
	}
}
