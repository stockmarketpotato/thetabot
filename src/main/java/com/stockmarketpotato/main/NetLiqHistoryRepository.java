package com.stockmarketpotato.main;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface NetLiqHistoryRepository extends CrudRepository<NetLiqHistory, String> {
	List<NetLiqHistory> findAll();
	List<NetLiqHistory> findAllById(String id);
}