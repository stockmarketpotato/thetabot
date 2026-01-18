package com.stockmarketpotato.integration.tastytrade.model.instruments;

import java.util.List;
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
  FutureFutureProduct.JSON_PROPERTY_ROOT_SYMBOL,
  FutureFutureProduct.JSON_PROPERTY_CODE,
  FutureFutureProduct.JSON_PROPERTY_DESCRIPTION,
  FutureFutureProduct.JSON_PROPERTY_EXCHANGE,
  FutureFutureProduct.JSON_PROPERTY_PRODUCT_TYPE,
  FutureFutureProduct.JSON_PROPERTY_LISTED_MONTHS,
  FutureFutureProduct.JSON_PROPERTY_ACTIVE_MONTHS,
  FutureFutureProduct.JSON_PROPERTY_NOTIONAL_MULTIPLIER,
  FutureFutureProduct.JSON_PROPERTY_TICK_SIZE,
  FutureFutureProduct.JSON_PROPERTY_DISPLAY_FACTOR,
  FutureFutureProduct.JSON_PROPERTY_BASE_TICK,
  FutureFutureProduct.JSON_PROPERTY_SUB_TICK,
  FutureFutureProduct.JSON_PROPERTY_STREAMER_EXCHANGE_CODE,
  FutureFutureProduct.JSON_PROPERTY_SMALL_NOTIONAL,
  FutureFutureProduct.JSON_PROPERTY_BACK_MONTH_FIRST_CALENDAR_SYMBOL,
  FutureFutureProduct.JSON_PROPERTY_FIRST_NOTICE,
  FutureFutureProduct.JSON_PROPERTY_CASH_SETTLED,
  FutureFutureProduct.JSON_PROPERTY_CONTRACT_LIMIT,
  FutureFutureProduct.JSON_PROPERTY_SECURITY_GROUP,
  FutureFutureProduct.JSON_PROPERTY_PRODUCT_SUBTYPE,
  FutureFutureProduct.JSON_PROPERTY_TRUE_UNDERLYING_CODE,
  FutureFutureProduct.JSON_PROPERTY_MARKET_SECTOR,
  FutureFutureProduct.JSON_PROPERTY_PRICE_FORMAT
})
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("Future_future_product")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-02-14T09:40:37.746219+01:00[Europe/Berlin]")
public class FutureFutureProduct {
  public static final String JSON_PROPERTY_ROOT_SYMBOL = "root-symbol";
  private String rootSymbol;

  public static final String JSON_PROPERTY_CODE = "code";
  private String code;

  public static final String JSON_PROPERTY_DESCRIPTION = "description";
  private String description;

  public static final String JSON_PROPERTY_EXCHANGE = "exchange";
  private String exchange;

  public static final String JSON_PROPERTY_PRODUCT_TYPE = "product-type";
  private String productType;

  public static final String JSON_PROPERTY_LISTED_MONTHS = "listed-months";
  private List<String> listedMonths;

  public static final String JSON_PROPERTY_ACTIVE_MONTHS = "active-months";
  private List<String> activeMonths;

  public static final String JSON_PROPERTY_NOTIONAL_MULTIPLIER = "notional-multiplier";
  private Double notionalMultiplier;

  public static final String JSON_PROPERTY_TICK_SIZE = "tick-size";
  private Double tickSize;

  public static final String JSON_PROPERTY_DISPLAY_FACTOR = "display-factor";
  private Double displayFactor;

  public static final String JSON_PROPERTY_BASE_TICK = "base-tick";
  private Integer baseTick;

  public static final String JSON_PROPERTY_SUB_TICK = "sub-tick";
  private Integer subTick;

  public static final String JSON_PROPERTY_STREAMER_EXCHANGE_CODE = "streamer-exchange-code";
  private String streamerExchangeCode;

  public static final String JSON_PROPERTY_SMALL_NOTIONAL = "small-notional";
  private Boolean smallNotional;

  public static final String JSON_PROPERTY_BACK_MONTH_FIRST_CALENDAR_SYMBOL = "back-month-first-calendar-symbol";
  private Boolean backMonthFirstCalendarSymbol;

  public static final String JSON_PROPERTY_FIRST_NOTICE = "first-notice";
  private Boolean firstNotice;

  public static final String JSON_PROPERTY_CASH_SETTLED = "cash-settled";
  private Boolean cashSettled;

  public static final String JSON_PROPERTY_CONTRACT_LIMIT = "contract-limit";
  private Integer contractLimit;

  public static final String JSON_PROPERTY_SECURITY_GROUP = "security-group";
  private String securityGroup;

  public static final String JSON_PROPERTY_PRODUCT_SUBTYPE = "product-subtype";
  private String productSubtype;

  public static final String JSON_PROPERTY_TRUE_UNDERLYING_CODE = "true-underlying-code";
  private String trueUnderlyingCode;

  public static final String JSON_PROPERTY_MARKET_SECTOR = "market-sector";
  private String marketSector;

  public static final String JSON_PROPERTY_PRICE_FORMAT = "price-format";
  private String priceFormat;

  public FutureFutureProduct() {
  }

  public FutureFutureProduct rootSymbol(String rootSymbol) {
    
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


  public FutureFutureProduct code(String code) {
    
    this.code = code;
    return this;
  }

   /**
   * 
   * @return code
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CODE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getCode() {
    return code;
  }


  @JsonProperty(JSON_PROPERTY_CODE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setCode(String code) {
    this.code = code;
  }


  public FutureFutureProduct description(String description) {
    
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


  public FutureFutureProduct exchange(String exchange) {
    
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


  public FutureFutureProduct productType(String productType) {
    
    this.productType = productType;
    return this;
  }

   /**
   * 
   * @return productType
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_PRODUCT_TYPE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getProductType() {
    return productType;
  }


  @JsonProperty(JSON_PROPERTY_PRODUCT_TYPE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setProductType(String productType) {
    this.productType = productType;
  }


  public FutureFutureProduct listedMonths(List<String> listedMonths) {
    
    this.listedMonths = listedMonths;
    return this;
  }

   /**
   * 
   * @return listedMonths
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_LISTED_MONTHS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public List<String> getListedMonths() {
    return listedMonths;
  }


  @JsonProperty(JSON_PROPERTY_LISTED_MONTHS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setListedMonths(List<String> listedMonths) {
    this.listedMonths = listedMonths;
  }


  public FutureFutureProduct activeMonths(List<String> activeMonths) {
    
    this.activeMonths = activeMonths;
    return this;
  }

   /**
   * 
   * @return activeMonths
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ACTIVE_MONTHS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public List<String> getActiveMonths() {
    return activeMonths;
  }


  @JsonProperty(JSON_PROPERTY_ACTIVE_MONTHS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setActiveMonths(List<String> activeMonths) {
    this.activeMonths = activeMonths;
  }


  public FutureFutureProduct notionalMultiplier(Double notionalMultiplier) {
    
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


  public FutureFutureProduct tickSize(Double tickSize) {
    
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


  public FutureFutureProduct displayFactor(Double displayFactor) {
    
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


  public FutureFutureProduct baseTick(Integer baseTick) {
    
    this.baseTick = baseTick;
    return this;
  }

   /**
   * 
   * @return baseTick
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_BASE_TICK)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getBaseTick() {
    return baseTick;
  }


  @JsonProperty(JSON_PROPERTY_BASE_TICK)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setBaseTick(Integer baseTick) {
    this.baseTick = baseTick;
  }


  public FutureFutureProduct subTick(Integer subTick) {
    
    this.subTick = subTick;
    return this;
  }

   /**
   * 
   * @return subTick
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SUB_TICK)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getSubTick() {
    return subTick;
  }


  @JsonProperty(JSON_PROPERTY_SUB_TICK)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setSubTick(Integer subTick) {
    this.subTick = subTick;
  }


  public FutureFutureProduct streamerExchangeCode(String streamerExchangeCode) {
    
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


  public FutureFutureProduct smallNotional(Boolean smallNotional) {
    
    this.smallNotional = smallNotional;
    return this;
  }

   /**
   * 
   * @return smallNotional
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SMALL_NOTIONAL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getSmallNotional() {
    return smallNotional;
  }


  @JsonProperty(JSON_PROPERTY_SMALL_NOTIONAL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setSmallNotional(Boolean smallNotional) {
    this.smallNotional = smallNotional;
  }


  public FutureFutureProduct backMonthFirstCalendarSymbol(Boolean backMonthFirstCalendarSymbol) {
    
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


  public FutureFutureProduct firstNotice(Boolean firstNotice) {
    
    this.firstNotice = firstNotice;
    return this;
  }

   /**
   * 
   * @return firstNotice
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_FIRST_NOTICE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getFirstNotice() {
    return firstNotice;
  }


  @JsonProperty(JSON_PROPERTY_FIRST_NOTICE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setFirstNotice(Boolean firstNotice) {
    this.firstNotice = firstNotice;
  }


  public FutureFutureProduct cashSettled(Boolean cashSettled) {
    
    this.cashSettled = cashSettled;
    return this;
  }

   /**
   * 
   * @return cashSettled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CASH_SETTLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getCashSettled() {
    return cashSettled;
  }


  @JsonProperty(JSON_PROPERTY_CASH_SETTLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setCashSettled(Boolean cashSettled) {
    this.cashSettled = cashSettled;
  }


  public FutureFutureProduct contractLimit(Integer contractLimit) {
    
    this.contractLimit = contractLimit;
    return this;
  }

   /**
   * 
   * @return contractLimit
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CONTRACT_LIMIT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getContractLimit() {
    return contractLimit;
  }


  @JsonProperty(JSON_PROPERTY_CONTRACT_LIMIT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setContractLimit(Integer contractLimit) {
    this.contractLimit = contractLimit;
  }


  public FutureFutureProduct securityGroup(String securityGroup) {
    
    this.securityGroup = securityGroup;
    return this;
  }

   /**
   * 
   * @return securityGroup
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SECURITY_GROUP)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getSecurityGroup() {
    return securityGroup;
  }


  @JsonProperty(JSON_PROPERTY_SECURITY_GROUP)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setSecurityGroup(String securityGroup) {
    this.securityGroup = securityGroup;
  }


  public FutureFutureProduct productSubtype(String productSubtype) {
    
    this.productSubtype = productSubtype;
    return this;
  }

   /**
   * 
   * @return productSubtype
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_PRODUCT_SUBTYPE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getProductSubtype() {
    return productSubtype;
  }


  @JsonProperty(JSON_PROPERTY_PRODUCT_SUBTYPE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setProductSubtype(String productSubtype) {
    this.productSubtype = productSubtype;
  }


  public FutureFutureProduct trueUnderlyingCode(String trueUnderlyingCode) {
    
    this.trueUnderlyingCode = trueUnderlyingCode;
    return this;
  }

   /**
   * 
   * @return trueUnderlyingCode
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_TRUE_UNDERLYING_CODE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getTrueUnderlyingCode() {
    return trueUnderlyingCode;
  }


  @JsonProperty(JSON_PROPERTY_TRUE_UNDERLYING_CODE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setTrueUnderlyingCode(String trueUnderlyingCode) {
    this.trueUnderlyingCode = trueUnderlyingCode;
  }


  public FutureFutureProduct marketSector(String marketSector) {
    
    this.marketSector = marketSector;
    return this;
  }

   /**
   * 
   * @return marketSector
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_MARKET_SECTOR)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getMarketSector() {
    return marketSector;
  }


  @JsonProperty(JSON_PROPERTY_MARKET_SECTOR)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setMarketSector(String marketSector) {
    this.marketSector = marketSector;
  }


  public FutureFutureProduct priceFormat(String priceFormat) {
    
    this.priceFormat = priceFormat;
    return this;
  }

   /**
   * 
   * @return priceFormat
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_PRICE_FORMAT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getPriceFormat() {
    return priceFormat;
  }


  @JsonProperty(JSON_PROPERTY_PRICE_FORMAT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setPriceFormat(String priceFormat) {
    this.priceFormat = priceFormat;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FutureFutureProduct futureFutureProduct = (FutureFutureProduct) o;
    return Objects.equals(this.rootSymbol, futureFutureProduct.rootSymbol) &&
        Objects.equals(this.code, futureFutureProduct.code) &&
        Objects.equals(this.description, futureFutureProduct.description) &&
        Objects.equals(this.exchange, futureFutureProduct.exchange) &&
        Objects.equals(this.productType, futureFutureProduct.productType) &&
        Objects.equals(this.listedMonths, futureFutureProduct.listedMonths) &&
        Objects.equals(this.activeMonths, futureFutureProduct.activeMonths) &&
        Objects.equals(this.notionalMultiplier, futureFutureProduct.notionalMultiplier) &&
        Objects.equals(this.tickSize, futureFutureProduct.tickSize) &&
        Objects.equals(this.displayFactor, futureFutureProduct.displayFactor) &&
        Objects.equals(this.baseTick, futureFutureProduct.baseTick) &&
        Objects.equals(this.subTick, futureFutureProduct.subTick) &&
        Objects.equals(this.streamerExchangeCode, futureFutureProduct.streamerExchangeCode) &&
        Objects.equals(this.smallNotional, futureFutureProduct.smallNotional) &&
        Objects.equals(this.backMonthFirstCalendarSymbol, futureFutureProduct.backMonthFirstCalendarSymbol) &&
        Objects.equals(this.firstNotice, futureFutureProduct.firstNotice) &&
        Objects.equals(this.cashSettled, futureFutureProduct.cashSettled) &&
        Objects.equals(this.contractLimit, futureFutureProduct.contractLimit) &&
        Objects.equals(this.securityGroup, futureFutureProduct.securityGroup) &&
        Objects.equals(this.productSubtype, futureFutureProduct.productSubtype) &&
        Objects.equals(this.trueUnderlyingCode, futureFutureProduct.trueUnderlyingCode) &&
        Objects.equals(this.marketSector, futureFutureProduct.marketSector) &&
        Objects.equals(this.priceFormat, futureFutureProduct.priceFormat);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rootSymbol, code, description, exchange, productType, listedMonths, activeMonths, notionalMultiplier, tickSize, displayFactor, baseTick, subTick, streamerExchangeCode, smallNotional, backMonthFirstCalendarSymbol, firstNotice, cashSettled, contractLimit, securityGroup, productSubtype, trueUnderlyingCode, marketSector, priceFormat);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FutureFutureProduct {\n");
    sb.append("    rootSymbol: ").append(toIndentedString(rootSymbol)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    exchange: ").append(toIndentedString(exchange)).append("\n");
    sb.append("    productType: ").append(toIndentedString(productType)).append("\n");
    sb.append("    listedMonths: ").append(toIndentedString(listedMonths)).append("\n");
    sb.append("    activeMonths: ").append(toIndentedString(activeMonths)).append("\n");
    sb.append("    notionalMultiplier: ").append(toIndentedString(notionalMultiplier)).append("\n");
    sb.append("    tickSize: ").append(toIndentedString(tickSize)).append("\n");
    sb.append("    displayFactor: ").append(toIndentedString(displayFactor)).append("\n");
    sb.append("    baseTick: ").append(toIndentedString(baseTick)).append("\n");
    sb.append("    subTick: ").append(toIndentedString(subTick)).append("\n");
    sb.append("    streamerExchangeCode: ").append(toIndentedString(streamerExchangeCode)).append("\n");
    sb.append("    smallNotional: ").append(toIndentedString(smallNotional)).append("\n");
    sb.append("    backMonthFirstCalendarSymbol: ").append(toIndentedString(backMonthFirstCalendarSymbol)).append("\n");
    sb.append("    firstNotice: ").append(toIndentedString(firstNotice)).append("\n");
    sb.append("    cashSettled: ").append(toIndentedString(cashSettled)).append("\n");
    sb.append("    contractLimit: ").append(toIndentedString(contractLimit)).append("\n");
    sb.append("    securityGroup: ").append(toIndentedString(securityGroup)).append("\n");
    sb.append("    productSubtype: ").append(toIndentedString(productSubtype)).append("\n");
    sb.append("    trueUnderlyingCode: ").append(toIndentedString(trueUnderlyingCode)).append("\n");
    sb.append("    marketSector: ").append(toIndentedString(marketSector)).append("\n");
    sb.append("    priceFormat: ").append(toIndentedString(priceFormat)).append("\n");
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

