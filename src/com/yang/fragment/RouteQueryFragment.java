package com.yang.fragment;

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

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.yang.app.RouteShowActivity;
import com.yang.entity.Route;
import com.yang.entity.SerializableList;
import com.yang.entity.Station;
import com.yang.mybus.R;
import com.yang.url.Static;

public class RouteQueryFragment extends Fragment {
	private EditText begin_station;
	private EditText end_station;
	private Button btn_query;

	// 接收子线程返回的消息
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x124) {
				SerializableList list = new SerializableList();
				list.setList((List<Route>) msg.obj);
				// 传送页面输入信息到查询结果页面
				Bundle data = new Bundle();
				data.putSerializable("begin_station", begin_station.getText().toString());
				data.putSerializable("end_station", end_station.getText().toString());
				data.putSerializable("routeList", list);
				Intent intent = new Intent(getActivity(),
						RouteShowActivity.class);
				intent.putExtras(data);
				startActivity(intent);
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle saveInstanceState) {
		View view = inflater.inflate(R.layout.fragment_routequery, null);
		begin_station = (EditText) view.findViewById(R.id.begin_name);
		end_station = (EditText) view.findViewById(R.id.end_name);
		btn_query = (Button) view.findViewById(R.id.btn_check);
		btn_query.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!begin_station.getText().toString().equals("")
						&& !end_station.getText().toString().equals("")) {
					RouteQueryThread thread = new RouteQueryThread();
					new Thread(thread).start();
				}

			}
		});
		return view;
	}

	// 处理请求的线程类
	class RouteQueryThread extends Thread {
		HttpClient httpClient = new DefaultHttpClient();

		@Override
		public void run() {
			List<Route> routeList = post(begin_station.getText().toString(),
					end_station.getText().toString());

			Message msg = new Message();
			msg.what = 0x124;
			msg.obj = routeList;
			handler.sendMessage(msg);
		}

		/**
		 * 
		 * @Description: 处理和发送请求
		 * @return List<Route>
		 * @author: fengmengyang
		 * @date: 2015年5月23日
		 */
		private List<Route> post(String begin_station, String end_station) {
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

					return getRouteList(json);
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
		public List<Route> getRouteList(String json) {
			// 保存接收的数据
			List<Route> routeList = new ArrayList<Route>();
			try {
				JSONArray arr = new JSONObject(json).getJSONArray("json");
				for (int i = 0; i < arr.length(); i++) {
					Route route = new Route();
					// 存储stationCount
					int stationCount = arr.getJSONObject(i)
							.getJSONObject("key")
							.getJSONObject("directRouteViewId")
							.getInt("stationCount");
					route.setStationCount(stationCount);

					// 存储站点信息stationList
					List<Station> stationList = new ArrayList<Station>();
					JSONArray stationArr = arr.getJSONObject(i).getJSONArray(
							"value");
					for (int j = 0; j < stationArr.length(); j++) {
						Station station = new Station();
						JSONObject stationJSON = stationArr.getJSONObject(j)
								.getJSONObject("station");
						station.setStationName(stationJSON
								.getString("stationName"));
						station.setAxis_x(stationJSON.getString("axis_x"));
						station.setAxis_y(stationJSON.getString("axis_y"));

						stationList.add(station);
					}
					route.setStationList(stationList);

					// 将route对象放入list中
					routeList.add(route);
				}
			} catch (Exception e) {
				
			}

			return routeList;
		}
	}
}
