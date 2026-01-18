package com.stockmarketpotato.integration.tastytrade.model.orders;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The directional action of the leg. i.e. &#x60;Sell to Open&#x60;, &#x60;Sell
 * to Close&#x60;, &#x60;Buy to Open&#x60;, &#x60;Buy to Close&#x60;,
 * &#x60;Sell&#x60; or &#x60;Buy&#x60;. Note: &#x60;Buy&#x60; and
 * &#x60;Sell&#x60; are only applicable to Futures orders.
 */
public enum ActionEnum {
	SELL_TO_OPEN("Sell to Open"),

	SELL_TO_CLOSE("Sell to Close"),

	BUY_TO_OPEN("Buy to Open"),

	BUY_TO_CLOSE("Buy to Close"),

	SELL("Sell"),

	BUY("Buy");

	private String value;

	ActionEnum(String value) {
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
	public static ActionEnum fromValue(String value) {
		for (ActionEnum b : ActionEnum.values()) {
			if (b.value.equals(value)) {
				return b;
			}
		}
		throw new IllegalArgumentException("Unexpected value '" + value + "'");
	}
}
