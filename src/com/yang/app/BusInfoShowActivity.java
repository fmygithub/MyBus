package com.yang.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.yang.entity.Route;
import com.yang.entity.SerializableList;
import com.yang.entity.Station;
import com.yang.mybus.R;

public class BusInfoShowActivity extends Activity {
	/**
	 * 公交路线名
	 */
	private TextView busName;
	/**
	 * 始发站
	 */
	private TextView beginStation;
	/**
	 * 终点站
	 */
	private TextView endStation;
	/**
	 * 站点列表
	 */
	private ListView stationListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bus_info);
		// 初始化元素
		busName = (TextView) findViewById(R.id.bus_info_name);
		beginStation = (TextView) findViewById(R.id.bus_info_begin);
		endStation = (TextView) findViewById(R.id.bus_info_end);
		stationListView = (ListView) findViewById(R.id.bus_info_list);

		// 接收数据
		Intent intent = getIntent();
		SerializableList list = (SerializableList) intent
				.getSerializableExtra("stationList");
		List<Station> stationList = list.getStationList();

		int length = stationList.size();
		beginStation.setText(stationList.get(0).getStationName());
		endStation.setText(stationList.get(length - 1).getStationName());

		// 列表显示
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		for (Station station : stationList) {
			Map<String, Object> temp = new HashMap<String, Object>();
			temp.put("stationName", station.getStationName());
			listItems.add(temp);
		}
		// 创建SimpleAdapter
		SimpleAdapter adapter = new SimpleAdapter(this, listItems,
				R.layout.bus_info_item, new String[] { "stationName" }, new int[] { R.id.bus_info_station_name });
		// 初始化listView
		stationListView.setAdapter(adapter);
	}
}
