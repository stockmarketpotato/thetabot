package com.stockmarketpotato.integration.tastytrade;

public abstract class TastytradeStreamer {
	public enum STREAMER {
		PRODUCTION("wss://streamer.tastyworks.com"),
		CERTIFICATION("wss://streamer.cert.tastyworks.com");

		private final String url;

		private STREAMER(String url) {
			this.url = url;
		}

		public String getURL() {
			return url;
		}
	}

	protected String streamerUrl;

	protected TastytradeStreamer() {
		super();
		this.streamerUrl = STREAMER.PRODUCTION.getURL();
	}

	public TastytradeStreamer(STREAMER streamer) {
		this();
		this.streamerUrl = streamer.getURL();
	}
}
