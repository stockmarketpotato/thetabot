package com.stockmarketpotato.bot.strategy;

import java.time.LocalTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.stockmarketpotato.bot.BotConfiguration;
import com.stockmarketpotato.bot.BotConfiguration.TradingDay;
import com.stockmarketpotato.bot.task.MonitoringTask;
import com.stockmarketpotato.integration.tradingcalendar.TradingHours;

/**
 * Strategy responsible for monitoring the portfolio and market status throughout the day.
 * This strategy schedules monitoring tasks at configurable times:
 * <ul>
 *   <li>Pre-market</li>
 *   <li>After market open</li>
 *   <li>Before market close</li>
 * </ul>
 * It checks if the current day is a configured trading day before scheduling.
 */
@Component
public class MonitoringStrategy extends BaseStrategy {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * Schedules the monitoring tasks based on the provided trading hours and bot configuration.
	 * 
	 * @param tradingHours The trading hours for the current day, used to determine open/close times.
	 */
	@Override
	public void schedule(TradingHours tradingHours) {
		terminateAllTasks();
		BotConfiguration cfg = botConfigurationRepository.findAll().get(0);
		boolean isTradingDay = false;
		for (final TradingDay d : cfg.getTradingDays()) {
			if (d.getDisplayValue().equals(tradingHours.getDay_of_week()))
				isTradingDay = true;
		}
		if (!isTradingDay) {
			log.info("Strategy not scheduled on " + tradingHours.getDay_of_week());
			return;
		}

		if (cfg.isMonitoringPremarket())
			scheduleTimedTask(new MonitoringTask(this, "Monitoring Pre-Market"),
					cfg.getMarketTimeMonitoringPreMarketAsZonedDateTime().toLocalTime());

		if (cfg.isMonitoringAfterOpen())
			scheduleTimedTask(new MonitoringTask(this, "Monitoring After Open"),
					cfg.getMarketTimeMonitoringAfterOpenAsZonedDateTime().toLocalTime());

		LocalTime marketTimeBeforeClose = cfg.getMarketTimeMonitoringBeforeCloseAsZonedDateTime().toLocalTime();
		if (tradingHours.getIs_early_close())
			marketTimeBeforeClose = calculateRelativeToRegularClose(marketTimeBeforeClose,
					tradingHours.getClose_time().toLocalTime());

		if (cfg.isMonitoringBeforeClose())
			scheduleTimedTask(new MonitoringTask(this, "Monitoring Before Close"), marketTimeBeforeClose);
	}
}
