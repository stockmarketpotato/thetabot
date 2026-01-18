package com.stockmarketpotato.integration.tastytrade.model.instruments;

/**
 * Abstract base class representing a financial instrument.
 * Provides a contract for retrieving the standard symbol and the symbol used for streaming data.
 */
public abstract class AbstractInstrument {
	/**
	 * Gets the standard symbol for the instrument.
	 * @return The symbol string.
	 */
	public abstract String getSymbol();
	/**
	 * Gets the symbol used for streaming market data (e.g., DxFeed).
	 * @return The streamer symbol string.
	 */
	public abstract String getStreamerSymbol();
}
