package com.stockmarketpotato.main;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class DataPoint implements Serializable {
	protected final static String TIME_ZONE = "America/New_York";
	protected final static ZoneId ZONE_ID_MARKET = ZoneId.of("America/New_York", ZoneId.SHORT_IDS);

	@JsonIgnore
	public LocalDate getDate() {
		return Instant.ofEpochMilli(this.X).atZone(ZONE_ID_MARKET).toLocalDate();
	}
	
	private long X;
	private Double Y;

	DataPoint(long x, Double y) {
		this.X = x;
		this.Y = y;
	}

	public long getX() {
		return X;
	}

	public void setX(long x) {
		X = x;
	}

	public Double getY() {
		return Y;
	}

	public void setY(Double y) {
		Y = y; 	
	}
}
