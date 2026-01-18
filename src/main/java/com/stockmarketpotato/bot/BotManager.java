package com.stockmarketpotato.bot;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.stockmarketpotato.bot.strategy.BaseStrategy.ScheduledTaskTiming;
import com.stockmarketpotato.bot.strategy.MonitoringStrategy;
import com.stockmarketpotato.bot.strategy.ThetaEngine45DTE;
import com.stockmarketpotato.broker.AccountStreamer;
import com.stockmarketpotato.broker.BrokerManager;
import com.stockmarketpotato.broker.SessionManager;
import com.stockmarketpotato.feeds.AggregatedQuote;
import com.stockmarketpotato.feeds.QuoteRepository;
import com.stockmarketpotato.feeds.dxfeed.DxFeedManager;
import com.stockmarketpotato.feeds.dxfeed.SimpleInstrument;
import com.stockmarketpotato.integration.tastytrade.AccountsAndCustomers;
import com.stockmarketpotato.integration.tastytrade.Instruments;
import com.stockmarketpotato.integration.tastytrade.model.accounts.Account;
import com.stockmarketpotato.integration.tastytrade.model.accounts.AccountPosition;
import com.stockmarketpotato.integration.tastytrade.model.instruments.AbstractInstrument;
import com.stockmarketpotato.integration.tastytrade.model.instruments.FuturesNestedOptionChainSerializer;
import com.stockmarketpotato.integration.tastytrade.model.instruments.FuturesOptionChain;
import com.stockmarketpotato.integration.tastytrade.model.instruments.FuturesOptionChainExpirations;
import com.stockmarketpotato.integration.tastytrade.model.instruments.FuturesOptionChainsExpirationsStrikes;
import com.stockmarketpotato.integration.tradingcalendar.TradingCalendar;
import com.stockmarketpotato.integration.tradingcalendar.TradingHours;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Component
@EnableScheduling
public class BotManager {
	/**
	 * Handles cleanup when a configuration is deleted.
	 * Stops market data feeds and disconnects the account streamer.
	 */
	public void onConfigurationDelete() {
		atMarketClose();
		preDestroy();
	}

	/**
	 * Handles updates to the bot configuration.
	 * Triggers a rescheduling of tasks for the day.
	 */
	public void onConfigurationUpdate() {
		scheduleBeforeOpen();
	}

	/**
	 * Retrieves a filtered option chain based on the provided criteria.
	 * Delegates the request to the trading strategy.
	 * 
	 * @param account The account number.
	 * @param symbol The underlying symbol.
	 * @param minDte Minimum days to expiration.
	 * @param maxDte Maximum days to expiration.
	 * @param minDelta Minimum delta.
	 * @param maxDelta Maximum delta.
	 * @return The filtered futures option chain.
	 */
	public FuturesOptionChain getFilteredOptionChain(String account, String symbol, Integer minDte, Integer maxDte,
			BigDecimal minDelta, BigDecimal maxDelta) {
		return ((ThetaEngine45DTE) tradingStrategy).getFilteredOptionChain(account, symbol, minDte, maxDte, minDelta,
				maxDelta);
	}

	/**
	 * Retrieves all scheduled tasks from the monitoring and trading strategies.
	 * 
	 * @return A map of task names to their scheduling timing details.
	 */
	public final Map<String, ScheduledTaskTiming> getScheduledTasks() {
		Map<String, ScheduledTaskTiming> tasks = new HashMap<>();
		tasks.putAll(this.monitoringStrategy.getScheduledTasks());
		tasks.putAll(this.tradingStrategy.getScheduledTasks());
		return tasks;
	}

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private final TaskScheduler executor;
	
	private ScheduledFuture<?> subscribeTask = null;
	private ScheduledFuture<?> atMarketCloseTask = null;

	@Autowired
	private BotConfigurationRepository botConfig;

	@Autowired
	private DxFeedManager feedManager;
	
	@Autowired
	private SessionManager tokenManager;

	@Autowired
	private QuoteRepository quoteRepository;
	
	@Autowired
	private MonitoringStrategy monitoringStrategy;
	
	@Autowired
	private ThetaEngine45DTE tradingStrategy;
	
	@Autowired
	private AccountStreamer accountStreamer;
	
	/**
	 * Constructor for BotManager.
	 * 
	 * @param taskExecutor The task scheduler to use.
	 */
	public BotManager(TaskScheduler taskExecutor) {
		this.executor = taskExecutor;
	}

	@PostConstruct
	private void postConstruct() {
		if (botConfig.findAll().isEmpty())
			return;
		scheduleBeforeOpen();
	}

	@PreDestroy
	private void preDestroy() {
		cancelIfScheduled(subscribeTask);
		cancelIfScheduled(atMarketCloseTask);
		this.monitoringStrategy.terminateAllTasks();
		this.tradingStrategy.terminateAllTasks();
	}

	private Optional<BotConfiguration> getFirstBotConfiguration() {
		List<BotConfiguration> allBots = botConfig.findAll();
		Optional<BotConfiguration> b = Optional.ofNullable(null);
		if (allBots.size() > 0)
			b = Optional.of(allBots.get(0));
		else
			log.warn("No Bot Configuration found.");
		return b;
	}

	/*
	 * Schedules tasks for the day in the morning
	 */
	@Scheduled(cron = "00 50 08 * * MON-FRI", zone = "America/New_York")
	private void scheduleBeforeOpen() {
		TradingHours th = TradingCalendar.getTradingHoursToday();
		if (th == null) {
			log.info("No trading today.");
		} else {
			log.info("Schedule tasks for the day");
			if (th.getIs_early_close())
				log.info("Market closes early today because of " + th.getHoliday_name());
			feedManager.connect();
			accountStreamer.connect();
			subscribe();
			
			monitoringStrategy.schedule(th);
			tradingStrategy.schedule(th);
			
			log.info("Schedule unsubscribe() at " + th.getClose_time().toString());
			cancelIfScheduled(atMarketCloseTask);
			atMarketCloseTask = executor.schedule(() -> {
				atMarketClose();
			}, th.getClose_time().toInstant());
		}
	}
	
	private void atMarketClose() {
		unsubscribe();
		feedManager.disconnect();
		accountStreamer.disconnect();
	}
	
	List<SimpleInstrument> quotes = new ArrayList<>();
	List<SimpleInstrument> greeks = new ArrayList<>();

	@Autowired
	private BrokerManager brokerManager;
	
	private void subscribe() {
		Optional<BotConfiguration> cfg = getFirstBotConfiguration();
		if (cfg.isEmpty())
			return;
		final String sessionToken = tokenManager.getSessionToken();
		if (sessionToken == null)
			return;
		String symbol = cfg.get().getUnderlying().getDisplayValue();
		symbol = symbol.replace("/", "");
		final Integer minDte = cfg.get().getOpenDte().intValue();
		final Integer maxDte = minDte + 7;
		
		// get the complete Nested Options Chain
		Instruments i = new Instruments();
		FuturesNestedOptionChainSerializer f = i.getFuturesOptionChainsSymbolNested(sessionToken, symbol);
		if (f != null) {
			FuturesOptionChain oc = f.getOptionChains().get(0);

			// Remote all Options outside target DTE range
			oc.getExpirations().removeIf(e -> e.getDaysToExpiration() > maxDte || e.getDaysToExpiration() < minDte);

			for (FuturesOptionChainExpirations e : oc.getExpirations()) {
				// Remote all Options with Strikes above current price
				Optional<AggregatedQuote> quote = quoteRepository.findById(e.getUnderlyingSymbol());
				if (quote.isPresent())
					e.getStrikes().removeIf(s -> s.getStrikePrice() > quote.get().getBidPrice().doubleValue());
			}

			// Create SimpleInstrument to subscribe Quotes
			for (FuturesOptionChainExpirations e : oc.getExpirations()) {
				for (FuturesOptionChainsExpirationsStrikes s : e.getStrikes()) {
					SimpleInstrument si = new SimpleInstrument(s.getPut(), s.getPutStreamerSymbol());
					quotes.add(si);
					greeks.add(si);
				}
			}
		}

		// add symbols of account positions
		AccountsAndCustomers accountsApi = new AccountsAndCustomers();
		List<Account> accounts = accountsApi.getAccounts(sessionToken);
		for (Account a : accounts) {
			List<AccountPosition> positions = brokerManager.prodGetPositions(a.account_number);
			for (AccountPosition p : positions) {
				SimpleInstrument si = new SimpleInstrument(p.symbol, p.streamerSymbol);
				quotes.add(si);
				if (p.instrument_type.contains("Option"))
					greeks.add(si);
			}
		}
		
		// Add default symbols
		// subscribe to SPX, SPY, /ES and /MES
		List<AbstractInstrument> d = new ArrayList<>();
		d.addAll(i.getFutures(sessionToken, new ArrayList<String>(), new ArrayList<String>(){{ add("ES"); }}));
		d.addAll(i.getFutures(sessionToken, new ArrayList<String>(), new ArrayList<String>(){{ add("MES"); }}));
		d.addAll(i.getEquities(sessionToken, new ArrayList<String>(){{ add("SPY"); }}, null, false, true));
		d.addAll(i.getEquities(sessionToken, new ArrayList<String>(){{ add("SPX"); }}, null, true, false));
		
		d.forEach(ai -> quotes.add(new SimpleInstrument(ai)));
		
		// Subscribe to Quote Feeds
		feedManager.subscribeMultipleFeeds(quotes);
		
		// Subscribe to Greeks Feeds
		feedManager.subscribeMultipleGreeksFeeds(greeks);
	}
	
	private void unsubscribe() {
		log.info("Unsubscribe " + quotes.size() + " Quotes");
		log.info("Unsubscribe " + greeks.size() + " Greeks");
		// Subscribe to Quote Feeds
		feedManager.cancelMultipleFeedSubscriptions(quotes);
		// Subscribe to Greeks Feeds
		feedManager.cancelMultipleGreeksFeedSubscriptions(greeks);
		quotes.clear();
		greeks.clear();
	}
	
	private boolean cancelIfScheduled(ScheduledFuture<?> task) {
		if (task != null && !task.isCancelled()) {
			log.info("Cancel Task " + task.toString());
			return task.cancel(true);
		}
		return false;
	}

	/**
	 * Retrieves the currently active bot configuration.
	 * 
	 * @return The active BotConfiguration, or null if none exists.
	 */
	public BotConfiguration getActiveConfiguration() {
		if (getFirstBotConfiguration().isPresent())
			return getFirstBotConfiguration().get();
		return null;
	}

	/**
	 * Retrieves all bot configurations.
	 * 
	 * @return A list of all bot configurations.
	 */
	public List<BotConfiguration> getAllBotConfigurations() {
		return botConfig.findAll();
	}

	/**
	 * Finds a bot configuration by its ID.
	 * 
	 * @param botId The ID of the bot configuration.
	 * @return An Optional containing the configuration if found.
	 */
	public Optional<BotConfiguration> findConfigurationById(Long botId) {
		return botConfig.findById(botId);
	}

	/**
	 * Deletes a bot configuration by its ID.
	 * 
	 * @param id The ID of the configuration to delete.
	 */
	public void deleteConfigurationById(Long id) {
		botConfig.deleteById(id);		
	}

	/**
	 * Saves a bot configuration.
	 * 
	 * @param bot The configuration to save.
	 * @return The saved configuration.
	 */
	public BotConfiguration saveConfiguration(BotConfiguration bot) {
		return botConfig.save(bot);		
	}
}
