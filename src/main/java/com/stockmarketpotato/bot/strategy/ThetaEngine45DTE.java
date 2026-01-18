package com.stockmarketpotato.bot.strategy;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.stockmarketpotato.bot.BotConfiguration;
import com.stockmarketpotato.bot.BotConfiguration.TradingDay;
import com.stockmarketpotato.bot.task.ThetaEngineClosePositionTask;
import com.stockmarketpotato.bot.task.ThetaEngineManualTradingTask;
import com.stockmarketpotato.bot.task.ThetaEngineOpenPositionTask;
import com.stockmarketpotato.bot.task.ThetaEnginePeriodicStopLossTask;
import com.stockmarketpotato.broker.OccOsiSymbology;
import com.stockmarketpotato.feeds.AggregatedQuote;
import com.stockmarketpotato.feeds.dxfeed.SimpleInstrument;

import com.stockmarketpotato.integration.tastytrade.Instruments;
import com.stockmarketpotato.integration.tastytrade.model.accounts.AccountPosition;
import com.stockmarketpotato.integration.tastytrade.model.instruments.FuturesNestedOptionChainSerializer;
import com.stockmarketpotato.integration.tastytrade.model.instruments.FuturesOptionChain;
import com.stockmarketpotato.integration.tastytrade.model.instruments.FuturesOptionChainExpirations;
import com.stockmarketpotato.integration.tastytrade.model.instruments.FuturesOptionChainsExpirationsStrikes;
import com.stockmarketpotato.integration.tastytrade.model.orders.InstrumentTypeEnum;
import com.stockmarketpotato.integration.tradingcalendar.TradingHours;


/**
 * Trading strategy implementation focusing on selling options with approximately 45 Days To Expiration (DTE).
 * <p>
 * This engine manages the lifecycle of trades including:
 * scanning for opportunities based on delta and DTE, opening positions, managing existing positions, and closing them based on profit/loss targets.
 */
@Component
public class ThetaEngine45DTE extends BaseStrategy {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * Default constructor for ThetaEngine45DTE.
	 */
	public ThetaEngine45DTE() {
		super();
	}

	/**
	 * Retrieves and filters the option chain for a given symbol to find suitable contracts.
	 * Filters based on DTE, Delta, and existing positions to avoid duplicates.
	 * 
	 * @param accountNo The account number to check for existing positions.
	 * @param symbol The underlying symbol (e.g., /ES).
	 * @param minDTE Minimum Days To Expiration.
	 * @param maxDTE Maximum Days To Expiration.
	 * @param minDelta Minimum absolute Delta.
	 * @param maxDelta Maximum absolute Delta.
	 * @return A filtered FuturesOptionChain containing only the expirations and strikes that match the criteria.
	 */
	public FuturesOptionChain getFilteredOptionChain(String accountNo, String symbol, Integer minDTE, Integer maxDTE,
			BigDecimal minDelta, BigDecimal maxDelta) {
		// get the complete Nested Options Chain
		Instruments i = new Instruments();
		FuturesNestedOptionChainSerializer f = i.getFuturesOptionChainsSymbolNested(tokenManager.getSessionToken(),
				symbol);
		FuturesOptionChain oc = f.getOptionChains().get(0);

		// Remote all Options outside target DTE range
		oc.getExpirations().removeIf(e -> e.getDaysToExpiration() > maxDTE || e.getDaysToExpiration() < minDTE);

		for (FuturesOptionChainExpirations e : oc.getExpirations()) {
			// Remote all Options with Strikes above current price
			Optional<AggregatedQuote> quote = quoteRepository.findById(e.getUnderlyingSymbol());
			if (quote.isPresent())
				e.getStrikes().removeIf(s -> s.getStrikePrice() > quote.get().getBidPrice().doubleValue());
		}

		// Create SimpleInstrument to subscribe Quotes
		List<SimpleInstrument> instruments = new ArrayList<>();
		for (FuturesOptionChainExpirations e : oc.getExpirations()) {
			for (FuturesOptionChainsExpirationsStrikes s : e.getStrikes())
				instruments.add(new SimpleInstrument(s.getPut(), s.getPutStreamerSymbol()));
		}

		// Subscribe to Quote Feeds
		dxFeedManager.subscribeMultipleFeeds(instruments);

		// Subscribe to Greeks Feeds
		dxFeedManager.subscribeMultipleGreeksFeeds(instruments);

		Set<Double> strikesInOpenPositions = new HashSet<>();
		for (AccountPosition p : brokerManager.prodGetPositions(accountNo)) {
			if (!p.instrument_type.equals(InstrumentTypeEnum.FUTURE_OPTION.getValue()))
				continue;
			OccOsiSymbology occSymbol = OccOsiSymbology.fromFuturesOptionSymbol(p.symbol);
			strikesInOpenPositions.add(occSymbol.getStrike().doubleValue());
			// log.info("strike (AccountPosition): " + occSymbol.getStrike().doubleValue());
		}
		for (FuturesOptionChainExpirations e : oc.getExpirations()) {

			// Remote all Options with delta equals 0
			e.getStrikes().removeIf(s -> {
				// log.info("strike (FuturesOptionChainExpirations): " + s.getStrikePrice());
				if (strikesInOpenPositions.contains(s.getStrikePrice())) {
					log.info("Filter " + s.getStrikePrice());
					return true;
				} else
					return false;
			});

			// Remote all Options with delta equals 0
			e.getStrikes().removeIf(s -> {
				Optional<AggregatedQuote> q = Optional
						.ofNullable(quoteRepository.findByStreamerSymbol(s.getPutStreamerSymbol()));
				if (q.isEmpty())
					return false;
				else
					return q.get().getDelta().compareTo(new BigDecimal("0.00")) == 0;
			});

			// Remote all Options with delta outside target range
			e.getStrikes().removeIf(s -> {
				Optional<AggregatedQuote> q = Optional
						.ofNullable(quoteRepository.findByStreamerSymbol(s.getPutStreamerSymbol()));
				if (q.isEmpty())
					return false;
				else
					return (q.get().getDelta().abs().compareTo(minDelta) < 0
							|| q.get().getDelta().abs().compareTo(maxDelta) > 0);
			});
		}

		// Sort chains by DTE
		Collections.sort(oc.getExpirations(), (e1, e2) -> {
			return e1.getDaysToExpiration().compareTo(e2.getDaysToExpiration());
		});

		// Sort inside chain by Strike price
		for (FuturesOptionChainExpirations e : oc.getExpirations()) {
			Collections.sort(e.getStrikes(), (s1, s2) -> {
				return s1.getStrikePrice().compareTo(s2.getStrikePrice());
			});
		}

		return oc;
	}

	/**
	 * Schedules trading tasks (Open, Close, Manage) for the day based on market hours.
	 * 
	 * @param tradingHours The trading hours for the current day.
	 */
	@Override
	public void schedule(TradingHours tradingHours) {
		terminateAllTasks();
		BotConfiguration cfg = botConfigurationRepository.findAll().get(0);
		if (!cfg.isScheduleEnabled())
			return;
		boolean isTradingDay = false;
		for (final TradingDay d : cfg.getTradingDays()) {
			if (d.getDisplayValue().equals(tradingHours.getDay_of_week()))
				isTradingDay = true;
		}
		if (!isTradingDay) {
			log.info("Strategy not scheduled on " + tradingHours.getDay_of_week());
			return;
		}

		LocalTime marketTimeOpenPosition = cfg.getMarketTimeOpenPositionAsZonedDateTime().toLocalTime();
		if (tradingHours.getIs_early_close())
			marketTimeOpenPosition = calculateRelativeToRegularClose(marketTimeOpenPosition,
					tradingHours.getClose_time().toLocalTime());

		if (cfg.isOpenPositions())
			scheduleTimedTask(new ThetaEngineOpenPositionTask(this, "Open Trade"), marketTimeOpenPosition);

		if (cfg.isManualTradingActive())
			scheduleTimedTask(new ThetaEngineManualTradingTask(this, "Manual Trading"), marketTimeOpenPosition);

		LocalTime marketTimeClosePosition = cfg.getMarketTimeClosePositionAsZonedDateTime().toLocalTime();
		if (tradingHours.getIs_early_close())
			marketTimeClosePosition = calculateRelativeToRegularClose(marketTimeClosePosition,
					tradingHours.getClose_time().toLocalTime());

		if (cfg.isClosePositions())
			scheduleTimedTask(new ThetaEngineClosePositionTask(this, "Close Trade"), marketTimeClosePosition);

		schedulePeriodicTask(new ThetaEnginePeriodicStopLossTask(this, "Periodic Stop Loss Check"),
				marketTimeOpenPosition);
	}
}
