package com.yang.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class SerializableMap implements Serializable{

	/**
	 * @Fields serialVersionUID: TODO
	 */
	private static final long serialVersionUID = 1L;
	private Map<String,List<Line>> lineMap;
	public Map<String, List<Line>> getLineMap() {
		return lineMap;
	}
	public void setLineMap(Map<String, List<Line>> lineMap) {
		this.lineMap = lineMap;
	}

}
