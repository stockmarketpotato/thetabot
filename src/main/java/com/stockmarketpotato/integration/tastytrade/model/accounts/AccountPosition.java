package com.stockmarketpotato.integration.tastytrade.model.accounts;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.stockmarketpotato.broker.OccOsiSymbology;
import com.stockmarketpotato.feeds.AggregatedQuote;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountPosition {
	@JsonProperty("account-number")
	public String account_number;
	@JsonProperty("symbol")
	public String symbol;
	@JsonProperty("instrument-type")
//    public InstrumentType instrument_type;
	public String instrument_type;
	@JsonProperty("underlying-symbol")
	public String underlying_symbol;
	@JsonProperty("quantity")
	public Double quantity;
	@JsonProperty("quantity-direction")
	public String quantity_direction;
	@JsonProperty("close-price")
	public Double close_price;
	@JsonProperty("average-open-price")
	public Double average_open_price;
	@JsonProperty("multiplier")
	public Integer multiplier;
	@JsonProperty("cost-effect")
	public String cost_effect;
	@JsonProperty("is-suppressed")
	public Boolean is_suppressed;
	@JsonProperty("is-frozen")
	public Boolean is_frozen;
	@JsonProperty("realized-day-gain")
	public Double realized_day_gain;
	@JsonProperty("realized-today")
	public Double realized_today;
	@JsonProperty("created-at")
	public String created_at;
	@JsonProperty("updated-at")
	public String updated_at;
	@JsonProperty("mark")
	public Double mark;
	@JsonProperty("mark-price")
	public Double mark_price;
	@JsonProperty("restricted-quantity")
	public Double restricted_quantity;
	@JsonProperty("expires-at")
	public String expires_at;
	@JsonProperty("fixing-price")
	public Double fixing_price;
	@JsonProperty("deliverable-type")
	public String deliverable_type;
	@JsonProperty("average-yearly-market-close-price")
	public Double average_yearly_market_close_price;
	@JsonProperty("average-daily-market-close-price")
	public Double average_daily_market_close_price;
	@JsonProperty("realized-day-gain-effect")
//     public PriceEffect realized_day_gain_effect;
	public String realized_day_gain_effect;
	@JsonProperty("realized-day-gain-date")
	public String realized_day_gain_date;
	@JsonProperty("realized-today-effect")
//    public PriceEffect realized_today_effect;
	public String realized_today_effect;
	@JsonProperty("realized-today-date")
	public String realized_today_date;

	public String streamerSymbol;
	public BigDecimal pnl;
	public BigDecimal lastQuoteAsk;
	public BigDecimal lastQuoteBid;
	private String name = null;

	public void setPnl(AggregatedQuote quote) {
		this.lastQuoteAsk = quote.getAskPrice();
		this.lastQuoteBid = quote.getBidPrice();
		BigDecimal bd_average_open_price = BigDecimal.valueOf(this.average_open_price);
		if (this.quantity_direction.equals("Long"))
			this.pnl = this.lastQuoteBid.subtract(bd_average_open_price).multiply(BigDecimal.valueOf(100))
					.divide(bd_average_open_price, 2, RoundingMode.HALF_UP);
		else
			this.pnl = bd_average_open_price.subtract(this.lastQuoteAsk).multiply(BigDecimal.valueOf(100))
					.divide(bd_average_open_price, 2, RoundingMode.HALF_UP);
	}

	public String getDteAsString() {
		if (!this.instrument_type.contains("Option"))
			return "";
		TemporalAccessor ta = DateTimeFormatter.ISO_INSTANT.parse(this.expires_at);
		Instant i = Instant.from(ta);
		Duration timeElapsed = Duration.between(Instant.now(), i);
		return new DecimalFormat("#").format(timeElapsed.toDays());
	}
	
	public Long getDte() {
		if (!this.instrument_type.contains("Option"))
			return null;
		TemporalAccessor ta = DateTimeFormatter.ISO_INSTANT.parse(this.expires_at);
		Instant i = Instant.from(ta);
		Duration timeElapsed = Duration.between(Instant.now(), i);
		return timeElapsed.toDays();
	}

	private String toDateString(final String dateString) {
		if (dateString == null || dateString.length() == 0)
			return "";
		TemporalAccessor ta = DateTimeFormatter.ISO_INSTANT.parse(dateString);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd. MMM yyyy")
				.withZone(ZoneId.of("EST", ZoneId.SHORT_IDS));
		return formatter.format(ta);
	}
	
	public String getExpiresAt() {
		return toDateString(this.expires_at);
	}
	
	public String getCreatedAt() {
		return toDateString(this.created_at);
	}
	
	public String getName() {
		if (name == null) {
			if (this.instrument_type.equals("Equity Option"))
				name = OccOsiSymbology.fromSymbol(this.symbol).getName();
			else if (this.instrument_type.equals("Future Option"))
				name = OccOsiSymbology.fromFuturesOptionSymbol(this.symbol).getName();
			else
				name = symbol;
		}
		return name;
	}
}
