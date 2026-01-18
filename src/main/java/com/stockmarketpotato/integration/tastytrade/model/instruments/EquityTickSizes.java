package com.stockmarketpotato.integration.tastytrade.model.instruments;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * 
 */
@JsonPropertyOrder({
  EquityTickSizes.JSON_PROPERTY_VALUE,
  EquityTickSizes.JSON_PROPERTY_THRESHOLD,
  EquityTickSizes.JSON_PROPERTY_SYMBOL
})
@JsonTypeName("Equity_tick_sizes")
@JsonIgnoreProperties(ignoreUnknown = true)
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-02-14T09:40:37.746219+01:00[Europe/Berlin]")
public class EquityTickSizes {
  public static final String JSON_PROPERTY_VALUE = "value";
  private Double value;

  public static final String JSON_PROPERTY_THRESHOLD = "threshold";
  private Double threshold;

  public static final String JSON_PROPERTY_SYMBOL = "symbol";
  private String symbol;

  public EquityTickSizes() {
  }

  public EquityTickSizes value(Double value) {
    
    this.value = value;
    return this;
  }

   /**
   * 
   * @return value
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_VALUE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Double getValue() {
    return value;
  }


  @JsonProperty(JSON_PROPERTY_VALUE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setValue(Double value) {
    this.value = value;
  }


  public EquityTickSizes threshold(Double threshold) {
    
    this.threshold = threshold;
    return this;
  }

   /**
   * 
   * @return threshold
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Double getThreshold() {
    return threshold;
  }


  @JsonProperty(JSON_PROPERTY_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setThreshold(Double threshold) {
    this.threshold = threshold;
  }


  public EquityTickSizes symbol(String symbol) {
    
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EquityTickSizes equityTickSizes = (EquityTickSizes) o;
    return Objects.equals(this.value, equityTickSizes.value) &&
        Objects.equals(this.threshold, equityTickSizes.threshold) &&
        Objects.equals(this.symbol, equityTickSizes.symbol);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value, threshold, symbol);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EquityTickSizes {\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    threshold: ").append(toIndentedString(threshold)).append("\n");
    sb.append("    symbol: ").append(toIndentedString(symbol)).append("\n");
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

