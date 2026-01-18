package com.stockmarketpotato.integration.tastytrade;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.OffsetDateTime;
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
import com.stockmarketpotato.integration.tastytrade.model.transactions.Transaction;

@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan(basePackages = { "com.stockmarketpotato.broker", "com.stockmarketpotato.feeds.dxfeed" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class TransactionApiTest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SessionManager tokenManager;

	@Autowired
	private TastytradeSettingsRepository tastytradeSettings;

	@Value("${tastytrade.api.prod.login}")
	private String prodApiLogin;
	@Value("${tastytrade.api.prod.password}")
	private String prodApiPassword;

	@Value("${tastytrade.api.cert01.login}")
//  @Value("${tastytrade.api.cert02.login}")
//  @Value("${tastytrade.api.cert03.login}")
	private String certApiLogin;

	@Value("${tastytrade.api.cert01.password}")
//  @Value("${tastytrade.api.cert02.password}")
//  @Value("${tastytrade.api.cert03.password}")
	private String certApiPassword;

	@Value("${tastytrade.api.cert01.account}")
//  @Value("${tastytrade.api.cert02.account}")
//  @Value("${tastytrade.api.cert03.account}")
	private String CERT_ACCOUNT;

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
		assertThat(TastytradeSettingsRepository.certIsInvalid(tastytradeSettings)).isNotEqualTo(true);
	}

	@Test
	void listTransactions() {
		final String sessionToken = tokenManager.getCertSessionToken();
		assertThat(sessionToken).isNotBlank();
		Transactions transactionsApi = new Transactions(TastytradeApi.BASE.CERTIFICATION);
		String accountNumber = CERT_ACCOUNT;
		Integer perPage = 250;
		Integer pageOffset = null;
		String sort = "Desc";
		String type = null;
		List<String> types = null;
		List<String> subType = null;
		LocalDate startDate = LocalDate.now().with(firstDayOfYear());
		LocalDate endDate = LocalDate.now();
		String instrumentType = null;
		String symbol = null;
		String underlyingSymbol = null;
		String action = null;
		String partitionKey = null;
		String futuresSymbol = null;
		OffsetDateTime startAt = null;
		OffsetDateTime endAt = null;
		List<Transaction> transactions = transactionsApi.getTransactions(sessionToken, accountNumber, perPage,
				pageOffset, sort, type, types, subType, startDate, endDate, instrumentType, symbol, underlyingSymbol,
				action, partitionKey, futuresSymbol, startAt, endAt);
		for (Transaction t : transactions)
			logger.info(t.toString());
	}
}
