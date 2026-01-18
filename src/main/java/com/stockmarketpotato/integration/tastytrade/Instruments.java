package com.stockmarketpotato.integration.tastytrade;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import com.stockmarketpotato.integration.tastytrade.model.accounts.AccountPosition;
import com.stockmarketpotato.integration.tastytrade.model.instruments.Equity;
import com.stockmarketpotato.integration.tastytrade.model.instruments.Future;
import com.stockmarketpotato.integration.tastytrade.model.instruments.FuturesNestedOptionChainSerializer;

public class Instruments extends TastytradeApi {
	public Instruments(BASE base) {
		super(base);
	}

	public Instruments() {
		super();
	}

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final InstrumentsApiImpl impl = new InstrumentsApiImpl();

	/**
	 * 
	 * Returns a futures option chain given a futures product code in a nested form
	 * to minimize redundant processing
	 * <p>
	 * <b>200</b> - Returns a futures option chain given a futures product code in a
	 * nested form to minimize redundant processing
	 * 
	 * @param symbol (required)
	 * @return FuturesNestedOptionChainSerializer
	 */
	public FuturesNestedOptionChainSerializer getFuturesOptionChainsSymbolNested(String token, String symbol) {
		return impl.getFuturesOptionChainsSymbolNested(this.baseUrl, token, symbol);
	}

	/**
	 * 
	 * Returns a set of outright futures given an array of one or more symbols.
	 * <p>
	 * <b>200</b> - Returns a set of outright futures given an array of one or more
	 * symbols.
	 * 
	 * @param symbol      The symbol of the future(s), i.e.
	 *                    &#x60;symbol[]&#x3D;ESZ9&#x60;. Leading forward slash is
	 *                    not required. (example:
	 *                    symbol[]&#x3D;{value1}&amp;symbol[]&#x3D;{value2})
	 *                    (optional)
	 * @param productCode The product code of the future(s), i.e.
	 *                    &#x60;product-code[]&#x3D;ES&amp;product-code[]&#x3D;6A&#x60;.
	 *                    \\ Ignored if &#x60;symbol&#x60; parameter is given.
	 *                    (example:
	 *                    product-code[]&#x3D;{value1}&amp;product-code[]&#x3D;{value2})
	 *                    (optional)
	 * @return List&lt;Future&gt;
	 */
	public List<Future> getFutures(String token, List<String> symbol, List<String> productCode) {
		return impl.getFutures(this.baseUrl, token, symbol, productCode);
	}

	/**
	 * 
	 * Returns a set of equity definitions given an array of one or more symbols
	 * <p>
	 * <b>200</b> - Returns a set of equity definitions given an array of one or
	 * more symbols
	 * 
	 * @param symbol      The symbol of the equity(s), i.e &#x60;AAPL&#x60;
	 *                    (example:
	 *                    symbol[]&#x3D;{value1}&amp;symbol[]&#x3D;{value2})
	 *                    (optional)
	 * @param lendability Lendability {Easy To Borrow, Locate Required, Preborrow]
	 *                    (optional)
	 * @param isIndex     Flag indicating if equity is an index instrument
	 *                    (optional)
	 * @param isEtf       Flag indicating if equity is an etf instrument (optional)
	 * @return List&lt;Equity&gt;
	 */
	public List<Equity> getEquities(String token, List<String> symbol, String lendability, Boolean isIndex,
			Boolean isEtf) {
		return impl.getEquities(this.baseUrl, token, symbol, lendability, isIndex, isEtf);
	}

	/**
	 * To receive live market event data via DXLink, clients must convert symbols
	 * into a format that meets DxLink's requirements. For convenience, tastytrade
	 * provide these symbols via a field called streamer-symbol. This function
	 * fetches instrument data endpoint that contains the streamer symbol.
	 * 
	 * @param position An existing AccountPosition
	 * @return Streamer Symbol for symbol in position
	 */
	public String getStreamerSymbol(String token, AccountPosition position) {
		return impl.getStreamerSymbol(this.baseUrl, token, position);
	}

	private class InstrumentsApiImpl {
		public FuturesNestedOptionChainSerializer getFuturesOptionChainsSymbolNested(String serverUrl, String token,
				String symbol) {
			String url = serverUrl + "/futures-option-chains/" + symbol + "/nested";
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", token);
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<Void> request = new HttpEntity<>(headers);
			RestTemplate restTemplate = new RestTemplate();
			try {
				ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
				JsonReader jsonReader = Json.createReader(new StringReader(response.getBody()));
				JsonObject object = jsonReader.readObject();
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
				objectMapper.registerModule(new JavaTimeModule());
				String balanceStr = object.getJsonObject("data").toString();
				FuturesNestedOptionChainSerializer foc = objectMapper.readValue(balanceStr,
						FuturesNestedOptionChainSerializer.class);
				return foc;
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

		public List<Future> getFutures(String serverUrl, String token, List<String> symbol, List<String> productCode) {
			List<Future> f = new ArrayList<>();
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", token);
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<Void> request = new HttpEntity<>(headers);
			String url = serverUrl;
			url += "/instruments/futures";

			// Query parameters
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
			for (String s : symbol)
				builder.queryParam("symbol", s);
			for (String p : productCode)
				builder.queryParam("product-code", p);
			url = builder.build().encode().toUriString();
			RestTemplate restTemplate = new RestTemplate();
			try {
				ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
				JsonReader jsonReader = Json.createReader(new StringReader(response.getBody()));
				JsonObject object = jsonReader.readObject();
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
				objectMapper.registerModule(new JavaTimeModule());
				JsonArray array = object.getJsonObject("data").getJsonArray("items");
				for (int i = 0; i < array.size(); i++) {
					String futureStr = array.get(i).asJsonObject().toString();
					Future fut = objectMapper.readValue(futureStr, Future.class);
					f.add(fut);
				}
				jsonReader.close();
				return f;
			} catch (HttpClientErrorException | JsonProcessingException e) {
				logger.error(e.getMessage());
				return f;
			}
		}

		public List<Equity> getEquities(String serverUrl, String token, List<String> symbol, String lendability,
				Boolean isIndex, Boolean isEtf) {
			List<Equity> equities = new ArrayList<>();
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", token);
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<Void> request = new HttpEntity<>(headers);
			String url = serverUrl;
			url += "/instruments/equities";

			// Query parameters
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
			for (String s : symbol)
				builder.queryParam("symbol", s);
			if (lendability != null)
				builder.queryParam("lendability", lendability);
			if (isIndex != null)
				builder.queryParam("is-index", isIndex);
			if (isEtf != null)
				builder.queryParam("is-etf", isEtf);
			url = builder.build().encode().toUriString();
			RestTemplate restTemplate = new RestTemplate();
			try {
				ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
				JsonReader jsonReader = Json.createReader(new StringReader(response.getBody()));
				JsonObject object = jsonReader.readObject();
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
				objectMapper.registerModule(new JavaTimeModule());
				JsonArray array = object.getJsonObject("data").getJsonArray("items");
				for (int i = 0; i < array.size(); i++) {
					String equityStr = array.get(i).asJsonObject().toString();
					Equity equity = objectMapper.readValue(equityStr, Equity.class);
					equities.add(equity);
				}
				jsonReader.close();
				return equities;
			} catch (HttpClientErrorException | JsonProcessingException e) {
				logger.error(e.getMessage());
				return equities;
			}
		}

		public String getStreamerSymbol(String serverUrl, String token, AccountPosition position) {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", token);
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<Void> request = new HttpEntity<>(headers);
			String url = serverUrl;
			String symbol = position.symbol;

			if (position.instrument_type.equals("Cryptocurrency")) {
				url += "/instruments/cryptocurrencies/" + symbol;
			} else if (position.instrument_type.equals("Equity") || position.instrument_type.equals("Index")) {
				url += "/instruments/equities/" + symbol;
			} else if (position.instrument_type.equals("Equity Option")) {
				url += "/instruments/equity-options/" + symbol;
			} else if (position.instrument_type.equals("Future")) {
				url += "/instruments/futures/" + symbol;
			} else if (position.instrument_type.equals("Future Option")) {
				url += "/instruments/future-options/" + symbol;
			} else if (position.instrument_type.equals("Warrant"))
				url += "/instruments/warrants/" + symbol;
			RestTemplate restTemplate = new RestTemplate();
			try {
				ResponseEntity<String> response = restTemplate.exchange(
						URLDecoder.decode(url, StandardCharsets.UTF_8.toString()).toString(), HttpMethod.GET, request,
						String.class);
				JsonReader jsonReader = Json.createReader(new StringReader(response.getBody()));
				JsonObject object = jsonReader.readObject();
				jsonReader.close();
				return object.getJsonObject("data").getString("streamer-symbol");
			} catch (HttpClientErrorException | UnsupportedEncodingException e) {
				logger.error(e.getMessage());
				return "";
			}
		}
	}

}