package com.stockmarketpotato.feeds;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class AggregatedQuote {
	@Id
	private String symbol;
	
	@Column(unique = true)
	private String streamerSymbol;
	
	@Column(scale = 15, precision = 30)
	private BigDecimal bidPrice;

	@Column(scale = 15, precision = 30)
	private BigDecimal askPrice;
	
	// option delta, greek
	@Column(scale = 15, precision = 30)
	private BigDecimal delta;

	// timestamp of bid price
	private Date bidTime;
	
	// timestamp of ask price
	private Date askTime;
	
	// time stamp of delta
	private Date deltaTime;
	
	public AggregatedQuote() {
		this("", "");
	}
	
	public AggregatedQuote(final String symbol, final BigDecimal bidPrice, BigDecimal askPrice) {
		this(symbol, bidPrice, askPrice, new BigDecimal("0.00"));
	}

	public AggregatedQuote(final String symbol, final BigDecimal bidPrice, BigDecimal askPrice, BigDecimal delta) {
		super();
		this.symbol = symbol;
		this.bidPrice = bidPrice;
		this.askPrice = askPrice;
		this.askTime = new Date();
		this.bidTime = new Date();
		this.delta = delta;
		this.deltaTime = new Date();
	}

	public AggregatedQuote(String symbol, String streamerSymbol) {
		super();
		this.symbol = symbol;
		this.streamerSymbol = streamerSymbol;
		this.bidPrice = new BigDecimal("0.00");
		this.askPrice = new BigDecimal("0.00");
		this.askTime = new Date();
		this.bidTime = new Date();
		this.delta = new BigDecimal("0.00");
		this.deltaTime = new Date();
	}
	
	public AggregatedQuote(String symbol, String streamerSymbol, BigDecimal bidPrice, BigDecimal askPrice, BigDecimal delta,
			Date bidTime, Date askTime, Date deltaTime) {
		super();
		this.symbol = symbol;
		this.streamerSymbol = streamerSymbol;
		this.bidPrice = bidPrice;
		this.askPrice = askPrice;
		this.delta = delta;
		this.bidTime = bidTime;
		this.askTime = askTime;
		this.deltaTime = deltaTime;
	}

	public BigDecimal getAskPrice() {
		return askPrice;
	}

	public String getAskPriceAsString() {
		if (askPrice == null) return "";
		return NumberFormat.getCurrencyInstance(Locale.US).format(askPrice);
	}

	public Date getAskTime() {
		return askTime;
	}

	public BigDecimal getBidPrice() {
		return bidPrice;
	}

	public String getBidPriceAsString() {
		if (bidPrice == null) return "";
		return NumberFormat.getCurrencyInstance(Locale.US).format(bidPrice);
	}

	public Date getBidTime() {
		return bidTime;
	}

	public BigDecimal getDelta() {
		return delta;
	}

	public String getDeltaAsString() {
		if (delta == null) return "";
		NumberFormat formatter = NumberFormat.getInstance(Locale.US);
		formatter.setMinimumFractionDigits(3);
		return formatter.format(delta);
	}

	public Date getDeltaTime() {
		return deltaTime;
	}

	public String getStreamerSymbol() {
		return streamerSymbol;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setAskPrice(BigDecimal askPrice) {
		this.askPrice = askPrice;
	}

	public void setAskTime(Date askTime) {
		this.askTime = askTime;
	}

	public void setBidPrice(BigDecimal bidPrice) {
		this.bidPrice = bidPrice;
	}

	public void setBidTime(Date bidTime) {
		this.bidTime = bidTime;
	}

	public void setDelta(BigDecimal delta) {
		this.delta = delta;
	}

	public void setDeltaTime(Date deltaTime) {
		this.deltaTime = deltaTime;
	}

	public void setStreamerSymbol(String streamerSymbol) {
		this.streamerSymbol = streamerSymbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Quote {\n");
		sb.append("    symbol: ").append(toIndentedString(symbol)).append("\n");
		sb.append("    streamerSymbol: ").append(toIndentedString(streamerSymbol)).append("\n");
		sb.append("    bidPrice: ").append(toIndentedString(bidPrice)).append("\n");
		sb.append("    askPrice: ").append(toIndentedString(askPrice)).append("\n");
		sb.append("    bidTime: ").append(toIndentedString(bidTime)).append("\n");
		sb.append("    askTime: ").append(toIndentedString(askTime)).append("\n");
		sb.append("    delta: ").append(toIndentedString(delta)).append("\n");
		sb.append("    deltaTime: ").append(toIndentedString(deltaTime)).append("\n");
		sb.append("}");
		return sb.toString();
	}
}
