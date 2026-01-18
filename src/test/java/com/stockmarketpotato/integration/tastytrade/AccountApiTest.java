package com.stockmarketpotato.integration.tastytrade;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;

import com.stockmarketpotato.broker.SessionManager;
import com.stockmarketpotato.broker.TastytradeSettings;
import com.stockmarketpotato.broker.TastytradeSettingsRepository;
import com.stockmarketpotato.integration.tastytrade.TastytradeApi.BASE;
import com.stockmarketpotato.integration.tastytrade.model.accounts.Account;
import com.stockmarketpotato.integration.tastytrade.model.accounts.AccountBalances;
import com.stockmarketpotato.integration.tastytrade.model.accounts.AccountPosition;

@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan(basePackages = { "com.stockmarketpotato.broker", "com.stockmarketpotato.feeds.dxfeed" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class AccountApiTest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SessionManager tokenManager;
	
	@Autowired
	private TastytradeSettingsRepository tastytradeSettings;
	
	@Value("${tastytrade.api.prod.login}")
	private String prodApiLogin;
	@Value("${tastytrade.api.prod.password}")
	private String prodApiPassword;
	
	@Value("${tastytrade.api.cert.login}")
	private String certApiLogin;
	@Value("${tastytrade.api.cert.password}")
	private String certApiPassword;
	
	@BeforeEach
	void saveTastytradeSetting() {
		logger.info("Save TastytradeSettings");
		TastytradeSettings s = new TastytradeSettings();
		s.setApiLogin(prodApiLogin);
		s.setApiPassword(prodApiPassword);
		s.setCertApiLogin(certApiLogin);
		s.setCertApiPassword(certApiPassword);
		tastytradeSettings.save(s);
		logger.info("Check not null");
		assertThat(tastytradeSettings).isNotNull();
		assertThat(tokenManager).isNotNull();
		
		logger.info("Connect to Account Streamer in Certification Environment");
		assertThat(TastytradeSettingsRepository.certIsInvalid(tastytradeSettings)).isNotEqualTo(true);
	}
	
	@Test
	void listAccounts() {
		final String sessionToken = tokenManager.getCertSessionToken();
		assertThat(sessionToken).isNotBlank();
		AccountsAndCustomers accountsApi = new AccountsAndCustomers(BASE.CERTIFICATION);
		List<Account> accounts = accountsApi.getAccounts(sessionToken);
		assertThat(accounts).isNotEmpty();
		for (Account a : accounts)
			logger.info(a.account_number + " " + a.account_type_name + " " + a.margin_or_cash);
	}

	@Test
	void listPositionsAndBalances() {
		String format = "  | %-10s | %-10s | %-10s | %-10s | %-10s |";
		final String sessionToken = tokenManager.getCertSessionToken();
		assertThat(sessionToken).isNotBlank();
		AccountsAndCustomers accountsApi = new AccountsAndCustomers(BASE.CERTIFICATION);
		List<Account> accounts = accountsApi.getAccounts(sessionToken);
		assertThat(accounts).isNotEmpty();
		for (Account a : accounts) {
			logger.info("POSITIONS in " + a.account_number + " " + a.account_type_name + " " + a.margin_or_cash);
			BalancesAndPositions positionsApi = new BalancesAndPositions(BASE.CERTIFICATION);
			AccountBalances balances = positionsApi.getBalances(sessionToken, a.account_number);
			logger.info("Cash Balance: " + balances.cash_balance);
			List<AccountPosition> positions = positionsApi.getPositions(sessionToken, a.account_number);
			for (AccountPosition p : positions)
				logger.info(String.format(format, p.getName(), p.quantity, p.multiplier, p.instrument_type, p.average_open_price));
		}
	}
}
