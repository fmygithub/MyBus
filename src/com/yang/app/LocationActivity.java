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
import com.yang.mybus.R;

public class LocationActivity extends Activity {
	private LocationClient mLocationClient;
	private Button startLocation;
	private LocationMode tempMode = LocationMode.Hight_Accuracy; ////设置定位模式
	private String tempcoor = "bd09ll"; //返回的定位结果是百度经纬度,默认值gcj02
	private int span = 5000; //设置发起定位请求的间隔时间为5000ms
	private boolean isNeedAddress = true; //返回的定位结果包含地址信息
	private boolean isNeedDeviceDirect = true;//返回的定位结果包含手机机头的方向

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_location);
		mLocationClient = new LocationClient(this.getApplicationContext());
		mLocationClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation location) {
				//拿到位置信息，并传到地图页面
				Bundle bundle = new Bundle();
				bundle.putSerializable("axis_y", location.getLongitude());
				bundle.putSerializable("axis_x", location.getLatitude());
				Intent intent = new Intent(LocationActivity.this, BusMapActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
			
		});

		//((ApplicationConfig) getApplication()).mLocationResult = LocationResult;
		startLocation = (Button) findViewById(R.id.location);
		startLocation.setOnClickListener(new OnClickListener() {

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
