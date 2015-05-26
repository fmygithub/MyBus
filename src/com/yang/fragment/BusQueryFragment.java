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

import com.yang.app.BusInfoShowActivity;
import com.yang.app.RouteShowActivity;
import com.yang.entity.SerializableList;
import com.yang.entity.Station;
import com.yang.mybus.R;
import com.yang.url.Static;

public class BusQueryFragment extends Fragment {
	private EditText busName;
	private Button btnBus;

	// 接收子线程返回的消息
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x125) {
				SerializableList list = new SerializableList();
				list.setStationList((List<Station>) msg.obj);
				System.out.println(list.getStationList().size());
				// 传送页面输入信息到查询结果页面
				Bundle data = new Bundle();
				data.putSerializable("busName", busName.getText().toString());
				data.putSerializable("stationList", list);
				Intent intent = new Intent(getActivity(),
						BusInfoShowActivity.class);
				intent.putExtras(data);
				startActivity(intent);
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle saveInstanceState) {
		View view = inflater.inflate(R.layout.fragment_busquery, null);
		// 初始化页面元素
		busName = (EditText) view.findViewById(R.id.bus_name);
		btnBus = (Button) view.findViewById(R.id.btn_bus);
		btnBus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!busName.getText().equals("")) {
					StationQueryThread thread = new StationQueryThread();
					new Thread(thread).start();
				}
			}
		});
		return view;
	}

	// 处理请求的线程类
	class StationQueryThread extends Thread {
		HttpClient httpClient = new DefaultHttpClient();

		@Override
		public void run() {
			List<Station> stationList = post(busName.getText().toString());

			Message msg = new Message();
			msg.what = 0x125;
			msg.obj = stationList;
			handler.sendMessage(msg);
		}

		/**
		 * 
		 * @Description: 处理和发送请求
		 * @return List<Route>
		 * @author: fengmengyang
		 * @date: 2015年5月23日
		 */
		private List<Station> post(String busName) {
			HttpPost post = new HttpPost(Static.BUSQUERY
					+ "?route.routeName=" + busName);

			try {
				// 发送post请求
				HttpResponse response = httpClient.execute(post);
				// 如果服务器成功返回相应
				System.out.println(response.getStatusLine().getStatusCode());
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					String json = EntityUtils.toString(response.getEntity(),
							"utf-8");

					return getStationList(json);
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
						station.setAxis_x(arr.getJSONObject(i).getString(
								"axis_x"));
						station.setAxis_y(arr.getJSONObject(i).getString(
								"axis_y"));

						// 将station对象放入list中
						stationList.add(station);
					}
				}
			} catch (Exception e) {

			}

			return stationList;
		}
	}
}
