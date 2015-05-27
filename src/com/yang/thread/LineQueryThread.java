package com.yang.thread;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;

import com.yang.entity.Bus;
import com.yang.entity.Line;
import com.yang.entity.Station;
import com.yang.url.Static;

//处理请求的线程类
public class LineQueryThread extends Thread {
	private Handler handler;
	private String beginStationName;
	private String endStationName;

	public LineQueryThread(Handler handler, String beginStationName,
			String endStationName) {
		this.handler = handler;
		this.beginStationName = beginStationName;
		this.endStationName = endStationName;
	}

	@Override
	public void run() {
		List<Line> lineList = post(beginStationName, endStationName);

		Message msg = new Message();
		msg.what = 0x124;
		msg.obj = lineList;
		handler.sendMessage(msg);
	}

	/**
	 * 
	 * @Description: 处理和发送请求
	 * @return List<Route>
	 * @author: fengmengyang
	 * @date: 2015年5月23日
	 */
	private List<Line> post(String begin_station, String end_station) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(Static.ROUTEQUERY
				+ "?directRouteViewId.beginStationId=" + begin_station
				+ "&directRouteViewId.endStationId=" + end_station);

		try {
			// 发送post请求
			HttpResponse response = httpClient.execute(post);
			// 如果服务器成功返回相应
			System.out.println(response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String json = EntityUtils.toString(response.getEntity(),
						"utf-8");

				return getLineList(json);
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
	public List<Line> getLineList(String json) {
		// 保存接收的数据
		List<Line> lineList = new ArrayList<Line>();
		try {
			JSONArray arr = new JSONObject(json).getJSONArray("json");
			for (int i = 0; i < arr.length(); i++) {
				Line line = new Line();
				// 存储stationCount
				int stationCount = arr.getJSONObject(i).getJSONObject("key")
						.getJSONObject("directRouteViewId")
						.getInt("stationCount");
				// line.setStationCount(stationCount);

				// 存储换乘路线
				List<Bus> busList = new ArrayList<Bus>();
				JSONArray routeArr = arr.getJSONObject(i).getJSONArray(
						"routeNameList");
				for (int j = 0; j < routeArr.length(); j++) {
					Bus bus = new Bus();
					bus.setBusName((String) routeArr.get(i));
					busList.add(bus);
				}
				line.setBusList(busList);

				// 存储站点信息stationList
				JSONArray stationArr = arr.getJSONObject(i).getJSONArray(
						"value");
				List<Station> stationList = new ArrayList<Station>();
				for (int j = 0; j < stationArr.length(); j++) {
					Station station = new Station();
					JSONObject stationJSON = stationArr.getJSONObject(j)
							.getJSONObject("station");
					station.setStationName(stationJSON.getString("stationName"));
					station.setAxis_x(stationJSON.getString("axis_x"));
					station.setAxis_y(stationJSON.getString("axis_y"));

					stationList.add(station);
				}
				line.setStationList(stationList);

				// 将route对象放入list中
				lineList.add(line);
			}
		} catch (Exception e) {

		}

		return lineList;
	}
}