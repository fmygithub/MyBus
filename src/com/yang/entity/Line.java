package com.yang.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * ClassName: Line
 * @Description: 换乘用的实体类
 * @author: fengmengyang
 * @date: 2015年5月27日
 */
public class Line implements Serializable{

	/**
	 * @Fields serialVersionUID: TODO
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 起点
	 */
	private Station beginStation;
	/**
	 * 目的站点
	 */
	private Station endStation;
	/**
	 * 换乘点
	 */
	private Station changeStation;
	/**
	 * 未换乘公交
	 */
	private Bus bus;
	/**
	 * 第一次换乘公交
	 */
	private Bus bus1;
	/**
	 * 换乘中路线经过的所有站点
	 */
	private List<BusStations> busStationsList;
	private int stationCount;
	
	public Station getBeginStation() {
		return beginStation;
	}
	public void setBeginStation(Station beginStation) {
		this.beginStation = beginStation;
	}
	public Station getEndStation() {
		return endStation;
	}
	public void setEndStation(Station endStation) {
		this.endStation = endStation;
	}
	public Station getChangeStation() {
		return changeStation;
	}
	public void setChangeStation(Station changeStation) {
		this.changeStation = changeStation;
	}
	
	public Bus getBus() {
		return bus;
	}
	public void setBus(Bus bus) {
		this.bus = bus;
	}
	public Bus getBus1() {
		return bus1;
	}
	public void setBus1(Bus bus1) {
		this.bus1 = bus1;
	}
	public List<BusStations> getBusStationsList() {
		return busStationsList;
	}
	public void setBusStationsList(List<BusStations> busStationsList) {
		this.busStationsList = busStationsList;
	}
	public int getStationCount() {
		return stationCount;
	}
	public void setStationCount(int stationCount) {
		this.stationCount = stationCount;
	}
	
	
}
