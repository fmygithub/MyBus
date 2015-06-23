package com.yang.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.yang.entity.Line;
import com.yang.mybus.R;

/**
 * 
 * ClassName: RouteInfoShowActivity
 * 
 * @Description: 换乘方案详情页
 * @author: fengmengyang
 * @date: 2015年5月25日
 */
public class DirectLineInfoShowActivity extends Activity {
	// 定位相关
	private LocationClient mLocationClient;
	
	private TextView beginStation;
	private TextView endStation;
	private TextView lineBegin;
	private TextView lineEnd;
	private TextView lineBus;
	private TextView lineStationCount;
	private Button back;
	private TextView btnMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_direct_bus_line_info);
		// 初始化元素
		beginStation = (TextView) findViewById(R.id.businfo_begin);
		endStation = (TextView) findViewById(R.id.businfo_end);
		lineBegin = (TextView) findViewById(R.id.line_direct_begin);
		lineEnd = (TextView) findViewById(R.id.line_direct_end);
		lineBus = (TextView) findViewById(R.id.line_direct_bus);
		lineStationCount = (TextView) findViewById(R.id.line_direct_stationcount);
		back = (Button) findViewById(R.id.btn_back);
		btnMap = (TextView) findViewById(R.id.btn_direct_map);
		// 接收数据
		Intent intent = getIntent();
		final Line line = (Line) intent.getSerializableExtra("line");

		beginStation.setText(line.getBeginStation().getStationName());
		lineBegin.setText(line.getBeginStation().getStationName());
		endStation.setText(line.getEndStation().getStationName());
		lineEnd.setText(line.getEndStation().getStationName());
		lineBus.setText(line.getBusStationsList().get(0).getBusName());
		lineStationCount.setText(line.getStationCount() + "");

		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DirectLineInfoShowActivity.this.finish();
			}
		});

		// 定位功能
		mLocationClient = new LocationClient(this.getApplicationContext());
		mLocationClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation location) {
				// 拿到位置信息，并传到地图页面
				Bundle bundle = new Bundle();
				bundle.putSerializable("axis_y", location.getLongitude());
				bundle.putSerializable("axis_x", location.getLatitude());
				bundle.putSerializable("address", location.getAddrStr());
				bundle.putSerializable("line", line);
				Intent intent = new Intent(DirectLineInfoShowActivity.this,
						LineMapActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}

		});

		btnMap.setOnClickListener(new OnClickListener() {

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

	// 初始化LocationClientOption的参数
	private void InitLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);
		option.setCoorType("bd09ll");
		option.setScanSpan(5000);
		option.setIsNeedAddress(true);
		option.setNeedDeviceDirect(true);

		mLocationClient.setLocOption(option);
	}
}
