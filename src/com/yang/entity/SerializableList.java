package com.yang.entity;

import java.io.Serializable;
import java.util.List;

public class SerializableList implements Serializable{
	/**
	 * @Fields serialVersionUID: TODO
	 */
	private static final long serialVersionUID = 1L;
	private List<Route> routeList;
	private List<Station> stationList;
	public List<Route> getRouteList() {
		return routeList;
	}
	public void setRouteList(List<Route> routeList) {
		this.routeList = routeList;
	}
	public List<Station> getStationList() {
		return stationList;
	}
	public void setStationList(List<Station> stationList) {
		this.stationList = stationList;
	}

	
	
}
