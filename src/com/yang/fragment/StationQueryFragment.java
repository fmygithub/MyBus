package com.yang.fragment;

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

import com.yang.app.StationInfoShowActivity;
import com.yang.entity.Route;
import com.yang.entity.SerializableList;
import com.yang.mybus.R;
import com.yang.url.Static;

/**
 * 
 * ClassName: StationQueryFragment
 * 
 * @Description: 站点查询fregment
 * @author: fengmengyang
 * @date: 2015年5月25日
 */
public class StationQueryFragment extends Fragment {
	/**
	 * 站点名
	 */
	private EditText stationName;
	/**
	 * 查询按钮
	 */
	private Button btnStation;
	/**
	 * 地图按钮
	 */
	private Button btnMap;

	// 接收子线程返回的消息
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x126) {
				SerializableList list = new SerializableList();
				list.setRouteList((List<Route>) msg.obj);
				// 传送页面输入信息到查询结果页面
				Bundle data = new Bundle();
				data.putSerializable("stationName", stationName.getText()
						.toString());
				data.putSerializable("routeList", list);
				Intent intent = new Intent(getActivity(),
						StationInfoShowActivity.class);
				intent.putExtras(data);
				startActivity(intent);
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle saveInstanceState) {
		View view = inflater.inflate(R.layout.fragment_stationquery, null);
		// 初始化元素
		stationName = (EditText) view.findViewById(R.id.station_name);
		btnStation = (Button) view.findViewById(R.id.btn_station);
		btnMap = (Button) view.findViewById(R.id.btn_map);

		btnStation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!stationName.getText().equals("")) {
					StationQueryThread thread = new StationQueryThread();
					new Thread(thread).start();
				}
			}
		});

		btnMap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		return view;
	}

	// 处理请求的线程类
	class StationQueryThread extends Thread {
		HttpClient httpClient = new DefaultHttpClient();

		@Override
		public void run() {
			List<Route> routeList = post(stationName.getText().toString());

			Message msg = new Message();
			msg.what = 0x126;
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
		private List<Route> post(String stationName) {
			try {
				System.out.println(stationName);
				HttpPost post = new HttpPost(Static.STATIONQUERY);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("station.stationName", stationName));
				post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
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
			/* {"json":[{"routeId":4,"routeName":"638","routeStations":[]}]} */
			// 保存接收的数据
			List<Route> routeList = new ArrayList<Route>();
			try {
				JSONArray arr = new JSONObject(json).getJSONArray("json");
				// 判断arr为空
				if (arr.length() == 1 && arr.get(0).toString().equals("null")) {
					return null;
				} else {
					for (int i = 0; i < arr.length(); i++) {
						Route route = new Route();
						route.setRouteId(arr.getJSONObject(i)
								.getLong("routeId"));
						route.setRouteName(arr.getJSONObject(i).getString(
								"routeName"));

						// 将route对象放入list中
						routeList.add(route);
					}
				}
			} catch (Exception e) {

			}

			return routeList;
		}
	}
}
