package com.stockmarketpotato.integration.tastytrade.model.orders;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The type of Instrument. i.e. &#x60;Cryptocurrency&#x60;, &#x60;Equity&#x60;,
 * &#x60;Equity Offering&#x60;, &#x60;Equity Option&#x60;, &#x60;Future&#x60; or
 * &#x60;Future Option&#x60;
 */
public enum InstrumentTypeEnum {
	CRYPTOCURRENCY("Cryptocurrency"),

	EQUITY("Equity"),

	EQUITY_OFFERING("Equity Offering"),

	EQUITY_OPTION("Equity Option"),

	FUTURE("Future"),

	FUTURE_OPTION("Future Option");

	private String value;

	InstrumentTypeEnum(String value) {
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
	public static InstrumentTypeEnum fromValue(String value) {
		for (InstrumentTypeEnum b : InstrumentTypeEnum.values()) {
			if (b.value.equals(value)) {
				return b;
			}
		}
		throw new IllegalArgumentException("Unexpected value '" + value + "'");
	}
}
