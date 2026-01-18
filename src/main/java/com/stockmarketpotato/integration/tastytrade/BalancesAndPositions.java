package com.stockmarketpotato.integration.tastytrade;

import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.base.Preconditions;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import com.stockmarketpotato.integration.tastytrade.model.accounts.AccountBalances;
import com.stockmarketpotato.integration.tastytrade.model.accounts.AccountPosition;
import com.stockmarketpotato.integration.tastytrade.model.positions.AccountBalanceSnapshot;

public class BalancesAndPositions extends TastytradeApi {
	public BalancesAndPositions(BASE base) {
		super(base);
	}

	public BalancesAndPositions() {
		super();
	}
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private BalancesAndPositionsImpl impl = new BalancesAndPositionsImpl();

	/**
	 * Returns the positions in the given account at tastytrade. It also receives
	 * the latest Quote via DxLink and calculates the PNL for every position before
	 * returning.
	 * 
	 * @param accountNo The identification number of the of the account (a String).
	 * @return List of AccountPosition including the latest Quote and PNL
	 *         calculated.
	 */
	public List<AccountPosition> getPositions(final String token, final String accountNo) {
		return impl.getPositions(this.baseUrl, token, accountNo);
	}

	public AccountBalances getBalances(final String token, final String accountNo) {
		return impl.getBalances(this.baseUrl, token, accountNo);
	}
	

	public List<AccountBalanceSnapshot> getAccountBalanceSnapshots(String sessionToken, String accountNo,
			String timeOfDay, LocalDate startDate) {
		return impl.getAccountBalanceSnapshots(
				this.baseUrl,	// final String serverUrl
				sessionToken,  	// final String token
				accountNo, 		// String accountNumber
				timeOfDay,      // String timeOfDay
				100,			// Integer perPage
				null,			// Integer pageOffset
				null,			// String currency
				null, 			// LocalDate snapshotDate
				startDate,		// LocalDate startDate
				null			// LocalDate endDate
				);
	}
	
	public List<AccountBalanceSnapshot> getAccountBalanceSnapshots(String sessionToken, String accountNo,
			String timeOfDay) {
		return impl.getAccountBalanceSnapshots(
				this.baseUrl,	// final String serverUrl
				sessionToken,  	// final String token
				accountNo, 		// String accountNumber
				timeOfDay,      // String timeOfDay
				100,			// Integer perPage
				null,			// Integer pageOffset
				null,			// String currency
				null, 			// LocalDate snapshotDate
				null,			// LocalDate startDate
				null			// LocalDate endDate
				);
	}
	
	private class BalancesAndPositionsImpl {
		public List<AccountPosition> getPositions(final String serverUrl, final String token, final String accountNo) {
			List<AccountPosition> positions = new ArrayList<>();
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", token);
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<Void> request = new HttpEntity<>(headers);
			RestTemplate restTemplate = new RestTemplate();
			try {
				ResponseEntity<String> response = restTemplate.exchange(serverUrl + "/accounts/" + accountNo + "/positions",
						HttpMethod.GET, request, String.class);
				JsonReader jsonReader = Json.createReader(new StringReader(response.getBody()));
				JsonObject object = jsonReader.readObject();
				ObjectMapper objectMapper = new ObjectMapper();
				JsonArray array = object.getJsonObject("data").getJsonArray("items");
				for (int i = 0; i < array.size(); i++) {
					String accountStr = array.get(i).asJsonObject().toString();
					AccountPosition p = objectMapper.readValue(accountStr, AccountPosition.class);
					positions.add(p);
				}
				jsonReader.close();
				return positions;
			} catch (HttpClientErrorException | JsonProcessingException e) {
				logger.error(e.getMessage());
				logger.error(e.getStackTrace().toString());
				return null;
			}
		}

		public AccountBalances getBalances(final String serverUrl, final String token, final String accountNo) {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", token);
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<Void> request = new HttpEntity<>(headers);
			RestTemplate restTemplate = new RestTemplate();
			try {
				ResponseEntity<String> response = restTemplate.exchange(serverUrl + "/accounts/" + accountNo + "/balances",
						HttpMethod.GET, request, String.class);
				JsonReader jsonReader = Json.createReader(new StringReader(response.getBody()));
				JsonObject object = jsonReader.readObject();
				ObjectMapper objectMapper = new ObjectMapper();
				String balanceStr = object.getJsonObject("data").toString();
				AccountBalances balances = objectMapper.readValue(balanceStr, AccountBalances.class);
				return balances;
			} catch (HttpClientErrorException e) {
				logger.error(e.getMessage());
				logger.error(e.getStackTrace().toString());
				return null;
			} catch (JsonProcessingException e) {
				logger.error(e.getMessage());
				logger.error(e.getStackTrace().toString());
				return null;
			}
		}
		
		
		/**
	     * 
	     * Returns most recent snapshot and current balance for an account
	     * <p><b>200</b> - Returns most recent snapshot and current balance for an account
	     * @param timeOfDay The abbreviation for the time of day. (required)
	     * @param accountNumber  (required)
	     * @param perPage  (optional, default to 250)
	     * @param pageOffset  (optional, default to 0)
	     * @param currency Currency (optional, default to USD)
	     * @param snapshotDate The day of the balance snapshot to retrieve (optional)
	     * @param startDate The first date in a range of dates to retrieve (optional)
	     * @param endDate The last date in a range of dates to retrieve (optional)
	     * @return ResponseEntity&lt;List&lt;AccountBalanceSnapshot&gt;&gt;
	     * @throws RestClientException if an error occurs while attempting to invoke the API
	     */
		public List<AccountBalanceSnapshot> getAccountBalanceSnapshots(final String serverUrl, final String token,
				String accountNumber, String timeOfDay, Integer perPage, Integer pageOffset, String currency,
				LocalDate snapshotDate, LocalDate startDate, LocalDate endDate) throws RestClientException {
			Preconditions.checkNotNull(accountNumber, "Missing the required parameter 'accountNumber' when calling getAccountBalanceSnapshots");
			Preconditions.checkNotNull(token, "Missing the required parameter 'token' when calling getAccountBalanceSnapshots");
			Preconditions.checkNotNull(timeOfDay, "Missing the required parameter 'timeOfDay' when calling getAccountBalanceSnapshots");
			RestTemplate restTemplate = new RestTemplate();
			List<AccountBalanceSnapshot> snapshots = new ArrayList<>();
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", token);
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<Void> request = new HttpEntity<>(headers);
			
			String baseUri = serverUrl + "/accounts/" + accountNumber + "/balance-snapshots";
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUri);
			if (perPage != null)
				builder.queryParam("per-page", perPage);
			if (pageOffset != null)
				builder.queryParam("page-offset", pageOffset);
			if (currency != null)
				builder.queryParam("currency", currency);
			if (snapshotDate != null)
				builder.queryParam("snapshot-date", snapshotDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
			if (timeOfDay != null)
				builder.queryParam("time-of-day", timeOfDay);
			if (startDate != null)
				builder.queryParam("start-date", startDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
			if (endDate != null)
				builder.queryParam("end-date", endDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
			String url = builder.build().encode().toUriString();
			logger.debug(url);
			int totalPages = 1;
			try {
				ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
				if (response == null) {
					throw new RestClientException("ResponseEntity is null");
				}
				if (response.getStatusCode().is2xxSuccessful()) {
					JsonReader jsonReader = Json.createReader(new StringReader(response.getBody()));
					JsonObject object = jsonReader.readObject();
					ObjectMapper objectMapper = new ObjectMapper();
					objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
					objectMapper.registerModule(new JavaTimeModule());
					totalPages = object.getJsonObject("pagination").getInt("total-pages");
					JsonArray array = object.getJsonObject("data").getJsonArray("items");
					for (int i = 0; i < array.size(); i++) {
						String snapshotStr = array.get(i).asJsonObject().toString();
						AccountBalanceSnapshot n = objectMapper.readValue(snapshotStr, AccountBalanceSnapshot.class);
						snapshots.add(n);
					}
					jsonReader.close();
				} else {
					// The error handler built into the RestTemplate should handle 400 and 500
					// series errors.
					throw new RestClientException("API returned " + response.getStatusCode()
							+ " and it wasn't handled by the RestTemplate error handler");
				}
			} catch (HttpClientErrorException e) {
				logger.error(e.getMessage());
				logger.error(e.getStackTrace().toString());
				return null;
			} catch (JsonProcessingException e) {
				logger.error(e.getMessage());
				logger.error(e.getStackTrace().toString());
				return null;
			}
			if (pageOffset == null) {
				for (int i = 1; i < totalPages; i++) {
					snapshots.addAll(this.getAccountBalanceSnapshots(serverUrl, token, accountNumber, timeOfDay,
							perPage, i, currency, snapshotDate, startDate, endDate));
				}
			}
			
			return snapshots;
		}
		
	}
}
