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
public class OneChangeLineInfoShowActivity extends Activity {
	// 定位相关
	private LocationClient mLocationClient;

	private TextView beginStation;
	private TextView endStation;
	private TextView lineBegin;
	private TextView lineEnd;
	private TextView lineChange1;
	private TextView lineChange2;
	private TextView lineBus1;
	private TextView lineBus2;
	private TextView lineStationCount1;
	private TextView lineStationCount2;
	private Button back;
	private TextView btnMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_one_change_bus_line_info);
		// 初始化元素
		beginStation = (TextView) findViewById(R.id.businfo_begin);
		endStation = (TextView) findViewById(R.id.businfo_end);
		lineChange1 = (TextView) findViewById(R.id.view_line_one_change_info)
				.findViewById(R.id.two_change_change_station21);
		lineChange2 = (TextView) findViewById(R.id.view_line_one_change_info)
				.findViewById(R.id.two_change_change_station2);
		lineBegin = (TextView) findViewById(R.id.view_line_one_change_info)
				.findViewById(R.id.two_change_begin);
		lineEnd = (TextView) findViewById(R.id.view_line_one_change_info)
				.findViewById(R.id.one_change_end);
		lineBus1 = (TextView) findViewById(R.id.view_line_one_change_info)
				.findViewById(R.id.one_change_bus1);
		lineBus2 = (TextView) findViewById(R.id.view_line_one_change_info)
				.findViewById(R.id.two_change_bus3);
		lineStationCount1 = (TextView) findViewById(
				R.id.view_line_one_change_info).findViewById(
				R.id.one_change_stationcount1);
		lineStationCount2 = (TextView) findViewById(
				R.id.view_line_one_change_info).findViewById(
				R.id.two_change_stationcount2);
		back = (Button) findViewById(R.id.btn_back);
		btnMap = (TextView) findViewById(R.id.btn_one_change_map);

		// 接收数据
		Intent intent = getIntent();
		final Line line = (Line) intent.getSerializableExtra("line");

		beginStation.setText(line.getBeginStation().getStationName());
		lineBegin.setText(line.getBeginStation().getStationName());
		endStation.setText(line.getEndStation().getStationName());
		lineEnd.setText(line.getEndStation().getStationName());
		lineChange1.setText(line.getChangeStation().getStationName());
		lineChange2.setText(line.getChangeStation().getStationName());
		lineBus1.setText(line.getBusStationsList().get(0).getBusName());
		lineBus2.setText(line.getBusStationsList().get(1).getBusName());
		lineStationCount1.setText((line.getBusStationsList().get(0)
				.getStationList().size() - 1)
				+ "");
		lineStationCount2.setText((line.getBusStationsList().get(1)
				.getStationList().size() - 1)
				+ "");

		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				OneChangeLineInfoShowActivity.this.finish();
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
				Intent intent = new Intent(OneChangeLineInfoShowActivity.this,
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
