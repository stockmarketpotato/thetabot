package com.stockmarketpotato.feeds.dxfeed;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;

import com.dxfeed.api.DXFeedEventListener;
import com.dxfeed.event.market.Quote;
import com.dxfeed.event.option.Greeks;
import com.stockmarketpotato.broker.SessionManager;
import com.stockmarketpotato.broker.TastytradeSettings;
import com.stockmarketpotato.broker.TastytradeSettingsRepository;


@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan(basePackages = {"com.stockmarketpotato.broker", "com.stockmarketpotato.feeds.dxfeed"}) 
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class DxLinkConnectorTest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private SessionManager tokenManager;
	
	@Autowired
	private TastytradeSettingsRepository tastytradeSettings;
	
	@Value("${tastytrade.api.prod.login}")
	private String prodApiLogin;
	@Value("${tastytrade.api.prod.password}")
	private String prodApiPassword;
	
	@Value("${tastytrade.api.cert.login}")
	private String certApiLogin;
	@Value("${tastytrade.api.cert.password}")
	private String certApiPassword;
	
	@Test
	void shouldGetQuoteAndGreeks() throws Exception {
		logger.info("Save TastytradeSettings");
		TastytradeSettings s = new TastytradeSettings();
		s.setApiLogin(prodApiLogin);
		s.setApiPassword(prodApiPassword);
		tastytradeSettings.save(s);
		logger.info("Check not null");
		assertThat(tastytradeSettings).isNotNull();
		assertThat(tokenManager).isNotNull();
		assertThat(TastytradeSettingsRepository.isInvalid(tastytradeSettings)).isNotEqualTo(true);
		logger.info("Check Api Quote Token");
		final String authToken = tokenManager.getApiQuoteToken();
		assertThat(authToken).isNotBlank();
		final String wssUrl = tokenManager.getDxLinkUrl();
		assertThat(wssUrl).isNotBlank();
		logger.info("Connect to Quote Feed");

		final String testQuote = "SPY";
		final String testGreek = ".TLT250117C101";
		
		var testQuoteWrapper = new Object(){ Quote q = null; };
		var testGreekWrapper = new Object(){ Greeks g = null; };
	
		CountDownLatch quoteReceived = new CountDownLatch(1);
		CountDownLatch greeksReceived = new CountDownLatch(1);
		
		DxLinkConnector dxlink = new DxLinkConnector(authToken, wssUrl, 
				new DXFeedEventListener<Quote>() {
					@Override
					public void eventsReceived(List<Quote> events) {
			            for (Quote quote : events) {
			                logger.info(quote.toString());
			                if (quote.getEventSymbol().equals(testQuote)) {
			                	testQuoteWrapper.q = quote;
			                	quoteReceived.countDown();
			                }
			            }
					}
				},
				new DXFeedEventListener<Greeks>() {
					@Override
					public void eventsReceived(List<Greeks> events) {
			            for (Greeks quote : events) {
			                logger.info(quote.toString());
			                if (quote.getEventSymbol().equals(testGreek)) {
			                	testGreekWrapper.g = quote;
			                	greeksReceived.countDown();
			                }
			            }
					}
				});
		dxlink.connectAndSubscribe();
		dxlink.getNotifyConnected().await(30, TimeUnit.SECONDS);
		assertThat(dxlink.getEndpointState().equals("CONNECTED")).isTrue();
		
		dxlink.addQuoteSymbols(new ArrayList<String>(){{ add(testQuote); }});
		dxlink.addGreeksSymbols(new ArrayList<String>() {{ add(testGreek); }});

		quoteReceived.await(30, TimeUnit.SECONDS);
		greeksReceived.await(30, TimeUnit.SECONDS);
		
		assertThat(testQuoteWrapper.q).isNotNull();
		assertThat(testGreekWrapper.g).isNotNull();
		
		assertThat(testQuoteWrapper.q.getAskPrice()).isNotEqualTo(0.);
		assertThat(testQuoteWrapper.q.getBidPrice()).isNotEqualTo(0.);
		assertThat(testGreekWrapper.g.getDelta()).isNotEqualTo(0.);
		dxlink.close();
	}
	
	@Test
	void shouldGetQuoteAndGreeksAfterReconnect() throws Exception {
		logger.info("Save TastytradeSettings");
		TastytradeSettings s = new TastytradeSettings();
		s.setApiLogin(prodApiLogin);
		s.setApiPassword(prodApiPassword);
		tastytradeSettings.save(s);
		logger.info("Check not null");
		assertThat(tastytradeSettings).isNotNull();
		assertThat(tokenManager).isNotNull();
		assertThat(TastytradeSettingsRepository.isInvalid(tastytradeSettings)).isNotEqualTo(true);
		logger.info("Check Api Quote Token");
		String authToken = tokenManager.getApiQuoteToken();
		assertThat(authToken).isNotBlank();
		final String wssUrl = tokenManager.getDxLinkUrl();
		assertThat(wssUrl).isNotBlank();
		logger.info("Connect to Quote Feed");

		final String testQuote = "SPY";
		final String testGreek = ".TLT250117C101";
		
		var testQuoteWrapper = new Object(){ Quote q = null; CountDownLatch received = new CountDownLatch(1); };
		var testGreekWrapper = new Object(){ Greeks g = null; CountDownLatch received = new CountDownLatch(1); };
		
		DxLinkConnector dxlink = new DxLinkConnector(authToken, wssUrl, 
				new DXFeedEventListener<Quote>() {
					@Override
					public void eventsReceived(List<Quote> events) {
			            for (Quote quote : events) {
			                logger.info(quote.toString());
			                if (quote.getEventSymbol().equals(testQuote)) {
			                	testQuoteWrapper.q = quote;
			                	testQuoteWrapper.received.countDown();
			                }
			            }
					}
				},
				new DXFeedEventListener<Greeks>() {
					@Override
					public void eventsReceived(List<Greeks> events) {
			            for (Greeks quote : events) {
			                logger.info(quote.toString());
			                if (quote.getEventSymbol().equals(testGreek)) {
			                	testGreekWrapper.g = quote;
			                	testGreekWrapper.received.countDown();
			                }
			            }
					}
				});
		dxlink.connectAndSubscribe();
		dxlink.getNotifyConnected().await(30, TimeUnit.SECONDS);
		assertThat(dxlink.getEndpointState().equals("CONNECTED")).isTrue();
		
		dxlink.addQuoteSymbols(new ArrayList<String>(){{ add(testQuote); }});
		dxlink.addGreeksSymbols(new ArrayList<String>() {{ add(testGreek); }});

		testQuoteWrapper.received.await(30, TimeUnit.SECONDS);
		testGreekWrapper.received.await(30, TimeUnit.SECONDS);
		
		assertThat(testQuoteWrapper.q).isNotNull();
		assertThat(testGreekWrapper.g).isNotNull();
		
		assertThat(testQuoteWrapper.q.getAskPrice()).isNotEqualTo(0.);
		assertThat(testQuoteWrapper.q.getBidPrice()).isNotEqualTo(0.);
		assertThat(testGreekWrapper.g.getDelta()).isNotEqualTo(0.);

		/**
		 * Do Reconnect
		 */
		logger.info("Reconnect with new API token");
		
		testQuoteWrapper.q = null;
		testGreekWrapper.g = null;
		testQuoteWrapper.received = new CountDownLatch(1);
		testGreekWrapper.received = new CountDownLatch(1);

		Set<?> greeks = dxlink.getGreeksSymbols();
		Set<?> quotes = dxlink.getQuoteSymbols();
		dxlink.close();
		assertThat(dxlink.getEndpointState().equals("CLOSED")).isTrue();
		
		authToken = tokenManager.getApiQuoteToken();
		DxLinkConnector dxlinkNew = new DxLinkConnector(authToken, wssUrl, new DXFeedEventListener<Quote>() {
			@Override
			public void eventsReceived(List<Quote> events) {
				for (Quote quote : events) {
					logger.info(quote.toString());
					if (quote.getEventSymbol().equals(testQuote)) {
						testQuoteWrapper.q = quote;
						testQuoteWrapper.received.countDown();
					}
				}
			}
		}, new DXFeedEventListener<Greeks>() {
			@Override
			public void eventsReceived(List<Greeks> events) {
				for (Greeks quote : events) {
					logger.info(quote.toString());
					if (quote.getEventSymbol().equals(testGreek)) {
						testGreekWrapper.g = quote;
						testGreekWrapper.received.countDown();
					}
				}
			}
		});
		dxlinkNew.connectAndSubscribe();
		dxlinkNew.getNotifyConnected().await(30, TimeUnit.SECONDS);
		assertThat(dxlinkNew.getEndpointState().equals("CONNECTED")).isTrue();
		
		dxlinkNew.addGreeksSymbols(greeks);
		dxlinkNew.addQuoteSymbols(quotes);

		testQuoteWrapper.received.await(30, TimeUnit.SECONDS);
		testGreekWrapper.received.await(30, TimeUnit.SECONDS);

		assertThat(testQuoteWrapper.q).isNotNull();
		assertThat(testGreekWrapper.g).isNotNull();

		assertThat(testQuoteWrapper.q.getAskPrice()).isNotEqualTo(0.);
		assertThat(testQuoteWrapper.q.getBidPrice()).isNotEqualTo(0.);
		assertThat(testGreekWrapper.g.getDelta()).isNotEqualTo(0.);
		dxlinkNew.close();
	}
}
