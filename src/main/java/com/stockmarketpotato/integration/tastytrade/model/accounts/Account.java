package com.stockmarketpotato.integration.tastytrade.model.accounts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public final class Account {
    @JsonProperty("account-number")
    public String account_number;
    @JsonProperty("opened-at")
    public String opened_at;
    @JsonProperty("nickname")
    public String nickname;
    @JsonProperty("account-type-name")
    public String account_type_name;
    @JsonProperty("is-closed")
    public boolean is_closed;
    @JsonProperty("day-trader-status")
    public String day_trader_status;
    @JsonProperty("is-firm-error")
    public boolean is_firm_error;
    @JsonProperty("is-firm-proprietary")
    public boolean is_firm_proprietary;
    @JsonProperty("is-futures-approved")
    public boolean is_futures_approved;
    @JsonProperty("is-test-drive")
    public boolean is_test_drive;
    @JsonProperty("margin-or-cash")
    public String margin_or_cash;
    @JsonProperty("is-foreign")
    public boolean is_foreign;
    @JsonProperty("created-at")
    public String created_at;
    @JsonProperty("external-id")
    public String external_id;
    @JsonProperty("closed-at")
    public String closed_at;
    @JsonProperty("funding-date")
    public String funding_date;
    @JsonProperty("investment-objective")
    public String investment_objective;
    @JsonProperty("liquidity-needs")
    public String liquidity_needs;
    @JsonProperty("risk-tolerance")
    public String risk_tolerance;
    @JsonProperty("investment-time-horizon")
    public String investment_time_horizon;
    @JsonProperty("futures-account-purpose")
    public String futures_account_purpose;
    @JsonProperty("external-fdid")
    public String external_fdid;
    @JsonProperty("suitable-options-level")
    public String suitable_options_level;
    @JsonProperty("submitting-user-id")
    public String submitting_user_id;
}