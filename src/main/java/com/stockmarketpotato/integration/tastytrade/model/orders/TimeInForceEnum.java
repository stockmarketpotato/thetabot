package com.stockmarketpotato.integration.tastytrade.model.orders;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The length in time before the order expires. i.e. `Day`, `GTC`, `GTD`, `Ext`, `GTC Ext` or `IOC`
 */
public enum TimeInForceEnum {
  DAY("Day"),
  
  GTC("GTC"),
  
  GTD("GTD"),
  
  EXT("Ext"),
  
  GTC_EXT("GTC Ext"),
  
  IOC("IOC");

  private String value;

  TimeInForceEnum(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static TimeInForceEnum fromValue(String value) {
    for (TimeInForceEnum b : TimeInForceEnum.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}
