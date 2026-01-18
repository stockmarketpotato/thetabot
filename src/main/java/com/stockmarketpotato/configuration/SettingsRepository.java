package com.stockmarketpotato.configuration;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface SettingsRepository extends CrudRepository<Settings, Long> {
	List<Settings> findAll();

	Settings findById(long id);
}
