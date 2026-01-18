package com.stockmarketpotato.feeds.dxfeed;

import com.stockmarketpotato.integration.tastytrade.model.accounts.AccountPosition;
import com.stockmarketpotato.integration.tastytrade.model.instruments.AbstractInstrument;

public class SimpleInstrument {
	private String streamingSymbol;
	private String symbol;
	
	public SimpleInstrument() {
		super();
		this.streamingSymbol = "";
		this.symbol = "";
	}
	
	public SimpleInstrument(String symbol, String streamingSymbol) {
		super();
		this.symbol = symbol;
		this.streamingSymbol = streamingSymbol;
	}
	
	public SimpleInstrument(AbstractInstrument instrument) {
		super();
		this.streamingSymbol = instrument.getStreamerSymbol();
		this.symbol = instrument.getSymbol();
	}

	public SimpleInstrument(AccountPosition position) {
		super();
		this.streamingSymbol = position.streamerSymbol;
		this.symbol = position.symbol;
	}

	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getStreamingSymbol() {
		return streamingSymbol;
	}
	public void setStreamingSymbol(String streamingSymbol) {
		this.streamingSymbol = streamingSymbol;
	}
}
