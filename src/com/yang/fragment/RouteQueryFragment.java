package com.yang.fragment;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.yang.app.RouteShowActivity;
import com.yang.mybus.R;
import com.yang.url.Static;
import com.yang.utils.HandleJson;

public class RouteQueryFragment extends Fragment {
	private EditText begin_station;
	private EditText end_station;
	private Button btn_query;

	// 接收子线程返回的消息
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x124) {

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
				// 传送页面输入信息到查询结果页面
				Bundle data = new Bundle();
				data.putSerializable("begin_name", begin_station.getText()
						.toString());
				data.putSerializable("end_name", end_station.getText()
						.toString());
				Intent intent = new Intent(getActivity(),
						RouteShowActivity.class);
				intent.putExtras(data);
				startActivity(intent);
			}
		});
		return view;
	}

	// 处理请求的线程类
	class RouteQueryThread extends Thread {
		HttpClient httpClient = new DefaultHttpClient();

		@Override
		public void run() {
			post(begin_station.getText().toString(), end_station.getText()
					.toString());
		}

		private String post(String begin_station, String end_station) {
			HttpPost post = new HttpPost(Static.ROUTEQUERY
					+ "?directRouteViewId.beginStationId=" + begin_station
					+ "&directRouteViewId.endStationId=" + end_station);

			try {
				// 发送post请求
				HttpResponse response = httpClient.execute(post);
				// 如果服务器成功返回相应
				System.out.println(response.getStatusLine().getStatusCode());
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					return HandleJson.parse_json(response).getString("message");
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}
