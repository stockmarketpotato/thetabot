package com.stockmarketpotato.integration.tastytrade.model.orders;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
	BuyingPowerEffect.JSON_PROPERTY_CHANGE_IN_MARGIN_REQUIREMENT,
	BuyingPowerEffect.JSON_PROPERTY_CHANGE_IN_MARGIN_REQUIREMENT_EFFECT,
	BuyingPowerEffect.JSON_PROPERTY_CHANGE_IN_BUYING_POWER,
	BuyingPowerEffect.JSON_PROPERTY_CHANGE_IN_BUYING_POWER_EFFECT,
	BuyingPowerEffect.JSON_PROPERTY_CURRENT_BUYING_POWER,
	BuyingPowerEffect.JSON_PROPERTY_CURRENT_BUYING_POWER_EFFECT,
	BuyingPowerEffect.JSON_PROPERTY_NEW_BUYING_POWER,
	BuyingPowerEffect.JSON_PROPERTY_NEW_BUYING_POWER_EFFECT,
	BuyingPowerEffect.JSON_PROPERTY_ISOLATED_ORDER_MARGIN_REQUIREMENT,
	BuyingPowerEffect.JSON_PROPERTY_ISOLATED_ORDER_MARGIN_REQUIREMENT_EFFECT,
	BuyingPowerEffect.JSON_PROPERTY_IS_SPREAD,
	BuyingPowerEffect.JSON_PROPERTY_IMPACT,
	BuyingPowerEffect.JSON_PROPERTY_EFFECT
})

@JsonIgnoreProperties(ignoreUnknown = true)
public class BuyingPowerEffect {
	public static final String JSON_PROPERTY_CHANGE_IN_MARGIN_REQUIREMENT = "change-in-margin-requirement";
	private Double changeInMarginRequirement;

	public static final String JSON_PROPERTY_CHANGE_IN_MARGIN_REQUIREMENT_EFFECT = "change-in-margin-requirement-effect";
	private String changeInMarginRequirementEffect;

	public static final String JSON_PROPERTY_CHANGE_IN_BUYING_POWER = "change-in-buying-power";
	private Double changeInBuyingPower;

	public static final String JSON_PROPERTY_CHANGE_IN_BUYING_POWER_EFFECT = "change-in-buying-power-effect";
	private String changeInBuyingPowerEffect;

	public static final String JSON_PROPERTY_CURRENT_BUYING_POWER = "current-buying-power";
	private Double currentBuyingPower;

	public static final String JSON_PROPERTY_CURRENT_BUYING_POWER_EFFECT = "current-buying-power-effect";
	private String currentBuyingPowerEffect;

	public static final String JSON_PROPERTY_NEW_BUYING_POWER = "new-buying-power";
	private Double newBuyingPower;

	public static final String JSON_PROPERTY_NEW_BUYING_POWER_EFFECT = "new-buying-power-effect";
	private String newBuyingPowerEffect;

	public static final String JSON_PROPERTY_ISOLATED_ORDER_MARGIN_REQUIREMENT = "isolated-order-margin-requirement";
	private Double isolatedOrderMarginRequirement;

	public static final String JSON_PROPERTY_ISOLATED_ORDER_MARGIN_REQUIREMENT_EFFECT = "isolated-order-margin-requirement-effect";
	private String isolatedOrderMarginRequirementEffect;

	public static final String JSON_PROPERTY_IS_SPREAD = "is-spread";
	private Boolean isSpread;

	public static final String JSON_PROPERTY_IMPACT = "impact";
	private Double impact;

	public static final String JSON_PROPERTY_EFFECT = "effect";
	private String effect;

	BuyingPowerEffect() {
		super();
	}

	@javax.annotation.Nullable
	@JsonProperty(JSON_PROPERTY_CHANGE_IN_MARGIN_REQUIREMENT)
	@JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

	public Double getChangeInMarginRequirement() {
		return changeInMarginRequirement;
	}

	public void setHangeInMarginRequirement(Double changeInMarginRequirement) {
		this.changeInMarginRequirement = changeInMarginRequirement;
	}

	@javax.annotation.Nullable
	@JsonProperty(JSON_PROPERTY_CHANGE_IN_MARGIN_REQUIREMENT_EFFECT)
	@JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

	public String getChangeInMarginRequirementEffect() {
		return changeInMarginRequirementEffect;
	}

	public void setChangeInMarginRequirementEffect(String changeInMarginRequirementEffect) {
		this.changeInMarginRequirementEffect = changeInMarginRequirementEffect;
	}

	@javax.annotation.Nullable
	@JsonProperty(JSON_PROPERTY_CHANGE_IN_BUYING_POWER)
	@JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

	public Double getChangeInBuyingPower() {
		return changeInBuyingPower;
	}

	public void setChangeInBuyingPower(Double changeInBuyingPower) {
		this.changeInBuyingPower = changeInBuyingPower;
	}

	@javax.annotation.Nullable
	@JsonProperty(JSON_PROPERTY_CHANGE_IN_BUYING_POWER_EFFECT)
	@JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

	public String getChangeInBuyingPowerEffect() {
		return changeInBuyingPowerEffect;
	}

	public void setChangeInBuyingPowerEffect(String changeInBuyingPowerEffect) {
		this.changeInBuyingPowerEffect = changeInBuyingPowerEffect;
	}

	@javax.annotation.Nullable
	@JsonProperty(JSON_PROPERTY_CURRENT_BUYING_POWER)
	@JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

	public Double getCurrentBuyingPower() {
		return currentBuyingPower;
	}

	public void setCurrentBuyingPower(Double currentBuyingPower) {
		this.currentBuyingPower = currentBuyingPower;
	}

	@javax.annotation.Nullable
	@JsonProperty(JSON_PROPERTY_CURRENT_BUYING_POWER_EFFECT)
	@JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

	public String getCurrentBuyingPowerEffect() {
		return currentBuyingPowerEffect;
	}

	public void setCurrentBuyingPowerEffect(String currentBuyingPowerEffect) {
		this.currentBuyingPowerEffect = currentBuyingPowerEffect;
	}

	@javax.annotation.Nullable
	@JsonProperty(JSON_PROPERTY_NEW_BUYING_POWER)
	@JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

	public Double getNewBuyingPower() {
		return newBuyingPower;
	}

	public void setNewBuyingPower(Double newBuyingPower) {
		this.newBuyingPower = newBuyingPower;
	}

	@javax.annotation.Nullable
	@JsonProperty(JSON_PROPERTY_NEW_BUYING_POWER_EFFECT)
	@JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

	public String getNewBuyingPowerEffect() {
		return newBuyingPowerEffect;
	}

	public void setNewBuyingPowerEffect(String newBuyingPowerEffect) {
		this.newBuyingPowerEffect = newBuyingPowerEffect;
	}

	@javax.annotation.Nullable
	@JsonProperty(JSON_PROPERTY_ISOLATED_ORDER_MARGIN_REQUIREMENT)
	@JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

	public Double getIsolatedOrderMarginRequirement() {
		return isolatedOrderMarginRequirement;
	}

	public void setIsolatedOrderMarginRequirement(Double isolatedOrderMarginRequirement) {
		this.isolatedOrderMarginRequirement = isolatedOrderMarginRequirement;
	}

	@javax.annotation.Nullable
	@JsonProperty(JSON_PROPERTY_ISOLATED_ORDER_MARGIN_REQUIREMENT_EFFECT)
	@JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

	public String getIsolatedOrderMarginRequirementEffect() {
		return isolatedOrderMarginRequirementEffect;
	}

	public void setIsolatedOrderMarginRequirementEffect(String isolatedOrderMarginRequirementEffect) {
		this.isolatedOrderMarginRequirementEffect = isolatedOrderMarginRequirementEffect;
	}

	@javax.annotation.Nullable
	@JsonProperty(JSON_PROPERTY_IS_SPREAD)
	@JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

	public Boolean getIsSpread() {
		return isSpread;
	}

	public void setIsSpread(Boolean isSpread) {
		this.isSpread = isSpread;
	}

	@javax.annotation.Nullable
	@JsonProperty(JSON_PROPERTY_IMPACT)
	@JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

	public Double getImpact() {
		return impact;
	}

	public void setImpact(Double impact) {
		this.impact = impact;
	}

	@javax.annotation.Nullable
	@JsonProperty(JSON_PROPERTY_EFFECT)
	@JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

	public String getEffect() {
		return effect;
	}

	public void setEffect(String effect) {
		this.effect = effect;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class BuyingPowerEffect {\n");
		if (changeInMarginRequirement != null)
			sb.append("    changeInMarginRequirement: ").append(toIndentedString(changeInMarginRequirement)).append("\n");
		if (changeInMarginRequirementEffect != null)
			sb.append("    changeInMarginRequirementEffect: ").append(toIndentedString(changeInMarginRequirementEffect)).append("\n");
		if (changeInBuyingPower != null)
			sb.append("    changeInBuyingPower: ").append(toIndentedString(changeInBuyingPower)).append("\n");
		if (changeInBuyingPowerEffect != null)
			sb.append("    changeInBuyingPowerEffect: ").append(toIndentedString(changeInBuyingPowerEffect)).append("\n");
		if (currentBuyingPower != null)
			sb.append("    currentBuyingPower: ").append(toIndentedString(currentBuyingPower)).append("\n");
		if (currentBuyingPowerEffect != null)
			sb.append("    currentBuyingPowerEffect: ").append(toIndentedString(currentBuyingPowerEffect)).append("\n");
		if (newBuyingPower != null)
			sb.append("    newBuyingPower: ").append(toIndentedString(newBuyingPower)).append("\n");
		if (newBuyingPowerEffect != null)
			sb.append("    newBuyingPowerEffect: ").append(toIndentedString(newBuyingPowerEffect)).append("\n");
		if (isolatedOrderMarginRequirement != null)
			sb.append("    isolatedOrderMarginRequirement: ").append(toIndentedString(isolatedOrderMarginRequirement)).append("\n");
		if (isolatedOrderMarginRequirementEffect != null)
			sb.append("    isolatedOrderMarginRequirementEffect: ").append(toIndentedString(isolatedOrderMarginRequirementEffect)).append("\n");
		if (isSpread != null)
			sb.append("    isSpread: ").append(toIndentedString(isSpread)).append("\n");
		if (impact != null)
			sb.append("    impact: ").append(toIndentedString(impact)).append("\n");
		if (effect != null)
			sb.append("    effect: ").append(toIndentedString(effect)).append("\n");
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
