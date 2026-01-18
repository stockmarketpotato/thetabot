package com.stockmarketpotato.feeds.dxfeed;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.dxfeed.api.DXFeedEventListener;
import com.dxfeed.event.market.Quote;
import com.dxfeed.event.option.Greeks;
import com.stockmarketpotato.broker.SessionManager;
import com.stockmarketpotato.feeds.AggregatedQuote;
import com.stockmarketpotato.feeds.QuoteReceiver;
import com.stockmarketpotato.feeds.QuoteRepository;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Controller
public class DxFeedManager {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SessionManager tokenManager;

	private List<QuoteReceiver> quoteReceiver = new ArrayList<>();

	@Autowired
	private QuoteRepository latestQuotes;

	DxLinkConnector dxlink = null;

	// Scheduling service that executes the token update and reconnect
	private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

	// Pointer to update task
	private ScheduledFuture<?> updateApiTokenTask = null;

	public void registerQuoteReceiver(QuoteReceiver receiver) {
		quoteReceiver.add(receiver);
	}

	@PostConstruct
	public boolean connect() {
		if (dxlink != null) {
			logger.info("Already connected");
			return false;
		}
		final String authToken = tokenManager.getApiQuoteToken();
		final String wssUrl = tokenManager.getDxLinkUrl();
		if (authToken == null || wssUrl == null) {
			logger.warn("Cannot connect Quote Feed. No valid authToken or wssUrl found.");
			return false;
		}
		logger.info("Connect DXEndpoint");
		dxlink = new DxLinkConnector(authToken, wssUrl, new QuoteListener(this), new GreeksListener(this));
		dxlink.connectAndSubscribe();
		scheduleReconnect();
		return true;
	}
	
	public void disconnect() {
		try {
			dxlink.close();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		dxlink = null;
	}

	public void reconnectWithNewApiQuoteToken() {
		if (dxlink == null) {
			logger.warn("Cannot reconnect DXEndpoint, dxlink = null");
			return;
		}
		logger.info("Reconnect with new API Quote Token");
		Set<?> greeks = dxlink.getGreeksSymbols();
		Set<?> quotes = dxlink.getQuoteSymbols();
		try {
			dxlink.close();
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
			logger.error(e.toString());
			e.printStackTrace();
		}
		dxlink = null;
		if (connect()) {
			dxlink.addGreeksSymbols(greeks);
			dxlink.addQuoteSymbols(quotes);
		}
	}

	/**
	 * API Quote Tokens expire after 24 hours. The token is sent in the KEEPALIVE
	 * message. We update the token every 23 hours and 58 minutes.
	 * TODO: remove, not required anymore
	 */
	private void scheduleReconnect() {
		logger.info("Schedule reconnect for Quote Feed");
		if (updateApiTokenTask != null && !updateApiTokenTask.isCancelled())
			updateApiTokenTask.cancel(true);
		final int period = (24 * 60) - 2;
		updateApiTokenTask = executorService.scheduleAtFixedRate(() -> {
			reconnectWithNewApiQuoteToken();
		}, period, period, TimeUnit.MINUTES);
	}

	public Set<String> getActiveSubscriptions() {
		if (dxlink == null)
			return new HashSet<>();
		Set<String> result = new HashSet<>();
		Set<?> symbols = dxlink.getQuoteSymbols();
		for (Object symbol : symbols) {
			if (symbol instanceof String) {
				result.add((String) symbol);
			}
		}
		return result;
	}

	@PreDestroy
	public void cancelAllFeedSubscriptions() {
		try {
			if (dxlink != null)
				dxlink.close();
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}

	public void cancelMultipleFeedSubscriptions(final List<SimpleInstrument> instruments) {
		unsubscribeQuotes(instruments);
	}

	public void cancelMultipleGreeksFeedSubscriptions(final List<SimpleInstrument> instruments) {
		unsubscribeGreeks(instruments);
	}

	private void unsubscribeGreeks(final List<SimpleInstrument> instruments) {
		List<String> removeSymbols = new ArrayList<>();
		for (SimpleInstrument i : instruments)
			removeSymbols.add(i.getStreamingSymbol());
		dxlink.removeGreeksSymbols(removeSymbols);
	}

	private void unsubscribeQuotes(final List<SimpleInstrument> instruments) {
		List<String> removeSymbols = new ArrayList<>();
		for (SimpleInstrument i : instruments)
			removeSymbols.add(i.getStreamingSymbol());
		dxlink.removeQuoteSymbols(removeSymbols);
	}

	public int subscribeMultipleFeeds(final List<SimpleInstrument> instruments) {
		try {
			dxlink.getNotifyConnected().await();
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return subscribeQuotes(instruments);
	}

	public int subscribeSingleFeed(final SimpleInstrument instrument) {
		try {
			dxlink.getNotifyConnected().await();
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}

		if (dxlink.getQuoteSymbols().contains(instrument.getStreamingSymbol()))
			return 0;

		Optional<AggregatedQuote> q = latestQuotes.findById(instrument.getSymbol());
		if (q.isEmpty()) {
			q = Optional.of(new AggregatedQuote(instrument.getSymbol(), instrument.getStreamingSymbol()));
			latestQuotes.save(q.get());
		}
		dxlink.addQuoteSymbols(new ArrayList<String>() {
			{
				add(instrument.getStreamingSymbol());
			}
		});
		return 1;
	}

	public int subscribeMultipleGreeksFeeds(final List<SimpleInstrument> instruments) {
		try {
			dxlink.getNotifyConnected().await();
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return subscribeGreeks(instruments);
	}

	private int subscribeGreeks(final List<SimpleInstrument> instruments) {
		int counter = 0;
		logger.info("Request total Greeks: " + instruments.size());
		Set<?> subscriptions = dxlink.getGreeksSymbols();
		List<String> newSymbols = new ArrayList<>();
		for (SimpleInstrument instrument : instruments) {
			if (subscriptions.contains(instrument.getSymbol()))
				continue;
			newSymbols.add(instrument.getStreamingSymbol());
			Optional<AggregatedQuote> q = latestQuotes.findById(instrument.getSymbol());
			if (q.isEmpty()) {
				q = Optional.of(new AggregatedQuote(instrument.getSymbol(), instrument.getStreamingSymbol()));
				latestQuotes.save(q.get());
			}
			counter++;
		}
		dxlink.addGreeksSymbols(newSymbols);
		return counter;
	}

	private int subscribeQuotes(final List<SimpleInstrument> instruments) {
		int counter = 0;
		logger.info("Request total Quotes: " + instruments.size());
		Set<?> subscriptions = dxlink.getQuoteSymbols();
		List<String> newSymbols = new ArrayList<>();
		for (SimpleInstrument instrument : instruments) {
			if (subscriptions.contains(instrument.getSymbol()))
				continue;
			newSymbols.add(instrument.getStreamingSymbol());
			Optional<AggregatedQuote> q = latestQuotes.findById(instrument.getSymbol());
			if (q.isEmpty()) {
				q = Optional.of(new AggregatedQuote(instrument.getSymbol(), instrument.getStreamingSymbol()));
				latestQuotes.save(q.get());
			}
			counter++;
		}
		dxlink.addQuoteSymbols(newSymbols);
		return counter;
	}

	public Map<String, String> getWebSocketState() {
		Map<String, String> states = new HashMap<>();
		if (dxlink != null)
			states.put("DXEndpoint", dxlink.getEndpointState());
		return states;
	}

	public void handleGreeks(Greeks g) {
		final String eventSymbol = g.getEventSymbol();
		Optional<AggregatedQuote> aq = Optional.ofNullable(latestQuotes.findByStreamerSymbol(eventSymbol));
		if (aq.isEmpty()) {
			logger.error("Unknown Streamer Symbol " + eventSymbol + " in " + g.toString());
			return;
		}

		aq.get().setDelta(fromDouble(g.getDelta()));
		aq.get().setDeltaTime(new Date());

		latestQuotes.save(aq.get());
		for (QuoteReceiver r : quoteReceiver)
			r.handleQuoteNotification(aq.get());
	}

	private static class GreeksListener implements DXFeedEventListener<Greeks> {
		private DxFeedManager manager = null;

		public GreeksListener(DxFeedManager manager) {
			super();
			this.manager = manager;
		}

		@Override
		public void eventsReceived(List<Greeks> events) {
			for (Greeks g : events)
				manager.handleGreeks(g);
		}
	}

	private BigDecimal fromDouble(final double d) {
		try {
			if (Double.valueOf(d).isNaN())
				return null;
			return BigDecimal.valueOf(d);
		} catch (NumberFormatException e) {
			logger.error(e.getMessage());
			logger.error("" + d);
			e.printStackTrace();
		}
		return null;
	}

	public void handleQuote(Quote q, String symbol) {
		final String eventSymbol = q.getEventSymbol();
		Optional<AggregatedQuote> aq = Optional.ofNullable(latestQuotes.findByStreamerSymbol(eventSymbol));
		if (aq.isEmpty()) {
			aq = Optional.of(new AggregatedQuote(symbol, eventSymbol));
		}

		aq.get().setBidPrice(fromDouble(q.getBidPrice()));
		aq.get().setBidTime(new Date());

		aq.get().setAskPrice(fromDouble(q.getAskPrice()));
		aq.get().setAskTime(new Date());

		addSaveAndNotifyQuote(aq.get());
	}
	
	public void handleQuote(Quote q) {
		final String eventSymbol = q.getEventSymbol();
		Optional<AggregatedQuote> aq = Optional.ofNullable(latestQuotes.findByStreamerSymbol(eventSymbol));
		if (aq.isEmpty()) {
			logger.error("Unknown Streamer Symbol " + eventSymbol + " in " + q.toString());
			return;
		}
		
		aq.get().setBidPrice(fromDouble(q.getBidPrice()));
		aq.get().setBidTime(new Date());

		aq.get().setAskPrice(fromDouble(q.getAskPrice()));
		aq.get().setAskTime(new Date());
		
		addSaveAndNotifyQuote(aq.get());
	}
	
	private void addSaveAndNotifyQuote(AggregatedQuote aq) {
		latestQuotes.save(aq);
		for (QuoteReceiver r : quoteReceiver)
			r.handleQuoteNotification(aq);
	}

	private static class QuoteListener implements DXFeedEventListener<Quote> {
		private DxFeedManager manager = null;

		public QuoteListener(DxFeedManager manager) {
			super();
			this.manager = manager;
		}

		@Override
		public void eventsReceived(List<Quote> events) {
			for (Quote q : events)
				manager.handleQuote(q);
		}
	}
}
