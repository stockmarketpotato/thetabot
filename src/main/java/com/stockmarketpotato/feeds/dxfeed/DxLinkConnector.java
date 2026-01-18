package com.stockmarketpotato.feeds.dxfeed;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dxfeed.api.DXEndpoint;
import com.dxfeed.api.DXEndpoint.State;
import com.dxfeed.api.DXFeed;
import com.dxfeed.api.DXFeedEventListener;
import com.dxfeed.api.DXFeedSubscription;
import com.dxfeed.event.market.Quote;
import com.dxfeed.event.option.Greeks;


public class DxLinkConnector {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String token = null;
	private String url = null;
	DXFeedEventListener<Quote> quoteListener = null;
	DXFeedEventListener<Greeks> greeksListener = null;
	PropertyChangeListener changeListener = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals("state") && evt.getNewValue() == State.CONNECTED) {
				logger.info("Endpoint CONNECTED");
				notifyConnected.countDown();
			} else if (evt.getPropertyName().equals("state") && evt.getNewValue() == State.NOT_CONNECTED) {
				connectAndSubscribe();
			} else if (evt.getPropertyName().equals("state")) {
				logger.info("Endpoint changed " + evt.getOldValue() + " -> " + evt.getNewValue());
			}
		}
	};
	DXEndpoint endpoint = null;
	DXFeed feed = null;
	DXFeedSubscription<Quote> quoteSubscription = null;
	DXFeedSubscription<Greeks> greeksSubscription = null;
	private CountDownLatch notifyConnected = new CountDownLatch(1);

	public CountDownLatch getNotifyConnected() {
		return notifyConnected;
	}
	
	public DxLinkConnector(String authToken, String wssUrl, DXFeedEventListener<Quote> quoteListener,
			DXFeedEventListener<Greeks> greeksListener) {
		this.token = authToken;
		this.url = wssUrl;
		this.greeksListener = greeksListener;
		this.quoteListener = quoteListener;
		System.setProperty("dxfeed.experimental.dxlink.enable", "true");
		System.setProperty("scheme", "ext:opt:sysprops,resource:dxlink.xml");
		this.endpoint = DXEndpoint.create();
	}

	public static DxLinkConnector fromExisting(String newAuthToken, DxLinkConnector dxLinkConnector) throws InterruptedException {
		DxLinkConnector newLink = new DxLinkConnector(newAuthToken, dxLinkConnector.url,
				dxLinkConnector.quoteListener, dxLinkConnector.greeksListener);
		newLink.connectAndSubscribe();
		newLink.getNotifyConnected().await(30, TimeUnit.SECONDS);
		newLink.greeksSubscription.addSymbols(dxLinkConnector.getGreeksSymbols());
		newLink.quoteSubscription.addSymbols(dxLinkConnector.getQuoteSymbols());
		return newLink;
	}
	
	public void close() throws InterruptedException {
		this.endpoint.removeStateChangeListener(changeListener);
		this.endpoint.close();
	}
	
	public void connect() {
		final String address = "dxlink:" + this.url + "[login=dxlink:" + this.token + "]";
		notifyConnected = new CountDownLatch(1);
		this.endpoint.connect(address);
		this.endpoint.addStateChangeListener(changeListener);
	}
	
	/**
	 * Connects endpoint and subscribes for Quote and Greeks.
	 */
	public void connectAndSubscribe() {
		connect();
		this.feed = endpoint.getFeed();

		// create subscription for a specific event type on default feed
		this.quoteSubscription = feed.createSubscription(Quote.class);
		this.greeksSubscription = feed.createSubscription(Greeks.class);
		
		// define listener for events
		this.quoteSubscription.addEventListener(quoteListener);
		this.greeksSubscription.addEventListener(greeksListener);
	}
	
	public void addQuoteSymbols(final Collection<?> symbols) {
		this.quoteSubscription.addSymbols(symbols);
	}
	
	public void addGreeksSymbols(final Collection<?> symbols) {
		this.greeksSubscription.addSymbols(symbols);
	}
	
	public void removeQuoteSymbols(final List<String> symbols) {
		this.quoteSubscription.removeSymbols(symbols);
	}
	
	public void removeGreeksSymbols(final List<String> symbols) {
		this.greeksSubscription.removeSymbols(symbols);
	}

	public Set<?> getQuoteSymbols() {
		return this.quoteSubscription.getSymbols();
	}
	
	public Set<?> getGreeksSymbols() {
		return this.greeksSubscription.getSymbols();
	}
	
	public String getEndpointState() {
		if (endpoint != null)
			return endpoint.getState().toString();
		return "null";
	}
}
