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

import com.yang.entity.SerializableList;
import com.yang.entity.Station;
import com.yang.entity.StationBuses;
import com.yang.mybus.R;
import com.yang.thread.BusQueryByStationNameThread;

public class BusInfoShowActivity extends Activity {
	/**
	 * 公交路线名
	 */
	private TextView busNameText;
	private TextView timeText;
	private TextView piaojiaText;
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
	private Button back;

	// 接收子线程返回的消息
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x126) {
				SerializableList list = new SerializableList();
				StationBuses routes = (StationBuses) msg.obj;
				list.setBusList(routes.getBusList());
				// 传送页面输入信息到查询结果页面
				Bundle data = new Bundle();
				data.putSerializable("stationName", routes.getStationName());
				data.putSerializable("busList", list);
				Intent intent = new Intent(BusInfoShowActivity.this,
						StationInfoShowActivity.class);
				intent.putExtras(data);
				startActivity(intent);
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bus_info);
		// 初始化元素
		busNameText = (TextView) findViewById(R.id.bus_info_name);
		timeText = (TextView) findViewById(R.id.time);
		piaojiaText = (TextView) findViewById(R.id.piaojia);
		beginStation = (TextView) findViewById(R.id.bus_info_begin);
		endStation = (TextView) findViewById(R.id.bus_info_end);
		stationListView = (ListView) findViewById(R.id.bus_info_list);
		back = (Button) findViewById(R.id.btn_back);
		// 接收数据
		Intent intent = getIntent();
		String busName = (String) intent.getSerializableExtra("busName");
		SerializableList list = (SerializableList) intent
				.getSerializableExtra("stationList");
		List<Station> stationList = list.getStationList();

		int length = stationList.size();
		busNameText.setText(busName);
		timeText.setText("早晚时间：06:30-21:30");
		piaojiaText.setText("票价2元");
		beginStation.setText(stationList.get(0).getStationName());
		endStation.setText(stationList.get(length - 1).getStationName());

		// 列表显示
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < length; i++) {
			Map<String, Object> temp = new HashMap<String, Object>();
			temp.put("stationName", stationList.get(i).getStationName());
			if (i == 0) {
				temp.put("leftImag", R.drawable.line_list_item_top_image);
			} else if (i == length - 1) {
				temp.put("leftImag", R.drawable.line_list_item_bottom_image);
			} else {
				temp.put("leftImag", R.drawable.line_list_item_center_image);
			}
			listItems.add(temp);
		}
		// 创建SimpleAdapter
		SimpleAdapter adapter = new SimpleAdapter(this, listItems,
				R.layout.item_bus_info, new String[] { "stationName",
						"leftImag" }, new int[] { R.id.bus_info_station_name,
						R.id.left_image });
		// 初始化listView
		stationListView.setAdapter(adapter);

		// 为list绑定点击事件
		stationListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Adapter adapter = parent.getAdapter();
				@SuppressWarnings("unchecked")
				Map<String, String> stationMap = (Map<String, String>) adapter
						.getItem(position);

				// 开启新的线程处理http请求
				BusQueryByStationNameThread thread = new BusQueryByStationNameThread(
						handler, stationMap.get("stationName"));
				new Thread(thread).start();
			}

		});
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BusInfoShowActivity.this.finish();
			}
		});
	}
}
