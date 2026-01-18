package com.stockmarketpotato.broker;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.json.JsonObject;

public class ApiUtilities {
	
	public static ObjectMapper createObjectMapper() {
		return new ObjectMapper()
				.setSerializationInclusion(Include.NON_NULL)
				.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
				.registerModule(new JavaTimeModule());
	}

	public static boolean containsOrder(final JsonObject object) {
		return object.containsKey("type") && object.getString("type").equals("Order");
	}
	
	public static BigDecimal roundToTickSize(final BigDecimal tickSize, final BigDecimal price) {
		return price.divide(tickSize, 0, RoundingMode.HALF_UP).multiply(tickSize);
	}
}
