package com.stockmarketpotato.bot.strategy;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.scheduling.support.CronTrigger;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.stockmarketpotato.bot.BotConfigurationRepository;
import com.stockmarketpotato.bot.task.BaseStrategyTimedTask;
import com.stockmarketpotato.broker.BrokerManager;
import com.stockmarketpotato.broker.SessionManager;
import com.stockmarketpotato.feeds.QuoteRepository;
import com.stockmarketpotato.feeds.dxfeed.DxFeedManager;
import com.stockmarketpotato.integration.tradingcalendar.TradingHours;
import com.stockmarketpotato.notification.MailManager;


/**
 * Abstract base class for all trading strategies. Implements the strategy pattern.
 * This class provides the foundational infrastructure for strategies, including:
 * <ul>
 *   <li>Dependency injection for broker, feed, and notification managers.</li>
 *   <li>Task scheduling capabilities (one-time and periodic) synchronized with market hours.</li>
 *   <li>Helper methods for cron expression generation based on market time (America/New_York).</li>
 * </ul>
 */
public abstract class BaseStrategy {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	protected final static String TIME_ZONE = "America/New_York";
	protected final static ZoneId ZONE_ID_MARKET = ZoneId.of("America/New_York", ZoneId.SHORT_IDS);
	protected final static ZoneId ZONE_ID_LOCAL = TimeZone.getDefault().toZoneId();

	protected Map<String, ScheduledFuture<?>> tasks;
	protected Map<String, CronExpression> crons;

	protected BotConfigurationRepository botConfigurationRepository;
	protected BrokerManager brokerManager;
	protected DxFeedManager dxFeedManager;
	protected MailManager mailManager;
	protected QuoteRepository quoteRepository;
	protected SpringTemplateEngine springTemplateEngine;
	protected TaskScheduler scheduler;
	protected SessionManager tokenManager;

	/**
	 * Sets the BotConfigurationRepository.
	 * @param botConfigurationRepository The repository to set.
	 */
	@Autowired
	public final void setBotConfigurationRepository(BotConfigurationRepository botConfigurationRepository) {
		this.botConfigurationRepository = botConfigurationRepository;
	}

	/**
	 * Sets the BrokerManager.
	 * @param brokerManager The manager to set.
	 */
	@Autowired
	public final void setBrokerManager(BrokerManager brokerManager) {
		this.brokerManager = brokerManager;
	}

	/**
	 * Sets the DxFeedManager.
	 * @param dxFeedManager The manager to set.
	 */
	@Autowired
	public final void setDxFeedManager(DxFeedManager dxFeedManager) {
		this.dxFeedManager = dxFeedManager;
	}

	/**
	 * Sets the MailManager.
	 * @param mailManager The manager to set.
	 */
	@Autowired
	public final void setMailManager(MailManager mailManager) {
		this.mailManager = mailManager;
	}

	/**
	 * Sets the QuoteRepository.
	 * @param quoteRepository The repository to set.
	 */
	@Autowired
	public final void setQuoteRepository(QuoteRepository quoteRepository) {
		this.quoteRepository = quoteRepository;
	}

	/**
	 * Sets the SpringTemplateEngine.
	 * @param springTemplateEngine The engine to set.
	 */
	@Autowired
	public final void setSpringTemplateEngine(SpringTemplateEngine springTemplateEngine) {
		this.springTemplateEngine = springTemplateEngine;
	}

	/**
	 * Sets the SessionManager (Token Manager).
	 * @param tokenManager The manager to set.
	 */
	@Autowired
	public final void setTokenManager(SessionManager tokenManager) {
		this.tokenManager = tokenManager;
	}

	/**
	 * Sets the TaskScheduler.
	 * @param taskScheduler The scheduler to set.
	 */
	@Autowired
	public final void setTaskScheduler(TaskScheduler taskScheduler) {
		this.scheduler = taskScheduler;
	}

	/**
	 * Gets the BrokerManager.
	 * @return The BrokerManager.
	 */
	public final BrokerManager getBrokerManager() {
		return brokerManager;
	}

	/**
	 * Gets the MailManager.
	 * @return The MailManager.
	 */
	public final MailManager getMailManager() {
		return mailManager;
	}

	/**
	 * Default constructor for BaseStrategy.
	 * Initializes internal maps for tasks and crons.
	 */
	public BaseStrategy() {
		this.crons = new HashMap<>();
		this.tasks = new HashMap<>();
	}

	/**
	 * Schedules a task to run at a specific time during the market day.
	 * 
	 * @param strategyTask The task to execute.
	 * @param startTime The time (ET) to execute the task.
	 */
	protected void scheduleTimedTask(BaseStrategyTimedTask strategyTask, LocalTime startTime) {
		cancelIfScheduled(tasks.get(strategyTask.getName()));
		ZonedDateTime startDateTime = ZonedDateTime.of(LocalDate.now(ZONE_ID_MARKET), startTime, ZONE_ID_MARKET);
		String cron = getCronExpression(startDateTime);
		Runnable task = () -> {
			strategyTask.tryExecute(botConfigurationRepository.findAll().get(0));
		};
		log.info("Schedule Task <" + strategyTask.getName() + ">");
		log.info("    at '" + cron + "' (America/New_York)");
		log.info("    at '" + getCronExpression(startDateTime.withZoneSameInstant(ZONE_ID_LOCAL)) + "' (local)");
		crons.put(strategyTask.getName(), CronExpression.parse(cron));
		tasks.put(strategyTask.getName(), scheduleCronET(task, cron));
	}

	/**
	 * Schedules a task to run periodically during the market day.
	 * 
	 * @param strategyTask The task to execute.
	 * @param startTime The start time (ET) for the periodic schedule.
	 */
	protected void schedulePeriodicTask(BaseStrategyTimedTask strategyTask, LocalTime startTime) {
		cancelIfScheduled(tasks.get(strategyTask.getName()));
		ZonedDateTime startDateTime = ZonedDateTime.of(LocalDate.now(ZONE_ID_MARKET), startTime, ZONE_ID_MARKET);
		String cron = getCronExpressionPeriodic(startDateTime);
		Runnable task = () -> {
			strategyTask.tryExecute(botConfigurationRepository.findAll().get(0));
		};
		log.info("Schedule Task <" + strategyTask.getName() + ">");
		log.info("    at '" + cron + "' (America/New_York)");
		log.info("    at '" + getCronExpressionPeriodic(startDateTime.withZoneSameInstant(ZONE_ID_LOCAL)) + "' (local)");
		crons.put(strategyTask.getName(), CronExpression.parse(cron));
		tasks.put(strategyTask.getName(), scheduleCronET(task, cron));
	}

	/**
	 * Cancels all currently scheduled tasks for this strategy.
	 */
	public void terminateAllTasks() {
		tasks.forEach((strategy, task) -> {
			cancelIfScheduled(tasks.get(strategy));
		});
	}

	/**
	 * Inner class representing the timing of a scheduled task.
	 */
	public class ScheduledTaskTiming {
		public LocalDateTime market;
		public LocalDateTime local;

		/**
		 * Constructor for ScheduledTaskTiming.
		 * 
		 * @param market The market time of the task.
		 * @param local The local time of the task.
		 */
		public ScheduledTaskTiming(LocalDateTime market, LocalDateTime local) {
			super();
			this.market = market;
			this.local = local;
		}
	}

	/**
	 * Retrieves a map of currently scheduled tasks and their next execution times.
	 * The map keys are the task names, and values are ScheduledTaskTiming objects
	 * containing both market and local times.
	 * 
	 * @return A map of scheduled tasks.
	 */
	public Map<String, ScheduledTaskTiming> getScheduledTasks() {
		Map<String, ScheduledTaskTiming> taskList = new HashMap<>();
		tasks.forEach((strategy, task) -> {
			if (task != null && !task.isCancelled()) {
				LocalDateTime market = crons.get(strategy).next(LocalDateTime.now(ZONE_ID_MARKET));
				LocalDateTime local = ZonedDateTime.of(market, ZONE_ID_MARKET).withZoneSameInstant(ZONE_ID_LOCAL)
						.toLocalDateTime();
				taskList.put(strategy, new ScheduledTaskTiming(market, local));
			}
		});
		return taskList;
	}

	private boolean cancelIfScheduled(ScheduledFuture<?> task) {
		if (task != null && !task.isCancelled()) {
			log.info("Cancel Task " + task.toString());
			return task.cancel(true);
		}
		return false;
	}

	private ScheduledFuture<?> scheduleCronET(final Runnable task, final String cron) {
		// Schedule a task with the given cron expression
		return scheduler.schedule(task, new CronTrigger(cron, ZONE_ID_MARKET));
	}

	/**
	 * Gets the QuoteRepository.
	 * @return The QuoteRepository.
	 */
	public QuoteRepository getQuoteRepository() {
		return this.quoteRepository;
	}

	/**
	 * Gets the SessionManager.
	 * @return The SessionManager.
	 */
	public SessionManager getTokenManager() {
		return this.tokenManager;
	}

	/**
	 * Gets the DxFeedManager.
	 * @return The DxFeedManager.
	 */
	public DxFeedManager getDxFeedManager() {
		return this.dxFeedManager;
	}

	/**
	 * Abstract method to schedule strategy-specific tasks based on the trading hours of the day.
	 * 
	 * @param tradingHours The trading hours for the current day.
	 */
	public abstract void schedule(final TradingHours tradingHours);

	/**
	 * Cron Expression that represents the execution pattern of this strategy.
	 */
	protected String getCronExpression(ZonedDateTime startTime) {
		/**
		 * ┌───────────── second (0-59)
		 * │ ┌───────────── minute (0 - 59)
		 * │ │ ┌───────────── hour (0 - 23)
		 * │ │ │ ┌───────────── day of the month (1 - 31)
		 * │ │ │ │ ┌───────────── month (1 - 12) (or JAN-DEC)
		 * │ │ │ │ │ ┌───────────── day of the week (0 - 7)
		 * │ │ │ │ │ │          (0 or 7 is Sunday, or MON-SUN)
		 * │ │ │ │ │ │
		 * * * * * * 0 mm HH d M *
		 */
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("0 mm HH d M '*'");
		return dateFormat.format(startTime);
	}

	/**
	 * Cron Expression that represents the execution pattern of this strategy.
	 */
	protected String getCronExpressionPeriodic(ZonedDateTime startTime) {
		/**
		 * ┌───────────── second (0-59)
		 * │ ┌───────────── minute (0 - 59)
		 * │ │ ┌───────────── hour (0 - 23)
		 * │ │ │ ┌───────────── day of the month (1 - 31)
		 * │ │ │ │ ┌───────────── month (1 - 12) (or JAN-DEC)
		 * │ │ │ │ │ ┌───────────── day of the week (0 - 7)
		 * │ │ │ │ │ │          (0 or 7 is Sunday, or MON-SUN)
		 * │ │ │ │ │ │
		 * * * * * * 0 mm HH d M *
		 */
		// every two minutes between 9am and 4pm
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("0 0/2 9-16 d M '*'");
		return dateFormat.format(startTime);
	}

	protected LocalTime calculateRelativeToRegularClose(LocalTime beforeRegularClose, LocalTime earlyClose) {
		LocalTime regularClose = LocalTime.of(16, 0);
		Duration offset = Duration.between(beforeRegularClose, regularClose);
		LocalTime offsetAsTime = LocalTime.of((int) offset.toHours(), (int) (offset.toMinutes() % 60));
		Duration earlyCloseTime = Duration.between(offsetAsTime, earlyClose);
		return LocalTime.of((int) earlyCloseTime.toHours(), (int) (earlyCloseTime.toMinutes() % 60));
	}
}
