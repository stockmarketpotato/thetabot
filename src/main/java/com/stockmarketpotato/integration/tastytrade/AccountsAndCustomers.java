package com.stockmarketpotato.integration.tastytrade;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockmarketpotato.broker.ApiUtilities;
import com.stockmarketpotato.integration.tastytrade.model.QuoteStreamerResponse;
import com.stockmarketpotato.integration.tastytrade.model.accounts.Account;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;


public class AccountsAndCustomers extends TastytradeApi {
	public AccountsAndCustomers(BASE base) {
		super(base);
	}
	public AccountsAndCustomers() {
		super();
	}
	private AccountsAndCustomersImpl impl = new AccountsAndCustomersImpl();

	/**
	 * Returns a list of accounts at Tastytrade.
	 * 
	 * @return List of Account or null
	 */
	public List<Account> getAccounts(final String token) {
		return impl.getAccounts(this.baseUrl, token);
	}
	
	public QuoteStreamerResponse quoteStreamerToken(String sessionToken) {
		return impl.quoteStreamerToken(this.baseUrl, sessionToken);
	}
	
	private class AccountsAndCustomersImpl {
		private final Logger logger = LoggerFactory.getLogger(this.getClass());
		RestTemplate restTemplate = new RestTemplate();
		public List<Account> getAccounts(final String baseUrl, final String token) {
			List<Account> accounts = new ArrayList<>();
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", token);
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<Void> request = new HttpEntity<>(headers);
			try {
				ResponseEntity<String> response = restTemplate.exchange(baseUrl + "/customers/me/accounts",
						HttpMethod.GET, request, String.class);
				JsonReader jsonReader = Json.createReader(new StringReader(response.getBody()));
				JsonObject object = jsonReader.readObject();
				ObjectMapper objectMapper = new ObjectMapper();
				JsonArray array = object.getJsonObject("data").getJsonArray("items");
				for (int i = 0; i < array.size(); i++) {
					String accountStr = array.get(i).asJsonObject().getJsonObject("account").toString();
					accounts.add(objectMapper.readValue(accountStr, Account.class));
				}
				jsonReader.close();
				return accounts;
			} catch (HttpClientErrorException e) {
				logger.error(e.getMessage());
				logger.error(e.getStackTrace().toString());
				logger.error(e.getResponseBodyAsString());
				return null;
			} catch (JsonProcessingException e) {
				logger.error(e.getMessage());
				logger.error(e.getStackTrace().toString());
				return null;
			}
		}
		/**
		 * requests quoteApiToken and retrieves dxLinkUrl
		 */
		private QuoteStreamerResponse quoteStreamerToken(String baseUrl, String sessionToken) {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", sessionToken);
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<Void> request = new HttpEntity<>(headers);
			QuoteStreamerResponse quoteToken = null;
			try {
				ResponseEntity<String> response = restTemplate.exchange(baseUrl + "/quote-streamer-tokens",
						HttpMethod.GET, request, String.class);
				JsonReader jsonReader = Json.createReader(new StringReader(response.getBody()));
				JsonObject object = jsonReader.readObject();
				jsonReader.close();
				ObjectMapper objectMapper = ApiUtilities.createObjectMapper();
				String sessionStr = object.getJsonObject("data").toString();
				quoteToken = objectMapper.readValue(sessionStr, QuoteStreamerResponse.class);
				return quoteToken;
			} catch (HttpClientErrorException e) {
				logger.error(e.getMessage());
				logger.error(e.getStackTrace().toString());
				logger.error(e.getResponseBodyAsString());
				return null;
			} catch (JsonProcessingException e) {
				logger.error(e.getMessage());
				logger.error(e.getStackTrace().toString());
				return null;
			}
		}
	}
}
