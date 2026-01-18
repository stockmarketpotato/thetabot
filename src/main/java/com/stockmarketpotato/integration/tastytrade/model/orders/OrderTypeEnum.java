package com.stockmarketpotato.integration.tastytrade.model.orders;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The type of order in regards to the price. i.e. &#x60;Limit&#x60;,
 * &#x60;Market&#x60;, &#x60;Marketable Limit&#x60;, &#x60;Stop&#x60;,
 * &#x60;Stop Limit&#x60; or &#x60;Notional Market&#x60;
 */
public enum OrderTypeEnum {
	LIMIT("Limit"),

	MARKET("Market"),

	MARKETABLE_LIMIT("Marketable Limit"),

	STOP("Stop"),

	STOP_LIMIT("Stop Limit"),

	NOTIONAL_MARKET("Notional Market");

	private String value;

	OrderTypeEnum(String value) {
		this.value = value;
	}

	@JsonValue
	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static OrderTypeEnum fromValue(String value) {
		for (OrderTypeEnum b : OrderTypeEnum.values()) {
			if (b.value.equals(value)) {
				return b;
			}
		}
		throw new IllegalArgumentException("Unexpected value '" + value + "'");
	}
}
