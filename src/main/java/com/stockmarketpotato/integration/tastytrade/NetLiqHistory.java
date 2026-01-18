package com.stockmarketpotato.integration.tastytrade;

import java.io.StringReader;
import java.time.ZonedDateTime;
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
import com.stockmarketpotato.integration.tastytrade.model.netliq.NetLiqOhlc;
import com.stockmarketpotato.integration.tastytrade.model.netliq.NetLiqOhlc.TimeBackEnum;

public class NetLiqHistory extends TastytradeApi {
	public NetLiqHistory(BASE base) {
		super(base);
	}

	public NetLiqHistory() {
		super();
	}
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private NetLiqHistoryImpl impl = new NetLiqHistoryImpl();
	
	/**
	 * 
	 * Returns a list of account net liquidating value snapshots.
	 * <p>
	 * <b>200</b> - Returns a transaction that corresponds to an order fill leg.
	 * 
	 * @param accountNumber (required)
	 * @param timeBack      If given, will return data for a specific period of time
	 *                      with a pre-defined time interval. Passing &#x60;1d&#x60;
	 *                      will return the previous day of data in 5 minute
	 *                      intervals. This param is required if
	 *                      &#x60;start-time&#x60; is not given. 1d - If equities
	 *                      market is open, this will return data starting from
	 *                      market open in 5 minute intervals. If market is closed,
	 *                      will return data from previous market open.
	 *                      &#x60;1w&#x60; - 1 week of data in 15 minute intervals
	 *                      &#x60;1m&#x60; - 1 month of data in 1 hour intervals
	 *                      &#x60;3m&#x60; - 3 months of data in 1 day intervals
	 *                      &#x60;6m&#x60; - 6 months of data in 1 day intervals
	 *                      &#x60;1y&#x60; - 1 year of data in 1 day intervals
	 *                      &#x60;all&#x60; - All historical data in 1 day intervals
	 *                      (optional)
	 * @param startTime     The start point for this query. This param is required
	 *                      is time-back is not given. If given, will take
	 *                      precedence over time-back. Value is a DateTime with time
	 *                      zone, for example:
	 *                      &#x60;2020-01-01T16:24:11Z[America/New_York]&#x60;
	 *                      &#x60;2020-01-01T16:24:11Z-5:00&#x60;
	 *                      &#x60;2020-01-01T16:24:11Z&#x60; (optional)
	 * @return ResponseEntity&lt;NetLiqOhlc&gt;
	 * @throws RestClientException if an error occurs while attempting to invoke the
	 *                             API
	 */
	public List<NetLiqOhlc> getNetLiqHistory(final String token, String accountNumber, TimeBackEnum timeBack,
			ZonedDateTime startTime) throws RestClientException {
		return impl.getNetLiqHistory(this.baseUrl, token, accountNumber, timeBack, startTime);
	}

	private class NetLiqHistoryImpl {
		public List<NetLiqOhlc> getNetLiqHistory(final String serverUrl, final String token, String accountNumber,
				TimeBackEnum timeBack, ZonedDateTime startTime) throws RestClientException {
			Preconditions.checkNotNull(accountNumber,
					"Missing the required parameter 'accountNumber' when calling getNetLiqHistory");
			Preconditions.checkNotNull(token, "Missing the required parameter 'token' when calling getNetLiqHistory");
			RestTemplate restTemplate = new RestTemplate();
			List<NetLiqOhlc> netLiqOhlc = new ArrayList<>();
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", token);
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<Void> request = new HttpEntity<>(headers);
			String baseUri = serverUrl + "/accounts/" + accountNumber + "/net-liq/history";

			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUri);
			if (timeBack != null)
				builder.queryParam("time-back", timeBack);
			if (startTime != null) {
				builder.queryParam("start-time", startTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
			}
			String url = builder.build().encode().toUriString();
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
					JsonArray array = object.getJsonObject("data").getJsonArray("items");
					for (int i = 0; i < array.size(); i++) {
						String netLiqOhlcStr = array.get(i).asJsonObject().toString();
						NetLiqOhlc n = objectMapper.readValue(netLiqOhlcStr, NetLiqOhlc.class);
						netLiqOhlc.add(n);
					}
					jsonReader.close();
					return netLiqOhlc;
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
		}
	}
}
