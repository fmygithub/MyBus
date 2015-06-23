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

import com.yang.app.StationInfoShowActivity;
import com.yang.entity.SerializableList;
import com.yang.entity.StationBuses;
import com.yang.mybus.R;
import com.yang.thread.BusQueryByStationNameThread;

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

	// 接收子线程返回的消息
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x126) {
				SerializableList list = new SerializableList();
				StationBuses buses = (StationBuses) msg.obj;
				list.setBusList(buses.getBusList());
				// 传送页面输入信息到查询结果页面
				Bundle data = new Bundle();
				data.putSerializable("station", buses.getStation());
				data.putSerializable("busList", list);
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

		btnStation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!stationName.getText().equals("")) {
					BusQueryByStationNameThread thread = new BusQueryByStationNameThread(
							handler, stationName.getText().toString());
					new Thread(thread).start();
				}
			}
		});

		return view;
	}

}
