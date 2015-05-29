package com.yang.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * ClassName: RouteStations
 * @Description: 一个路线对应多个站点对象
 * @author: fengmengyang
 * @date: 2015年5月27日
 */
public class BusStations implements Serializable{
	/**
	 * @Fields serialVersionUID: TODO
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 路线名
	 */
	private String busName;
	/**
	 * 路线对应的站点列表
	 */
	private List<Station> stationList;
	public String getBusName() {
		return busName;
	}
	public void setBusName(String busName) {
		this.busName = busName;
	}
	public List<Station> getStationList() {
		return stationList;
	}
	public void setStationList(List<Station> stationList) {
		this.stationList = stationList;
	}
	
}
