package com.yang.entity;

import java.io.Serializable;
import java.util.List;

public class StationBuses implements Serializable{
	/**
	 * @Fields serialVersionUID: TODO
	 */
	private static final long serialVersionUID = 1L;
	private String stationName;
	private List<Bus> busList;
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public List<Bus> getBusList() {
		return busList;
	}
	public void setBusList(List<Bus> busList) {
		this.busList = busList;
	}
	
}
