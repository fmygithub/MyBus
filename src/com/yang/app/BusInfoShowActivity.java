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

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
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
	/**
	 * 返回按钮
	 */
	private Button back;
	/**
	 * 地图按钮
	 */
	private TextView map;
	//定位相关
	private LocationClient mLocationClient;
	private LocationMode tempMode = LocationMode.Hight_Accuracy; ////设置定位模式
	private String tempcoor = "bd09ll"; //返回的定位结果是百度经纬度,默认值gcj02
	private int span = 5000; //设置发起定位请求的间隔时间为5000ms
	private boolean isNeedAddress = true; //返回的定位结果包含地址信息
	private boolean isNeedDeviceDirect = true;//返回的定位结果包含手机机头的方向
	
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
				data.putSerializable("station", routes.getStation());
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
		map = (TextView) findViewById(R.id.btn_bus_info_map);
		
		
		// 接收BusQueryFragment传过来的数据，包含路线名和该路线所有的站点信息
		Intent intent = getIntent();
		final String busName = (String) intent.getSerializableExtra("busName");
		final SerializableList list = (SerializableList) intent
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
		
		
		
		
		//定位功能
		mLocationClient = new LocationClient(this.getApplicationContext());
		mLocationClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation location) {
				//拿到位置信息，并传到地图页面
				Bundle bundle = new Bundle();
				bundle.putSerializable("axis_y", location.getLongitude());
				bundle.putSerializable("axis_x", location.getLatitude());
				bundle.putSerializable("address", location.getAddrStr());
				bundle.putSerializable("busName", busName);
				bundle.putSerializable("stationList", list);
				Intent intent = new Intent(BusInfoShowActivity.this, BusMapActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
			
		});

		map.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InitLocation();
				mLocationClient.start();
			}
		});
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		mLocationClient.stop();
		super.onStop();
	}
	
	//初始化LocationClientOption的参数
	private void InitLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);
		option.setCoorType(tempcoor);
		option.setScanSpan(span);
		option.setIsNeedAddress(isNeedAddress);
		option.setNeedDeviceDirect(isNeedDeviceDirect);
		
		mLocationClient.setLocOption(option);
	}
}
