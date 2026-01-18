package com.stockmarketpotato.integration.tastytrade.model.instruments;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * 
 */
@JsonPropertyOrder({
  FuturesNestedOptionChainSerializerFutures.JSON_PROPERTY_SYMBOL,
  FuturesNestedOptionChainSerializerFutures.JSON_PROPERTY_ROOT_SYMBOL,
  FuturesNestedOptionChainSerializerFutures.JSON_PROPERTY_MATURITY_DATE,
  FuturesNestedOptionChainSerializerFutures.JSON_PROPERTY_EXPIRATION_DATE,
  FuturesNestedOptionChainSerializerFutures.JSON_PROPERTY_DAYS_TO_EXPIRATION,
  FuturesNestedOptionChainSerializerFutures.JSON_PROPERTY_ACTIVE_MONTH,
  FuturesNestedOptionChainSerializerFutures.JSON_PROPERTY_NEXT_ACTIVE_MONTH,
  FuturesNestedOptionChainSerializerFutures.JSON_PROPERTY_STOPS_TRADING_AT,
  FuturesNestedOptionChainSerializerFutures.JSON_PROPERTY_EXPIRES_AT
})
@JsonTypeName("FuturesNestedOptionChainSerializer_futures")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-02-14T09:40:37.746219+01:00[Europe/Berlin]")
public class FuturesNestedOptionChainSerializerFutures {
  public static final String JSON_PROPERTY_SYMBOL = "symbol";
  private String symbol;

  public static final String JSON_PROPERTY_ROOT_SYMBOL = "root-symbol";
  private String rootSymbol;

  public static final String JSON_PROPERTY_MATURITY_DATE = "maturity-date";
  private LocalDate maturityDate;

  public static final String JSON_PROPERTY_EXPIRATION_DATE = "expiration-date";
  private LocalDate expirationDate;

  public static final String JSON_PROPERTY_DAYS_TO_EXPIRATION = "days-to-expiration";
  private Integer daysToExpiration;

  public static final String JSON_PROPERTY_ACTIVE_MONTH = "active-month";
  private Boolean activeMonth;

  public static final String JSON_PROPERTY_NEXT_ACTIVE_MONTH = "next-active-month";
  private Boolean nextActiveMonth;

  public static final String JSON_PROPERTY_STOPS_TRADING_AT = "stops-trading-at";
  private OffsetDateTime stopsTradingAt;

  public static final String JSON_PROPERTY_EXPIRES_AT = "expires-at";
  private OffsetDateTime expiresAt;

  public FuturesNestedOptionChainSerializerFutures() {
  }

  public FuturesNestedOptionChainSerializerFutures symbol(String symbol) {
    
    this.symbol = symbol;
    return this;
  }

   /**
   * 
   * @return symbol
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SYMBOL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getSymbol() {
    return symbol;
  }


  @JsonProperty(JSON_PROPERTY_SYMBOL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }


  public FuturesNestedOptionChainSerializerFutures rootSymbol(String rootSymbol) {
    
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


  public FuturesNestedOptionChainSerializerFutures maturityDate(LocalDate maturityDate) {
    
    this.maturityDate = maturityDate;
    return this;
  }

   /**
   * 
   * @return maturityDate
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_MATURITY_DATE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public LocalDate getMaturityDate() {
    return maturityDate;
  }


  @JsonProperty(JSON_PROPERTY_MATURITY_DATE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setMaturityDate(LocalDate maturityDate) {
    this.maturityDate = maturityDate;
  }


  public FuturesNestedOptionChainSerializerFutures expirationDate(LocalDate expirationDate) {
    
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


  public FuturesNestedOptionChainSerializerFutures daysToExpiration(Integer daysToExpiration) {
    
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


  public FuturesNestedOptionChainSerializerFutures activeMonth(Boolean activeMonth) {
    
    this.activeMonth = activeMonth;
    return this;
  }

   /**
   * 
   * @return activeMonth
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ACTIVE_MONTH)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getActiveMonth() {
    return activeMonth;
  }


  @JsonProperty(JSON_PROPERTY_ACTIVE_MONTH)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setActiveMonth(Boolean activeMonth) {
    this.activeMonth = activeMonth;
  }


  public FuturesNestedOptionChainSerializerFutures nextActiveMonth(Boolean nextActiveMonth) {
    
    this.nextActiveMonth = nextActiveMonth;
    return this;
  }

   /**
   * 
   * @return nextActiveMonth
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_NEXT_ACTIVE_MONTH)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getNextActiveMonth() {
    return nextActiveMonth;
  }


  @JsonProperty(JSON_PROPERTY_NEXT_ACTIVE_MONTH)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setNextActiveMonth(Boolean nextActiveMonth) {
    this.nextActiveMonth = nextActiveMonth;
  }


  public FuturesNestedOptionChainSerializerFutures stopsTradingAt(OffsetDateTime stopsTradingAt) {
    
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


  public FuturesNestedOptionChainSerializerFutures expiresAt(OffsetDateTime expiresAt) {
    
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FuturesNestedOptionChainSerializerFutures futuresNestedOptionChainSerializerFutures = (FuturesNestedOptionChainSerializerFutures) o;
    return Objects.equals(this.symbol, futuresNestedOptionChainSerializerFutures.symbol) &&
        Objects.equals(this.rootSymbol, futuresNestedOptionChainSerializerFutures.rootSymbol) &&
        Objects.equals(this.maturityDate, futuresNestedOptionChainSerializerFutures.maturityDate) &&
        Objects.equals(this.expirationDate, futuresNestedOptionChainSerializerFutures.expirationDate) &&
        Objects.equals(this.daysToExpiration, futuresNestedOptionChainSerializerFutures.daysToExpiration) &&
        Objects.equals(this.activeMonth, futuresNestedOptionChainSerializerFutures.activeMonth) &&
        Objects.equals(this.nextActiveMonth, futuresNestedOptionChainSerializerFutures.nextActiveMonth) &&
        Objects.equals(this.stopsTradingAt, futuresNestedOptionChainSerializerFutures.stopsTradingAt) &&
        Objects.equals(this.expiresAt, futuresNestedOptionChainSerializerFutures.expiresAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(symbol, rootSymbol, maturityDate, expirationDate, daysToExpiration, activeMonth, nextActiveMonth, stopsTradingAt, expiresAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FuturesNestedOptionChainSerializerFutures {\n");
    sb.append("    symbol: ").append(toIndentedString(symbol)).append("\n");
    sb.append("    rootSymbol: ").append(toIndentedString(rootSymbol)).append("\n");
    sb.append("    maturityDate: ").append(toIndentedString(maturityDate)).append("\n");
    sb.append("    expirationDate: ").append(toIndentedString(expirationDate)).append("\n");
    sb.append("    daysToExpiration: ").append(toIndentedString(daysToExpiration)).append("\n");
    sb.append("    activeMonth: ").append(toIndentedString(activeMonth)).append("\n");
    sb.append("    nextActiveMonth: ").append(toIndentedString(nextActiveMonth)).append("\n");
    sb.append("    stopsTradingAt: ").append(toIndentedString(stopsTradingAt)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
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

