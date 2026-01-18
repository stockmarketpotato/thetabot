package com.stockmarketpotato.integration.tastytrade.model.instruments;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * 
 */
@JsonPropertyOrder({
  FuturesOptionChainExpirations.JSON_PROPERTY_UNDERLYING_SYMBOL,
  FuturesOptionChainExpirations.JSON_PROPERTY_ROOT_SYMBOL,
  FuturesOptionChainExpirations.JSON_PROPERTY_OPTION_ROOT_SYMBOL,
  FuturesOptionChainExpirations.JSON_PROPERTY_OPTION_CONTRACT_SYMBOL,
  FuturesOptionChainExpirations.JSON_PROPERTY_ASSET,
  FuturesOptionChainExpirations.JSON_PROPERTY_EXPIRATION_DATE,
  FuturesOptionChainExpirations.JSON_PROPERTY_DAYS_TO_EXPIRATION,
  FuturesOptionChainExpirations.JSON_PROPERTY_EXPIRATION_TYPE,
  FuturesOptionChainExpirations.JSON_PROPERTY_SETTLEMENT_TYPE,
  FuturesOptionChainExpirations.JSON_PROPERTY_NOTIONAL_VALUE,
  FuturesOptionChainExpirations.JSON_PROPERTY_DISPLAY_FACTOR,
  FuturesOptionChainExpirations.JSON_PROPERTY_STRIKE_FACTOR,
  FuturesOptionChainExpirations.JSON_PROPERTY_STOPS_TRADING_AT,
  FuturesOptionChainExpirations.JSON_PROPERTY_EXPIRES_AT,
  FuturesOptionChainExpirations.JSON_PROPERTY_TICK_SIZES,
  FuturesOptionChainExpirations.JSON_PROPERTY_STRIKES
})
@JsonTypeName("FuturesNestedOptionChainSerializer_option_chains_expirations")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-02-14T09:40:37.746219+01:00[Europe/Berlin]")
public class FuturesOptionChainExpirations {
  public static final String JSON_PROPERTY_UNDERLYING_SYMBOL = "underlying-symbol";
  private String underlyingSymbol;

  public static final String JSON_PROPERTY_ROOT_SYMBOL = "root-symbol";
  private String rootSymbol;

  public static final String JSON_PROPERTY_OPTION_ROOT_SYMBOL = "option-root-symbol";
  private String optionRootSymbol;

  public static final String JSON_PROPERTY_OPTION_CONTRACT_SYMBOL = "option-contract-symbol";
  private String optionContractSymbol;

  public static final String JSON_PROPERTY_ASSET = "asset";
  private String asset;

  public static final String JSON_PROPERTY_EXPIRATION_DATE = "expiration-date";
  private LocalDate expirationDate;

  public static final String JSON_PROPERTY_DAYS_TO_EXPIRATION = "days-to-expiration";
  private Integer daysToExpiration;

  public static final String JSON_PROPERTY_EXPIRATION_TYPE = "expiration-type";
  private String expirationType;

  public static final String JSON_PROPERTY_SETTLEMENT_TYPE = "settlement-type";
  private String settlementType;

  public static final String JSON_PROPERTY_NOTIONAL_VALUE = "notional-value";
  private Double notionalValue;

  public static final String JSON_PROPERTY_DISPLAY_FACTOR = "display-factor";
  private Double displayFactor;

  public static final String JSON_PROPERTY_STRIKE_FACTOR = "strike-factor";
  private Double strikeFactor;

  public static final String JSON_PROPERTY_STOPS_TRADING_AT = "stops-trading-at";
  private OffsetDateTime stopsTradingAt;

  public static final String JSON_PROPERTY_EXPIRES_AT = "expires-at";
  private OffsetDateTime expiresAt;

  public static final String JSON_PROPERTY_TICK_SIZES = "tick-sizes";
  private List<EquityTickSizes> tickSizes;

  public static final String JSON_PROPERTY_STRIKES = "strikes";
  private List<FuturesOptionChainsExpirationsStrikes> strikes;

  public FuturesOptionChainExpirations() {
  }

  public FuturesOptionChainExpirations underlyingSymbol(String underlyingSymbol) {
    
    this.underlyingSymbol = underlyingSymbol;
    return this;
  }

   /**
   * 
   * @return underlyingSymbol
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_UNDERLYING_SYMBOL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getUnderlyingSymbol() {
    return underlyingSymbol;
  }


  @JsonProperty(JSON_PROPERTY_UNDERLYING_SYMBOL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setUnderlyingSymbol(String underlyingSymbol) {
    this.underlyingSymbol = underlyingSymbol;
  }


  public FuturesOptionChainExpirations rootSymbol(String rootSymbol) {
    
    this.rootSymbol = rootSymbol;
    return this;
  }

   /**
   * 
   * @return rootSymbol
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ROOT_SYMBOL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getRootSymbol() {
    return rootSymbol;
  }


  @JsonProperty(JSON_PROPERTY_ROOT_SYMBOL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setRootSymbol(String rootSymbol) {
    this.rootSymbol = rootSymbol;
  }


  public FuturesOptionChainExpirations optionRootSymbol(String optionRootSymbol) {
    
    this.optionRootSymbol = optionRootSymbol;
    return this;
  }

   /**
   * 
   * @return optionRootSymbol
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_OPTION_ROOT_SYMBOL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getOptionRootSymbol() {
    return optionRootSymbol;
  }


  @JsonProperty(JSON_PROPERTY_OPTION_ROOT_SYMBOL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setOptionRootSymbol(String optionRootSymbol) {
    this.optionRootSymbol = optionRootSymbol;
  }


  public FuturesOptionChainExpirations optionContractSymbol(String optionContractSymbol) {
    
    this.optionContractSymbol = optionContractSymbol;
    return this;
  }

   /**
   * 
   * @return optionContractSymbol
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_OPTION_CONTRACT_SYMBOL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getOptionContractSymbol() {
    return optionContractSymbol;
  }


  @JsonProperty(JSON_PROPERTY_OPTION_CONTRACT_SYMBOL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setOptionContractSymbol(String optionContractSymbol) {
    this.optionContractSymbol = optionContractSymbol;
  }


  public FuturesOptionChainExpirations asset(String asset) {
    
    this.asset = asset;
    return this;
  }

   /**
   * 
   * @return asset
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ASSET)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getAsset() {
    return asset;
  }


  @JsonProperty(JSON_PROPERTY_ASSET)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAsset(String asset) {
    this.asset = asset;
  }


  public FuturesOptionChainExpirations expirationDate(LocalDate expirationDate) {
    
    this.expirationDate = expirationDate;
    return this;
  }

   /**
   * 
   * @return expirationDate
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_EXPIRATION_DATE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public LocalDate getExpirationDate() {
    return expirationDate;
  }


  @JsonProperty(JSON_PROPERTY_EXPIRATION_DATE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setExpirationDate(LocalDate expirationDate) {
    this.expirationDate = expirationDate;
  }


  public FuturesOptionChainExpirations daysToExpiration(Integer daysToExpiration) {
    
    this.daysToExpiration = daysToExpiration;
    return this;
  }

   /**
   * 
   * @return daysToExpiration
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_DAYS_TO_EXPIRATION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getDaysToExpiration() {
    return daysToExpiration;
  }


  @JsonProperty(JSON_PROPERTY_DAYS_TO_EXPIRATION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setDaysToExpiration(Integer daysToExpiration) {
    this.daysToExpiration = daysToExpiration;
  }


  public FuturesOptionChainExpirations expirationType(String expirationType) {
    
    this.expirationType = expirationType;
    return this;
  }

   /**
   * 
   * @return expirationType
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_EXPIRATION_TYPE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getExpirationType() {
    return expirationType;
  }


  @JsonProperty(JSON_PROPERTY_EXPIRATION_TYPE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setExpirationType(String expirationType) {
    this.expirationType = expirationType;
  }


  public FuturesOptionChainExpirations settlementType(String settlementType) {
    
    this.settlementType = settlementType;
    return this;
  }

   /**
   * 
   * @return settlementType
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SETTLEMENT_TYPE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getSettlementType() {
    return settlementType;
  }


  @JsonProperty(JSON_PROPERTY_SETTLEMENT_TYPE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setSettlementType(String settlementType) {
    this.settlementType = settlementType;
  }


  public FuturesOptionChainExpirations notionalValue(Double notionalValue) {
    
    this.notionalValue = notionalValue;
    return this;
  }

   /**
   * 
   * @return notionalValue
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_NOTIONAL_VALUE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Double getNotionalValue() {
    return notionalValue;
  }


  @JsonProperty(JSON_PROPERTY_NOTIONAL_VALUE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setNotionalValue(Double notionalValue) {
    this.notionalValue = notionalValue;
  }


  public FuturesOptionChainExpirations displayFactor(Double displayFactor) {
    
    this.displayFactor = displayFactor;
    return this;
  }

   /**
   * 
   * @return displayFactor
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_DISPLAY_FACTOR)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Double getDisplayFactor() {
    return displayFactor;
  }


  @JsonProperty(JSON_PROPERTY_DISPLAY_FACTOR)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setDisplayFactor(Double displayFactor) {
    this.displayFactor = displayFactor;
  }


  public FuturesOptionChainExpirations strikeFactor(Double strikeFactor) {
    
    this.strikeFactor = strikeFactor;
    return this;
  }

   /**
   * 
   * @return strikeFactor
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_STRIKE_FACTOR)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Double getStrikeFactor() {
    return strikeFactor;
  }


  @JsonProperty(JSON_PROPERTY_STRIKE_FACTOR)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setStrikeFactor(Double strikeFactor) {
    this.strikeFactor = strikeFactor;
  }


  public FuturesOptionChainExpirations stopsTradingAt(OffsetDateTime stopsTradingAt) {
    
    this.stopsTradingAt = stopsTradingAt;
    return this;
  }

   /**
   * 
   * @return stopsTradingAt
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_STOPS_TRADING_AT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public OffsetDateTime getStopsTradingAt() {
    return stopsTradingAt;
  }


  @JsonProperty(JSON_PROPERTY_STOPS_TRADING_AT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setStopsTradingAt(OffsetDateTime stopsTradingAt) {
    this.stopsTradingAt = stopsTradingAt;
  }


  public FuturesOptionChainExpirations expiresAt(OffsetDateTime expiresAt) {
    
    this.expiresAt = expiresAt;
    return this;
  }

   /**
   * 
   * @return expiresAt
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_EXPIRES_AT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public OffsetDateTime getExpiresAt() {
    return expiresAt;
  }


  @JsonProperty(JSON_PROPERTY_EXPIRES_AT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setExpiresAt(OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }


  public FuturesOptionChainExpirations tickSizes(List<EquityTickSizes> tickSizes) {
    
    this.tickSizes = tickSizes;
    return this;
  }

   /**
   * Get tickSizes
   * @return tickSizes
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_TICK_SIZES)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public List<EquityTickSizes> getTickSizes() {
    return tickSizes;
  }


  @JsonProperty(JSON_PROPERTY_TICK_SIZES)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setTickSizes(List<EquityTickSizes> tickSizes) {
    this.tickSizes = tickSizes;
  }


  public FuturesOptionChainExpirations strikes(List<FuturesOptionChainsExpirationsStrikes> strikes) {
    
    this.strikes = strikes;
    return this;
  }

   /**
   * Get strikes
   * @return strikes
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_STRIKES)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public List<FuturesOptionChainsExpirationsStrikes> getStrikes() {
    return strikes;
  }


  @JsonProperty(JSON_PROPERTY_STRIKES)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setStrikes(List<FuturesOptionChainsExpirationsStrikes> strikes) {
    this.strikes = strikes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FuturesOptionChainExpirations futuresNestedOptionChainSerializerOptionChainsExpirations = (FuturesOptionChainExpirations) o;
    return Objects.equals(this.underlyingSymbol, futuresNestedOptionChainSerializerOptionChainsExpirations.underlyingSymbol) &&
        Objects.equals(this.rootSymbol, futuresNestedOptionChainSerializerOptionChainsExpirations.rootSymbol) &&
        Objects.equals(this.optionRootSymbol, futuresNestedOptionChainSerializerOptionChainsExpirations.optionRootSymbol) &&
        Objects.equals(this.optionContractSymbol, futuresNestedOptionChainSerializerOptionChainsExpirations.optionContractSymbol) &&
        Objects.equals(this.asset, futuresNestedOptionChainSerializerOptionChainsExpirations.asset) &&
        Objects.equals(this.expirationDate, futuresNestedOptionChainSerializerOptionChainsExpirations.expirationDate) &&
        Objects.equals(this.daysToExpiration, futuresNestedOptionChainSerializerOptionChainsExpirations.daysToExpiration) &&
        Objects.equals(this.expirationType, futuresNestedOptionChainSerializerOptionChainsExpirations.expirationType) &&
        Objects.equals(this.settlementType, futuresNestedOptionChainSerializerOptionChainsExpirations.settlementType) &&
        Objects.equals(this.notionalValue, futuresNestedOptionChainSerializerOptionChainsExpirations.notionalValue) &&
        Objects.equals(this.displayFactor, futuresNestedOptionChainSerializerOptionChainsExpirations.displayFactor) &&
        Objects.equals(this.strikeFactor, futuresNestedOptionChainSerializerOptionChainsExpirations.strikeFactor) &&
        Objects.equals(this.stopsTradingAt, futuresNestedOptionChainSerializerOptionChainsExpirations.stopsTradingAt) &&
        Objects.equals(this.expiresAt, futuresNestedOptionChainSerializerOptionChainsExpirations.expiresAt) &&
        Objects.equals(this.tickSizes, futuresNestedOptionChainSerializerOptionChainsExpirations.tickSizes) &&
        Objects.equals(this.strikes, futuresNestedOptionChainSerializerOptionChainsExpirations.strikes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(underlyingSymbol, rootSymbol, optionRootSymbol, optionContractSymbol, asset, expirationDate, daysToExpiration, expirationType, settlementType, notionalValue, displayFactor, strikeFactor, stopsTradingAt, expiresAt, tickSizes, strikes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FuturesNestedOptionChainSerializerOptionChainsExpirations {\n");
    sb.append("    underlyingSymbol: ").append(toIndentedString(underlyingSymbol)).append("\n");
    sb.append("    rootSymbol: ").append(toIndentedString(rootSymbol)).append("\n");
    sb.append("    optionRootSymbol: ").append(toIndentedString(optionRootSymbol)).append("\n");
    sb.append("    optionContractSymbol: ").append(toIndentedString(optionContractSymbol)).append("\n");
    sb.append("    asset: ").append(toIndentedString(asset)).append("\n");
    sb.append("    expirationDate: ").append(toIndentedString(expirationDate)).append("\n");
    sb.append("    daysToExpiration: ").append(toIndentedString(daysToExpiration)).append("\n");
    sb.append("    expirationType: ").append(toIndentedString(expirationType)).append("\n");
    sb.append("    settlementType: ").append(toIndentedString(settlementType)).append("\n");
    sb.append("    notionalValue: ").append(toIndentedString(notionalValue)).append("\n");
    sb.append("    displayFactor: ").append(toIndentedString(displayFactor)).append("\n");
    sb.append("    strikeFactor: ").append(toIndentedString(strikeFactor)).append("\n");
    sb.append("    stopsTradingAt: ").append(toIndentedString(stopsTradingAt)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
    sb.append("    tickSizes: ").append(toIndentedString(tickSizes)).append("\n");
    sb.append("    strikes: ").append(toIndentedString(strikes)).append("\n");
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

