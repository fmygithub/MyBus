package com.yang.entity;

import java.io.Serializable;

public class RouteStation implements Serializable{
	/**
	 * @Fields serialVersionUID: TODO
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	/**
	 * 站点在路线中的位置
	 */
	private Long marker;
	/**
	 * 路线实体，与中间表构成一对多
	 */
	private Route route;
	/**
	 * 站点信息，与中间表构成一对多关系
	 */
	private Station station;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getMarker() {
		return marker;
	}
	public void setMarker(Long marker) {
		this.marker = marker;
	}
	public Route getRoute() {
		return route;
	}
	public void setRoute(Route route) {
		this.route = route;
	}
	public Station getStation() {
		return station;
	}
	public void setStation(Station station) {
		this.station = station;
	}
	
}
