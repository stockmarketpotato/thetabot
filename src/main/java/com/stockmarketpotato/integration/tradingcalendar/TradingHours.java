package com.stockmarketpotato.integration.tradingcalendar;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Data model representing trading hours for a specific market and date.
 * Typically deserialized from the Trading Calendar API response.
 */
@JsonPropertyOrder({
	TradingHours.JSON_PROPERTY_MIC,
	TradingHours.JSON_PROPERTY_DATE,
	TradingHours.JSON_PROPERTY_DAY_OF_WEEK,
	TradingHours.JSON_PROPERTY_IS_EARLY_CLOSE,
	TradingHours.JSON_PROPERTY_OPEN_TIME,
	TradingHours.JSON_PROPERTY_CLOSE_TIME,
	TradingHours.JSON_PROPERTY_HOLIDAY_NAME })
@JsonIgnoreProperties(ignoreUnknown = true)
public class TradingHours {
	public static final String JSON_PROPERTY_MIC = "mic";
	private String mic;
	public static final String JSON_PROPERTY_DATE = "date";
	private LocalDate date;
	public static final String JSON_PROPERTY_DAY_OF_WEEK = "day_of_week";
	private String day_of_week;
	public static final String JSON_PROPERTY_IS_EARLY_CLOSE = "is_early_close";
	private Boolean is_early_close;
	public static final String JSON_PROPERTY_OPEN_TIME = "open_time";
	private ZonedDateTime open_time;
	public static final String JSON_PROPERTY_CLOSE_TIME = "close_time";
	private ZonedDateTime close_time;
	public static final String JSON_PROPERTY_HOLIDAY_NAME = "holiday_name";
	private String holiday_name;

	/**
	 * Gets the Market Identifier Code (MIC).
	 * @return The MIC string (e.g., XNYS).
	 */
	public String getMic() {
		return mic;
	}

	/**
	 * Sets the Market Identifier Code (MIC).
	 * @param mic The MIC to set.
	 */
	public void setMic(String mic) {
		this.mic = mic;
	}

	/**
	 * Gets the date for these trading hours.
	 * @return The date.
	 */
	public LocalDate getDate() {
		return date;
	}

	/**
	 * Sets the date.
	 * @param date The date to set.
	 */
	public void setDate(LocalDate date) {
		this.date = date;
	}

	/**
	 * Gets the day of the week.
	 * @return The day of the week string.
	 */
	public String getDay_of_week() {
		return day_of_week;
	}

	/**
	 * Sets the day of the week.
	 * @param day_of_week The day of the week to set.
	 */
	public void setDay_of_week(String day_of_week) {
		this.day_of_week = day_of_week;
	}

	/**
	 * Checks if the market closes early on this date.
	 * @return True if early close, false otherwise.
	 */
	public Boolean getIs_early_close() {
		return is_early_close;
	}

	/**
	 * Sets the early close flag.
	 * @param is_early_close The flag to set.
	 */
	public void setIs_early_close(Boolean is_early_close) {
		this.is_early_close = is_early_close;
	}

	/**
	 * Gets the market open time, converted to America/New_York time zone.
	 * @return The open time ZonedDateTime.
	 */
	public ZonedDateTime getOpen_time() {
		return open_time.withZoneSameInstant(ZoneId.of("America/New_York", ZoneId.SHORT_IDS));
	}

	/**
	 * Sets the market open time.
	 * @param open_time The open time to set.
	 */
	public void setOpen_time(ZonedDateTime open_time) {
		this.open_time = open_time;
	}

	/**
	 * Gets the market close time, converted to America/New_York time zone.
	 * @return The close time ZonedDateTime.
	 */
	public ZonedDateTime getClose_time() {
		return close_time.withZoneSameInstant(ZoneId.of("America/New_York", ZoneId.SHORT_IDS));
	}

	/**
	 * Sets the market close time.
	 * @param close_time The close time to set.
	 */
	public void setClose_time(ZonedDateTime close_time) {
		this.close_time = close_time;
	}

	/**
	 * Gets the name of the holiday if applicable.
	 * @return The holiday name or null.
	 */
	@javax.annotation.Nullable
	public String getHoliday_name() {
		return holiday_name;
	}

	/**
	 * Sets the holiday name.
	 * @param holiday_name The holiday name to set.
	 */
	public void setHoliday_name(String holiday_name) {
		this.holiday_name = holiday_name;
	}

	@Override
	public String toString() {
		return "TradingHours [mic=" + mic + ", date=" + date + ", day_of_week=" + day_of_week + ", is_early_close="
				+ is_early_close + ", open_time=" + open_time + ", close_time=" + close_time + ", holiday_name="
				+ holiday_name + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(close_time, date, day_of_week, holiday_name, is_early_close, mic, open_time);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TradingHours other = (TradingHours) obj;
		return Objects.equals(close_time, other.close_time) && Objects.equals(date, other.date)
				&& Objects.equals(day_of_week, other.day_of_week) && Objects.equals(holiday_name, other.holiday_name)
				&& Objects.equals(is_early_close, other.is_early_close) && Objects.equals(mic, other.mic)
				&& Objects.equals(open_time, other.open_time);
	}
}