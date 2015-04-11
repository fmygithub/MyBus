package com.yang.utils;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * ClassName: HandleJson
 * @Description: 处理json的工具类
 * @author: fengmengyang
 * @date: 2015年4月6日
 */
public class HandleJson {
	
	/**
	 * 
	 * @Description: 将服务器返回的json对象解析
	 * @param @param response
	 * @param @return
	 * @return JSONObject
	 * @throws
	 * @author: fengmengyang
	 * @date: 2015年4月7日
	 */
	public static JSONObject parse_json(HttpResponse response){
		String temp;
		try {
			temp = EntityUtils.toString(response.getEntity());
			JSONObject json = new JSONObject(temp).getJSONObject("json");
			return json;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static JSONObject package_json(String... params){
		JSONObject json = new JSONObject();
		
		return json;
	}
}
