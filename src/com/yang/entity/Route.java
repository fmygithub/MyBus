package com.yang.entity;

import java.util.Set;

/**
 * 
 * ClassName: Route
 * @Description: 路线实体
 * @author: fengmengyang
 * @date: 2015-5-15
 */
public class Route {
	private Long routeId;
	/**
	 * 路线名
	 */
	private String routeName;
	/**
	 * 站点对象（多对多关系）
	 *//*
	private Set<Station> stations;*/
	
	/**
	 * 与中间表构成一对多关系
	 */
	private Set<RouteStation> routeStations;
	
	public Long getRouteId() {
		return routeId;
	}
	public void setRouteId(Long routeId) {
		this.routeId = routeId;
	}
	public String getRouteName() {
		return routeName;
	}
	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}
	/*public Set<Station> getStations() {
		return stations;
	}
	public void setStations(Set<Station> stations) {
		this.stations = stations;
	}*/
	public Set<RouteStation> getRouteStations() {
		return routeStations;
	}
	public void setRouteStations(Set<RouteStation> routeStations) {
		this.routeStations = routeStations;
	}
	
}
