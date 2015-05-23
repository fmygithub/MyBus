package com.yang.entity;

import java.io.Serializable;
import java.util.List;

public class Route implements Serializable{
	/**
	 * @Fields serialVersionUID: TODO
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 查询出来始末站点之间的站数
	 */
	private int stationCount;
	/**
	 * 始末站点之间的站点
	 */
	private List<Station> stationList;
	public int getStationCount() {
		return stationCount;
	}
	public void setStationCount(int stationCount) {
		this.stationCount = stationCount;
	}
	public List<Station> getStationList() {
		return stationList;
	}
	public void setStationList(List<Station> stationList) {
		this.stationList = stationList;
	}
	
}
