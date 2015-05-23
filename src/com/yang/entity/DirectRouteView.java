package com.yang.entity;

import java.io.Serializable;

/**
 * 
 * ClassName: DirectRoute
 * @Description: 直达路线视图对应的实体
 * @author: fengmengyang
 * @date: 2015-5-18
 */
public class DirectRouteView implements Serializable{

	/**
	 * @Fields serialVersionUID: TODO
	 */
	private static final long serialVersionUID = 1L;
	private DirectRouteViewId directRouteViewId;
	public DirectRouteViewId getDirectRouteViewId() {
		return directRouteViewId;
	}
	public void setDirectRouteViewId(DirectRouteViewId directRouteViewId) {
		this.directRouteViewId = directRouteViewId;
	}
	
	
}
