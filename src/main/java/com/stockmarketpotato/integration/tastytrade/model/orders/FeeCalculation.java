package com.stockmarketpotato.integration.tastytrade.model.orders;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
	FeeCalculation.JSON_PROPERTY_REGULATORY_FEES,
	FeeCalculation.JSON_PROPERTY_REGULATORY_FEES_EFFECT,
	FeeCalculation.JSON_PROPERTY_CLEARING_FEES,
	FeeCalculation.JSON_PROPERTY_CLEARING_FEES_EFFECT,
	FeeCalculation.JSON_PROPERTY_COMMISSION,
	FeeCalculation.JSON_PROPERTY_COMMISSION_EFFECT,
	FeeCalculation.JSON_PROPERTY_PROPRIETARY_INDEX_OPTION_FEES,
	FeeCalculation.JSON_PROPERTY_PROPRIETARY_INDEX_OPTION_FEES_EFFECT,
	FeeCalculation.JSON_PROPERTY_TOTAL_FEES,
	FeeCalculation.JSON_PROPERTY_TOTAL_FEES_EFFECT
})

@JsonIgnoreProperties(ignoreUnknown = true)
public class FeeCalculation {
	public static final String JSON_PROPERTY_REGULATORY_FEES = "regulatory-fees";
	private Double regulatoryFees;

	public static final String JSON_PROPERTY_REGULATORY_FEES_EFFECT = "regulatory-fees-effect";
	private String regulatoryFeesEffect;

	public static final String JSON_PROPERTY_CLEARING_FEES = "clearing-fees";
	private Double clearingFees;

	public static final String JSON_PROPERTY_CLEARING_FEES_EFFECT = "clearing-fees-effect";
	private String clearingFeesEffect;

	public static final String JSON_PROPERTY_COMMISSION = "commission";
	private Double commission;

	public static final String JSON_PROPERTY_COMMISSION_EFFECT = "commission-effect";
	private String commissionEffect;

	public static final String JSON_PROPERTY_PROPRIETARY_INDEX_OPTION_FEES = "proprietary-index-option-fees";
	private Double proprietaryIndexOptionFees;

	public static final String JSON_PROPERTY_PROPRIETARY_INDEX_OPTION_FEES_EFFECT = "proprietary-index-option-fees-effect";
	private String proprietaryIndexOptionFeesEffect;

	public static final String JSON_PROPERTY_TOTAL_FEES = "total-fees";
	private Double totalFees;

	public static final String JSON_PROPERTY_TOTAL_FEES_EFFECT = "total-fees-effect";
	private String totalFeesEffect;

	@javax.annotation.Nullable
	@JsonProperty(JSON_PROPERTY_REGULATORY_FEES)
	@JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
	public Double getRegulatoryFees() {
		return regulatoryFees;
	}

	public void setRegulatoryFees(Double regulatoryFees) {
		this.regulatoryFees = regulatoryFees;
	}

	@javax.annotation.Nullable
	@JsonProperty(JSON_PROPERTY_REGULATORY_FEES_EFFECT)
	@JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
	public String getRegulatoryFeesEffect() {
		return regulatoryFeesEffect;
	}

	public void setRegulatoryFeesEffect(String regulatoryFeesEffect) {
		this.regulatoryFeesEffect = regulatoryFeesEffect;
	}

	@javax.annotation.Nullable
	@JsonProperty(JSON_PROPERTY_CLEARING_FEES)
	@JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
	public Double getClearingFees() {
		return clearingFees;
	}

	public void setClearingFees(Double clearingFees) {
		this.clearingFees = clearingFees;
	}

	@javax.annotation.Nullable
	@JsonProperty(JSON_PROPERTY_CLEARING_FEES_EFFECT)
	@JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
	public String getClearingFeesEffect() {
		return clearingFeesEffect;
	}

	public void setClearingFeesEffect(String clearingFeesEffect) {
		this.clearingFeesEffect = clearingFeesEffect;
	}

	@javax.annotation.Nullable
	@JsonProperty(JSON_PROPERTY_COMMISSION)
	@JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
	public Double getCommission() {
		return commission;
	}

	public void setCommission(Double commission) {
		this.commission = commission;
	}

	@javax.annotation.Nullable
	@JsonProperty(JSON_PROPERTY_COMMISSION_EFFECT)
	@JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
	public String getCommissionEffect() {
		return commissionEffect;
	}

	public void setCommissionEffect(String commissionEffect) {
		this.commissionEffect = commissionEffect;
	}

	@javax.annotation.Nullable
	@JsonProperty(JSON_PROPERTY_PROPRIETARY_INDEX_OPTION_FEES)
	@JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
	public Double getProprietaryIndexOptionFees() {
		return proprietaryIndexOptionFees;
	}

	public void setProprietaryIndexOptionFees(Double proprietaryIndexOptionFees) {
		this.proprietaryIndexOptionFees = proprietaryIndexOptionFees;
	}

	@javax.annotation.Nullable
	@JsonProperty(JSON_PROPERTY_PROPRIETARY_INDEX_OPTION_FEES_EFFECT)
	@JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
	public String getProprietaryIndexOptionFeesEffect() {
		return proprietaryIndexOptionFeesEffect;
	}

	public void setProprietaryIndexOptionFeesEffect(String proprietaryIndexOptionFeesEffect) {
		this.proprietaryIndexOptionFeesEffect = proprietaryIndexOptionFeesEffect;
	}

	@javax.annotation.Nullable
	@JsonProperty(JSON_PROPERTY_TOTAL_FEES)
	@JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
	public Double getTotalFees() {
		return totalFees;
	}

	public void setTotalFees(Double totalFees) {
		this.totalFees = totalFees;
	}

	@javax.annotation.Nullable
	@JsonProperty(JSON_PROPERTY_TOTAL_FEES_EFFECT)
	@JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
	public String getTotalFeesEffect() {
		return totalFeesEffect;
	}

	public void setTotalFeesEffect(String totalFeesEffect) {
		this.totalFeesEffect = totalFeesEffect;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class FeeCalculation {\n");
		if (regulatoryFees != null)
			sb.append("    regulatoryFees: ").append(toIndentedString(regulatoryFees)).append("\n");
		if (regulatoryFeesEffect != null)
			sb.append("    regulatoryFeesEffect: ").append(toIndentedString(regulatoryFeesEffect)).append("\n");
		if (clearingFees != null)
			sb.append("    clearingFees: ").append(toIndentedString(clearingFees)).append("\n");
		if (clearingFeesEffect != null)
			sb.append("    clearingFeesEffect: ").append(toIndentedString(clearingFeesEffect)).append("\n");
		if (commission != null)
			sb.append("    commission: ").append(toIndentedString(commission)).append("\n");
		if (commissionEffect != null)
			sb.append("    commissionEffect: ").append(toIndentedString(commissionEffect)).append("\n");
		if (proprietaryIndexOptionFees != null)
			sb.append("    proprietaryIndexOptionFees: ").append(toIndentedString(proprietaryIndexOptionFees)).append("\n");
		if (proprietaryIndexOptionFeesEffect != null)
			sb.append("    proprietaryIndexOptionFeesEffect: ").append(toIndentedString(proprietaryIndexOptionFeesEffect)).append("\n");
		if (totalFees != null)
			sb.append("    totalFees: ").append(toIndentedString(totalFees)).append("\n");
		if (totalFeesEffect != null)
			sb.append("    totalFeesEffect: ").append(toIndentedString(totalFeesEffect)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}
