package com.yang.url;

/**
 * 
 * ClassName: Static
 * @Description: 保存各种静态数据，如url
 * @author: fengmengyang
 * @date: 2015年4月7日
 */
public class Static {
	//private static final String ip = "http://172.27.35.4:8080/MyBus_Server_1/";
	private static final String ip = "http://192.168.191.7:8080/MyBus_Server_1/";
	/**
	 * 登陆请求地址
	 */
	public static final String LOGIN = ip+"userAction_login.action";
	/**
	 * 注册请求地址
	 */
	public static final String REGISTER = ip + "userAction_add.action";
	/**
	 * 直达路线查询请求地址
	 */
	public static final String DIRECTROUTEQUERY = ip + "directRouteCheckAction_check.action";
	/**
	 * 一次换乘路线查询请求地址
	 */
	public static final String ONECHANGEROUTEQUERY = ip + "oneChangeRouteCheckAction_check.action";
	/**
	 * 公交查询请求地址
	 */
	public static final String BUSQUERY = ip + "busQueryAction_query.action";
	/**
	 * 站点查询请求地址
	 */
	public static final String STATIONQUERY = ip + "stationQueryAction_query.action";
}
