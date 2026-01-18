package com.stockmarketpotato.integration.tastytrade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
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

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.stockmarketpotato.broker.AccountStreamer.AccountUpdateListener;
import com.stockmarketpotato.broker.ApiUtilities;
import com.stockmarketpotato.broker.SessionManager;
import com.stockmarketpotato.broker.TastytradeSettings;
import com.stockmarketpotato.broker.TastytradeSettingsRepository;
import com.stockmarketpotato.integration.tastytrade.model.orders.ActionEnum;
import com.stockmarketpotato.integration.tastytrade.model.orders.InstrumentTypeEnum;
import com.stockmarketpotato.integration.tastytrade.model.orders.Order;
import com.stockmarketpotato.integration.tastytrade.model.orders.OrderTypeEnum;
import com.stockmarketpotato.integration.tastytrade.model.orders.PlacedOrderResponse;
import com.stockmarketpotato.integration.tastytrade.model.orders.PostAccountsAccountNumberOrders;
import com.stockmarketpotato.integration.tastytrade.model.orders.PostAccountsAccountNumberOrdersDryRunLegsInner;
import com.stockmarketpotato.integration.tastytrade.model.orders.PriceEffectEnum;
import com.stockmarketpotato.integration.tastytrade.model.orders.TimeInForceEnum;

import jakarta.json.JsonObject;

@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan(basePackages = { "com.stockmarketpotato.broker", "com.stockmarketpotato.feeds.dxfeed" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class AccountStreamerSocketClientTest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SessionManager tokenManager;

	@Autowired
	private TastytradeSettingsRepository tastytradeSettings;

	@Value("${tastytrade.api.prod.login}")
	private String prodApiLogin;
	@Value("${tastytrade.api.prod.password}")
	private String prodApiPassword;

	@Value("${tastytrade.api.cert01.login}")
//	@Value("${tastytrade.api.cert02.login}")
//	@Value("${tastytrade.api.cert03.login}")
	private String certApiLogin;

	@Value("${tastytrade.api.cert01.password}")
//	@Value("${tastytrade.api.cert02.password}")
//	@Value("${tastytrade.api.cert03.password}")
	private String certApiPassword;

	@Value("${tastytrade.api.cert01.account}")
//	@Value("${tastytrade.api.cert02.account}")
//	@Value("${tastytrade.api.cert03.account}")
	private String CERT_ACCOUNT;

	@BeforeEach
	void saveTastytradeSetting() {
		logger.info("Save TastytradeSettings");
		TastytradeSettings s = new TastytradeSettings();
		s.setApiLogin(prodApiLogin);
		s.setApiPassword(prodApiPassword);
		s.setCertApiLogin(certApiLogin);
		s.setCertApiPassword(certApiPassword);
		tastytradeSettings.save(s);
		logger.info("Check not null");
		assertThat(tastytradeSettings).isNotNull();
		assertThat(tokenManager).isNotNull();

		logger.info("Connect to Account Streamer in Certification Environment");
		assertThat(TastytradeSettingsRepository.certIsInvalid(tastytradeSettings)).isNotEqualTo(true);
	}

	@Test
	void limitOrderAndWebsocket() throws Exception {

		final String sessionToken = tokenManager.getCertSessionToken();
		assertThat(sessionToken).isNotBlank();
		var socketResponseWrapper = new Object() {
			Order orderResponse = null;
			CountDownLatch routed = new CountDownLatch(1);
			CountDownLatch filled = new CountDownLatch(1);
		};
		AccountStreamerSocketClient certAccountStreamer = new AccountStreamerSocketClient(sessionToken,
				new ArrayList<String>() {
					{
						add(CERT_ACCOUNT);
					}
				});
		certAccountStreamer.addConnectListener(new AccountUpdateListener() {
			@Override
			public void onUpdate(JsonObject object) {
				logger.info("Connect Listener: " + object.toString());
			}
		});
		certAccountStreamer.addPublicWatchlistsSubscribeListener(new AccountUpdateListener() {
			@Override
			public void onUpdate(JsonObject object) {
				logger.info("Public Watchlist Listener: " + object.toString());
			}
		});
		certAccountStreamer.addQuoteAlertsSubscribeListener(new AccountUpdateListener() {
			@Override
			public void onUpdate(JsonObject object) {
				logger.info("Quote Alert Listener: " + object.toString());
			}
		});
		certAccountStreamer.addUserMessageSubscribeListener(new AccountUpdateListener() {
			@Override
			public void onUpdate(JsonObject object) {
				if (object.containsKey("type") && object.getString("type").equals("Order")) {
					ObjectMapper objectMapper = new ObjectMapper();
					objectMapper.setSerializationInclusion(Include.NON_NULL);
					objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
					objectMapper.registerModule(new JavaTimeModule());
					try {
						Order order = objectMapper.readValue(object.getJsonObject("data").toString(), Order.class);
						logger.info(order.getOrderType() + " Order #" + order.getId() + " " + order.getStatus());
						if (order.getStatus().equals("Filled")) {
							socketResponseWrapper.filled.countDown();
						} else {
							socketResponseWrapper.routed.countDown();
							socketResponseWrapper.orderResponse = order;
							logger.info(object.getJsonObject("data").toString());
						}
					} catch (JsonProcessingException e) {
						logger.error(e.getMessage());
						logger.error(e.getStackTrace().toString());
					}
				} else
					logger.info("User Message Listener: " + object.toString());
			}
		});

		/**
		 * Clear Alert
		 */
		certAccountStreamer.addUserMessageSubscribeListener(new AccountUpdateListener() {
			@Override
			public void onUpdate(JsonObject object) {
				if (ApiUtilities.containsOrder(object)) {
					ObjectMapper objectMapper = ApiUtilities.createObjectMapper();
					try {
						Order order = objectMapper.readValue(object.getJsonObject("data").toString(), Order.class);
						if (order.getLegs().get(0).getAction().equals(ActionEnum.BUY_TO_OPEN.toString())
								&& order.getStatus().equals("Filled")) {
							logger.info("Order #" + order.getId() + " filled. Now delete Quote Alert.");
						}
					} catch (JsonProcessingException e) {
						logger.error(e.getMessage());
					}
				}
			}
		});

		WebSocket certWebSocket = HttpClient.newHttpClient().newWebSocketBuilder()
				.buildAsync(URI.create(TastytradeStreamer.STREAMER.CERTIFICATION.getURL()), certAccountStreamer).join();

		certAccountStreamer.getConnected().await(30, TimeUnit.SECONDS);
		assertThat(certAccountStreamer.getConnected().getCount()).isEqualTo(0);
		certAccountStreamer.getUserMessageReady().await(30, TimeUnit.SECONDS);
		logger.info("Place DryRun Order on Certification Environment");

		/**
		 * Build Dry Run Limit Order
		 */
		PostAccountsAccountNumberOrders order = new PostAccountsAccountNumberOrders();
		order.setTimeInForce(TimeInForceEnum.DAY);
		order.setOrderType(OrderTypeEnum.LIMIT);
		order.setPrice(1.09);
		order.setPriceEffect(PriceEffectEnum.DEBIT);
		order.setSource("Thetabot Test");

		PostAccountsAccountNumberOrdersDryRunLegsInner leg = new PostAccountsAccountNumberOrdersDryRunLegsInner();
		leg.setInstrumentType(InstrumentTypeEnum.EQUITY);
		leg.symbol("AAPL");
		leg.quantity(1.);
		leg.action(ActionEnum.BUY_TO_OPEN);
		order.addLegsItem(leg);

		/**
		 * Post Limit Order (Dry Run)
		 */
		Orders cert = new Orders(TastytradeApi.BASE.CERTIFICATION);
		PlacedOrderResponse orderResponse = cert.postOrderDryRun(CERT_ACCOUNT, tokenManager.getCertSessionToken(),
				order);
		logger.info(orderResponse.toString());
		assertThat(orderResponse).isNotNull();
		assertThat(orderResponse.getErrors()).isNull();

		logger.info("Place Limit Order on Certification Environment");
		orderResponse = null;

		/**
		 * Post Limit Order
		 */
		orderResponse = cert.postOrder(CERT_ACCOUNT, tokenManager.getCertSessionToken(), order);
		assertThat(orderResponse).isNotNull();
		assertThat(orderResponse.getErrors()).isNull();

		boolean awaitSuccess = socketResponseWrapper.routed.await(30, TimeUnit.SECONDS);
		assertTrue(awaitSuccess);

		assertTrue(Objects.equals(orderResponse.getOrder().getId(), socketResponseWrapper.orderResponse.getId()));
		assertTrue(Objects.equals(orderResponse.getOrder().getAccountNumber(),
				socketResponseWrapper.orderResponse.getAccountNumber()));
		assertTrue(Objects.equals(orderResponse.getOrder().getTimeInForce(),
				socketResponseWrapper.orderResponse.getTimeInForce()));
		assertTrue(Objects.equals(orderResponse.getOrder().getGtcDate(),
				socketResponseWrapper.orderResponse.getGtcDate()));
		assertTrue(Objects.equals(orderResponse.getOrder().getOrderType(),
				socketResponseWrapper.orderResponse.getOrderType()));
		assertTrue(Objects.equals(orderResponse.getOrder().getSize(), socketResponseWrapper.orderResponse.getSize()));
		assertTrue(Objects.equals(orderResponse.getOrder().getUnderlyingSymbol(),
				socketResponseWrapper.orderResponse.getUnderlyingSymbol()));
		assertTrue(Objects.equals(orderResponse.getOrder().getUnderlyingInstrumentType(),
				socketResponseWrapper.orderResponse.getUnderlyingInstrumentType()));
		assertTrue(Objects.equals(orderResponse.getOrder().getPrice(), socketResponseWrapper.orderResponse.getPrice()));
		assertTrue(Objects.equals(orderResponse.getOrder().getPriceEffect(),
				socketResponseWrapper.orderResponse.getPriceEffect()));
		assertTrue(Objects.equals(orderResponse.getOrder().getValue(), socketResponseWrapper.orderResponse.getValue()));
		assertTrue(Objects.equals(orderResponse.getOrder().getValueEffect(),
				socketResponseWrapper.orderResponse.getValueEffect()));
		assertTrue(Objects.equals(orderResponse.getOrder().getStopTrigger(),
				socketResponseWrapper.orderResponse.getStopTrigger()));
		assertTrue(
				Objects.equals(orderResponse.getOrder().getStatus(), socketResponseWrapper.orderResponse.getStatus()));
		assertTrue(Objects.equals(orderResponse.getOrder().getContingentStatus(),
				socketResponseWrapper.orderResponse.getContingentStatus()));
		assertTrue(Objects.equals(orderResponse.getOrder().getConfirmationStatus(),
				socketResponseWrapper.orderResponse.getConfirmationStatus()));
		assertTrue(Objects.equals(orderResponse.getOrder().getCancellable(),
				socketResponseWrapper.orderResponse.getCancellable()));
		assertTrue(Objects.equals(orderResponse.getOrder().getCancelledAt(),
				socketResponseWrapper.orderResponse.getCancelledAt()));
		assertTrue(Objects.equals(orderResponse.getOrder().getCancelUserId(),
				socketResponseWrapper.orderResponse.getCancelUserId()));
		assertTrue(Objects.equals(orderResponse.getOrder().getCancelUsername(),
				socketResponseWrapper.orderResponse.getCancelUsername()));
		assertTrue(Objects.equals(orderResponse.getOrder().getEditable(),
				socketResponseWrapper.orderResponse.getEditable()));
		assertTrue(
				Objects.equals(orderResponse.getOrder().getEdited(), socketResponseWrapper.orderResponse.getEdited()));
		assertTrue(Objects.equals(orderResponse.getOrder().getReplacingOrderId(),
				socketResponseWrapper.orderResponse.getReplacingOrderId()));
		assertTrue(Objects.equals(orderResponse.getOrder().getReplacesOrderId(),
				socketResponseWrapper.orderResponse.getReplacesOrderId()));
		assertTrue(Objects.equals(orderResponse.getOrder().getReceivedAt(),
				socketResponseWrapper.orderResponse.getReceivedAt()));
		assertTrue(Objects.equals(orderResponse.getOrder().getUpdatedAt(),
				socketResponseWrapper.orderResponse.getUpdatedAt()));
		assertTrue(Objects.equals(orderResponse.getOrder().getInFlightAt(),
				socketResponseWrapper.orderResponse.getInFlightAt()));
		assertTrue(
				Objects.equals(orderResponse.getOrder().getLiveAt(), socketResponseWrapper.orderResponse.getLiveAt()));
		assertTrue(Objects.equals(orderResponse.getOrder().getRejectReason(),
				socketResponseWrapper.orderResponse.getRejectReason()));
		assertTrue(
				Objects.equals(orderResponse.getOrder().getUserId(), socketResponseWrapper.orderResponse.getUserId()));
		assertTrue(Objects.equals(orderResponse.getOrder().getUsername(),
				socketResponseWrapper.orderResponse.getUsername()));
		assertTrue(Objects.equals(orderResponse.getOrder().getTerminalAt(),
				socketResponseWrapper.orderResponse.getTerminalAt()));
		assertTrue(Objects.equals(orderResponse.getOrder().getComplexOrderId(),
				socketResponseWrapper.orderResponse.getComplexOrderId()));
		assertTrue(Objects.equals(orderResponse.getOrder().getComplexOrderTag(),
				socketResponseWrapper.orderResponse.getComplexOrderTag()));
		assertTrue(Objects.equals(orderResponse.getOrder().getPreflightId(),
				socketResponseWrapper.orderResponse.getPreflightId()));
		assertTrue(Objects.equals(orderResponse.getOrder().getGlobalRequestId(),
				socketResponseWrapper.orderResponse.getGlobalRequestId()));
		assertTrue(Objects.equals(orderResponse.getOrder().getLegs(), socketResponseWrapper.orderResponse.getLegs()));
		assertTrue(Objects.equals(orderResponse.getOrder().getOrderRule(),
				socketResponseWrapper.orderResponse.getOrderRule()));

		awaitSuccess = socketResponseWrapper.filled.await(30, TimeUnit.SECONDS);
		assertTrue(awaitSuccess);

		certWebSocket.sendClose(WebSocket.NORMAL_CLOSURE, "");
	}
}
