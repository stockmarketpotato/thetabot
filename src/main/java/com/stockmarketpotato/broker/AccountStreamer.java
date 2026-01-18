package com.stockmarketpotato.broker;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockmarketpotato.bot.BotConfiguration;
import com.stockmarketpotato.bot.BotConfigurationRepository;

import jakarta.annotation.PostConstruct;
import jakarta.json.JsonObject;
import com.stockmarketpotato.integration.tastytrade.AccountStreamerSocketClient;
import com.stockmarketpotato.integration.tastytrade.TastytradeStreamer;
import com.stockmarketpotato.integration.tastytrade.model.orders.ActionEnum;
import com.stockmarketpotato.integration.tastytrade.model.orders.Order;

@Component
public class AccountStreamer extends TastytradeStreamer {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TastytradeSettingsRepository tastytradeSettings;

	@Autowired
	SessionManager tokenManager;

	@Autowired
	BotConfigurationRepository botConfigurationRepository;

	WebSocket webSocket = null;

	private AccountStreamerSocketClient accountStreamer = null;

	@PostConstruct
	public void connect() {
		if (accountStreamer != null) {
			logger.info("Already connected");
			return;
		}
		if (TastytradeSettingsRepository.isInvalid(tastytradeSettings)) {
			logger.warn("No valid TastytradeSettings found.");
			return;
		}
		logger.info("Connect to Tastyworks Account Streamer WebSocket");
		final String authToken = tokenManager.getSessionToken();
		if (authToken == null) {
			logger.warn("No valid authToken found.");
			return;
		}
		List<BotConfiguration> cfgs = botConfigurationRepository.findAll();
		if (cfgs.isEmpty()) {
			logger.warn("No bot configuration found");
			return;
		}
		boolean connected = false;
		try {
			connected = connectAccountStreamer(authToken, cfgs.get(0).getAccountNumber());
		} catch (InterruptedException e) {
			e.printStackTrace();
			return;
		}
		if (connected)
			addOrderFilledListener();
	}

	private void addOrderFilledListener() {
		accountStreamer.addUserMessageSubscribeListener(new AccountUpdateListener() {
			@Override
			public void onUpdate(JsonObject object) {
				if (ApiUtilities.containsOrder(object)) {
					ObjectMapper objectMapper = ApiUtilities.createObjectMapper();
					try {
						Order order = objectMapper.readValue(object.getJsonObject("data").toString(), Order.class);
						if (order.getLegs().get(0).getAction().equals(ActionEnum.BUY_TO_CLOSE.toString())
								&& order.getStatus().equals("Filled")) {
							logger.info("Order #" + order.getId() + " Filled");
						}
					} catch (JsonProcessingException e) {
						logger.error(e.getMessage());
					}
				}
			}
		});
	}

	private boolean connectAccountStreamer(final String token, final String accountNumber) throws InterruptedException {
		accountStreamer = new AccountStreamerSocketClient(token, new ArrayList<String>() {
			{
				add(accountNumber);
			}
		});
		webSocket = HttpClient.newHttpClient().newWebSocketBuilder()
				.buildAsync(URI.create(this.streamerUrl), accountStreamer).join();
		accountStreamer.getConnected().await(30, TimeUnit.SECONDS);
		if (accountStreamer.getConnected().getCount() != 0) {
			logger.error("Connection to WebSocket timeout.");
			disconnectAccountStreamer();
			return false;
		}
		accountStreamer.getUserMessageReady().await(30, TimeUnit.SECONDS);
		if (accountStreamer.getUserMessageReady().getCount() != 0) {
			logger.error("Connection for User Message Ready timeout.");
			disconnectAccountStreamer();
			return false;
		}
		return true;
	}

	public interface AccountUpdateListener {
		public void onUpdate(JsonObject object);
	}

	private void disconnectAccountStreamer() {
		this.webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "");
		this.accountStreamer = null;
	}

	public void disconnect() {
		disconnectAccountStreamer();
	}
}
