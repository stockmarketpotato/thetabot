package com.stockmarketpotato.integration.tastytrade.model.instruments;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * 
 */
@JsonPropertyOrder({
  FuturesOptionChainsExpirationsStrikes.JSON_PROPERTY_STRIKE_PRICE,
  FuturesOptionChainsExpirationsStrikes.JSON_PROPERTY_CALL,
  FuturesOptionChainsExpirationsStrikes.JSON_PROPERTY_CALL_STREAMER_SYMBOL,
  FuturesOptionChainsExpirationsStrikes.JSON_PROPERTY_PUT,
  FuturesOptionChainsExpirationsStrikes.JSON_PROPERTY_PUT_STREAMER_SYMBOL
})
@JsonTypeName("FuturesNestedOptionChainSerializer_option_chains_expirations_strikes")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-02-14T09:40:37.746219+01:00[Europe/Berlin]")
public class FuturesOptionChainsExpirationsStrikes {
  public static final String JSON_PROPERTY_STRIKE_PRICE = "strike-price";
  private Double strikePrice;

  public static final String JSON_PROPERTY_CALL = "call";
  private String call;

  public static final String JSON_PROPERTY_CALL_STREAMER_SYMBOL = "call-streamer-symbol";
  private String callStreamerSymbol;

  public static final String JSON_PROPERTY_PUT = "put";
  private String put;

  public static final String JSON_PROPERTY_PUT_STREAMER_SYMBOL = "put-streamer-symbol";
  private String putStreamerSymbol;

  public FuturesOptionChainsExpirationsStrikes() {
  }

  public FuturesOptionChainsExpirationsStrikes strikePrice(Double strikePrice) {
    
    this.strikePrice = strikePrice;
    return this;
  }

   /**
   * 
   * @return strikePrice
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_STRIKE_PRICE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Double getStrikePrice() {
    return strikePrice;
  }


  @JsonProperty(JSON_PROPERTY_STRIKE_PRICE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setStrikePrice(Double strikePrice) {
    this.strikePrice = strikePrice;
  }


  public FuturesOptionChainsExpirationsStrikes call(String call) {
    
    this.call = call;
    return this;
  }

   /**
   * 
   * @return call
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CALL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getCall() {
    return call;
  }


  @JsonProperty(JSON_PROPERTY_CALL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setCall(String call) {
    this.call = call;
  }


  public FuturesOptionChainsExpirationsStrikes callStreamerSymbol(String callStreamerSymbol) {
    
    this.callStreamerSymbol = callStreamerSymbol;
    return this;
  }

   /**
   * 
   * @return callStreamerSymbol
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CALL_STREAMER_SYMBOL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getCallStreamerSymbol() {
    return callStreamerSymbol;
  }


  @JsonProperty(JSON_PROPERTY_CALL_STREAMER_SYMBOL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setCallStreamerSymbol(String callStreamerSymbol) {
    this.callStreamerSymbol = callStreamerSymbol;
  }


  public FuturesOptionChainsExpirationsStrikes put(String put) {
    
    this.put = put;
    return this;
  }

   /**
   * 
   * @return put
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_PUT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getPut() {
    return put;
  }


  @JsonProperty(JSON_PROPERTY_PUT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setPut(String put) {
    this.put = put;
  }


  public FuturesOptionChainsExpirationsStrikes putStreamerSymbol(String putStreamerSymbol) {
    
    this.putStreamerSymbol = putStreamerSymbol;
    return this;
  }

   /**
   * 
   * @return putStreamerSymbol
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_PUT_STREAMER_SYMBOL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getPutStreamerSymbol() {
    return putStreamerSymbol;
  }


  @JsonProperty(JSON_PROPERTY_PUT_STREAMER_SYMBOL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setPutStreamerSymbol(String putStreamerSymbol) {
    this.putStreamerSymbol = putStreamerSymbol;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FuturesOptionChainsExpirationsStrikes futuresNestedOptionChainSerializerOptionChainsExpirationsStrikes = (FuturesOptionChainsExpirationsStrikes) o;
    return Objects.equals(this.strikePrice, futuresNestedOptionChainSerializerOptionChainsExpirationsStrikes.strikePrice) &&
        Objects.equals(this.call, futuresNestedOptionChainSerializerOptionChainsExpirationsStrikes.call) &&
        Objects.equals(this.callStreamerSymbol, futuresNestedOptionChainSerializerOptionChainsExpirationsStrikes.callStreamerSymbol) &&
        Objects.equals(this.put, futuresNestedOptionChainSerializerOptionChainsExpirationsStrikes.put) &&
        Objects.equals(this.putStreamerSymbol, futuresNestedOptionChainSerializerOptionChainsExpirationsStrikes.putStreamerSymbol);
  }

  @Override
  public int hashCode() {
    return Objects.hash(strikePrice, call, callStreamerSymbol, put, putStreamerSymbol);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FuturesNestedOptionChainSerializerOptionChainsExpirationsStrikes {\n");
    sb.append("    strikePrice: ").append(toIndentedString(strikePrice)).append("\n");
    sb.append("    call: ").append(toIndentedString(call)).append("\n");
    sb.append("    callStreamerSymbol: ").append(toIndentedString(callStreamerSymbol)).append("\n");
    sb.append("    put: ").append(toIndentedString(put)).append("\n");
    sb.append("    putStreamerSymbol: ").append(toIndentedString(putStreamerSymbol)).append("\n");
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

