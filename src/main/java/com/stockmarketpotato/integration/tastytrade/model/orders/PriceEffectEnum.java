package com.stockmarketpotato.integration.tastytrade.model.orders;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * If pay or receive payment for placing the order. i.e. &#x60;Credit&#x60; or
 * &#x60;Debit&#x60;
 */
public enum PriceEffectEnum {
	CREDIT("Credit"),

	DEBIT("Debit");

	private String value;

	PriceEffectEnum(String value) {
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
	public static PriceEffectEnum fromValue(String value) {
		for (PriceEffectEnum b : PriceEffectEnum.values()) {
			if (b.value.equals(value)) {
				return b;
			}
		}
		throw new IllegalArgumentException("Unexpected value '" + value + "'");
	}
}