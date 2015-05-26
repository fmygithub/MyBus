package com.yang.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.yang.entity.Route;
import com.yang.entity.SerializableList;
import com.yang.mybus.R;

/**
 * 
 * ClassName: StationInfoShowActivity
 * 
 * @Description: 显示站点信息的activity
 * @author: fengmengyang
 * @date: 2015年5月25日
 */
public class StationInfoShowActivity extends Activity {
	private Button back;
	private TextView stationNameText;
	private TextView lineNum;
	private TextView btnMap;
	private ListView busListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_station_info);
		// 初始化元素
		back = (Button) findViewById(R.id.btn_back);
		stationNameText = (TextView) findViewById(R.id.station_info_name);
		lineNum = (TextView) findViewById(R.id.line_num);
		btnMap = (TextView) findViewById(R.id.btn_station_map);
		busListView = (ListView) findViewById(R.id.station_info_list);
		
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(StationInfoShowActivity.this,MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
		btnMap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		// 接收数据
		Intent intent = getIntent();
		String stationName = (String) intent
				.getSerializableExtra("stationName");
		SerializableList list = (SerializableList) intent
				.getSerializableExtra("routeList");
		List<Route> routeList = list.getRouteList();
		
		int length = routeList.size();
		stationNameText.setText(stationName);
		lineNum.setText("共有" + length +"线路经过该站");

		// 列表显示
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		for (Route route : routeList) {
			Map<String, Object> temp = new HashMap<String, Object>();
			temp.put("routeName", route.getRouteName());
			listItems.add(temp);
		}
		// 创建SimpleAdapter
		SimpleAdapter adapter = new SimpleAdapter(this, listItems,
				R.layout.station_info_item,
				new String[] { "routeName" }, new int[] {
						R.id.station_info_route_name});
		// 初始化listView
		busListView.setAdapter(adapter);
	}
}
