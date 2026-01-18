package com.stockmarketpotato.integration.tastytrade.model.instruments;

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
  FuturesOptionChain.JSON_PROPERTY_UNDERLYING_SYMBOL,
  FuturesOptionChain.JSON_PROPERTY_ROOT_SYMBOL,
  FuturesOptionChain.JSON_PROPERTY_EXERCISE_STYLE,
  FuturesOptionChain.JSON_PROPERTY_EXPIRATIONS
})
@JsonTypeName("FuturesNestedOptionChainSerializer_option_chains")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-02-14T09:40:37.746219+01:00[Europe/Berlin]")
public class FuturesOptionChain {
  public static final String JSON_PROPERTY_UNDERLYING_SYMBOL = "underlying-symbol";
  private String underlyingSymbol;

  public static final String JSON_PROPERTY_ROOT_SYMBOL = "root-symbol";
  private String rootSymbol;

  public static final String JSON_PROPERTY_EXERCISE_STYLE = "exercise-style";
  private String exerciseStyle;

  public static final String JSON_PROPERTY_EXPIRATIONS = "expirations";
  private List<FuturesOptionChainExpirations> expirations;

  public FuturesOptionChain() {
  }

  public FuturesOptionChain underlyingSymbol(String underlyingSymbol) {
    
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


  public FuturesOptionChain rootSymbol(String rootSymbol) {
    
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


  public FuturesOptionChain exerciseStyle(String exerciseStyle) {
    
    this.exerciseStyle = exerciseStyle;
    return this;
  }

   /**
   * 
   * @return exerciseStyle
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_EXERCISE_STYLE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getExerciseStyle() {
    return exerciseStyle;
  }


  @JsonProperty(JSON_PROPERTY_EXERCISE_STYLE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setExerciseStyle(String exerciseStyle) {
    this.exerciseStyle = exerciseStyle;
  }


  public FuturesOptionChain expirations(List<FuturesOptionChainExpirations> expirations) {
    
    this.expirations = expirations;
    return this;
  }

   /**
   * Get expirations
   * @return expirations
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_EXPIRATIONS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public List<FuturesOptionChainExpirations> getExpirations() {
    return expirations;
  }


  @JsonProperty(JSON_PROPERTY_EXPIRATIONS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setExpirations(List<FuturesOptionChainExpirations> expirations) {
    this.expirations = expirations;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FuturesOptionChain futuresNestedOptionChainSerializerOptionChains = (FuturesOptionChain) o;
    return Objects.equals(this.underlyingSymbol, futuresNestedOptionChainSerializerOptionChains.underlyingSymbol) &&
        Objects.equals(this.rootSymbol, futuresNestedOptionChainSerializerOptionChains.rootSymbol) &&
        Objects.equals(this.exerciseStyle, futuresNestedOptionChainSerializerOptionChains.exerciseStyle) &&
        Objects.equals(this.expirations, futuresNestedOptionChainSerializerOptionChains.expirations);
  }

  @Override
  public int hashCode() {
    return Objects.hash(underlyingSymbol, rootSymbol, exerciseStyle, expirations);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FuturesNestedOptionChainSerializerOptionChains {\n");
    sb.append("    underlyingSymbol: ").append(toIndentedString(underlyingSymbol)).append("\n");
    sb.append("    rootSymbol: ").append(toIndentedString(rootSymbol)).append("\n");
    sb.append("    exerciseStyle: ").append(toIndentedString(exerciseStyle)).append("\n");
    sb.append("    expirations: ").append(toIndentedString(expirations)).append("\n");
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

