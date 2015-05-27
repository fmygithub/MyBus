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
	 * 换乘路线的bus列表
	 */
	private List<Bus> busList;
	/**
	 * 经过的所有站点
	 */
	private List<Station> stationList;
	public List<Bus> getBusList() {
		return busList;
	}
	public void setBusList(List<Bus> busList) {
		this.busList = busList;
	}
	public List<Station> getStationList() {
		return stationList;
	}
	public void setStationList(List<Station> stationList) {
		this.stationList = stationList;
	}
	
}
