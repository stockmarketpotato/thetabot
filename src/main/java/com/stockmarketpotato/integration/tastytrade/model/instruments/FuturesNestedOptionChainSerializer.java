package com.stockmarketpotato.integration.tastytrade.model.instruments;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * FuturesNestedOptionChainSerializer model
 */
@JsonPropertyOrder({
  FuturesNestedOptionChainSerializer.JSON_PROPERTY_FUTURES,
  FuturesNestedOptionChainSerializer.JSON_PROPERTY_OPTION_CHAINS
})
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-02-14T09:40:37.746219+01:00[Europe/Berlin]")
public class FuturesNestedOptionChainSerializer {
  public static final String JSON_PROPERTY_FUTURES = "futures";
  private List<FuturesNestedOptionChainSerializerFutures> futures;

  public static final String JSON_PROPERTY_OPTION_CHAINS = "option-chains";
  private List<FuturesOptionChain> optionChains;

  public FuturesNestedOptionChainSerializer() {
  }

  public FuturesNestedOptionChainSerializer futures(List<FuturesNestedOptionChainSerializerFutures> futures) {
    
    this.futures = futures;
    return this;
  }

   /**
   * Get futures
   * @return futures
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_FUTURES)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public List<FuturesNestedOptionChainSerializerFutures> getFutures() {
    return futures;
  }


  @JsonProperty(JSON_PROPERTY_FUTURES)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setFutures(List<FuturesNestedOptionChainSerializerFutures> futures) {
    this.futures = futures;
  }


  public FuturesNestedOptionChainSerializer optionChains(List<FuturesOptionChain> optionChains) {
    
    this.optionChains = optionChains;
    return this;
  }

   /**
   * Get optionChains
   * @return optionChains
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_OPTION_CHAINS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public List<FuturesOptionChain> getOptionChains() {
    return optionChains;
  }


  @JsonProperty(JSON_PROPERTY_OPTION_CHAINS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setOptionChains(List<FuturesOptionChain> optionChains) {
    this.optionChains = optionChains;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FuturesNestedOptionChainSerializer futuresNestedOptionChainSerializer = (FuturesNestedOptionChainSerializer) o;
    return Objects.equals(this.futures, futuresNestedOptionChainSerializer.futures) &&
        Objects.equals(this.optionChains, futuresNestedOptionChainSerializer.optionChains);
  }

  @Override
  public int hashCode() {
    return Objects.hash(futures, optionChains);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FuturesNestedOptionChainSerializer {\n");
    sb.append("    futures: ").append(toIndentedString(futures)).append("\n");
    sb.append("    optionChains: ").append(toIndentedString(optionChains)).append("\n");
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

