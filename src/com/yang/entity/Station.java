package com.yang.entity;

import java.util.Set;

/**
 * 
 * ClassName: Station
 * @Description: 站点实体
 * @author: fengmengyang
 * @date: 2015-5-15
 */
public class Station {
	private Long stationId;
	/**
	 * 站点名
	 */
	private String stationName;
	/**
	 * x轴坐标值
	 */
	private String axis_x;
	/**
	 * y轴坐标值
	 */
	private String axis_y;
	/**
	 * 路线对象（多对多关系）
	 */
	/*private Set<Route> routes;*/
	/**
	 * 与中间表构成一对多关系
	 */
	private Set<RouteStation> routeStations;
	
	public Long getStationId() {
		return stationId;
	}
	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}
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
	/*public Set<Route> getRoutes() {
		return routes;
	}
	public void setRoutes(Set<Route> routes) {
		this.routes = routes;
	}*/
	public Set<RouteStation> getRouteStations() {
		return routeStations;
	}
	public void setRouteStations(Set<RouteStation> routeStations) {
		this.routeStations = routeStations;
	}
	
	
}
