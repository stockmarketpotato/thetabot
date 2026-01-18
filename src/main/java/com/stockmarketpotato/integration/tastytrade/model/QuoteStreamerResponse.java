package com.stockmarketpotato.integration.tastytrade.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QuoteStreamerResponse {
	@JsonProperty("token")
	public String token;
    @JsonProperty("websocket-url")
    public String websocket_url;
    @JsonProperty("dxlink-url")
    public String dxlink_url;
    @JsonProperty("level")
    public String level;
}
