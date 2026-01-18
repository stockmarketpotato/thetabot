package com.stockmarketpotato.broker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import com.stockmarketpotato.integration.tastytrade.AccountsAndCustomers;
import com.stockmarketpotato.integration.tastytrade.SessionsAndUsers;
import com.stockmarketpotato.integration.tastytrade.TastytradeApi.BASE;
import com.stockmarketpotato.integration.tastytrade.model.QuoteStreamerResponse;
import com.stockmarketpotato.integration.tastytrade.model.sessions.SessionResponse;

@Component
public class SessionManager {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private QuoteStreamerResponse quoteToken = null;
	private SessionResponse session = null;

	@Autowired
	private TastytradeSettingsRepository tastytradeSettings;
	
	public String getApiQuoteToken() {
		getSessionToken();
		requestQuoteStreamerToken();
		if (quoteToken != null)
			return quoteToken.token;
		return null;
	}

	public String getCertSessionToken() {
		if (TastytradeSettingsRepository.certIsInvalid(tastytradeSettings)) {
			logger.warn("No valid Cert TastytradeSettings found.");
			return null;
		}
		final TastytradeSettings t = tastytradeSettings.findAll().get(0);
		SessionsAndUsers s = new SessionsAndUsers(BASE.CERTIFICATION);
		session = s.login(t.getApiLogin(), t.getApiPassword());
		return session.session_token;
	}
	
	public String getDxLinkUrl() {
		getSessionToken();
		requestQuoteStreamerToken();
		if (quoteToken != null)
			return quoteToken.dxlink_url;
		return null;
	}

	/**
	 * This method retrieves a new session and a new remember token from endpoint
	 * /sessions.
	 * 
	 * @return String Returns the session token.
	 */
	public final String getSessionToken() {
		if (TastytradeSettingsRepository.isInvalid(tastytradeSettings)) {
			logger.warn("No valid TastytradeSettings found.");
			return null;
		}
		final TastytradeSettings t = tastytradeSettings.findAll().get(0);
		SessionsAndUsers s = new SessionsAndUsers();
		session = s.login(t.getApiLogin(), t.getApiPassword());
		return session.session_token;
	}

	@PostConstruct
	private void postConstruct() {
		getSessionToken();
		requestQuoteStreamerToken();
	}

	public void reconnect() {
		postConstruct();		
	}

	/**
	 * requests quoteApiToken and retrieves dxLinkUrl
	 */
	private void requestQuoteStreamerToken() {
		if (session == null)
			return;
		AccountsAndCustomers a = new AccountsAndCustomers();
		quoteToken = a.quoteStreamerToken(session.session_token);
	}
}
