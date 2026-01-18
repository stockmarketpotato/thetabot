package com.stockmarketpotato.integration.tastytrade.model.sessions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
	@JsonProperty("email")
	public String email;
	@JsonProperty("name")
	public String name;
	@JsonProperty("nickname")
	public String nickname;
	@JsonProperty("username")
	public String username;
	@JsonProperty("external-id")
	public String external_id;
	@JsonProperty("is-confirmed")
	public String is_confirmed;
}
