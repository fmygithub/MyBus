package com.yang.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.yang.entity.Route;
import com.yang.entity.SerializableList;
import com.yang.mybus.R;

public class RouteShowActivity extends Activity {
	/**
	 * 起始站
	 */
	private TextView beginStation;
	/**
	 * 目的站点
	 */
	private TextView endStation;
	/**
	 * 路线列表
	 */
	private ExpandableListView listView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_bus_line);
		//初始化页面元素
		beginStation = (TextView)findViewById(R.id.bus_begin);
		endStation = (TextView)findViewById(R.id.bus_end);
		listView = (ExpandableListView)findViewById(R.id.bus_line_list);
		
		// 接收数据
		Intent intent = getIntent();
		SerializableList list = (SerializableList) intent
				.getSerializableExtra("routeList");
		List<Route> routeList = list.getList();
		
		int length = routeList.get(0).getStationList().size();
		beginStation.setText(routeList.get(0).getStationList().get(0).getStationName());
		endStation.setText(routeList.get(0).getStationList().get(length-1).getStationName());

		// 列表显示
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		for (Route route : routeList) {
			Map<String, Object> temp = new HashMap<String, Object>();
			temp.put("stationCount", "共  " + route.getStationCount() + " 站");
			temp.put("routeName", "乘 " + route.getRouteNameList().get(0) + "直达");
			listItems.add(temp);
		}
		// 创建SimpleAdapter
		SimpleAdapter adapter = new SimpleAdapter(this, listItems,
				R.layout.simple_item, new String[] { "routeName",
						"stationCount" }, new int[] { R.id.routeName,
						R.id.stationCount });
		// 初始化listView
		listView.setAdapter(adapter);
	}

}
