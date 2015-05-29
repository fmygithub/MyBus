package com.yang.entity;

import java.io.Serializable;

public class Station implements Serializable{
	/**
	 * @Fields serialVersionUID: TODO
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 站名
	 */
	private String stationName;
	/**
	 * 经度
	 */
	private String axis_x;
	/**
	 * 纬度
	 */
	private String axis_y;
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public String getAxis_x() {
		return axis_x;
	}
	public void setAxis_x(String axis_x) {
		this.axis_x = axis_x;
	}
	public String getAxis_y() {
		return axis_y;
	}
	public void setAxis_y(String axis_y) {
		this.axis_y = axis_y;
	}
	
}
