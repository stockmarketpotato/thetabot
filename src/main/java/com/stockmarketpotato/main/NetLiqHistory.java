package com.stockmarketpotato.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class NetLiqHistory {
	@Id
	private String id;
	
	@JdbcTypeCode(SqlTypes.JSON)
	private TreeMap<LocalDate, DataPoint> history;
	
	public NetLiqHistory() {
		this("");
	}
	
	public NetLiqHistory(String id) {
		this.id = id;
		history = new TreeMap<LocalDate, DataPoint>();
	}
	
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
	
	public TreeMap<LocalDate, DataPoint> getHistory() {
		return history;
	}
	
	public List<DataPoint> getDataPoints() {
		return new ArrayList<DataPoint>(history.values());
	}
	
	public void setHistory(TreeMap<LocalDate, DataPoint> history) {
		this.history = history;
	}	
	
	public void add(DataPoint dataPoint) {
		
		this.history.put(dataPoint.getDate(), dataPoint);
	}
	
	public void addAll(List<DataPoint> dataPointList) {
		dataPointList.forEach(dataPoint -> {
			this.history.put(dataPoint.getDate(), dataPoint);	
		});		
	}
}
