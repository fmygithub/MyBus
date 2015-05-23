package com.yang.entity;

import java.io.Serializable;

/**
 * 
 * ClassName: OneTranferRouteView
 * @Description: 一次换乘路线的视图实体
 * @author: fengmengyang
 * @date: 2015-5-19
 */
public class OneTranferRouteView implements Serializable{

	/**
	 * @Fields serialVersionUID: TODO
	 */
	private static final long serialVersionUID = 1L;
	private OneTranferRouteViewId id;
	public OneTranferRouteViewId getId() {
		return id;
	}
	public void setId(OneTranferRouteViewId id) {
		this.id = id;
	}
	
}
