package com.yang.thread;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;

import com.yang.entity.Bus;
import com.yang.entity.StationBuses;
import com.yang.url.Static;

/**
 * 
 * ClassName: BusQueryByStationNameThread
 * 
 * @Description: 通过站点名查询所有路线的线程
 * @author: fengmengyang
 * @date: 2015年5月26日
 */
public class BusQueryByStationNameThread extends Thread {
	private Handler handler;
	private String stationName;

	public BusQueryByStationNameThread(Handler handler, String stationName) {
		this.handler = handler;
		this.stationName = stationName;
	}

	HttpClient httpClient = new DefaultHttpClient();

	@Override
	public void run() {
		StationBuses buses = post(stationName);

		Message msg = new Message();
		msg.what = 0x126;
		msg.obj = buses;
		handler.sendMessage(msg);
	}

	/**
	 * 
	 * @Description: 处理和发送请求
	 * @return Map<String,List<Route>>
	 * @author: fengmengyang
	 * @date: 2015年5月23日
	 */
	private StationBuses post(String stationName) {
		try {
			HttpPost post = new HttpPost(Static.STATIONQUERY);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("station.stationName",
					stationName));
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			// 发送post请求
			HttpResponse response = httpClient.execute(post);
			// 如果服务器成功返回相应
			System.out.println(response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String json = EntityUtils.toString(response.getEntity(),
						"utf-8");
				List<Bus> busList = getRouteList(json);
				StationBuses buses = new StationBuses();
				buses.setStationName(stationName);
				buses.setBusList(busList);
				return buses;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @Description: 保存获取的route信息
	 * @return List<Route>
	 * @author: fengmengyang
	 * @date: 2015年5月23日
	 */
	public List<Bus> getRouteList(String json) {
		// 保存接收的数据
		List<Bus> busList = new ArrayList<Bus>();
		try {
			JSONArray arr = new JSONObject(json).getJSONArray("json");
			// 判断arr为空
			if (arr.length() == 1 && arr.get(0).toString().equals("null")) {
				return null;
			} else {
				for (int i = 0; i < arr.length(); i++) {
					Bus bus = new Bus();
					bus.setBusId(arr.getJSONObject(i).getLong("routeId"));
					bus.setBusName(arr.getJSONObject(i).getString(
							"routeName"));

					// 将route对象放入list中
					busList.add(bus);
				}
			}
		} catch (Exception e) {

		}

		return busList;
	}
}
