package com.stockmarketpotato.integration.tastytrade.model.sessions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SessionResponse {
	@JsonProperty("remember-token")
	public String remember_token;
	@JsonProperty("session-token")
	public String session_token;
	@JsonProperty("user")
	public User user;
}
