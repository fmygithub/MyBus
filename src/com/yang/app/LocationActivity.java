package com.yang.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.yang.mybus.R;

public class LocationActivity extends Activity{
	private LocationClient locationClient; //定位client
	public MyLocationListenner myListener = new MyLocationListenner();
	private MapView mapView ; //地图控件
	private BaiduMap baiduMap;
	private BitmapDescriptor currentMarker;
	
	private boolean isFirstLocation = true; //是否首次定位
	private Button btn_location;
	private TextView show_message;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		btn_location = (Button) findViewById(R.id.button1); //定位按钮
		btn_location.setText("开始定位");
//		show_message = (TextView) findViewById(R.id.show_message);
		btn_location.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				baiduMap.setMyLocationConfigeration(new MyLocationConfiguration(LocationMode.NORMAL, true, currentMarker));
			}
		});
		
		//地图初始化
		mapView = (MapView) findViewById(R.id.bmapView);
		baiduMap = mapView.getMap();
		
		//开启定位图层
		baiduMap.setMyLocationEnabled(true);
		//定位初始化
		locationClient = new LocationClient(this);
		locationClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); //打开gps
		option.setCoorType("bd09ll"); //设置坐标类型
		option.setScanSpan(1000);
		locationClient.setLocOption(option);
		locationClient.start();
	}
	
	/**
	 * 
	 * ClassName: MyLocationListenner
	 * @Description: 定位SDK监听函数
	 * @author: fengmengyang
	 * @date: 2015年5月2日
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不再处理新接收的位置
			if (location == null || mapView == null)
				return;
			MyLocationData locationData = new MyLocationData.Builder()
				.accuracy(location.getRadius())
				.direction(100).latitude(location.getLatitude())
				.longitude(location.getAltitude()).build();
			baiduMap.setMyLocationData(locationData);
			if (isFirstLocation) {
				isFirstLocation = false;
				LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				baiduMap.animateMapStatus(u);
			}
		}
		public void onReceivePoi(BDLocation poiLocation) {
			
		}
	}
	@Override
	protected void onPause() {
		mapView.onPause();
		super.onPause();
	}
	@Override
	protected void onResume() {
		mapView.onResume();
		super.onResume();
	}
	@Override
	protected void onDestroy() {
		//销毁定位
		locationClient.stop();
		//关闭定位图层
		baiduMap.setMyLocationEnabled(false);
		mapView.onDestroy();
		mapView = null;
		super.onDestroy();
	}
}

