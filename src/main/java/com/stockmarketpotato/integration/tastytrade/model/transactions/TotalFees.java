package com.stockmarketpotato.integration.tastytrade.model.transactions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import com.stockmarketpotato.integration.tastytrade.model.orders.PriceEffectEnum;

@JsonPropertyOrder({ TotalFees.JSON_PROPERTY_TOTAL_FEES, TotalFees.JSON_PROPERTY_TOTAL_FEES_EFFECT })
@JsonIgnoreProperties(ignoreUnknown = true)
public class TotalFees {
	public static final String JSON_PROPERTY_TOTAL_FEES = "total-fees";
	private Double totalFees;

	public static final String JSON_PROPERTY_TOTAL_FEES_EFFECT = "total-fees-effect";
	private PriceEffectEnum totalFeesEffect; // Debit or Credit

	@javax.annotation.Nullable
	@JsonProperty(JSON_PROPERTY_TOTAL_FEES)
	@JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
	public Double getTotalFees() {
		return totalFees;
	}

	@JsonProperty(JSON_PROPERTY_TOTAL_FEES)
	@JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
	public void setTotalFees(Double totalFees) {
		this.totalFees = totalFees;
	}

	@javax.annotation.Nonnull
	@JsonProperty(JSON_PROPERTY_TOTAL_FEES_EFFECT)
	@JsonInclude(value = JsonInclude.Include.ALWAYS)
	public PriceEffectEnum getTotalFeesEffect() {
		return totalFeesEffect;
	}

	@JsonProperty(JSON_PROPERTY_TOTAL_FEES_EFFECT)
	@JsonInclude(value = JsonInclude.Include.ALWAYS)
	public void setTotalFeesEffect(PriceEffectEnum totalFeesEffect) {
		this.totalFeesEffect = totalFeesEffect;
	}

}
