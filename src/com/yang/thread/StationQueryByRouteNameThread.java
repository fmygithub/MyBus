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

import com.yang.entity.BusStations;
import com.yang.entity.Station;
import com.yang.url.Static;

public class StationQueryByRouteNameThread extends Thread {
	private Handler handler;
	private String busName;

	public StationQueryByRouteNameThread(Handler handler, String busName) {
		this.handler = handler;
		this.busName = busName;
	}

	HttpClient httpClient = new DefaultHttpClient();

	@Override
	public void run() {
		BusStations stations = post(busName);

		Message msg = new Message();
		msg.what = 0x125;
		msg.obj = stations;
		handler.sendMessage(msg);
	}

	/**
	 * 
	 * @Description: 处理和发送请求
	 * @return List<Route>
	 * @author: fengmengyang
	 * @date: 2015年5月23日
	 */
	private BusStations post(String busName) {
		try {
			HttpPost post = new HttpPost(Static.BUSQUERY);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("route.routeName", busName));
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

			// 发送post请求
			HttpResponse response = httpClient.execute(post);
			// 如果服务器成功返回相应
			System.out.println(response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String json = EntityUtils.toString(response.getEntity(),
						"utf-8");
				
				BusStations stations = new BusStations();
				stations.setBusName(busName);
				stations.setStationList(getStationList(json));
				return stations;
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
	 * @Description: 保存当前路线的所有站点
	 * @return List<Station>
	 * @author: fengmengyang
	 * @date: 2015年5月23日
	 */
	public List<Station> getStationList(String json) {
		/*
		 * {"json":[{"axis_x":"117.099194","axis_y":"39.094998","routeStations"
		 * :[],"stationId":7,"stationName":"宝德学院"}
		 */
		// 保存接收的数据
		List<Station> stationList = new ArrayList<Station>();
		try {
			JSONArray arr = new JSONObject(json).getJSONArray("json");
			// 判断arr为空
			if (arr.length() == 1 && arr.get(0).toString().equals("null")) {
				return null;
			} else {
				for (int i = 0; i < arr.length(); i++) {
					Station station = new Station();
					station.setStationName(arr.getJSONObject(i).getString(
							"stationName"));
					station.setAxis_x(arr.getJSONObject(i).getString("axis_x"));
					station.setAxis_y(arr.getJSONObject(i).getString("axis_y"));

					// 将station对象放入list中
					stationList.add(station);
				}
			}
		} catch (Exception e) {

		}

		return stationList;
	}
}
