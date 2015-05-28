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
import com.yang.entity.BusStations;
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
		// 发送直达路线查询请求
		List<Line> directLineList = post(Static.DIRECTROUTEQUERY, beginStationName,
				endStationName);
		// 发送一次换乘路线查询请求
		List<Line> oneChangeLineList = post(Static.ONECHANGEROUTEQUERY,
				beginStationName, endStationName);
		
		//整合路线
		List<Line> lineList = new ArrayList<Line>();
		for (Line line : directLineList) {
			lineList.add(line);
		}
		for (Line line : oneChangeLineList) {
			lineList.add(line);
		}
		/*Map<String,List<Line>> lineMap =  new HashMap<String, List<Line>>();
		lineMap.put("directLine", directLineList);
		lineMap.put("oneChangeLine", oneChangeLineList);*/

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
	private List<Line> post(String require, String beginStationName,
			String endStationName) {

		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost post = new HttpPost(require);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("id.beginStationName",
					beginStationName));
			params.add(new BasicNameValuePair("id.endStationName",
					endStationName));
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

			// 发送post请求
			HttpResponse response = httpClient.execute(post);
			// 如果服务器成功返回相应
			System.out.println(response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String json = EntityUtils.toString(response.getEntity(),
						"utf-8");
				if (require.equals(Static.DIRECTROUTEQUERY)) {
					return getDirectLineList(json);
				} else if (require.equals(Static.ONECHANGEROUTEQUERY)) {
					return getOneChangeLineList(json);
				}
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
	public List<Line> getDirectLineList(String json) {
		/*
		 * {"key":
		 * {"id":{"beginMarker":1,"beginStationId":7,"beginStationName":"宝德学院"
		 * ,"endMarker"
		 * :3,"endStationId":11,"endStationName":"后台村","routeId":4,"routeName"
		 * :"638","stationCount":2}},
		 * 
		 * 
		 * "value":[
		 * {"axis_x":"117.099194","axis_y":"39.094998","routeStations":
		 * [],"stationId":7,"stationName":"宝德学院"},
		 * {"axis_x":"117.101839","axis_y"
		 * :"39.095058","routeStations":[],"stationId"
		 * :10,"stationName":"天津城建大学"},
		 * {"axis_x":"117.12339","axis_y":"39.105271"
		 * ,"routeStations":[],"stationId":11,"stationName":"后台村"} ]
		 */
		// 保存接收的数据
		List<Line> lineList = new ArrayList<Line>();
		try {
			JSONArray arr = new JSONObject(json).getJSONArray("json");
			for (int i = 0; i < arr.length(); i++) {
				Line line = new Line();

				// 存储换乘路线
				JSONObject direct = arr.getJSONObject(i).getJSONObject("key")
						.getJSONObject("id");
				Station beginStation = new Station();
				Station endStation = new Station();
				beginStation.setStationName(direct
						.getString("beginStationName"));
				endStation.setStationName(direct.getString("endStationName"));
				line.setBeginStation(beginStation);
				line.setEndStation(endStation);
				line.setStationCount(direct.getInt("stationCount"));

				Bus bus = new Bus();
				bus.setBusName(direct.getString("routeName"));
				line.setBus(bus);

				// 存储站点信息stationList
				JSONArray stationArr = arr.getJSONObject(i).getJSONArray(
						"value");
				List<BusStations> busStationsList = new ArrayList<BusStations>();
				BusStations stations = new BusStations();
				List<Station> stationList = new ArrayList<Station>();
				for (int j = 0; j < stationArr.length(); j++) {
					Station station = new Station();
					JSONObject stationJSON = stationArr.getJSONObject(j);
					station.setStationName(stationJSON.getString("stationName"));
					station.setAxis_x(stationJSON.getString("axis_x"));
					station.setAxis_y(stationJSON.getString("axis_y"));

					stationList.add(station);
				}
				stations.setBusName(bus.getBusName());
				stations.setStationList(stationList);
				busStationsList.add(stations);

				line.setBusStationsList(busStationsList);

				// 将route对象放入list中
				lineList.add(line);
			}
		} catch (Exception e) {

		}

		return lineList;
	}

	public List<Line> getOneChangeLineList(String json) {
		/*
		 * "key":{ "id":{
		 * "beginMarker":1,"beginStationId":7,"beginStationName":"宝德学院",
		 * "endMarker":3,"endStationId":11,"endStationName":"后台村",
		 * "routeId1":4,"routeId2"
		 * :5,"routeName1":"638","routeName2":"700","staionCount"
		 * :0,"stationCount1":1,
		 * "stationCount2":1,"tranferMarker":2,"tranferStationId":10,
		 * "tranferStationName":"天津城建大学" } },
		 */

		// 保存接收的数据
		List<Line> lineList = new ArrayList<Line>();
		try {
			JSONArray arr = new JSONObject(json).getJSONArray("json");
			for (int i = 0; i < arr.length(); i++) {
				Line line = new Line();

				// 存储换乘路线
				JSONObject change = arr.getJSONObject(i).getJSONObject("key")
						.getJSONObject("id");
				Station beginStation = new Station();
				Station endStation = new Station();
				Station changeStation = new Station();
				beginStation.setStationName(change
						.getString("beginStationName"));
				endStation.setStationName(change.getString("endStationName"));
				changeStation.setStationName(change
						.getString("tranferStationName"));
				line.setBeginStation(beginStation);
				line.setEndStation(endStation);
				line.setChangeStation(changeStation);
				
				line.setStationCount(change.getInt("stationCount1") + change.getInt("stationCount2"));
				
				Bus bus1 = new Bus();
				Bus bus2 = new Bus();
				bus1.setBusName(change.getString("routeName1"));
				bus2.setBusName(change.getString("routeName2"));

				/*
				 * "value": { "700":[
				 * {"axis_x":"117.101839","axis_y":"39.095058"
				 * ,"routeStations":[],"stationId":10,"stationName":"天津城建大学"},
				 * {"axis_x"
				 * :"117.12339","axis_y":"39.105271","routeStations":[],
				 * "stationId":11,"stationName":"后台村"} ], "638":[
				 * {"axis_x":"117.099194"
				 * ,"axis_y":"39.094998","routeStations":[]
				 * ,"stationId":7,"stationName":"宝德学院"},
				 * {"axis_x":"117.101839","axis_y"
				 * :"39.095058","routeStations":[]
				 * ,"stationId":10,"stationName":"天津城建大学"} ] }
				 */
				// 存储站点信息stationList
				JSONObject busStations = arr.getJSONObject(i).getJSONObject(
						"value");

				List<BusStations> busStationsList = new ArrayList<BusStations>();

				BusStations stations1 = new BusStations();
				List<Station> stationList1 = new ArrayList<Station>();
				JSONArray stationArr1 = busStations.getJSONArray(bus1
						.getBusName());
				for (int j = 0; j < stationArr1.length(); j++) {
					Station station = new Station();
					JSONObject stationJSON = stationArr1.getJSONObject(j);
					station.setStationName(stationJSON.getString("stationName"));
					station.setAxis_x(stationJSON.getString("axis_x"));
					station.setAxis_y(stationJSON.getString("axis_y"));

					stationList1.add(station);
				}
				stations1.setBusName(bus1.getBusName());
				stations1.setStationList(stationList1);
				System.out.println("stations1:" + stations1.getStationList().size());
				busStationsList.add(stations1);

				BusStations stations2 = new BusStations();
				List<Station> stationList2 = new ArrayList<Station>();
				JSONArray stationArr2 = busStations.getJSONArray(bus1
						.getBusName());
				for (int j = 0; j < stationArr2.length(); j++) {
					Station station = new Station();
					JSONObject stationJSON = stationArr2.getJSONObject(j);
					station.setStationName(stationJSON.getString("stationName"));
					station.setAxis_x(stationJSON.getString("axis_x"));
					station.setAxis_y(stationJSON.getString("axis_y"));

					stationList2.add(station);
				}
				stations2.setBusName(bus2.getBusName());
				stations2.setStationList(stationList2);
				System.out.println("stations2" + stations2.getStationList().size());
				busStationsList.add(stations2);

				line.setBusStationsList(busStationsList);
				// 将route对象放入list中
				lineList.add(line);
			}
		} catch (Exception e) {

		}
		return lineList;
	}
}