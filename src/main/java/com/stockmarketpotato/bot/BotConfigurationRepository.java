package com.stockmarketpotato.bot;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

/**
 * Repository interface for managing {@link BotConfiguration} entities.
 * Extends CrudRepository to provide basic CRUD operations.
 */
public interface BotConfigurationRepository extends CrudRepository<BotConfiguration, Long> {
	/**
	 * Retrieves all bot configurations.
	 * @return A list of all BotConfiguration entities.
	 */
	List<BotConfiguration> findAll();

	/**
	 * Retrieves a bot configuration by its ID.
	 * @param id The ID of the configuration.
	 * @return The BotConfiguration entity.
	 */
	BotConfiguration findById(long id);
}
