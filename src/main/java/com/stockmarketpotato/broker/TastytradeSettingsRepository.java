package com.stockmarketpotato.broker;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TastytradeSettingsRepository extends CrudRepository<TastytradeSettings, Long> {
	List<TastytradeSettings> findAll();

	TastytradeSettings findById(long id);
	
	public static boolean isInvalid(TastytradeSettingsRepository t) {
		if (t.findAll().size() != 1)
			return true;
		TastytradeSettings s = t.findAll().get(0);
		if (s.getApiPassword() == null || s.getApiLogin() == null)
			return true;
		if (s.getApiPassword().isEmpty() || s.getApiLogin().isEmpty())
			return true;
		return false;
	}

	public static boolean certIsInvalid(TastytradeSettingsRepository t) {
		if (t.findAll().size() != 1)
			return true;
		TastytradeSettings s = t.findAll().get(0);
		if (s.getCertApiPassword() == null || s.getCertApiLogin() == null)
			return true;
		if (s.getCertApiPassword().isEmpty() || s.getCertApiLogin().isEmpty())
			return true;
		return false;
	}
}
