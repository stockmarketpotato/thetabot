package com.stockmarketpotato.integration.tradingcalendar;

import java.io.StringReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockmarketpotato.broker.ApiUtilities;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonReader;

/**
 * Utility class for interacting with the Trading Calendar API.
 * <p>
 * This class provides static methods to query market hours and status,
 * specifically for the New York Stock Exchange (XNYS).
 * It relies on an external service configured via static URLs.
 */
public final class TradingCalendar {
	private final static String MARKETS_HOURS_URL = "http://<...url...>/api/v1/markets/hours"; // TODO: configure
	private final static String MARKETS_STATUS_URL = "http://<...url...>/api/v1/markets/status"; // TODO: configure

	/**
	 * Retrieves trading hours for a specific date range.
	 * 
	 * @param from The start date (inclusive).
	 * @param to The end date (inclusive).
	 * @return A list of TradingHours objects for the requested period.
	 */
	public static List<TradingHours> getTradingHours(final LocalDate from, final LocalDate to) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Void> request = new HttpEntity<>(headers);
		String url = MARKETS_HOURS_URL;

		// Query parameters
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
		builder.queryParam("mic", "XNYS"); // NYSE
		builder.queryParam("start", from.toString());
		builder.queryParam("end", to.toString());
		url = builder.build().encode().toUriString();
		RestTemplate restTemplate = new RestTemplate();
		List<TradingHours> tradingHours = new ArrayList<>();
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
			JsonReader jsonReader = Json.createReader(new StringReader(response.getBody()));
			JsonArray array = jsonReader.readArray();
			jsonReader.close();
			ObjectMapper objectMapper = ApiUtilities.createObjectMapper();
			for (int i = 0; i < array.size(); i++) {
				String tradingHoursStr = array.get(i).asJsonObject().toString();
				TradingHours th = objectMapper.readValue(tradingHoursStr, TradingHours.class);
				tradingHours.add(th);
			}
		} catch (HttpClientErrorException | JsonProcessingException e) {
			e.printStackTrace();
		}
		return tradingHours;
	}
	
	/**
	 * Retrieves the trading hours for the current day.
	 * 
	 * @return The TradingHours for today, or null if not found or dates mismatch.
	 */
	public static TradingHours getTradingHoursToday() {
		List<TradingHours> tradingHours = TradingCalendar.getTradingHours(LocalDate.now(), LocalDate.now());
		if (tradingHours.size() == 0) 
			return null;
		TradingHours today = tradingHours.get(0);
		if (!today.getDate().equals(LocalDate.now()))
			return null;
		return today;
	}
	
	/**
	 * Retrieves the current market status (e.g., "Open", "Closed") for NYSE (XNYS).
	 * 
	 * @return The status string.
	 */
	public static String getMarketStatus() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Void> request = new HttpEntity<>(headers);
		String url = MARKETS_STATUS_URL;

		// Query parameters
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
		builder.queryParam("mic", "XNYS"); // NYSE
		url = builder.build().encode().toUriString();
		RestTemplate restTemplate = new RestTemplate();
		String status = "";
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
			JsonReader jsonReader = Json.createReader(new StringReader(response.getBody()));
			JsonArray array = jsonReader.readArray();
			jsonReader.close();
			status = array.getJsonObject(0).getString("status");
		} catch (HttpClientErrorException e) {
			e.printStackTrace();
		}
		return status;
	}
}
