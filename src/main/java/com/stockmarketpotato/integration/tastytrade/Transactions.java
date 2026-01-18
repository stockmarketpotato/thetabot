package com.stockmarketpotato.integration.tastytrade;

import java.io.StringReader;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockmarketpotato.broker.ApiUtilities;
import com.stockmarketpotato.integration.tastytrade.model.transactions.TotalFees;
import com.stockmarketpotato.integration.tastytrade.model.transactions.Transaction;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Transactions extends TastytradeApi {
	public Transactions(BASE base) {
		super(base);
	}

	public Transactions() {
		super();
	}

	private TransactionsApiImpl impl = new TransactionsApiImpl();
	
	public TotalFees getTransactionsTotalFees(String token, String accountNumber, LocalDate date) {
		return impl.getTransactionsTotalFees(this.baseUrl, token, accountNumber, date);
	}
	
	public Transaction getTransactionById(String token, String accountNumber, String id) {
		return impl.getTransactionById(this.baseUrl, token, accountNumber, id);
	}
	
	public List<Transaction> getTransactions(String token, String accountNumber, Integer perPage,
			Integer pageOffset, String sort, String type, List<String> types, List<String> subType,
			LocalDate startDate, LocalDate endDate, String instrumentType, String symbol, String underlyingSymbol,
			String action, String partitionKey, String futuresSymbol, OffsetDateTime startAt, OffsetDateTime endAt) {
		return impl.getTransactions(this.baseUrl, token, accountNumber, perPage, pageOffset, sort, type, types, subType,
				startDate, endDate, instrumentType, symbol, underlyingSymbol, action, partitionKey, futuresSymbol,
				startAt, endAt);
	}
	
	private class TransactionsApiImpl {
		private final Logger logger = LoggerFactory.getLogger(this.getClass());
		
	    /**
	     * 
	     * Return the total fees for an account for a given day
	     * <p><b>200</b> - Return the total fees for an account for a given day
	     * @param accountNumber  (required)
	     * @param date The date to get fees for, defaults to today (optional)
	     * @return 
	     * @return ResponseEntity&lt;Void&gt;
	     */
	    public TotalFees getTransactionsTotalFees(String baseUrl, String token, String accountNumber, LocalDate date) {
	        // verify the required parameter 'baseUrl' is set
	        if (baseUrl == null) {
	            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'baseUrl' when calling getTransactionsTotalFees");
	        }
	        
	        // verify the required parameter 'token' is set
	        if (token == null) {
	            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'token' when calling getTransactionsTotalFees");
	        }
	        
	        // verify the required parameter 'accountNumber' is set
	        if (accountNumber == null) {
	            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'accountNumber' when calling getTransactionsTotalFees");
	        }

	    	RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", token);
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<Void> request = new HttpEntity<>(headers);
			String baseUri = baseUrl + "/accounts/" + accountNumber + "/transactions/total-fees";
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUri);
			if (date != null)
				builder.queryParam("date", date);
			String url = builder.build().encode().toUriString();
			try {
				ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
				if (response == null) {
					throw new RestClientException("ResponseEntity is null");
				}
				if (response.getStatusCode().is2xxSuccessful()) {
					JsonReader jsonReader = Json.createReader(new StringReader(response.getBody()));
					JsonObject object = jsonReader.readObject();
					jsonReader.close();
					ObjectMapper objectMapper = ApiUtilities.createObjectMapper();
					String feesStr = object.getJsonObject("data").toString();
					TotalFees f = objectMapper.readValue(feesStr, TotalFees.class);
					return f;
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
			} catch (JsonMappingException e) {
				e.printStackTrace();
				return null;
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				return null;
			}
	    }
	    
	    /**
	     * 
	     * Retrieve a transaction by account number and ID
	     * <p><b>200</b> - Retrieve a transaction by account number and ID
	     * @param id  (required)
	     * @param accountNumber  (required)
	     * @return ResponseEntity&lt;Transaction&gt;
	     * @throws RestClientException if an error occurs while attempting to invoke the API
	     */
	    public Transaction getTransactionById(String baseUrl, String token, String accountNumber, String id) {
	        // verify the required parameter 'baseUrl' is set
	        if (baseUrl == null) {
	            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'baseUrl' when calling getTransactionById");
	        }
	        
	        // verify the required parameter 'token' is set
	        if (token == null) {
	            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'token' when calling getTransactionById");
	        }
	    	
	    	// verify the required parameter 'id' is set
	        if (id == null) {
	            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'id' when calling getAccountsAccountNumberTransactionsId");
	        }
	        
	        // verify the required parameter 'accountNumber' is set
	        if (accountNumber == null) {
	            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'accountNumber' when calling getAccountsAccountNumberTransactionsId");
	        }

	        // ---------------------
	    	RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", token);
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<Void> request = new HttpEntity<>(headers);
			String url = baseUrl + "/accounts/" + accountNumber + "/transactions/" + id;
			try {
				ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
				if (response == null) {
					throw new RestClientException("ResponseEntity is null");
				}
				if (response.getStatusCode().is2xxSuccessful()) {
					JsonReader jsonReader = Json.createReader(new StringReader(response.getBody()));
					JsonObject object = jsonReader.readObject();
					ObjectMapper objectMapper = ApiUtilities.createObjectMapper();
					String transactionStr = object.getJsonObject("data").toString();
					Transaction t = objectMapper.readValue(transactionStr, Transaction.class);
					return t;
				} else {
					// The error handler built into the RestTemplate should handle 400 and 500
					// series errors.
					throw new RestClientException("API returned " + response.getStatusCode()
							+ " and it wasn't handled by the RestTemplate error handler");
				}
			} catch (HttpClientErrorException | JsonProcessingException e) {
				logger.error(e.getMessage());
				logger.error(e.getStackTrace().toString());
				return null;
			}
	    }
	
	    /**
	     * 
	     * Returns a paginated list of the account&#39;s transactions (as identified by the provided authentication token)             based on sort param. If no sort is passed in, it defaults to descending order.
	     * <p><b>200</b> - Returns a paginated list of the account&#39;s transactions (as identified by the provided authentication token)             based on sort param. If no sort is passed in, it defaults to descending order.
	     * @param accountNumber  (required)
	     * @param perPage  (optional, default to 250)
	     * @param pageOffset  (optional, default to 0)
	     * @param sort The order to sort results in. Defaults to Desc, Accepts &#x60;Desc&#x60; or &#x60;Asc&#x60;. (optional, default to Desc)
	     * @param type Filter based on transaction_type (optional)
	     * @param types Allows filtering on multiple transaction_types (example: types[]&#x3D;{value1}&amp;types[]&#x3D;{value2}) (optional)
	     * @param subType Filter based on transaction_sub_type (example: sub-type[]&#x3D;{value1}&amp;sub-type[]&#x3D;{value2}) (optional)
	     * @param startDate The start date of transactions to query. (optional)
	     * @param endDate The end date of transactions to query. Defaults to now. (optional)
	     * @param instrumentType The type of Instrument. i.e. &#x60;Bond&#x60;, &#x60;Cryptocurrency&#x60;, &#x60;Equity&#x60;, &#x60;Equity Offering&#x60;, &#x60;Equity Option&#x60;, &#x60;Future&#x60;, &#x60;Future Option&#x60;, &#x60;Index&#x60;, &#x60;Unknown&#x60; or &#x60;Warrant&#x60; (optional)
	     * @param symbol The Stock Ticker Symbol &#x60;AAPL&#x60;, OCC Option Symbol &#x60;AAPL  191004P00275000&#x60;, \\                                                  TW Future Symbol &#x60;/ESZ9&#x60;, or TW Future Option Symbol &#x60;./ESZ9 EW4U9 190927P2975&#x60; (optional)
	     * @param underlyingSymbol The Underlying Symbol. The Ticker Symbol &#x60;FB&#x60; or \\                                                               TW Future Symbol with out date component &#x60;/M6E&#x60; or                                                               the full TW Future Symbol &#x60;/ESU9&#x60; (optional)
	     * @param action The action of the transaction. i.e. &#x60;Sell to Open&#x60;, &#x60;Sell to Close&#x60;, &#x60;Buy to Open&#x60;, &#x60;Buy to Close&#x60;, &#x60;Sell&#x60; or &#x60;Buy&#x60; (optional)
	     * @param partitionKey Account partition key (optional)
	     * @param futuresSymbol The full TW Future Symbol &#x60;/ESZ9&#x60; or \\                                           &#x60;/NGZ19&#x60; if two year digit are appropriate (optional)
	     * @param startAt DateTime start range for filtering transactions in full date-time (optional)
	     * @param endAt DateTime end range for filtering transactions in full date-time (optional)
	     * @return ResponseEntity&lt;List&lt;Transaction&gt;&gt;
	     * @throws RestClientException if an error occurs while attempting to invoke the API
	     */
		public List<Transaction> getTransactions(String baseUrl, String token, String accountNumber, Integer perPage,
				Integer pageOffset, String sort, String type, List<String> types, List<String> subType,
				LocalDate startDate, LocalDate endDate, String instrumentType, String symbol, String underlyingSymbol,
				String action, String partitionKey, String futuresSymbol, OffsetDateTime startAt, OffsetDateTime endAt)
				throws RestClientException {
	        // verify the required parameter 'baseUrl' is set
	        if (baseUrl == null) {
	            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'baseUrl' when calling getTransactionsTotalFees");
	        }
	        
	        // verify the required parameter 'token' is set
	        if (token == null) {
	            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'token' when calling getTransactionsTotalFees");
	        }
	        
	        // verify the required parameter 'accountNumber' is set
	        if (accountNumber == null) {
	            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'accountNumber' when calling getAccountsAccountNumberTransactions");
	        }
	
	        // ----------------------------
	    	RestTemplate restTemplate = new RestTemplate();
			List<Transaction> transactions = new ArrayList<>();
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", token);
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<Void> request = new HttpEntity<>(headers);
			String baseUri = baseUrl + "/accounts/" + accountNumber + "/transactions";
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUri);
	        if (perPage != null)
	        	builder.queryParam("per-page", perPage);
	        if (pageOffset != null)
	        	builder.queryParam("page-offset", pageOffset);
	        if (sort != null)
	        	builder.queryParam("sort", sort);
	        if (type != null)
	        	builder.queryParam("type", type);
	        if (types != null)
	        	builder.queryParam("types", types);
	        if (subType != null)
	        	builder.queryParam("sub-type", subType);
	        if (startDate != null)
	        	builder.queryParam("start-date", startDate);
	        if (endDate != null)
	        	builder.queryParam("end-date", endDate);
	        if (instrumentType != null)
	        	builder.queryParam("instrument-type", instrumentType);
	        if (symbol != null)
	        	builder.queryParam("symbol", symbol);
	        if (underlyingSymbol != null)
	        	builder.queryParam("underlying-symbol", underlyingSymbol);
	        if (action != null)
	        	builder.queryParam("action", action);
	        if (partitionKey != null)
	        	builder.queryParam("partition-key", partitionKey);
	        if (futuresSymbol != null)
	        	builder.queryParam("futures-symbol", futuresSymbol);
	        if (startAt != null)
	        	builder.queryParam("start-at", startAt);
	        if (endAt != null)
	        	builder.queryParam("end-at", endAt);
			String url = builder.build().encode().toUriString();
			logger.info(url);
			int totalPages = 1;
			try {
				ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
				if (response == null) {
					throw new RestClientException("ResponseEntity is null");
				}
				if (response.getStatusCode().is2xxSuccessful()) {
					JsonReader jsonReader = Json.createReader(new StringReader(response.getBody()));
					JsonObject object = jsonReader.readObject();
					jsonReader.close();
					totalPages = object.getJsonObject("pagination").getInt("total-pages");
					ObjectMapper objectMapper = ApiUtilities.createObjectMapper();
					JsonArray array = object.getJsonObject("data").getJsonArray("items");
					for (int i = 0; i < array.size(); i++) {
						String transactionStr = array.get(i).asJsonObject().toString();
						Transaction t = objectMapper.readValue(transactionStr, Transaction.class);
						transactions.add(t);
					}
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
			} catch (JsonMappingException e) {
				e.printStackTrace();
				return null;
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				return null;
			}
			if (pageOffset == null) {
				for (int i = 1; i < totalPages; i++) {
					transactions.addAll(this.getTransactions(baseUrl, token, accountNumber, perPage, i, sort, type,
							types, subType, startDate, endDate, instrumentType, symbol, underlyingSymbol, action,
							partitionKey, futuresSymbol, startAt, endAt));
				}
			}
			return transactions;
		}
	}
	
    public enum CollectionFormat {
        CSV(","), TSV("\t"), SSV(" "), PIPES("|"), MULTI(null);

        private final String separator;

        private CollectionFormat(String separator) {
            this.separator = separator;
        }

        private String collectionToString(Collection<?> collection) {
            return StringUtils.collectionToDelimitedString(collection, separator);
        }
    }
}
