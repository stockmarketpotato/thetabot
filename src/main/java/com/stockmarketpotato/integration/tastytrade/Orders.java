package com.stockmarketpotato.integration.tastytrade;

import java.io.StringReader;
import java.time.LocalDate;
import java.time.OffsetDateTime;
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
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.base.Preconditions;
import com.stockmarketpotato.broker.ApiUtilities;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import com.stockmarketpotato.integration.tastytrade.model.orders.Order;
import com.stockmarketpotato.integration.tastytrade.model.orders.OrdersFactory;
import com.stockmarketpotato.integration.tastytrade.model.orders.PlacedOrderResponse;
import com.stockmarketpotato.integration.tastytrade.model.orders.PostAccountsAccountNumberOrders;
import com.stockmarketpotato.integration.tastytrade.model.orders.PostAccountsAccountNumberOrdersDryRun;
import com.stockmarketpotato.integration.tastytrade.model.orders.PostAccountsAccountNumberOrdersIdDryRun;
import com.stockmarketpotato.integration.tastytrade.model.orders.PutAccountsAccountNumberOrdersId;

public class Orders extends TastytradeApi {
	public Orders(BASE base) {
		super(base);
	}

	public Orders() {
		super();
	}

	private final Logger logger = LoggerFactory.getLogger(Orders.class);
	protected final OrderApiImpl impl = new OrderApiImpl();

		
	public Order deleteOrder(final String accountNumber, final String token, String orderId) {
		Preconditions.checkNotNull(accountNumber,
				"Missing the required parameter 'accountNumber' when calling certDeleteOrder");
		Preconditions.checkNotNull(token, "Missing the required parameter 'token' when calling certDeleteOrder");
		ObjectMapper objectMapper = ApiUtilities.createObjectMapper();
		Order orderResponse = null;
		try {
			String deleteOrderResponseStr = impl.deleteOrder(this.baseUrl, token, accountNumber, orderId);
			orderResponse = objectMapper.readValue(deleteOrderResponseStr, Order.class);
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage());
			logger.error(e.getStackTrace().toString());
		}
		return orderResponse;
	}

	
	public List<Order> getOrders(final String accountNumber, final String token, Integer perPage, Integer pageOffset,
			LocalDate startDate, LocalDate endDate, String underlyingSymbol, List<String> status, String futuresSymbol,
			String underlyingInstrumentType, String sort, OffsetDateTime startAt, OffsetDateTime endAt) {
		Preconditions.checkNotNull(accountNumber,
				"Missing the required parameter 'accountNumber' when calling certGetOrders");
		Preconditions.checkNotNull(token, "Missing the required parameter 'token' when calling certGetOrders");
		return impl.getOrders(accountNumber, perPage, pageOffset, startDate, endDate, underlyingSymbol, status,
				futuresSymbol, underlyingInstrumentType, sort, startAt, endAt, this.baseUrl, token);
	}

	
	public Order getOrdersId(String accountNumber, String token, String id) {
		Preconditions.checkNotNull(accountNumber,
				"Missing the required parameter 'accountNumber' when calling certGetOrdersId");
		Preconditions.checkNotNull(token, "Missing the required parameter 'token' when calling certGetOrdersId");
		Preconditions.checkNotNull(id, "Missing the required parameter 'id' when calling certGetOrdersId");
		ObjectMapper objectMapper = ApiUtilities.createObjectMapper();
		Order order = null;
		try {
			String orderResponseStr = impl.getOrdersId(this.baseUrl, accountNumber, token, id);
			order = objectMapper.readValue(orderResponseStr, Order.class);
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage());
			logger.error(e.getStackTrace().toString());
		}
		return order;
	}

	
	public List<Order> getOrdersLive(final String accountNumber, final String token) {
		Preconditions.checkNotNull(accountNumber,
				"Missing the required parameter 'accountNumber' when calling getAccountsAccountNumberOrdersLive");
		return impl.getOrdersLive(accountNumber, this.baseUrl, token);
	}

	
	public boolean isGoodResponse(PlacedOrderResponse orderResponse) {
		return impl.isGoodResponse(orderResponse);
	}

	
	public PlacedOrderResponse postOrder(final String accountNumber, final String token,
			PostAccountsAccountNumberOrders postAccountsAccountNumberOrders) {
		Preconditions.checkNotNull(accountNumber,
				"Missing the required parameter 'accountNumber' when calling certPostOrder");
		Preconditions.checkNotNull(token, "Missing the required parameter 'token' when calling certPostOrder");
		ObjectMapper objectMapper = ApiUtilities.createObjectMapper();
		PlacedOrderResponse orderResponse = null;
		try {
			String postOrderString = objectMapper.writeValueAsString(postAccountsAccountNumberOrders);
			String orderResponseStr = impl.postOrder(this.baseUrl, token, accountNumber, postOrderString, false);
			orderResponse = objectMapper.readValue(orderResponseStr, PlacedOrderResponse.class);
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage());
			logger.error(e.getStackTrace().toString());
		}
		return orderResponse;
	}

	
	public PlacedOrderResponse postOrderDryRun(final String accountNumber, final String token,
			PostAccountsAccountNumberOrders postAccountsAccountNumberOrders) {
		Preconditions.checkNotNull(accountNumber,
				"Missing the required parameter 'accountNumber' when calling certPostOrderDryRun");
		Preconditions.checkNotNull(token, "Missing the required parameter 'token' when calling certPostOrderDryRun");
		PostAccountsAccountNumberOrdersDryRun dryRunOrder = OrdersFactory
				.createPostAccountsAccountNumberOrdersDryRun(postAccountsAccountNumberOrders);
		ObjectMapper objectMapper = ApiUtilities.createObjectMapper();
		PlacedOrderResponse orderResponse = null;
		try {
			String postOrderString = objectMapper.writeValueAsString(dryRunOrder);
			String orderResponseStr = impl.postOrder(this.baseUrl, token, accountNumber, postOrderString, true);
			orderResponse = objectMapper.readValue(orderResponseStr, PlacedOrderResponse.class);
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage());
			logger.error(e.getStackTrace().toString());
			return null;
		}
		return orderResponse;
	}

	
	public PlacedOrderResponse postOrdersIdDryRun(final String accountNumber, final String token, final String id,
			PostAccountsAccountNumberOrders postAccountsAccountNumberOrders) {
		Preconditions.checkNotNull(accountNumber,
				"Missing the required parameter 'accountNumber' when calling certPostOrderIdDryRun");
		Preconditions.checkNotNull(token, "Missing the required parameter 'token' when calling certPostOrderIdDryRun");
		Preconditions.checkNotNull(id, "Missing the required parameter 'token' when calling certPostOrderIdDryRun");
		PostAccountsAccountNumberOrdersIdDryRun closeOrderDryRun = OrdersFactory
				.createPostAccountsAccountNumberOrdersIdDryRun(postAccountsAccountNumberOrders);
		ObjectMapper objectMapper = ApiUtilities.createObjectMapper();
		PlacedOrderResponse orderResponse = null;
		try {
			String postOrderString = objectMapper.writeValueAsString(closeOrderDryRun);
			String orderResponseStr = impl.postOrderIdDryRun(this.baseUrl, token, accountNumber, id, postOrderString);
			orderResponse = objectMapper.readValue(orderResponseStr, PlacedOrderResponse.class);
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage());
			logger.error(e.getStackTrace().toString());
		}
		return orderResponse;
	}

	
	public PlacedOrderResponse putOrdersId(final String accountNumber, final String token, String id,
			PutAccountsAccountNumberOrdersId putAccountsAccountNumberOrdersId) {
		Preconditions.checkNotNull(accountNumber,
				"Missing the required parameter 'accountNumber' when calling certPutOrder");
		Preconditions.checkNotNull(token, "Missing the required parameter 'token' when calling certPutOrder");
		ObjectMapper objectMapper = ApiUtilities.createObjectMapper();
		PlacedOrderResponse orderResponse = null;
		try {
			String putOrderString = objectMapper.writeValueAsString(putAccountsAccountNumberOrdersId);
			String orderResponseStr = impl.putOrdersId(this.baseUrl, token, accountNumber, id, putOrderString);
			orderResponse = objectMapper.readValue(orderResponseStr, PlacedOrderResponse.class);
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage());
			logger.error(e.getStackTrace().toString());
		}
		return orderResponse;
	}
	
	private class OrderApiImpl {
		/**
		 * 
		 * Requests order cancellation
		 * <p>
		 * <b>200</b> - Requests order cancellation
		 * 
		 * @param accountNumber (required)
		 * @param id            (required)
		 * @return ResponseEntity&lt;Order&gt;
		 * @throws RestClientException if an error occurs while attempting to invoke the
		 *                             API
		 */
		public String deleteOrder(final String serverUrl, final String token, String accountNumber, String id)
				throws RestClientException {
			Preconditions.checkNotNull(id, "Missing the required parameter 'token' when calling deleteOrder");
			RestTemplate restTemplate = new RestTemplate();
			String url = serverUrl + "/accounts/" + accountNumber + "/orders/" + id;
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", token);
			headers.setContentType(MediaType.APPLICATION_JSON);

			try {
				HttpEntity<String> request = new HttpEntity<String>(headers);
				ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, request, String.class);

				if (response == null) {
					throw new RestClientException("ResponseEntity is null");
				}
				if (response.getStatusCode().is2xxSuccessful()) {
					JsonReader jsonReader = Json.createReader(new StringReader(response.getBody()));
					JsonObject object = jsonReader.readObject();
					jsonReader.close();
					return object.getJsonObject("data").toString();
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
			}
		}

		/**
		 * 
		 * Returns a paginated list of the customer&#39;s orders (as identified by the
		 * provided authentication token) based on sort param. If no sort is passed in,
		 * it defaults to descending order.
		 * <p>
		 * <b>200</b> - Returns a paginated list of the customer&#39;s orders (as
		 * identified by the provided authentication token) based on sort param. If no
		 * sort is passed in, it defaults to descending order.
		 * 
		 * @param accountNumber            (required)
		 * @param perPage                  (optional, default to 10)
		 * @param pageOffset               (optional, default to 0)
		 * @param startDate                (optional)
		 * @param endDate                  (optional)
		 * @param underlyingSymbol         (optional)
		 * @param status                   (example:
		 *                                 status[]&#x3D;{value1}&amp;status[]&#x3D;{value2})
		 *                                 (optional)
		 * @param futuresSymbol            Used to fetch both futures and futures
		 *                                 options orders (optional)
		 * @param underlyingInstrumentType Underlying instrument type (optional)
		 * @param sort                     The order to sort results in. Accepts
		 *                                 &#x60;Desc&#x60; or &#x60;Asc&#x60;. Defaults
		 *                                 to &#x60;Desc&#x60; (optional, default to
		 *                                 Desc)
		 * @param startAt                  DateTime start rage for filtering orders in
		 *                                 full date-time (optional)
		 * @param endAt                    DateTime end range for filtering orders in
		 *                                 full date-time (optional)
		 * @return ResponseEntity&lt;List&lt;Order&gt;&gt;
		 * @throws RestClientException if an error occurs while attempting to invoke the
		 *                             API
		 */
		public List<Order> getOrders(String accountNumber, Integer perPage, Integer pageOffset, LocalDate startDate,
				LocalDate endDate, String underlyingSymbol, List<String> status, String futuresSymbol,
				String underlyingInstrumentType, String sort, OffsetDateTime startAt, OffsetDateTime endAt,
				String serverUrl, String token) throws RestClientException {
			RestTemplate restTemplate = new RestTemplate();
			List<Order> orders = new ArrayList<>();
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", token);
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<Void> request = new HttpEntity<>(headers);
			String baseUri = serverUrl + "/accounts/" + accountNumber + "/orders";

			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUri);
			if (perPage != null)
				builder.queryParam("per-page", perPage);
			if (pageOffset != null)
				builder.queryParam("page-offset", pageOffset);
			if (startDate != null)
				builder.queryParam("start-date", startDate);
			if (endDate != null)
				builder.queryParam("end-date", endDate);
			if (underlyingSymbol != null)
				builder.queryParam("underlying-symbol", underlyingSymbol);
			if (status != null)
				builder.queryParam("status", String.join(",", status));
			if (futuresSymbol != null)
				builder.queryParam("futures-symbol", futuresSymbol);
			if (underlyingInstrumentType != null)
				builder.queryParam("underlying-instrument-type", underlyingInstrumentType);
			if (sort != null)
				builder.queryParam("sort", sort);
			if (startAt != null)
				builder.queryParam("start-at", startAt);
			if (endAt != null)
				builder.queryParam("end-at", endAt);
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
						String orderStr = array.get(i).asJsonObject().toString();
						Order o = objectMapper.readValue(orderStr, Order.class);
						orders.add(o);
					}
					jsonReader.close();
					return orders;
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

		/**
		 * 
		 * Returns a single order based on the id
		 * <p>
		 * <b>200</b> - Returns a single order based on the id
		 * 
		 * @param accountNumber (required)
		 * @param id            (required)
		 * @return ResponseEntity&lt;Order&gt;
		 * @throws RestClientException if an error occurs while attempting to invoke the
		 *                             API
		 */
		public String getOrdersId(final String serverUrl, final String accountNumber, final String token, final String id) {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", token);
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<Void> request = new HttpEntity<>(headers);
			String url = serverUrl + "/accounts/" + accountNumber + "/orders/" + id;
			try {
				ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
				JsonReader jsonReader = Json.createReader(new StringReader(response.getBody()));
				JsonObject object = jsonReader.readObject();
				jsonReader.close();
				return object.getJsonObject("data").toString();
			} catch (HttpClientErrorException e) {
				logger.error(e.getMessage());
				logger.error(e.getStackTrace().toString());
				return null;
			}

		}

		/**
		 * 
		 * Returns a list of live orders for the resource
		 * <p>
		 * <b>200</b> - Returns a list of live orders for the resource
		 * 
		 * @param accountNumber (required)
		 * @return List&lt;Order&gt;
		 */
		public List<Order> getOrdersLive(String accountNumber, String serverUrl, String token) {
			RestTemplate restTemplate = new RestTemplate();
			List<Order> orders = new ArrayList<>();

			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", token);
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<Void> request = new HttpEntity<>(headers);
			String url = serverUrl + "/accounts/" + accountNumber + "/orders/live";
			try {
				ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
				JsonReader jsonReader = Json.createReader(new StringReader(response.getBody()));
				JsonObject object = jsonReader.readObject();
				ObjectMapper objectMapper = ApiUtilities.createObjectMapper();
				JsonArray array = object.getJsonObject("data").getJsonArray("items");
				for (int i = 0; i < array.size(); i++) {
					String orderStr = array.get(i).asJsonObject().toString();
					Order o = objectMapper.readValue(orderStr, Order.class);
					orders.add(o);
				}
				jsonReader.close();
				return orders;
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

		public boolean isGoodResponse(PlacedOrderResponse orderResponse) {
			if (orderResponse == null) {
				logger.error("Bad PlacedOrderResponse (null).");
				return false;
			}
			if (orderResponse.getErrors() != null) {
				logger.error("PlacedOrderResponse has error:\n" + orderResponse.getErrors().toString());
				return false;
			}
			return true;
		}

		/**
		 * 
		 * Accepts a json document containing parameters to create an order and then
		 * runs the prefights without placing the order.
		 * <p>
		 * <b>201</b> - Accepts a json document containing parameters to create an order
		 * and \\ then runs the prefights without placing the order.
		 * 
		 * @param accountNumber                         (required)
		 * @param postAccountsAccountNumberOrdersDryRun (required)
		 * @return PlacedOrderResponse
		 * @throws RestClientException if an error occurs while attempting to invoke the
		 *                             API
		 */
		public String postOrder(final String serverUrl, final String token, String accountNumber, String postOrderString,
				boolean dryRun) throws RestClientException {
			Preconditions.checkNotNull(postOrderString, "Missing the required parameter 'token' when calling postOrder");
			RestTemplate restTemplate = new RestTemplate();
			String url = serverUrl + "/accounts/" + accountNumber + "/orders";
			if (dryRun)
				url += "/dry-run";

			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", token);
			headers.setContentType(MediaType.APPLICATION_JSON);
			ResponseEntity<String> response = null;
			try {
				HttpEntity<String> request = new HttpEntity<String>(postOrderString, headers);
				response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

				if (response == null) {
					throw new RestClientException("ResponseEntity is null");
				}
				if (response.getStatusCode().is2xxSuccessful()) {
					JsonReader jsonReader = Json.createReader(new StringReader(response.getBody()));
					JsonObject object = jsonReader.readObject();
					jsonReader.close();
					return object.getJsonObject("data").toString();
				} else {
					// The error handler built into the RestTemplate should handle 400 and 500
					// series errors.
					throw new RestClientException("API returned " + response.getStatusCode()
							+ " and it wasn't handled by the RestTemplate error handler");
				}
			} catch (HttpServerErrorException e) {
				logger.error(e.getMessage());
				return null;
			} catch (HttpClientErrorException e) {
				logger.error(e.getMessage());
				if (e.getStatusCode().is4xxClientError() && e.getStatusText().equals("Unprocessable Entity")) {
					JsonReader jsonReader = Json.createReader(new StringReader(e.getResponseBodyAsString()));
					JsonObject object = jsonReader.readObject();
					jsonReader.close();
					String errorMsg = Json.createObjectBuilder()
							.add("errors", object.getJsonObject("error").getJsonArray("errors")).build().toString();
					return errorMsg;
				}
				return null;
			}
		}

		public String postOrderIdDryRun(String serverUrl, String token, String accountNumber, String id,
				String postOrderString) {
			Preconditions.checkNotNull(postOrderString,
					"Missing the required parameter 'postOrderString' when calling postOrderIdDryRun");
			RestTemplate restTemplate = new RestTemplate();
			String url = serverUrl + "/accounts/" + accountNumber + "/orders/" + id + "/dry-run";

			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", token);
			headers.setContentType(MediaType.APPLICATION_JSON);
			ResponseEntity<String> response = null;
			try {
				HttpEntity<String> request = new HttpEntity<String>(postOrderString, headers);
				response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

				if (response == null) {
					throw new RestClientException("ResponseEntity is null");
				}
				if (response.getStatusCode().is2xxSuccessful()) {
					JsonReader jsonReader = Json.createReader(new StringReader(response.getBody()));
					JsonObject object = jsonReader.readObject();
					jsonReader.close();
					return object.getJsonObject("data").toString();
				} else {
					// The error handler built into the RestTemplate should handle 400 and 500
					// series errors.
					throw new RestClientException("API returned " + response.getStatusCode()
							+ " and it wasn't handled by the RestTemplate error handler");
				}
			} catch (HttpServerErrorException e) {
				logger.error(e.getMessage());
				return null;
			} catch (HttpClientErrorException e) {
				logger.error(e.getMessage());
				if (e.getStatusCode().is4xxClientError() && e.getStatusText().equals("Unprocessable Entity")) {
					JsonReader jsonReader = Json.createReader(new StringReader(e.getResponseBodyAsString()));
					JsonObject object = jsonReader.readObject();
					jsonReader.close();
					String errorMsg = Json.createObjectBuilder()
							.add("errors", object.getJsonObject("error").getJsonArray("errors")).build().toString();
					return errorMsg;
				}
				return null;
			}
		}

		/**
		 * 
		 * Replaces a live order with a new one. Subsequent fills of the original order
		 * will abort the replacement.
		 * <p>
		 * <b>200</b> - Replaces a live order with a new one. Subsequent fills of the
		 * original order will abort the replacement.
		 * 
		 * @param accountNumber                    (required)
		 * @param id                               (required)
		 * @param putAccountsAccountNumberOrdersId (required)
		 * @return ResponseEntity&lt;Order&gt;
		 * @throws RestClientException if an error occurs while attempting to invoke the
		 *                             API
		 */
		public String putOrdersId(final String serverUrl, final String token, String accountNumber, String id,
				String putOrderString) throws RestClientException {
			Preconditions.checkNotNull(id, "Missing the required parameter 'token' when calling putOrder");
			Preconditions.checkNotNull(putOrderString, "Missing the required parameter 'token' when calling putOrder");
			RestTemplate restTemplate = new RestTemplate();
			String url = serverUrl + "/accounts/" + accountNumber + "/orders/" + id;
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", token);
			headers.setContentType(MediaType.APPLICATION_JSON);

			try {
				HttpEntity<String> request = new HttpEntity<String>(putOrderString, headers);
				ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);

				if (response == null) {
					throw new RestClientException("ResponseEntity is null");
				}
				if (response.getStatusCode().is2xxSuccessful()) {
					JsonReader jsonReader = Json.createReader(new StringReader(response.getBody()));
					JsonObject object = jsonReader.readObject();
					jsonReader.close();
					return object.getJsonObject("data").toString();
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
			}
		}
	}

}
