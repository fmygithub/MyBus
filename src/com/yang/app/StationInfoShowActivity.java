package com.yang.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.yang.entity.Bus;
import com.yang.entity.BusStations;
import com.yang.entity.SerializableList;
import com.yang.mybus.R;
import com.yang.thread.StationQueryByRouteNameThread;

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

	// 接收子线程返回的消息
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x125) {
				SerializableList list = new SerializableList();
				BusStations stations = (BusStations) msg.obj;
				list.setStationList(stations.getStationList());
				// 传送页面输入信息到查询结果页面
				Bundle data = new Bundle();
				data.putSerializable("busName", stations.getBusName());
				data.putSerializable("stationList", list);
				Intent intent = new Intent(StationInfoShowActivity.this,
						BusInfoShowActivity.class);
				intent.putExtras(data);
				startActivity(intent);
			}
		};
	};

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

		// 接收数据
		Intent intent = getIntent();
		String stationName = (String) intent
				.getSerializableExtra("stationName");
		SerializableList list = (SerializableList) intent
				.getSerializableExtra("busList");
		List<Bus> routeList = list.getBusList();

		int length = routeList.size();
		stationNameText.setText(stationName);
		lineNum.setText("共有" + length + "线路经过该站");

		// 列表显示
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		for (Bus route : routeList) {
			Map<String, Object> temp = new HashMap<String, Object>();
			temp.put("busName", route.getBusName());
			temp.put("time", "早晚时间：06:00-21:30");
			temp.put("piaojia", "票价2元");
			listItems.add(temp);
		}
		// 创建SimpleAdapter
		SimpleAdapter adapter = new SimpleAdapter(this, listItems,
				R.layout.item_station_info, new String[] { "busName", "time",
						"piaojia" }, new int[] { R.id.station_info_route_name,
						R.id.time, R.id.piaojia });
		// 初始化listView
		busListView.setAdapter(adapter);

		// 为list绑定点击事件
		busListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Adapter adapter = parent.getAdapter();
				@SuppressWarnings("unchecked")
				Map<String, String> stationMap = (Map<String, String>) adapter
						.getItem(position);

				// 开启新的线程处理http请求
				StationQueryByRouteNameThread thread = new StationQueryByRouteNameThread(
						handler, stationMap.get("busName"));
				new Thread(thread).start();
			}

		});

		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				StationInfoShowActivity.this.finish();
			}
		});
		btnMap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
	}
}
