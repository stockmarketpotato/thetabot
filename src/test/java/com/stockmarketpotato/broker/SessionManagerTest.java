package com.stockmarketpotato.broker;

import static org.assertj.core.api.Assertions.assertThat;

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

@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan(basePackages = { "com.stockmarketpotato.broker", "com.stockmarketpotato.integration.tastytrade", "com.stockmarketpotato.feeds.dxfeed"})
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class SessionManagerTest {
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
	
	@Test
	void shouldReturnSessionToken() throws Exception {
		logger.info("Save TastytradeSettings");
		TastytradeSettings s = new TastytradeSettings();
		s.setApiLogin(prodApiLogin);
		s.setApiPassword(prodApiPassword);
		tastytradeSettings.save(s);
		logger.info("Check not null");
		assertThat(tastytradeSettings).isNotNull();
		assertThat(tokenManager).isNotNull();
		assertThat(TastytradeSettingsRepository.isInvalid(tastytradeSettings)).isNotEqualTo(true);
		logger.info("Check Session Token");
		String token = tokenManager.getSessionToken();
		assertThat(token).isNotBlank();
	}

	@Test
	void shouldReturnApiQuoteToken() throws Exception {
		logger.info("Save TastytradeSettings");
		TastytradeSettings s = new TastytradeSettings();
		s.setApiLogin(prodApiLogin);
		s.setApiPassword(prodApiPassword);
		tastytradeSettings.save(s);
		logger.info("Check not null");
		assertThat(tastytradeSettings).isNotNull();
		assertThat(tokenManager).isNotNull();
		assertThat(TastytradeSettingsRepository.isInvalid(tastytradeSettings)).isNotEqualTo(true);
		logger.info("Check Api Quote Token");
		String token = tokenManager.getApiQuoteToken();
		assertThat(token).isNotBlank();
	}
	
	@Test
	void shouldReturnCertSessionToken() throws Exception {
		logger.info("Save TastytradeSettings");
		TastytradeSettings s = new TastytradeSettings();
		s.setCertApiLogin(certApiLogin);
		s.setCertApiPassword(certApiPassword);
		tastytradeSettings.save(s);
        Iterable<TastytradeSettings> findAll = tastytradeSettings.findAll();
        assertThat(findAll).hasSize(1);
		logger.info("Check not null");
		assertThat(tastytradeSettings).isNotNull();
		assertThat(tokenManager).isNotNull();
		assertThat(TastytradeSettingsRepository.certIsInvalid(tastytradeSettings)).isNotEqualTo(true);
		logger.info("Check Cert Session Token");
		String token = tokenManager.getCertSessionToken();
		assertThat(token).isNotBlank();
	}
}
