package com.stockmarketpotato.integration.tastytrade.model.instruments;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Future model
 */
@JsonPropertyOrder({
  Future.JSON_PROPERTY_SYMBOL,
  Future.JSON_PROPERTY_PRODUCT_CODE,
  Future.JSON_PROPERTY_CONTRACT_SIZE,
  Future.JSON_PROPERTY_TICK_SIZE,
  Future.JSON_PROPERTY_NOTIONAL_MULTIPLIER,
  Future.JSON_PROPERTY_MAIN_FRACTION,
  Future.JSON_PROPERTY_SUB_FRACTION,
  Future.JSON_PROPERTY_DISPLAY_FACTOR,
  Future.JSON_PROPERTY_LAST_TRADE_DATE,
  Future.JSON_PROPERTY_EXPIRATION_DATE,
  Future.JSON_PROPERTY_CLOSING_ONLY_DATE,
  Future.JSON_PROPERTY_ACTIVE,
  Future.JSON_PROPERTY_ACTIVE_MONTH,
  Future.JSON_PROPERTY_NEXT_ACTIVE_MONTH,
  Future.JSON_PROPERTY_IS_CLOSING_ONLY,
  Future.JSON_PROPERTY_FIRST_NOTICE_DATE,
  Future.JSON_PROPERTY_STOPS_TRADING_AT,
  Future.JSON_PROPERTY_EXPIRES_AT,
  Future.JSON_PROPERTY_PRODUCT_GROUP,
  Future.JSON_PROPERTY_EXCHANGE,
  Future.JSON_PROPERTY_ROLL_TARGET_SYMBOL,
  Future.JSON_PROPERTY_STREAMER_EXCHANGE_CODE,
  Future.JSON_PROPERTY_STREAMER_SYMBOL,
  Future.JSON_PROPERTY_BACK_MONTH_FIRST_CALENDAR_SYMBOL,
  Future.JSON_PROPERTY_IS_TRADEABLE,
  Future.JSON_PROPERTY_TRUE_UNDERLYING_SYMBOL,
  Future.JSON_PROPERTY_EXCHANGE_DATA,
  Future.JSON_PROPERTY_FUTURE_ETF_EQUIVALENT,
  Future.JSON_PROPERTY_FUTURE_PRODUCT,
  Future.JSON_PROPERTY_TICK_SIZES,
  Future.JSON_PROPERTY_OPTION_TICK_SIZES,
  Future.JSON_PROPERTY_SPREAD_TICK_SIZES
})
@JsonIgnoreProperties(ignoreUnknown = true)
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-02-14T09:40:37.746219+01:00[Europe/Berlin]")
public class Future extends AbstractInstrument {
  public static final String JSON_PROPERTY_SYMBOL = "symbol";
  private String symbol;

  public static final String JSON_PROPERTY_PRODUCT_CODE = "product-code";
  private String productCode;

  public static final String JSON_PROPERTY_CONTRACT_SIZE = "contract-size";
  private Double contractSize;

  public static final String JSON_PROPERTY_TICK_SIZE = "tick-size";
  private Double tickSize;

  public static final String JSON_PROPERTY_NOTIONAL_MULTIPLIER = "notional-multiplier";
  private Double notionalMultiplier;

  public static final String JSON_PROPERTY_MAIN_FRACTION = "main-fraction";
  private Double mainFraction;

  public static final String JSON_PROPERTY_SUB_FRACTION = "sub-fraction";
  private Double subFraction;

  public static final String JSON_PROPERTY_DISPLAY_FACTOR = "display-factor";
  private Double displayFactor;

  public static final String JSON_PROPERTY_LAST_TRADE_DATE = "last-trade-date";
  private LocalDate lastTradeDate;

  public static final String JSON_PROPERTY_EXPIRATION_DATE = "expiration-date";
  private LocalDate expirationDate;

  public static final String JSON_PROPERTY_CLOSING_ONLY_DATE = "closing-only-date";
  private LocalDate closingOnlyDate;

  public static final String JSON_PROPERTY_ACTIVE = "active";
  private Boolean active;

  public static final String JSON_PROPERTY_ACTIVE_MONTH = "active-month";
  private Boolean activeMonth;

  public static final String JSON_PROPERTY_NEXT_ACTIVE_MONTH = "next-active-month";
  private Boolean nextActiveMonth;

  public static final String JSON_PROPERTY_IS_CLOSING_ONLY = "is-closing-only";
  private Boolean isClosingOnly;

  public static final String JSON_PROPERTY_FIRST_NOTICE_DATE = "first-notice-date";
  private LocalDate firstNoticeDate;

  public static final String JSON_PROPERTY_STOPS_TRADING_AT = "stops-trading-at";
  private OffsetDateTime stopsTradingAt;

  public static final String JSON_PROPERTY_EXPIRES_AT = "expires-at";
  private OffsetDateTime expiresAt;

  public static final String JSON_PROPERTY_PRODUCT_GROUP = "product-group";
  private String productGroup;

  public static final String JSON_PROPERTY_EXCHANGE = "exchange";
  private String exchange;

  public static final String JSON_PROPERTY_ROLL_TARGET_SYMBOL = "roll-target-symbol";
  private String rollTargetSymbol;

  public static final String JSON_PROPERTY_STREAMER_EXCHANGE_CODE = "streamer-exchange-code";
  private String streamerExchangeCode;

  public static final String JSON_PROPERTY_STREAMER_SYMBOL = "streamer-symbol";
  private String streamerSymbol;

  public static final String JSON_PROPERTY_BACK_MONTH_FIRST_CALENDAR_SYMBOL = "back-month-first-calendar-symbol";
  private Boolean backMonthFirstCalendarSymbol;

  public static final String JSON_PROPERTY_IS_TRADEABLE = "is-tradeable";
  private Boolean isTradeable;

  public static final String JSON_PROPERTY_TRUE_UNDERLYING_SYMBOL = "true-underlying-symbol";
  private String trueUnderlyingSymbol;

  public static final String JSON_PROPERTY_EXCHANGE_DATA = "exchange-data";
  private Object exchangeData;

  public static final String JSON_PROPERTY_FUTURE_ETF_EQUIVALENT = "future-etf-equivalent";
  private FutureFutureEtfEquivalent futureEtfEquivalent;

  public static final String JSON_PROPERTY_FUTURE_PRODUCT = "future-product";
  private FutureFutureProduct futureProduct;

  public static final String JSON_PROPERTY_TICK_SIZES = "tick-sizes";
  private List<EquityTickSizes> tickSizes;

  public static final String JSON_PROPERTY_OPTION_TICK_SIZES = "option-tick-sizes";
  private List<EquityTickSizes> optionTickSizes;

  public static final String JSON_PROPERTY_SPREAD_TICK_SIZES = "spread-tick-sizes";
  private List<EquityTickSizes> spreadTickSizes;

  public Future() {
  }

  public Future symbol(String symbol) {
    
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


  public Future productCode(String productCode) {
    
    this.productCode = productCode;
    return this;
  }

   /**
   * 
   * @return productCode
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_PRODUCT_CODE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getProductCode() {
    return productCode;
  }


  @JsonProperty(JSON_PROPERTY_PRODUCT_CODE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setProductCode(String productCode) {
    this.productCode = productCode;
  }


  public Future contractSize(Double contractSize) {
    
    this.contractSize = contractSize;
    return this;
  }

   /**
   * 
   * @return contractSize
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CONTRACT_SIZE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Double getContractSize() {
    return contractSize;
  }


  @JsonProperty(JSON_PROPERTY_CONTRACT_SIZE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setContractSize(Double contractSize) {
    this.contractSize = contractSize;
  }


  public Future tickSize(Double tickSize) {
    
    this.tickSize = tickSize;
    return this;
  }

   /**
   * 
   * @return tickSize
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_TICK_SIZE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Double getTickSize() {
    return tickSize;
  }


  @JsonProperty(JSON_PROPERTY_TICK_SIZE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setTickSize(Double tickSize) {
    this.tickSize = tickSize;
  }


  public Future notionalMultiplier(Double notionalMultiplier) {
    
    this.notionalMultiplier = notionalMultiplier;
    return this;
  }

   /**
   * 
   * @return notionalMultiplier
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_NOTIONAL_MULTIPLIER)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Double getNotionalMultiplier() {
    return notionalMultiplier;
  }


  @JsonProperty(JSON_PROPERTY_NOTIONAL_MULTIPLIER)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setNotionalMultiplier(Double notionalMultiplier) {
    this.notionalMultiplier = notionalMultiplier;
  }


  public Future mainFraction(Double mainFraction) {
    
    this.mainFraction = mainFraction;
    return this;
  }

   /**
   * 
   * @return mainFraction
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_MAIN_FRACTION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Double getMainFraction() {
    return mainFraction;
  }


  @JsonProperty(JSON_PROPERTY_MAIN_FRACTION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setMainFraction(Double mainFraction) {
    this.mainFraction = mainFraction;
  }


  public Future subFraction(Double subFraction) {
    
    this.subFraction = subFraction;
    return this;
  }

   /**
   * 
   * @return subFraction
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SUB_FRACTION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Double getSubFraction() {
    return subFraction;
  }


  @JsonProperty(JSON_PROPERTY_SUB_FRACTION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setSubFraction(Double subFraction) {
    this.subFraction = subFraction;
  }


  public Future displayFactor(Double displayFactor) {
    
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


  public Future lastTradeDate(LocalDate lastTradeDate) {
    
    this.lastTradeDate = lastTradeDate;
    return this;
  }

   /**
   * 
   * @return lastTradeDate
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_LAST_TRADE_DATE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public LocalDate getLastTradeDate() {
    return lastTradeDate;
  }


  @JsonProperty(JSON_PROPERTY_LAST_TRADE_DATE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setLastTradeDate(LocalDate lastTradeDate) {
    this.lastTradeDate = lastTradeDate;
  }


  public Future expirationDate(LocalDate expirationDate) {
    
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


  public Future closingOnlyDate(LocalDate closingOnlyDate) {
    
    this.closingOnlyDate = closingOnlyDate;
    return this;
  }

   /**
   * 
   * @return closingOnlyDate
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CLOSING_ONLY_DATE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public LocalDate getClosingOnlyDate() {
    return closingOnlyDate;
  }


  @JsonProperty(JSON_PROPERTY_CLOSING_ONLY_DATE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setClosingOnlyDate(LocalDate closingOnlyDate) {
    this.closingOnlyDate = closingOnlyDate;
  }


  public Future active(Boolean active) {
    
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


  public Future activeMonth(Boolean activeMonth) {
    
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


  public Future nextActiveMonth(Boolean nextActiveMonth) {
    
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


  public Future isClosingOnly(Boolean isClosingOnly) {
    
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


  public Future firstNoticeDate(LocalDate firstNoticeDate) {
    
    this.firstNoticeDate = firstNoticeDate;
    return this;
  }

   /**
   * 
   * @return firstNoticeDate
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_FIRST_NOTICE_DATE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public LocalDate getFirstNoticeDate() {
    return firstNoticeDate;
  }


  @JsonProperty(JSON_PROPERTY_FIRST_NOTICE_DATE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setFirstNoticeDate(LocalDate firstNoticeDate) {
    this.firstNoticeDate = firstNoticeDate;
  }


  public Future stopsTradingAt(OffsetDateTime stopsTradingAt) {
    
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


  public Future expiresAt(OffsetDateTime expiresAt) {
    
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


  public Future productGroup(String productGroup) {
    
    this.productGroup = productGroup;
    return this;
  }

   /**
   * 
   * @return productGroup
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_PRODUCT_GROUP)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getProductGroup() {
    return productGroup;
  }


  @JsonProperty(JSON_PROPERTY_PRODUCT_GROUP)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setProductGroup(String productGroup) {
    this.productGroup = productGroup;
  }


  public Future exchange(String exchange) {
    
    this.exchange = exchange;
    return this;
  }

   /**
   * 
   * @return exchange
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_EXCHANGE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getExchange() {
    return exchange;
  }


  @JsonProperty(JSON_PROPERTY_EXCHANGE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setExchange(String exchange) {
    this.exchange = exchange;
  }


  public Future rollTargetSymbol(String rollTargetSymbol) {
    
    this.rollTargetSymbol = rollTargetSymbol;
    return this;
  }

   /**
   * 
   * @return rollTargetSymbol
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ROLL_TARGET_SYMBOL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getRollTargetSymbol() {
    return rollTargetSymbol;
  }


  @JsonProperty(JSON_PROPERTY_ROLL_TARGET_SYMBOL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setRollTargetSymbol(String rollTargetSymbol) {
    this.rollTargetSymbol = rollTargetSymbol;
  }


  public Future streamerExchangeCode(String streamerExchangeCode) {
    
    this.streamerExchangeCode = streamerExchangeCode;
    return this;
  }

   /**
   * 
   * @return streamerExchangeCode
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_STREAMER_EXCHANGE_CODE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getStreamerExchangeCode() {
    return streamerExchangeCode;
  }


  @JsonProperty(JSON_PROPERTY_STREAMER_EXCHANGE_CODE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setStreamerExchangeCode(String streamerExchangeCode) {
    this.streamerExchangeCode = streamerExchangeCode;
  }


  public Future streamerSymbol(String streamerSymbol) {
    
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


  public Future backMonthFirstCalendarSymbol(Boolean backMonthFirstCalendarSymbol) {
    
    this.backMonthFirstCalendarSymbol = backMonthFirstCalendarSymbol;
    return this;
  }

   /**
   * 
   * @return backMonthFirstCalendarSymbol
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_BACK_MONTH_FIRST_CALENDAR_SYMBOL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getBackMonthFirstCalendarSymbol() {
    return backMonthFirstCalendarSymbol;
  }


  @JsonProperty(JSON_PROPERTY_BACK_MONTH_FIRST_CALENDAR_SYMBOL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setBackMonthFirstCalendarSymbol(Boolean backMonthFirstCalendarSymbol) {
    this.backMonthFirstCalendarSymbol = backMonthFirstCalendarSymbol;
  }


  public Future isTradeable(Boolean isTradeable) {
    
    this.isTradeable = isTradeable;
    return this;
  }

   /**
   * 
   * @return isTradeable
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_IS_TRADEABLE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getIsTradeable() {
    return isTradeable;
  }


  @JsonProperty(JSON_PROPERTY_IS_TRADEABLE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setIsTradeable(Boolean isTradeable) {
    this.isTradeable = isTradeable;
  }


  public Future trueUnderlyingSymbol(String trueUnderlyingSymbol) {
    
    this.trueUnderlyingSymbol = trueUnderlyingSymbol;
    return this;
  }

   /**
   * 
   * @return trueUnderlyingSymbol
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_TRUE_UNDERLYING_SYMBOL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getTrueUnderlyingSymbol() {
    return trueUnderlyingSymbol;
  }


  @JsonProperty(JSON_PROPERTY_TRUE_UNDERLYING_SYMBOL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setTrueUnderlyingSymbol(String trueUnderlyingSymbol) {
    this.trueUnderlyingSymbol = trueUnderlyingSymbol;
  }


  public Future exchangeData(Object exchangeData) {
    
    this.exchangeData = exchangeData;
    return this;
  }

   /**
   * 
   * @return exchangeData
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_EXCHANGE_DATA)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Object getExchangeData() {
    return exchangeData;
  }


  @JsonProperty(JSON_PROPERTY_EXCHANGE_DATA)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setExchangeData(Object exchangeData) {
    this.exchangeData = exchangeData;
  }


  public Future futureEtfEquivalent(FutureFutureEtfEquivalent futureEtfEquivalent) {
    
    this.futureEtfEquivalent = futureEtfEquivalent;
    return this;
  }

   /**
   * Get futureEtfEquivalent
   * @return futureEtfEquivalent
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_FUTURE_ETF_EQUIVALENT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public FutureFutureEtfEquivalent getFutureEtfEquivalent() {
    return futureEtfEquivalent;
  }


  @JsonProperty(JSON_PROPERTY_FUTURE_ETF_EQUIVALENT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setFutureEtfEquivalent(FutureFutureEtfEquivalent futureEtfEquivalent) {
    this.futureEtfEquivalent = futureEtfEquivalent;
  }


  public Future futureProduct(FutureFutureProduct futureProduct) {
    
    this.futureProduct = futureProduct;
    return this;
  }

   /**
   * Get futureProduct
   * @return futureProduct
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_FUTURE_PRODUCT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public FutureFutureProduct getFutureProduct() {
    return futureProduct;
  }


  @JsonProperty(JSON_PROPERTY_FUTURE_PRODUCT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setFutureProduct(FutureFutureProduct futureProduct) {
    this.futureProduct = futureProduct;
  }


  public Future tickSizes(List<EquityTickSizes> tickSizes) {
    
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


  public Future optionTickSizes(List<EquityTickSizes> optionTickSizes) {
    
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


  public Future spreadTickSizes(List<EquityTickSizes> spreadTickSizes) {
    
    this.spreadTickSizes = spreadTickSizes;
    return this;
  }

   /**
   * Get spreadTickSizes
   * @return spreadTickSizes
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SPREAD_TICK_SIZES)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public List<EquityTickSizes> getSpreadTickSizes() {
    return spreadTickSizes;
  }


  @JsonProperty(JSON_PROPERTY_SPREAD_TICK_SIZES)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setSpreadTickSizes(List<EquityTickSizes> spreadTickSizes) {
    this.spreadTickSizes = spreadTickSizes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Future future = (Future) o;
    return Objects.equals(this.symbol, future.symbol) &&
        Objects.equals(this.productCode, future.productCode) &&
        Objects.equals(this.contractSize, future.contractSize) &&
        Objects.equals(this.tickSize, future.tickSize) &&
        Objects.equals(this.notionalMultiplier, future.notionalMultiplier) &&
        Objects.equals(this.mainFraction, future.mainFraction) &&
        Objects.equals(this.subFraction, future.subFraction) &&
        Objects.equals(this.displayFactor, future.displayFactor) &&
        Objects.equals(this.lastTradeDate, future.lastTradeDate) &&
        Objects.equals(this.expirationDate, future.expirationDate) &&
        Objects.equals(this.closingOnlyDate, future.closingOnlyDate) &&
        Objects.equals(this.active, future.active) &&
        Objects.equals(this.activeMonth, future.activeMonth) &&
        Objects.equals(this.nextActiveMonth, future.nextActiveMonth) &&
        Objects.equals(this.isClosingOnly, future.isClosingOnly) &&
        Objects.equals(this.firstNoticeDate, future.firstNoticeDate) &&
        Objects.equals(this.stopsTradingAt, future.stopsTradingAt) &&
        Objects.equals(this.expiresAt, future.expiresAt) &&
        Objects.equals(this.productGroup, future.productGroup) &&
        Objects.equals(this.exchange, future.exchange) &&
        Objects.equals(this.rollTargetSymbol, future.rollTargetSymbol) &&
        Objects.equals(this.streamerExchangeCode, future.streamerExchangeCode) &&
        Objects.equals(this.streamerSymbol, future.streamerSymbol) &&
        Objects.equals(this.backMonthFirstCalendarSymbol, future.backMonthFirstCalendarSymbol) &&
        Objects.equals(this.isTradeable, future.isTradeable) &&
        Objects.equals(this.trueUnderlyingSymbol, future.trueUnderlyingSymbol) &&
        Objects.equals(this.exchangeData, future.exchangeData) &&
        Objects.equals(this.futureEtfEquivalent, future.futureEtfEquivalent) &&
        Objects.equals(this.futureProduct, future.futureProduct) &&
        Objects.equals(this.tickSizes, future.tickSizes) &&
        Objects.equals(this.optionTickSizes, future.optionTickSizes) &&
        Objects.equals(this.spreadTickSizes, future.spreadTickSizes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(symbol, productCode, contractSize, tickSize, notionalMultiplier, mainFraction, subFraction, displayFactor, lastTradeDate, expirationDate, closingOnlyDate, active, activeMonth, nextActiveMonth, isClosingOnly, firstNoticeDate, stopsTradingAt, expiresAt, productGroup, exchange, rollTargetSymbol, streamerExchangeCode, streamerSymbol, backMonthFirstCalendarSymbol, isTradeable, trueUnderlyingSymbol, exchangeData, futureEtfEquivalent, futureProduct, tickSizes, optionTickSizes, spreadTickSizes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Future {\n");
    sb.append("    symbol: ").append(toIndentedString(symbol)).append("\n");
    sb.append("    productCode: ").append(toIndentedString(productCode)).append("\n");
    sb.append("    contractSize: ").append(toIndentedString(contractSize)).append("\n");
    sb.append("    tickSize: ").append(toIndentedString(tickSize)).append("\n");
    sb.append("    notionalMultiplier: ").append(toIndentedString(notionalMultiplier)).append("\n");
    sb.append("    mainFraction: ").append(toIndentedString(mainFraction)).append("\n");
    sb.append("    subFraction: ").append(toIndentedString(subFraction)).append("\n");
    sb.append("    displayFactor: ").append(toIndentedString(displayFactor)).append("\n");
    sb.append("    lastTradeDate: ").append(toIndentedString(lastTradeDate)).append("\n");
    sb.append("    expirationDate: ").append(toIndentedString(expirationDate)).append("\n");
    sb.append("    closingOnlyDate: ").append(toIndentedString(closingOnlyDate)).append("\n");
    sb.append("    active: ").append(toIndentedString(active)).append("\n");
    sb.append("    activeMonth: ").append(toIndentedString(activeMonth)).append("\n");
    sb.append("    nextActiveMonth: ").append(toIndentedString(nextActiveMonth)).append("\n");
    sb.append("    isClosingOnly: ").append(toIndentedString(isClosingOnly)).append("\n");
    sb.append("    firstNoticeDate: ").append(toIndentedString(firstNoticeDate)).append("\n");
    sb.append("    stopsTradingAt: ").append(toIndentedString(stopsTradingAt)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
    sb.append("    productGroup: ").append(toIndentedString(productGroup)).append("\n");
    sb.append("    exchange: ").append(toIndentedString(exchange)).append("\n");
    sb.append("    rollTargetSymbol: ").append(toIndentedString(rollTargetSymbol)).append("\n");
    sb.append("    streamerExchangeCode: ").append(toIndentedString(streamerExchangeCode)).append("\n");
    sb.append("    streamerSymbol: ").append(toIndentedString(streamerSymbol)).append("\n");
    sb.append("    backMonthFirstCalendarSymbol: ").append(toIndentedString(backMonthFirstCalendarSymbol)).append("\n");
    sb.append("    isTradeable: ").append(toIndentedString(isTradeable)).append("\n");
    sb.append("    trueUnderlyingSymbol: ").append(toIndentedString(trueUnderlyingSymbol)).append("\n");
    sb.append("    exchangeData: ").append(toIndentedString(exchangeData)).append("\n");
    sb.append("    futureEtfEquivalent: ").append(toIndentedString(futureEtfEquivalent)).append("\n");
    sb.append("    futureProduct: ").append(toIndentedString(futureProduct)).append("\n");
    sb.append("    tickSizes: ").append(toIndentedString(tickSizes)).append("\n");
    sb.append("    optionTickSizes: ").append(toIndentedString(optionTickSizes)).append("\n");
    sb.append("    spreadTickSizes: ").append(toIndentedString(spreadTickSizes)).append("\n");
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

