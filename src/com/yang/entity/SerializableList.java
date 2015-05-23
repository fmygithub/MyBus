package com.yang.entity;

import java.io.Serializable;
import java.util.List;

public class SerializableList implements Serializable{
	/**
	 * @Fields serialVersionUID: TODO
	 */
	private static final long serialVersionUID = 1L;
	List<Route> list;

	public List<Route> getList() {
		return list;
	}

	public void setList(List<Route> list) {
		this.list = list;
	}
	
}
