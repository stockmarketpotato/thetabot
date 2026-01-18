package com.stockmarketpotato.integration.tastytrade.model.accounts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Data model representing the balances of a Tastytrade account.
 * Contains various margin requirements, cash balances, and equity values.
 * Typically deserialized from the API response.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountBalances {
    @JsonProperty("futures-margin-requirement")
    public Double futures_margin_requirement = 0.0;
    @JsonProperty("unsettled-cryptocurrency-fiat-amount")
    public Double unsettled_cryptocurrency_fiat_amount = 0.0;
    @JsonProperty("cash-balance")
    public Double cash_balance = 0.0;
    @JsonProperty("equity-offering-margin-requirement")
    public Double equity_offering_margin_requirement = 0.0;
    @JsonProperty("short-futures-value")
    public Double short_futures_value = 0.0;
    @JsonProperty("total-settle-balance")
    public Double total_settle_balance = 0.0;
    @JsonProperty("long-futures-value")
    public Double long_futures_value = 0.0;
    @JsonProperty("long-margineable-value")
    public Double long_margineable_value = 0.0;
    @JsonProperty("margin-equity")
    public Double margin_equity = 0.0;
    @JsonProperty("short-margineable-value")
    public Double short_margineable_value = 0.0;
    @JsonProperty("updated-at")
    public String updated_at = "";
    @JsonProperty("buying-power-adjustment")
    public Double buying_power_adjustment = 0.0;
    @JsonProperty("time-of-day")
    public String time_of_day = "";
    @JsonProperty("short-cryptocurrency-value")
    public Double short_cryptocurrency_value = 0.0;
    @JsonProperty("net-liquidating-value")
    public Double net_liquidating_value = 0.0;
    @JsonProperty("long-cryptocurrency-value")
    public Double long_cryptocurrency_value = 0.0;
    @JsonProperty("maintenance-requirement")
    public Double maintenance_requirement = 0.0;
    @JsonProperty("pending-cash")
    public Double pending_cash = 0.0;
    @JsonProperty("effective-cryptocurrency-buying-power")
    public Double effective_cryptocurrency_buying_power = 0.0;
    @JsonProperty("account-number")
    public String account_number = "";
    @JsonProperty("used-derivative-buying-power")
    public Double used_derivative_buying_power = 0.0;
    @JsonProperty("futures-overnight-margin-requirement")
    public Double futures_overnight_margin_requirement = 0.0;
    @JsonProperty("bond-margin-requirement")
    public Double bond_margin_requirement = 0.0;
    @JsonProperty("special-memorandum-account-value")
    public Double special_memorandum_account_value = 0.0;
    @JsonProperty("day-trade-excess")
    public Double day_trade_excess = 0.0;
    @JsonProperty("maintenance-excess")
    public Double maintenance_excess = 0.0;
    @JsonProperty("closed-loop-available-balance")
    public Double closed_loop_available_balance = 0.0;
    @JsonProperty("day-trading-buying-power")
    public Double day_trading_buying_power = 0.0;
    @JsonProperty("apex-starting-day-margin-equity")
    public Double apex_starting_day_margin_equity = 0.0;
    @JsonProperty("pending-margin-interest")
    public Double pending_margin_interest = 0.0;
    @JsonProperty("reg-t-call-value")
    public Double reg_t_call_value = 0.0;
    @JsonProperty("pending-cash-effect")
    public String pending_cash_effect = "";
    @JsonProperty("reg-t-margin-requirement")
    public Double reg_t_margin_requirement = 0.0;
    @JsonProperty("special-memorandum-account-apex-adjustment")
    public Double special_memorandum_account_apex_adjustment = 0.0;
    @JsonProperty("futures-intraday-margin-requirement")
    public Double futures_intraday_margin_requirement = 0.0;
    @JsonProperty("snapshot-date")
    public String snapshot_date = "";
    @JsonProperty("long-bond-value")
    public Double long_bond_value = 0.0;
    @JsonProperty("buying-power-adjustment-effect")
    public String buying_power_adjustment_effect = "";
    @JsonProperty("long-futures-derivative-value")
    public Double long_futures_derivative_value = 0.0;
    @JsonProperty("day-equity-call-value")
    public Double day_equity_call_value = 0.0;
    @JsonProperty("short-futures-derivative-value")
    public Double short_futures_derivative_value = 0.0;
    @JsonProperty("long-equity-value")
    public Double long_equity_value = 0.0;
    @JsonProperty("short-equity-value")
    public Double short_equity_value = 0.0;
    @JsonProperty("derivative-buying-power")
    public Double derivative_buying_power = 0.0;
    @JsonProperty("unsettled-cryptocurrency-fiat-effect")
    public String unsettled_cryptocurrency_fiat_effect = "";
    @JsonProperty("maintenance-call-value")
    public Double maintenance_call_value = 0.0;
    @JsonProperty("long-derivative-value")
    public Double long_derivative_value = 0.0;
    @JsonProperty("equity-buying-power")
    public Double equity_buying_power = 0.0;
    @JsonProperty("short-derivative-value")
    public Double short_derivative_value = 0.0;
    @JsonProperty("cash-available-to-withdraw")
    public Double cash_available_to_withdraw = 0.0;
    @JsonProperty("available-trading-funds")
    public Double available_trading_funds = 0.0;
    @JsonProperty("cryptocurrency-margin-requirement")
    public Double cryptocurrency_margin_requirement = 0.0;
    @JsonProperty("day-trading-call-value")
    public Double day_trading_call_value = 0.0;
}
