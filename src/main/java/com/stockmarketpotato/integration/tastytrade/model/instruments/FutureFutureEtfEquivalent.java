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
  FutureFutureEtfEquivalent.JSON_PROPERTY_SYMBOL,
  FutureFutureEtfEquivalent.JSON_PROPERTY_SHARE_QUANTITY
})
@JsonTypeName("Future_future_etf_equivalent")
@JsonIgnoreProperties(ignoreUnknown = true)
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-02-14T09:40:37.746219+01:00[Europe/Berlin]")
public class FutureFutureEtfEquivalent {
  public static final String JSON_PROPERTY_SYMBOL = "symbol";
  private String symbol;

  public static final String JSON_PROPERTY_SHARE_QUANTITY = "share-quantity";
  private Integer shareQuantity;

  public FutureFutureEtfEquivalent() {
  }

  public FutureFutureEtfEquivalent symbol(String symbol) {
    
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


  public FutureFutureEtfEquivalent shareQuantity(Integer shareQuantity) {
    
    this.shareQuantity = shareQuantity;
    return this;
  }

   /**
   * 
   * @return shareQuantity
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SHARE_QUANTITY)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getShareQuantity() {
    return shareQuantity;
  }


  @JsonProperty(JSON_PROPERTY_SHARE_QUANTITY)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setShareQuantity(Integer shareQuantity) {
    this.shareQuantity = shareQuantity;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FutureFutureEtfEquivalent futureFutureEtfEquivalent = (FutureFutureEtfEquivalent) o;
    return Objects.equals(this.symbol, futureFutureEtfEquivalent.symbol) &&
        Objects.equals(this.shareQuantity, futureFutureEtfEquivalent.shareQuantity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(symbol, shareQuantity);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FutureFutureEtfEquivalent {\n");
    sb.append("    symbol: ").append(toIndentedString(symbol)).append("\n");
    sb.append("    shareQuantity: ").append(toIndentedString(shareQuantity)).append("\n");
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

