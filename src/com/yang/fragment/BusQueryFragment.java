package com.yang.fragment;

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

import com.yang.app.BusInfoShowActivity;
import com.yang.entity.BusStations;
import com.yang.entity.SerializableList;
import com.yang.mybus.R;
import com.yang.thread.StationQueryByRouteNameThread;

public class BusQueryFragment extends Fragment {
	private EditText busName;
	private Button btnBus;

	// 接收子线程返回的消息
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x125) {
				SerializableList list = new SerializableList();
				BusStations stations = (BusStations) msg.obj;
				list.setStationList(stations.getStationList());
				//System.out.println(list.getStationList().size());
				// 传送页面输入信息到查询结果页面
				Bundle data = new Bundle();
				data.putSerializable("busName", stations.getBusName());
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
					StationQueryByRouteNameThread thread = new StationQueryByRouteNameThread(
							handler, busName.getText().toString());
					new Thread(thread).start();
				}
			}
		});
		return view;
	}
}
