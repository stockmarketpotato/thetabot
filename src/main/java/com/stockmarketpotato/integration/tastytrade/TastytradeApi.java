package com.stockmarketpotato.integration.tastytrade;

public abstract class TastytradeApi {
	public enum BASE {
		PRODUCTION("https://api.tastyworks.com"),
		CERTIFICATION("https://api.cert.tastyworks.com");

		private final String url;

		private BASE(String url) {
			this.url = url;
		}

		public String getURL() {
			return url;
		}
	}

	protected String baseUrl;

	protected TastytradeApi() {
		super();
		this.baseUrl = BASE.PRODUCTION.getURL();
	}

	public TastytradeApi(BASE base) {
		this();
		this.baseUrl = base.getURL();
	}
}
