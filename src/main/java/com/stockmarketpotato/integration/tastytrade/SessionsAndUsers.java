package com.stockmarketpotato.integration.tastytrade;

import java.io.StringReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockmarketpotato.broker.ApiUtilities;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import com.stockmarketpotato.integration.tastytrade.model.sessions.SessionResponse;

public class SessionsAndUsers extends TastytradeApi {
	public SessionsAndUsers(BASE base) {
		super(base);
	}

	public SessionsAndUsers() {
		super();
	}

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private SessionsApiImpl impl = new SessionsApiImpl();

	public SessionResponse login(String login, String pw)  {
		return impl.login(this.baseUrl, login, pw);
	}

	public SessionResponse loginWithRememberToken(String login, String rememberToken)  {
		return impl.loginWithRememberToken(this.baseUrl, login, rememberToken);
	}
	
	private class SessionsApiImpl {
		private RestTemplate restTemplate = new RestTemplate();
		
		/**
		 * This method retrieves a new session and a new remember token from endpoint
		 * /sessions.
		 * 
		 * @param baseUrl The base URL for the
		 * @param login   user name or email
		 * @param pw      the password
		 * @return SessionResponse Returns the session details or null if invalid
		 */
		private final SessionResponse login(String baseUrl, String login, String pw) {
			JsonObject value = Json.createObjectBuilder().add("login", login).add("password", pw)
					.add("remember-me", true).build();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			String jsonString = value.toString();
			HttpEntity<String> request = new HttpEntity<String>(jsonString, headers);
			SessionResponse session = null;
			try {
				ResponseEntity<String> response = restTemplate.exchange(baseUrl + "/sessions", HttpMethod.POST,
						request, String.class);
				JsonReader jsonReader = Json.createReader(new StringReader(response.getBody()));
				JsonObject object = jsonReader.readObject();
				jsonReader.close();
				ObjectMapper objectMapper = ApiUtilities.createObjectMapper();
				String sessionStr = object.getJsonObject("data").toString();
				session = objectMapper.readValue(sessionStr, SessionResponse.class);
			} catch (HttpClientErrorException | JsonProcessingException e) {
				logger.error(e.getMessage());
				return null;
			}

			// check validity of Session Token
			if (session != null && validate(baseUrl, session.session_token))
				return session;
			return null;
		}
		
		/**
		 * 
		 * @param baseUrl 		The base URL for the
		 * @param login   		user name or email
		 * @param rememberToken A valid remember token
		 * @return SessionResponse Returns the session details or null if invalid
		 */
		public SessionResponse loginWithRememberToken(String baseUrl, String login, String rememberToken) {
			JsonObject value = Json.createObjectBuilder()
					.add("login", login)
					.add("remember-token", rememberToken)
					.add("remember-me", true).build();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			String jsonString = value.toString();
			HttpEntity<String> request = new HttpEntity<String>(jsonString, headers);
			SessionResponse session = null;
			try {
				ResponseEntity<String> response = restTemplate.exchange(baseUrl + "/sessions", HttpMethod.POST,
						request, String.class);
				JsonReader jsonReader = Json.createReader(new StringReader(response.getBody()));
				JsonObject object = jsonReader.readObject();
				jsonReader.close();
				ObjectMapper objectMapper = ApiUtilities.createObjectMapper();
				String sessionStr = object.getJsonObject("data").toString();
				session = objectMapper.readValue(sessionStr, SessionResponse.class);
			} catch (HttpClientErrorException | JsonProcessingException e) {
				logger.error(e.getMessage());
				return null;
			}

			// check validity of Session Token
			if (session != null && validate(baseUrl, session.session_token))
				return session;
			return null;
		}

		/**
		 * This method queries the endpoint /sessions/validate to validate the session
		 * token.
		 * 
		 * @return boolean Returns true, if the session token is valid and false
		 *         otherwise.
		 */
		private boolean validate(String baseUrl, String sessionToken) {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", sessionToken);
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<Void> request = new HttpEntity<>(headers);
			try {
				ResponseEntity<String> response = restTemplate.exchange(baseUrl + "/sessions/validate",
						HttpMethod.POST, request, String.class);
				if (response.getStatusCode() == HttpStatus.CREATED)
					return true;
			} catch (HttpClientErrorException e) {
				logger.error(e.getMessage());
				return false;
			}
			return true;
		}
	}
}
