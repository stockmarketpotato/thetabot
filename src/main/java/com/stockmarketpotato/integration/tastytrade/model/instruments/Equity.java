package com.stockmarketpotato.integration.tastytrade.model.instruments;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Equity model
 */
@JsonPropertyOrder({
  Equity.JSON_PROPERTY_SYMBOL,
  Equity.JSON_PROPERTY_INSTRUMENT_TYPE,
  Equity.JSON_PROPERTY_SHORT_DESCRIPTION,
  Equity.JSON_PROPERTY_IS_INDEX,
  Equity.JSON_PROPERTY_LISTED_MARKET,
  Equity.JSON_PROPERTY_DESCRIPTION,
  Equity.JSON_PROPERTY_LENDABILITY,
  Equity.JSON_PROPERTY_BORROW_RATE,
  Equity.JSON_PROPERTY_HALTED_AT,
  Equity.JSON_PROPERTY_STOPS_TRADING_AT,
  Equity.JSON_PROPERTY_MARKET_TIME_INSTRUMENT_COLLECTION,
  Equity.JSON_PROPERTY_IS_CLOSING_ONLY,
  Equity.JSON_PROPERTY_IS_OPTIONS_CLOSING_ONLY,
  Equity.JSON_PROPERTY_ACTIVE,
  Equity.JSON_PROPERTY_IS_FRACTIONAL_QUANTITY_ELIGIBLE,
  Equity.JSON_PROPERTY_IS_ILLIQUID,
  Equity.JSON_PROPERTY_IS_ETF,
  Equity.JSON_PROPERTY_STREAMER_SYMBOL,
  Equity.JSON_PROPERTY_TICK_SIZES,
  Equity.JSON_PROPERTY_OPTION_TICK_SIZES
})
@JsonIgnoreProperties(ignoreUnknown = true)
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-02-14T09:40:37.746219+01:00[Europe/Berlin]")
public class Equity extends AbstractInstrument {
  public static final String JSON_PROPERTY_SYMBOL = "symbol";
  private String symbol;

  public static final String JSON_PROPERTY_INSTRUMENT_TYPE = "instrument-type";
  private String instrumentType;

  public static final String JSON_PROPERTY_SHORT_DESCRIPTION = "short-description";
  private String shortDescription;

  public static final String JSON_PROPERTY_IS_INDEX = "is-index";
  private Boolean isIndex;

  public static final String JSON_PROPERTY_LISTED_MARKET = "listed-market";
  private String listedMarket;

  public static final String JSON_PROPERTY_DESCRIPTION = "description";
  private String description;

  public static final String JSON_PROPERTY_LENDABILITY = "lendability";
  private String lendability;

  public static final String JSON_PROPERTY_BORROW_RATE = "borrow-rate";
  private Double borrowRate;

  public static final String JSON_PROPERTY_HALTED_AT = "halted-at";
  private OffsetDateTime haltedAt;

  public static final String JSON_PROPERTY_STOPS_TRADING_AT = "stops-trading-at";
  private OffsetDateTime stopsTradingAt;

  public static final String JSON_PROPERTY_MARKET_TIME_INSTRUMENT_COLLECTION = "market-time-instrument-collection";
  private String marketTimeInstrumentCollection;

  public static final String JSON_PROPERTY_IS_CLOSING_ONLY = "is-closing-only";
  private Boolean isClosingOnly;

  public static final String JSON_PROPERTY_IS_OPTIONS_CLOSING_ONLY = "is-options-closing-only";
  private Boolean isOptionsClosingOnly;

  public static final String JSON_PROPERTY_ACTIVE = "active";
  private Boolean active;

  public static final String JSON_PROPERTY_IS_FRACTIONAL_QUANTITY_ELIGIBLE = "is-fractional-quantity-eligible";
  private Boolean isFractionalQuantityEligible;

  public static final String JSON_PROPERTY_IS_ILLIQUID = "is-illiquid";
  private Boolean isIlliquid;

  public static final String JSON_PROPERTY_IS_ETF = "is-etf";
  private Boolean isEtf;

  public static final String JSON_PROPERTY_STREAMER_SYMBOL = "streamer-symbol";
  private String streamerSymbol;

  public static final String JSON_PROPERTY_TICK_SIZES = "tick-sizes";
  private List<EquityTickSizes> tickSizes;

  public static final String JSON_PROPERTY_OPTION_TICK_SIZES = "option-tick-sizes";
  private List<EquityTickSizes> optionTickSizes;

  public Equity() {
  }

  public Equity symbol(String symbol) {
    
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


  public Equity instrumentType(String instrumentType) {
    
    this.instrumentType = instrumentType;
    return this;
  }

   /**
   * 
   * @return instrumentType
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_INSTRUMENT_TYPE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getInstrumentType() {
    return instrumentType;
  }


  @JsonProperty(JSON_PROPERTY_INSTRUMENT_TYPE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setInstrumentType(String instrumentType) {
    this.instrumentType = instrumentType;
  }


  public Equity shortDescription(String shortDescription) {
    
    this.shortDescription = shortDescription;
    return this;
  }

   /**
   * 
   * @return shortDescription
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SHORT_DESCRIPTION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getShortDescription() {
    return shortDescription;
  }


  @JsonProperty(JSON_PROPERTY_SHORT_DESCRIPTION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setShortDescription(String shortDescription) {
    this.shortDescription = shortDescription;
  }


  public Equity isIndex(Boolean isIndex) {
    
    this.isIndex = isIndex;
    return this;
  }

   /**
   * 
   * @return isIndex
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_IS_INDEX)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getIsIndex() {
    return isIndex;
  }


  @JsonProperty(JSON_PROPERTY_IS_INDEX)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setIsIndex(Boolean isIndex) {
    this.isIndex = isIndex;
  }


  public Equity listedMarket(String listedMarket) {
    
    this.listedMarket = listedMarket;
    return this;
  }

   /**
   * 
   * @return listedMarket
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_LISTED_MARKET)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getListedMarket() {
    return listedMarket;
  }


  @JsonProperty(JSON_PROPERTY_LISTED_MARKET)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setListedMarket(String listedMarket) {
    this.listedMarket = listedMarket;
  }


  public Equity description(String description) {
    
    this.description = description;
    return this;
  }

   /**
   * 
   * @return description
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_DESCRIPTION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getDescription() {
    return description;
  }


  @JsonProperty(JSON_PROPERTY_DESCRIPTION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setDescription(String description) {
    this.description = description;
  }


  public Equity lendability(String lendability) {
    
    this.lendability = lendability;
    return this;
  }

   /**
   * 
   * @return lendability
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_LENDABILITY)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getLendability() {
    return lendability;
  }


  @JsonProperty(JSON_PROPERTY_LENDABILITY)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setLendability(String lendability) {
    this.lendability = lendability;
  }


  public Equity borrowRate(Double borrowRate) {
    
    this.borrowRate = borrowRate;
    return this;
  }

   /**
   * 
   * @return borrowRate
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_BORROW_RATE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Double getBorrowRate() {
    return borrowRate;
  }


  @JsonProperty(JSON_PROPERTY_BORROW_RATE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setBorrowRate(Double borrowRate) {
    this.borrowRate = borrowRate;
  }


  public Equity haltedAt(OffsetDateTime haltedAt) {
    
    this.haltedAt = haltedAt;
    return this;
  }

   /**
   * 
   * @return haltedAt
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_HALTED_AT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public OffsetDateTime getHaltedAt() {
    return haltedAt;
  }


  @JsonProperty(JSON_PROPERTY_HALTED_AT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setHaltedAt(OffsetDateTime haltedAt) {
    this.haltedAt = haltedAt;
  }


  public Equity stopsTradingAt(OffsetDateTime stopsTradingAt) {
    
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


  public Equity marketTimeInstrumentCollection(String marketTimeInstrumentCollection) {
    
    this.marketTimeInstrumentCollection = marketTimeInstrumentCollection;
    return this;
  }

   /**
   * 
   * @return marketTimeInstrumentCollection
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_MARKET_TIME_INSTRUMENT_COLLECTION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getMarketTimeInstrumentCollection() {
    return marketTimeInstrumentCollection;
  }


  @JsonProperty(JSON_PROPERTY_MARKET_TIME_INSTRUMENT_COLLECTION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setMarketTimeInstrumentCollection(String marketTimeInstrumentCollection) {
    this.marketTimeInstrumentCollection = marketTimeInstrumentCollection;
  }


  public Equity isClosingOnly(Boolean isClosingOnly) {
    
    this.isClosingOnly = isClosingOnly;
    return this;
  }

   /**
   * 
   * @return isClosingOnly
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_IS_CLOSING_ONLY)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getIsClosingOnly() {
    return isClosingOnly;
  }


  @JsonProperty(JSON_PROPERTY_IS_CLOSING_ONLY)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setIsClosingOnly(Boolean isClosingOnly) {
    this.isClosingOnly = isClosingOnly;
  }


  public Equity isOptionsClosingOnly(Boolean isOptionsClosingOnly) {
    
    this.isOptionsClosingOnly = isOptionsClosingOnly;
    return this;
  }

   /**
   * 
   * @return isOptionsClosingOnly
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_IS_OPTIONS_CLOSING_ONLY)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getIsOptionsClosingOnly() {
    return isOptionsClosingOnly;
  }


  @JsonProperty(JSON_PROPERTY_IS_OPTIONS_CLOSING_ONLY)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setIsOptionsClosingOnly(Boolean isOptionsClosingOnly) {
    this.isOptionsClosingOnly = isOptionsClosingOnly;
  }


  public Equity active(Boolean active) {
    
    this.active = active;
    return this;
  }

   /**
   * 
   * @return active
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ACTIVE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getActive() {
    return active;
  }


  @JsonProperty(JSON_PROPERTY_ACTIVE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setActive(Boolean active) {
    this.active = active;
  }


  public Equity isFractionalQuantityEligible(Boolean isFractionalQuantityEligible) {
    
    this.isFractionalQuantityEligible = isFractionalQuantityEligible;
    return this;
  }

   /**
   * 
   * @return isFractionalQuantityEligible
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_IS_FRACTIONAL_QUANTITY_ELIGIBLE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getIsFractionalQuantityEligible() {
    return isFractionalQuantityEligible;
  }


  @JsonProperty(JSON_PROPERTY_IS_FRACTIONAL_QUANTITY_ELIGIBLE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setIsFractionalQuantityEligible(Boolean isFractionalQuantityEligible) {
    this.isFractionalQuantityEligible = isFractionalQuantityEligible;
  }


  public Equity isIlliquid(Boolean isIlliquid) {
    
    this.isIlliquid = isIlliquid;
    return this;
  }

   /**
   * 
   * @return isIlliquid
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_IS_ILLIQUID)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getIsIlliquid() {
    return isIlliquid;
  }


  @JsonProperty(JSON_PROPERTY_IS_ILLIQUID)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setIsIlliquid(Boolean isIlliquid) {
    this.isIlliquid = isIlliquid;
  }


  public Equity isEtf(Boolean isEtf) {
    
    this.isEtf = isEtf;
    return this;
  }

   /**
   * 
   * @return isEtf
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_IS_ETF)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getIsEtf() {
    return isEtf;
  }


  @JsonProperty(JSON_PROPERTY_IS_ETF)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setIsEtf(Boolean isEtf) {
    this.isEtf = isEtf;
  }


  public Equity streamerSymbol(String streamerSymbol) {
    
    this.streamerSymbol = streamerSymbol;
    return this;
  }

   /**
   * 
   * @return streamerSymbol
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_STREAMER_SYMBOL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getStreamerSymbol() {
    return streamerSymbol;
  }


  @JsonProperty(JSON_PROPERTY_STREAMER_SYMBOL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setStreamerSymbol(String streamerSymbol) {
    this.streamerSymbol = streamerSymbol;
  }


  public Equity tickSizes(List<EquityTickSizes> tickSizes) {
    
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


  public Equity optionTickSizes(List<EquityTickSizes> optionTickSizes) {
    
    this.optionTickSizes = optionTickSizes;
    return this;
  }

   /**
   * Get optionTickSizes
   * @return optionTickSizes
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_OPTION_TICK_SIZES)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public List<EquityTickSizes> getOptionTickSizes() {
    return optionTickSizes;
  }


  @JsonProperty(JSON_PROPERTY_OPTION_TICK_SIZES)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setOptionTickSizes(List<EquityTickSizes> optionTickSizes) {
    this.optionTickSizes = optionTickSizes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Equity equity = (Equity) o;
    return Objects.equals(this.symbol, equity.symbol) &&
        Objects.equals(this.instrumentType, equity.instrumentType) &&
        Objects.equals(this.shortDescription, equity.shortDescription) &&
        Objects.equals(this.isIndex, equity.isIndex) &&
        Objects.equals(this.listedMarket, equity.listedMarket) &&
        Objects.equals(this.description, equity.description) &&
        Objects.equals(this.lendability, equity.lendability) &&
        Objects.equals(this.borrowRate, equity.borrowRate) &&
        Objects.equals(this.haltedAt, equity.haltedAt) &&
        Objects.equals(this.stopsTradingAt, equity.stopsTradingAt) &&
        Objects.equals(this.marketTimeInstrumentCollection, equity.marketTimeInstrumentCollection) &&
        Objects.equals(this.isClosingOnly, equity.isClosingOnly) &&
        Objects.equals(this.isOptionsClosingOnly, equity.isOptionsClosingOnly) &&
        Objects.equals(this.active, equity.active) &&
        Objects.equals(this.isFractionalQuantityEligible, equity.isFractionalQuantityEligible) &&
        Objects.equals(this.isIlliquid, equity.isIlliquid) &&
        Objects.equals(this.isEtf, equity.isEtf) &&
        Objects.equals(this.streamerSymbol, equity.streamerSymbol) &&
        Objects.equals(this.tickSizes, equity.tickSizes) &&
        Objects.equals(this.optionTickSizes, equity.optionTickSizes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(symbol, instrumentType, shortDescription, isIndex, listedMarket, description, lendability, borrowRate, haltedAt, stopsTradingAt, marketTimeInstrumentCollection, isClosingOnly, isOptionsClosingOnly, active, isFractionalQuantityEligible, isIlliquid, isEtf, streamerSymbol, tickSizes, optionTickSizes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Equity {\n");
    sb.append("    symbol: ").append(toIndentedString(symbol)).append("\n");
    sb.append("    instrumentType: ").append(toIndentedString(instrumentType)).append("\n");
    sb.append("    shortDescription: ").append(toIndentedString(shortDescription)).append("\n");
    sb.append("    isIndex: ").append(toIndentedString(isIndex)).append("\n");
    sb.append("    listedMarket: ").append(toIndentedString(listedMarket)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    lendability: ").append(toIndentedString(lendability)).append("\n");
    sb.append("    borrowRate: ").append(toIndentedString(borrowRate)).append("\n");
    sb.append("    haltedAt: ").append(toIndentedString(haltedAt)).append("\n");
    sb.append("    stopsTradingAt: ").append(toIndentedString(stopsTradingAt)).append("\n");
    sb.append("    marketTimeInstrumentCollection: ").append(toIndentedString(marketTimeInstrumentCollection)).append("\n");
    sb.append("    isClosingOnly: ").append(toIndentedString(isClosingOnly)).append("\n");
    sb.append("    isOptionsClosingOnly: ").append(toIndentedString(isOptionsClosingOnly)).append("\n");
    sb.append("    active: ").append(toIndentedString(active)).append("\n");
    sb.append("    isFractionalQuantityEligible: ").append(toIndentedString(isFractionalQuantityEligible)).append("\n");
    sb.append("    isIlliquid: ").append(toIndentedString(isIlliquid)).append("\n");
    sb.append("    isEtf: ").append(toIndentedString(isEtf)).append("\n");
    sb.append("    streamerSymbol: ").append(toIndentedString(streamerSymbol)).append("\n");
    sb.append("    tickSizes: ").append(toIndentedString(tickSizes)).append("\n");
    sb.append("    optionTickSizes: ").append(toIndentedString(optionTickSizes)).append("\n");
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

