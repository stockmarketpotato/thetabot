package com.stockmarketpotato.bot;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Entity class representing the configuration for a trading bot.
 * Stores strategy parameters, scheduling times, and account details.
 * Note: Bomb Shelter is currently not supported.
 */
@Entity
public class BotConfiguration {
	/**
	 * Enum representing the supported underlying assets.
	 * WARNING: only /ES has been tested.
	 */
	public enum Underlying {
		SPX("SPX"), SPY("SPY"), ES("/ES"), MES("/MES");

		private final String displayValue;

		private Underlying(String displayValue) {
			this.displayValue = displayValue;
		}

		public String getDisplayValue() {
			return displayValue;
		}
	}

	/**
	 * Enum representing days of the week for trading.
	 */
	public enum TradingDay {
		MONDAY("Monday"), TUESDAY("Tuesday"), WEDNESDAY("Wednesday"), THURSDAY("Thursday"), FRIDAY("Friday");

		private final String displayValue;

		private TradingDay(String displayValue) {
			this.displayValue = displayValue;
		}

		public String getDisplayValue() {
			return displayValue;
		}
		
		/**
		 * Returns a list of all trading days of a week (Monday to Friday).
		 * @return List of TradingDay enums.
		 */
		static public List<TradingDay> getEveryDay() {
			return Arrays.asList(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY);
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	/**
	 * BASIC STRATEGY PARAMETERS
	 */
	
	/**
	 * The name of the strategy.
	 */
	private String name = "SPY 45 DTE Income Strategy";

	/**
	 * Maximum allowed option delta of the short put.
	 */
	private BigDecimal maxDelta = new BigDecimal("0.05");

	/**
	 * When to take profit of open positions
	 */
	private BigDecimal takeProfit = new BigDecimal("0.6");
	
	/**
	 * Underlying product for which we sell options.
	 */
	private Underlying underlying = Underlying.SPX;
	
	/**
	 * Sell closest to openDte DTE and go further out.
	 */
	private Long openDte = 45L;
	
	/**
	 * Exit at exitDte DTE no matter what
	 */
	private Long exitDte = 21L;
	
	/**
	 * Negative limit for the Stop Loss.
	 */
	private BigDecimal stopLoss = new BigDecimal("-2.0");
	
	/**
	 * Number of the account we trade.
	 */
	private String accountNumber = "";

	/**
	 * The maximum buying power that will be used
	 */
	private BigDecimal maxBuyingPower = new BigDecimal("0.4");
	
	/**
	 * Position entries per week
	 */
	private Long entriesPerWeek;
	
	/**
	 * Days of the week when new positions are opened.
	 */
	private List<TradingDay> tradingDays = new ArrayList<>();
	
	/**
	 * ORDER PARAMETERS	 
	 */
	
	/**
	 * The time of day at which new positions are opened.
	 */
	@DateTimeFormat(pattern = "HH:mm")
	private Date marketTimeOpenPosition = new Date();
	
	/**
	 * The time of day at which open positions are closed.
	 */
	@DateTimeFormat(pattern = "HH:mm")
	private Date marketTimeClosePosition = new Date();
	
	/**
	 * Amount of credit to collect per entry in a position.
	 * 
	 * creditPerEntry = premiumPerYear / entriesPerYear;
	 */
	private BigDecimal creditPerEntry = BigDecimal.valueOf(100);

	private boolean scheduleEnabled = true;

	/**
	 * Checks if the schedule is enabled.
	 * @return true if enabled, false otherwise.
	 */
	public boolean isScheduleEnabled() {
		return scheduleEnabled;
	}

	/**
	 * Sets the schedule enabled status.
	 * @param scheduleEnabled true to enable, false to disable.
	 */
	public void setScheduleEnabled(boolean scheduleEnabled) {
		this.scheduleEnabled = scheduleEnabled;
	}

	/**
	 * Monitoring
	 */
	
	private boolean monitoringPremarket;
	
	@DateTimeFormat(pattern = "HH:mm")
	private Date marketTimeMonitoringPreMarket = new Date();
	
	private boolean monitoringAfterOpen;

	@DateTimeFormat(pattern = "HH:mm")
	private Date marketTimeMonitoringAfterOpen = new Date();
	
	private boolean monitoringBeforeClose;

	@DateTimeFormat(pattern = "HH:mm")
	private Date marketTimeMonitoringBeforeClose = new Date();


	private boolean openPositions = true;
	
	private boolean closePositions = true;
	
	/**
	 * The Bomb Shelter is a hedge for black swan events. Not supported at the
	 * moment.
	 */
	private boolean bombShelter = false;

	private boolean manualTradingActive;

	/**
	 * Protected constructor for JPA and internal use.
	 */
	protected BotConfiguration() {
		super();
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MILLISECOND, 0);
		c.set(Calendar.HOUR_OF_DAY, 9);
		c.set(Calendar.MINUTE, 20);
		this.marketTimeMonitoringPreMarket = c.getTime();
		c.set(Calendar.MINUTE, 40);
		this.marketTimeMonitoringAfterOpen = c.getTime();	
		c.set(Calendar.HOUR_OF_DAY, 15);
		c.set(Calendar.MINUTE, 40);
		this.marketTimeClosePosition = c.getTime();
		c.set(Calendar.MINUTE, 45);
		this.marketTimeOpenPosition = c.getTime();
		c.set(Calendar.MINUTE, 50);
		this.marketTimeMonitoringBeforeClose = c.getTime();
		this.tradingDays.add(TradingDay.MONDAY);
	}
	
	/**
	 * Gets the market time for monitoring after open as a ZonedDateTime.
	 * @return ZonedDateTime representing the time.
	 */
	public ZonedDateTime getMarketTimeMonitoringAfterOpenAsZonedDateTime() {
		return asZoneDatedTime(marketTimeMonitoringAfterOpen);
	}
	
	/**
	 * Gets the market time for opening positions.
	 * @return Date object.
	 */
	public Date getMarketTimeOpenPosition() {
		return marketTimeOpenPosition;
	}

	/**
	 * Sets the market time for opening positions.
	 * @param marketTimeOpenPosition The time to set.
	 */
	public void setMarketTimeOpenPosition(Date marketTimeOpenPosition) {
		this.marketTimeOpenPosition = marketTimeOpenPosition;
	}

	/**
	 * Gets the market time for closing positions.
	 * @return Date object.
	 */
	public Date getMarketTimeClosePosition() {
		return marketTimeClosePosition;
	}

	/**
	 * Sets the market time for closing positions.
	 * @param marketTimeClosePosition The time to set.
	 */
	public void setMarketTimeClosePosition(Date marketTimeClosePosition) {
		this.marketTimeClosePosition = marketTimeClosePosition;
	}

	/**
	 * Gets the market time for pre-market monitoring.
	 * @return Date object.
	 */
	public Date getMarketTimeMonitoringPreMarket() {
		return marketTimeMonitoringPreMarket;
	}

	/**
	 * Sets the market time for pre-market monitoring.
	 * @param marketTimeMonitoringPreMarket The time to set.
	 */
	public void setMarketTimeMonitoringPreMarket(Date marketTimeMonitoringPreMarket) {
		this.marketTimeMonitoringPreMarket = marketTimeMonitoringPreMarket;
	}

	/**
	 * Gets the market time for monitoring after open.
	 * @return Date object.
	 */
	public Date getMarketTimeMonitoringAfterOpen() {
		return marketTimeMonitoringAfterOpen;
	}

	/**
	 * Sets the market time for monitoring after open.
	 * @param marketTimeMonitoringAfterOpen The time to set.
	 */
	public void setMarketTimeMonitoringAfterOpen(Date marketTimeMonitoringAfterOpen) {
		this.marketTimeMonitoringAfterOpen = marketTimeMonitoringAfterOpen;
	}

	/**
	 * Gets the market time for monitoring before close.
	 * @return Date object.
	 */
	public Date getMarketTimeMonitoringBeforeClose() {
		return marketTimeMonitoringBeforeClose;
	}

	/**
	 * Sets the market time for monitoring before close.
	 * @param marketTimeMonitoringBeforeClose The time to set.
	 */
	public void setMarketTimeMonitoringBeforeClose(Date marketTimeMonitoringBeforeClose) {
		this.marketTimeMonitoringBeforeClose = marketTimeMonitoringBeforeClose;
	}

	/**
	 * Constructor with name.
	 * @param name The name of the configuration.
	 */
	public BotConfiguration(final String name) {
		this.name = name;
		this.maxDelta = new BigDecimal("0.05");
		this.takeProfit = new BigDecimal("0.6");
		this.underlying = Underlying.SPX;
		this.openDte = 45L;
		this.exitDte = 21L;
		this.accountNumber = "";
	}

	/**
	 * Constructor with key parameters.
	 * @param name The name of the configuration.
	 * @param maxDelta Maximum delta.
	 * @param takeProfit Take profit percentage.
	 * @param underlying Underlying asset.
	 */
	public BotConfiguration(final String name, final BigDecimal maxDelta, final BigDecimal takeProfit,
			final Underlying underlying) {
		this.name = name;
		this.maxDelta = maxDelta;
		this.takeProfit = takeProfit;
		this.underlying = underlying;
		this.accountNumber = "";
	}
	
	/**
	 * Converts a Date object to ZonedDateTime in America/New_York time zone.
	 * @param date The date to convert.
	 * @return ZonedDateTime.
	 */
	private ZonedDateTime asZoneDatedTime(final Date date) {
		return ZonedDateTime.ofInstant(date.toInstant(), TimeZone.getDefault().toZoneId())
				.withZoneSameLocal(ZoneId.of("America/New_York", ZoneId.SHORT_IDS));
	}

	/**
	 * Gets the ID.
	 * @return The ID.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the ID.
	 * @param id The ID.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the name.
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * @param name The name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the max delta.
	 * @return The max delta.
	 */
	public BigDecimal getMaxDelta() {
		return maxDelta;
	}

	/**
	 * Sets the max delta.
	 * @param maxDelta The max delta.
	 */
	public void setMaxDelta(BigDecimal maxDelta) {
		this.maxDelta = maxDelta;
	}

	/**
	 * Gets the take profit.
	 * @return The take profit.
	 */
	public BigDecimal getTakeProfit() {
		return takeProfit;
	}

	/**
	 * Sets the take profit.
	 * @param takeProfit The take profit.
	 */
	public void setTakeProfit(BigDecimal takeProfit) {
		this.takeProfit = takeProfit;
	}

	/**
	 * Gets the underlying asset.
	 * @return The underlying asset.
	 */
	public Underlying getUnderlying() {
		return underlying;
	}

	/**
	 * Sets the underlying asset.
	 * @param underlying The underlying asset.
	 */
	public void setUnderlying(Underlying underlying) {
		this.underlying = underlying;
	}

	/**
	 * Gets the open DTE.
	 * @return The open DTE.
	 */
	public Long getOpenDte() {
		return openDte;
	}

	/**
	 * Sets the open DTE.
	 * @param openDte The open DTE.
	 */
	public void setOpenDte(Long openDte) {
		this.openDte = openDte;
	}


	/**
	 * Gets the account number.
	 * @return The account number.
	 */
	public String getAccountNumber() {
		return accountNumber;
	}

	/**
	 * Gets the stop loss.
	 * @return The stop loss.
	 */
	public BigDecimal getStopLoss() {
		return stopLoss;
	}

	/**
	 * Sets the stop loss.
	 * @param stopLoss The stop loss.
	 */
	public void setStopLoss(BigDecimal stopLoss) {
		this.stopLoss = stopLoss;
	}

	/**
	 * Sets the account number.
	 * @param accountNumber The account number.
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	@Override
	public String toString() {
		return "BotConfiguration" + "[id='" + id + "', name='" + name + "', maxDelta='" + maxDelta + "', takeProfit='"
				+ takeProfit + "', underlying='" + underlying.toString() + "', openDte='" + openDte + "', exitDte='"
				+ exitDte + "', stopLoss='" + stopLoss + "', accountNumber='" + accountNumber + "']";
	}

	/**
	 * Gets the max buying power.
	 * @return The max buying power.
	 */
	public BigDecimal getMaxBuyingPower() {
		return maxBuyingPower;
	}

	/**
	 * Sets the max buying power.
	 * @param maxBuyingPower The max buying power.
	 */
	public void setMaxBuyingPower(BigDecimal maxBuyingPower) {
		this.maxBuyingPower = maxBuyingPower;
	}

	/**
	 * Gets the entries per week.
	 * @return The entries per week.
	 */
	public Long getEntriesPerWeek() {
		return entriesPerWeek;
	}

	/**
	 * Sets the entries per week.
	 * @param entriesPerWeek The entries per week.
	 */
	public void setEntriesPerWeek(Long entriesPerWeek) {
		this.entriesPerWeek = entriesPerWeek;
	}

	/**
	 * Checks if bomb shelter is enabled.
	 * @return true if enabled, false otherwise.
	 */
	public boolean isBombShelter() {
		return bombShelter;
	}

	/**
	 * Sets the bomb shelter status.
	 * @param bombShelter true to enable, false to disable.
	 */
	public void setBombShelter(boolean bombShelter) {
		this.bombShelter = bombShelter;
	}

	/**
	 * Gets the credit per entry.
	 * @return The credit per entry.
	 */
	public BigDecimal getCreditPerEntry() {
		return creditPerEntry;
	}

	/**
	 * Sets the credit per entry.
	 * @param creditPerEntry The credit per entry.
	 */
	public void setCreditPerEntry(BigDecimal creditPerEntry) {
		this.creditPerEntry = creditPerEntry;
	}

	/**
	 * Gets the trading days.
	 * @return List of trading days.
	 */
	public List<TradingDay> getTradingDays() {
		return tradingDays;
	}

	/**
	 * Sets the trading days.
	 * @param tradingDays List of trading days.
	 */
	public void setTradingDays(List<TradingDay> tradingDays) {
		this.tradingDays = tradingDays;
	}

	/**
	 * Gets the exit DTE.
	 * @return The exit DTE.
	 */
	public Long getExitDte() {
		return exitDte;
	}

	/**
	 * Sets the exit DTE.
	 * @param exitDte The exit DTE.
	 */
	public void setExitDte(Long exitDte) {
		this.exitDte = exitDte;
	}

	/**
	 * Checks if monitoring after open is enabled.
	 * @return true if enabled, false otherwise.
	 */
	public boolean isMonitoringAfterOpen() {
		return monitoringAfterOpen;
	}

	/**
	 * Sets the monitoring after open status.
	 * @param monitoringAfterOpen true to enable, false to disable.
	 */
	public void setMonitoringAfterOpen(boolean monitoringAfterOpen) {
		this.monitoringAfterOpen = monitoringAfterOpen;
	}

	/**
	 * Checks if manual trading is active.
	 * @return true if active, false otherwise.
	 */
	public boolean isManualTradingActive() {
		return manualTradingActive;
	}

	/**
	 * Sets the manual trading status.
	 * @param manualTradingActive true to activate, false to deactivate.
	 */
	public void setManualTradingActive(boolean manualTradingActive) {
		this.manualTradingActive = manualTradingActive;
	}

	/**
	 * Gets the market time for closing positions as ZonedDateTime.
	 * @return ZonedDateTime.
	 */
	public ZonedDateTime getMarketTimeClosePositionAsZonedDateTime() {
		return asZoneDatedTime(marketTimeClosePosition);
	}

	/**
	 * Checks if opening positions is enabled.
	 * @return true if enabled, false otherwise.
	 */
	public boolean isOpenPositions() {
		return this.openPositions;
	}
	
	/**
	 * Sets the open positions status.
	 * @param openPositions true to enable, false to disable.
	 */
	public void setOpenPositions(boolean openPositions) {
		this.openPositions = openPositions;
	}

	/**
	 * Checks if closing positions is enabled.
	 * @return true if enabled, false otherwise.
	 */
	public boolean isClosePositions() {
		return this.closePositions;
	}
	
	/**
	 * Sets the close positions status.
	 * @param closePositions true to enable, false to disable.
	 */
	public void setClosePositions(boolean closePositions) {
		this.closePositions = closePositions;
	}

	/**
	 * Checks if monitoring before close is enabled.
	 * @return true if enabled, false otherwise.
	 */
	public boolean isMonitoringBeforeClose() {
		return monitoringBeforeClose;
	}

	/**
	 * Sets the monitoring before close status.
	 * @param monitoringBeforeClose true to enable, false to disable.
	 */
	public void setMonitoringBeforeClose(boolean monitoringBeforeClose) {
		this.monitoringBeforeClose = monitoringBeforeClose;
	}
	
	/**
	 * Checks if pre-market monitoring is enabled.
	 * @return true if enabled, false otherwise.
	 */
	public boolean isMonitoringPremarket() {
		return monitoringPremarket;
	}

	/**
	 * Sets the pre-market monitoring status.
	 * @param monitoringPremarket true to enable, false to disable.
	 */
	public void setMonitoringPremarket(boolean monitoringPremarket) {
		this.monitoringPremarket = monitoringPremarket;
	}

	/**
	 * Gets the market time for pre-market monitoring as ZonedDateTime (truncated to minutes).
	 * @return ZonedDateTime.
	 */
	public ZonedDateTime getMarketTimeMonitoringPreMarketAsZonedDateTime() {
		return asZoneDatedTime(marketTimeMonitoringPreMarket).truncatedTo(ChronoUnit.MINUTES);
	}

	/**
	 * Gets the market time for opening positions as ZonedDateTime (truncated to minutes).
	 * @return ZonedDateTime.
	 */
	public ZonedDateTime getMarketTimeOpenPositionAsZonedDateTime() {
		return asZoneDatedTime(marketTimeOpenPosition).truncatedTo(ChronoUnit.MINUTES);
	}

	/**
	 * Gets the market time for monitoring before close as ZonedDateTime (truncated to minutes).
	 * @return ZonedDateTime.
	 */
	public ZonedDateTime getMarketTimeMonitoringBeforeCloseAsZonedDateTime() {
		return asZoneDatedTime(marketTimeMonitoringBeforeClose).truncatedTo(ChronoUnit.MINUTES);
	}
}
