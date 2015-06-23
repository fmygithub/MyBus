package com.yang.entity;

import java.io.Serializable;
import java.util.List;

public class StationBuses implements Serializable{
	/**
	 * @Fields serialVersionUID: TODO
	 */
	private static final long serialVersionUID = 1L;
	private Station station;
	private List<Bus> busList;
	public Station getStation() {
		return station;
	}
	public void setStation(Station station) {
		this.station = station;
	}
	public List<Bus> getBusList() {
		return busList;
	}
	public void setBusList(List<Bus> busList) {
		this.busList = busList;
	}
	
}
