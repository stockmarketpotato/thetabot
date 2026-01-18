package com.stockmarketpotato.integration.tastytrade;

import java.io.StringReader;
import java.net.http.WebSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stockmarketpotato.broker.AccountStreamer.AccountUpdateListener;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class AccountStreamerSocketClient implements WebSocket.Listener {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	String token = null;
	private List<String> accounts;

	List<AccountUpdateListener> connectListener = new ArrayList<>();;
	List<AccountUpdateListener> publicWatchlistsSubscribeListener = new ArrayList<>();
	List<AccountUpdateListener> quoteAlertsSubscribeListener = new ArrayList<>();
	List<AccountUpdateListener> userMessageSubscribeListener = new ArrayList<>();

	public void addConnectListener(AccountUpdateListener listener) {
		connectListener.add(listener);
	}

	public void addPublicWatchlistsSubscribeListener(AccountUpdateListener listener) {
		publicWatchlistsSubscribeListener.add(listener);
	}

	public void addQuoteAlertsSubscribeListener(AccountUpdateListener listener) {
		quoteAlertsSubscribeListener.add(listener);
	}

	public void addUserMessageSubscribeListener(AccountUpdateListener listener) {
		userMessageSubscribeListener.add(listener);
	}

	public final CountDownLatch connected = new CountDownLatch(1);
	public final CountDownLatch userMessageReady = new CountDownLatch(1);

	public CountDownLatch getConnected() {
		return connected;
	}

	public CountDownLatch getUserMessageReady() {
		return userMessageReady;
	}

	public AccountStreamerSocketClient(String sessionToken, List<String> accounts) {
		this.token = sessionToken;
		this.accounts = accounts;
	}

	/**
	 * public-watchlists-subscribe
	 *
	 * - Although this subscribes to public watchlist updates, an auth token is
	 * still required - When sending this message, value is blank
	 */
	private void subscribePublicWatchlists(WebSocket webSocket) {
		JsonObject jo = Json.createObjectBuilder().add("action", "public-watchlists-subscribe").add("value", "")
				.add("auth-token", this.token).build();
		webSocket.sendText(jo.toString(), true);
	}

	/**
	 * quote-alerts-subscribe
	 * 
	 * - Subscribes to quote alert messages about alerts the user has previously
	 * configured via a POST request to the /quote-alerts endpoint - When sending
	 * this message, value is blank - Important note: quote alerts exist at a user
	 * level, and not an account level
	 */
	private void subscribeQuoteAlerts(WebSocket webSocket) {
		JsonObject jo = Json.createObjectBuilder().add("action", "quote-alerts-subscribe").add("auth-token", this.token)
				.build();
		webSocket.sendText(jo.toString(), true);
	}

	/**
	 * user-message-subscribe
	 * 
	 * - Subscribes to user-level messages like new account created. - When sending
	 * this message, value is the user's external-id returned in the POST /sessions
	 * response.
	 */
	private void subscribeUserMessage(WebSocket webSocket) {
		JsonObject jo = Json.createObjectBuilder().add("action", "user-message-subscribe").add("auth-token", this.token)
				.build();
		webSocket.sendText(jo.toString(), true);
	}

	/**
	 * heartbeat
	 * 
	 * - Sent periodically to the streamer server to prevent the socket connection
	 * from being considered "stale" - When sending this message, value is blank -
	 * Heartbeat messages should be sent at regular intervals (15s-1m)
	 */
	private void sendHeartbeat(WebSocket webSocket) {
		JsonObject jo = Json.createObjectBuilder().add("action", "heartbeat").add("auth-token", this.token).build();
		webSocket.sendText(jo.toString(), true);
	}

	@Override
	public void onOpen(WebSocket webSocket) {
		logger.info("Open");
		WebSocket.Listener.super.onOpen(webSocket);
		JsonArrayBuilder accountBuilder = Json.createArrayBuilder();
		for (String account : accounts)
			accountBuilder.add(account);
		JsonObject jo = Json.createObjectBuilder().add("action", "connect").add("value", accountBuilder.build())
				.add("auth-token", this.token).build();
		webSocket.sendText(jo.toString(), true);
	}

	private void notifyAll(final List<AccountUpdateListener> listener, JsonObject object) {
		for (AccountUpdateListener l : listener)
			l.onUpdate(object);
	}

	private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

	@Override
	public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
		JsonReader jsonReader = Json.createReader(new StringReader(data.toString()));
		JsonObject object = jsonReader.readObject();
		jsonReader.close();

		if (object.containsKey("type")) {
			if (object.getString("type").equals("Order")) {
				notifyAll(userMessageSubscribeListener, object);
			} else
				notifyAll(userMessageSubscribeListener, object);

		} else if (object.containsKey("action")) {
			if (object.getString("action").equals("connect") && object.getString("status").equals("ok")) {
				this.connected.countDown();
				notifyAll(connectListener, object);
				subscribePublicWatchlists(webSocket);
				subscribeQuoteAlerts(webSocket);
				subscribeUserMessage(webSocket);

				// keep this shit alive
				executorService.scheduleAtFixedRate(() -> {
					sendHeartbeat(webSocket);
				}, 0, 30, TimeUnit.SECONDS);
			} else if (object.getString("action").equals("public-watchlists-subscribe")) {
				notifyAll(publicWatchlistsSubscribeListener, object);
			} else if (object.getString("action").equals("quote-alerts-subscribe")) {
				notifyAll(quoteAlertsSubscribeListener, object);
			} else if (object.getString("action").equals("user-message-subscribe")) {
				if (object.getString("status").equals("ok"))
					userMessageReady.countDown();
				notifyAll(userMessageSubscribeListener, object);
			} else if (object.getString("action").equals("heartbeat")) {
				// nothing to be done here
			}
		} else
			logger.info(object.toString());
		return WebSocket.Listener.super.onText(webSocket, data, last);
	}

	@Override
	public void onError(WebSocket webSocket, Throwable error) {
		logger.error("Bad day! " + webSocket.toString());
		logger.error(error.getMessage());
		logger.error(error.getStackTrace().toString());
		error.printStackTrace();
		WebSocket.Listener.super.onError(webSocket, error);
	}

}
