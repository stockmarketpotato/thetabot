package com.stockmarketpotato.bot.task;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockmarketpotato.broker.ApiUtilities;
import com.stockmarketpotato.broker.AccountStreamer.AccountUpdateListener;

import jakarta.json.JsonObject;
import com.stockmarketpotato.integration.tastytrade.AccountStreamerSocketClient;
import com.stockmarketpotato.integration.tastytrade.Orders;
import com.stockmarketpotato.integration.tastytrade.TastytradeStreamer;
import com.stockmarketpotato.integration.tastytrade.model.orders.ActionEnum;
import com.stockmarketpotato.integration.tastytrade.model.orders.Order;
import com.stockmarketpotato.integration.tastytrade.model.orders.OrdersFactory;
import com.stockmarketpotato.integration.tastytrade.model.orders.PlacedOrderResponse;
import com.stockmarketpotato.integration.tastytrade.model.orders.PostAccountsAccountNumberOrders;
import com.stockmarketpotato.integration.tastytrade.model.orders.PutAccountsAccountNumberOrdersId;

/**
 * Singleton utility class for handling complex order operations.
 * Manages order replacement logic, including dry runs, actual placement, and waiting for fills via websocket stream.
 */
public class OrderOperations {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private AccountStreamerSocketClient accountStreamer;
	private CountDownLatch buyToCloseFilled;
	private WebSocket webSocket;

	private final BigDecimal TICK_SIZE = BigDecimal.valueOf(0.05);
	private static OrderOperations INSTANCE;
	private static Semaphore performOperation;

	private OrderOperations() {
	}

	/**
	 * Retrieves the singleton instance of OrderOperations.
	 * 
	 * @return The singleton instance.
	 */
	public static OrderOperations getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new OrderOperations();
			performOperation = new Semaphore(1);
		}
		return INSTANCE;
	}

	/**
	 * Replaces an existing Buy-to-Close order with a new price.
	 * 
	 * @param orderId The ID of the order to replace.
	 * @param accountNumber The account number.
	 * @param token The session token.
	 * @param askPrice The new ask price to set.
	 * @return true if the replacement was successful and filled, false otherwise.
	 */
	public boolean replaceBuyToCloseOrder(final String orderId, final String accountNumber, final String token,
			final Double askPrice) {
		try {
			OrderOperations.performOperation.acquire();
		} catch (InterruptedException e) {
			log.error("InterruptedException while trying to acquire Semaphore.");
			e.printStackTrace();
			return false;
		}

		boolean success = false;
		try {
			success = connectAccountStreamer(token, accountNumber);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (!success) {
			log.error("Could not connect to account streamer.");
			releaseAndDisconnect();
			return false;
		}

		Orders o = new Orders();
		Order liveBuyToCloseOrder = o.getOrdersId(accountNumber, token, orderId);
		if (liveBuyToCloseOrder.getStatus().equals("Filled")) {
			log.info("Order #" + liveBuyToCloseOrder.getId() + " already filled.");
			releaseAndDisconnect();
		}

		log.info("Replace Buy-to-Close Order");
		PostAccountsAccountNumberOrders updatedBuyToCloseOrderDryRun = OrdersFactory
				.createPostAccountsAccountNumberOrders(liveBuyToCloseOrder);
		BigDecimal newPrice = ApiUtilities.roundToTickSize(TICK_SIZE, BigDecimal.valueOf(askPrice));
		updatedBuyToCloseOrderDryRun.price(newPrice.doubleValue());

		PlacedOrderResponse orderResponse = o.postOrdersIdDryRun(accountNumber, token, orderId,
				updatedBuyToCloseOrderDryRun);
		boolean good = o.isGoodResponse(orderResponse);
		if (!good) {
			log.error(
					"Bad Response on postOrdersIdDryRun. Failed Order was: " + updatedBuyToCloseOrderDryRun.toString());
			releaseAndDisconnect();
			return false;
		}

		// PUT /accounts/{account_number}/orders/{id} PutAccountsAccountNumberOrdersId
		PutAccountsAccountNumberOrdersId updatedBuyToCloseOrder = OrdersFactory
				.createPutAccountsAccountNumberOrdersId(updatedBuyToCloseOrderDryRun);
		o.putOrdersId(accountNumber, token, orderId, updatedBuyToCloseOrder);
		good = o.isGoodResponse(orderResponse);
		if (!good) {
			log.info("Failed Order was:" + updatedBuyToCloseOrder.toString());
			releaseAndDisconnect();
			return false;
		}

		// wait order filled
		log.info("Wait for Buy-to-Close Order to be filled.");
		boolean awaitSuccess = false;
		try {
			awaitSuccess = buyToCloseFilled.await(30, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			log.error("Error while waiting for Buy-to-Close Order to be filled");
			releaseAndDisconnect();
			e.printStackTrace();
			return false;
		}

		if (!awaitSuccess)
			log.error("Replace Buy-to-Close failed, fill timed out.");
		else
			log.info("Replaced Buy-to-Close filled.");
		releaseAndDisconnect();
		return awaitSuccess;
	}

	private boolean connectAccountStreamer(final String token, final String accountNumber) throws InterruptedException {
		buyToCloseFilled = new CountDownLatch(1);
		accountStreamer = new AccountStreamerSocketClient(token, new ArrayList<String>() {
			{
				add(accountNumber);
			}
		});
		accountStreamer.addUserMessageSubscribeListener(new AccountUpdateListener() {
			private CountDownLatch buyToCloseFilled;

			public AccountUpdateListener setLatch(CountDownLatch buyToCloseFilled) {
				this.buyToCloseFilled = buyToCloseFilled;
				return this;
			}

			@Override
			public void onUpdate(JsonObject object) {
				if (ApiUtilities.containsOrder(object)) {
					ObjectMapper objectMapper = ApiUtilities.createObjectMapper();
					try {
						Order order = objectMapper.readValue(object.getJsonObject("data").toString(), Order.class);
						if (order.getLegs().get(0).getAction().equals(ActionEnum.BUY_TO_CLOSE.toString())
								&& order.getStatus().equals("Filled"))
							this.buyToCloseFilled.countDown();
					} catch (JsonProcessingException e) {
						log.error(e.getMessage());
					}
				}
			}
		}.setLatch(buyToCloseFilled));
		this.webSocket = HttpClient.newHttpClient().newWebSocketBuilder()
				.buildAsync(URI.create(TastytradeStreamer.STREAMER.PRODUCTION.getURL()), this.accountStreamer).join();
		this.accountStreamer.getConnected().await(30, TimeUnit.SECONDS);
		if (this.accountStreamer.getConnected().getCount() != 0) {
			disconnectAccountStreamer();
			return false;
		}
		this.accountStreamer.getUserMessageReady().await(30, TimeUnit.SECONDS);
		return true;
	}

	private void releaseAndDisconnect() {
		OrderOperations.performOperation.release();
		disconnectAccountStreamer();
	}

	private void disconnectAccountStreamer() {
		this.webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "");
		this.accountStreamer = null;
	}
}
