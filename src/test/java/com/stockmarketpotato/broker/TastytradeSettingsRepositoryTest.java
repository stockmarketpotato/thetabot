package com.stockmarketpotato.broker;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class TastytradeSettingsRepositoryTest {
	@Autowired
    private TastytradeSettingsRepository repository;

	@Value("${tastytrade.api.prod.login}")
	private String prodApiLogin;
	@Value("${tastytrade.api.prod.password}")
	private String prodApiPassword;
	
	@Value("${tastytrade.api.cert.login}")
	private String certApiLogin;
	@Value("${tastytrade.api.cert.password}")
	private String certApiPassword;
	
    @Test
    public void testSaveAndFindOne() {
    	assertNotNull(repository);
        Iterable<TastytradeSettings> findAll = repository.findAll();
        assertThat(findAll).hasSize(0);
        
		TastytradeSettings s = new TastytradeSettings();
		s.setApiLogin(certApiLogin);
		s.setApiPassword(certApiPassword);
		repository.save(s);

        findAll = repository.findAll();
        assertThat(findAll).hasSize(1);
    }
}
