package com.yang.entity;

import java.io.Serializable;

public class OneTranferRouteViewId implements Serializable{

	/**
	 * @Fields serialVersionUID: TODO
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 起始站点
	 */
	private Long beginStationId;
	/**
	 * 起始站点在换乘路线1中的位置
	 */
	private Long beginMarker;
	/**
	 * 换乘路线1
	 */
	private Long routeId1;
	/**
	 * 换乘站点
	 */
	private Long tranferStationId;
	/**
	 * 换乘站点在路线1中的位置
	 */
	private Long tranferMarker;
	/**
	 * 换乘路线2
	 */
	private Long routeId2;
	/**
	 * 目标站点
	 */
	private Long endStationId;
	/**
	 * 目标站点在路线2中的位置
	 */
	private Long endMarker;
	/**
	 * 从起点到终点的所经过的站点数
	 */
	private Integer staionCount;
	public Long getBeginStationId() {
		return beginStationId;
	}
	public void setBeginStationId(Long beginStationId) {
		this.beginStationId = beginStationId;
	}
	public Long getBeginMarker() {
		return beginMarker;
	}
	public void setBeginMarker(Long beginMarker) {
		this.beginMarker = beginMarker;
	}
	public Long getRouteId1() {
		return routeId1;
	}
	public void setRouteId1(Long routeId1) {
		this.routeId1 = routeId1;
	}
	public Long getTranferStationId() {
		return tranferStationId;
	}
	public void setTranferStationId(Long tranferStationId) {
		this.tranferStationId = tranferStationId;
	}
	public Long getTranferMarker() {
		return tranferMarker;
	}
	public void setTranferMarker(Long tranferMarker) {
		this.tranferMarker = tranferMarker;
	}
	public Long getRouteId2() {
		return routeId2;
	}
	public void setRouteId2(Long routeId2) {
		this.routeId2 = routeId2;
	}
	public Long getEndStationId() {
		return endStationId;
	}
	public void setEndStationId(Long endStationId) {
		this.endStationId = endStationId;
	}
	public Long getEndMarker() {
		return endMarker;
	}
	public void setEndMarker(Long endMarker) {
		this.endMarker = endMarker;
	}
	public Integer getStaionCount() {
		return staionCount;
	}
	public void setStaionCount(Integer staionCount) {
		this.staionCount = staionCount;
	}
}
