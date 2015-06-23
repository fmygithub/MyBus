package com.yang.app;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapDrawFrameCallback;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.yang.entity.SerializableList;
import com.yang.entity.Station;
import com.yang.mybus.R;
import com.yang.utils.OpenglUtils;

public class BusMapActivity extends Activity implements OnMapDrawFrameCallback {
	/**
	 * MapView 是地图主控件
	 */
	private MapView mMapView;
	private TextView title;
	private Button btnBack;
	private BaiduMap mBaiduMap;
	// 定位相关
	private LocationClient mLocationClient;

	private InfoWindow mInfoWindow;
	// 站点位置列表
	private List<LatLng> latLngList = new ArrayList<LatLng>();
	private LatLng locPoint;
	private String locAddress;
	// 定位marker
	private Marker locMarker;

	// 初始化全局 bitmap 信息，不用时及时 recycle
	// 起点终点标记,以及站点位置图标
	BitmapDescriptor beginImg = BitmapDescriptorFactory
			.fromResource(R.drawable.qd_image);
	BitmapDescriptor endImg = BitmapDescriptorFactory
			.fromResource(R.drawable.zd_image);
	BitmapDescriptor stationImg = BitmapDescriptorFactory
			.fromResource(R.drawable.bus_line_info_left_center_top);

	BitmapDescriptor bdGround = BitmapDescriptorFactory
			.fromResource(R.drawable.location_image);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		// 获取地图控件引用
		mMapView = (MapView) findViewById(R.id.bmapView);
		title = (TextView) findViewById(R.id.title);
		btnBack = (Button) findViewById(R.id.btn_back);
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL); // 普通地图模式

		mBaiduMap.setOnMapDrawFrameCallback(this);
		
		
		/*mLocationClient = new LocationClient(this.getApplicationContext());
		mLocationClient.registerLocationListener(new BDLocationListener() {
			@Override
			public void onReceiveLocation(BDLocation location) {
				// 拿到位置信息，并传到地图页面
				System.out.println(location.getLatitude());
				locPoint = new LatLng(location.getLatitude(),location.getLongitude());
				locAddress = location.getAddrStr();
			}
		});
		// 定位功能
		InitLocation();
		mLocationClient.start();*/

		// 接收定位传过来的坐标，并标注
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		double axis_x = bundle.getDouble("axis_x");
		double axis_y = bundle.getDouble("axis_y");
		locAddress = bundle.getString("address");
		SerializableList list = (SerializableList) bundle
				.getSerializable("stationList");
		String busName = bundle.getString("busName");
		// 拿到该路线的所有站点
		final List<Station> stationList = list.getStationList();
		title.setText(busName + "公交站点详情");

		Log.i("坐标", axis_x + "," + axis_y + "," + stationList.size());
		// 定义Maker坐标点
		locPoint = new LatLng(axis_x, axis_y);
		// 定义地图状态
		MapStatus mMapStatus = new MapStatus.Builder().target(locPoint).zoom(16)
				.build();
		// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
				.newMapStatus(mMapStatus);
		// 改变地图状态
		mBaiduMap.setMapStatus(mMapStatusUpdate);

		// 构建MarkerOption，用于在地图上添加Marker
		// 定位marker
		OverlayOptions locOption = new MarkerOptions().position(locPoint)
				.icon(bdGround).zIndex(9);
		// 在地图上添加Marker，并显示
		locMarker = (Marker) mBaiduMap.addOverlay(locOption);
		for (Station station : stationList) {
			LatLng stationPoint = new LatLng(Double.parseDouble(station
					.getAxis_y()), Double.parseDouble(station.getAxis_x()));
			latLngList.add(stationPoint);
			OverlayOptions stationOption = new MarkerOptions()
					.position(stationPoint).icon(stationImg).zIndex(9);
			mBaiduMap.addOverlay(stationOption);
		}

		// 设置点击marker监听
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			public boolean onMarkerClick(final Marker marker) {
				// 信息显示
				TextView showMessage = new TextView(getApplicationContext());
				showMessage.setBackgroundResource(R.drawable.popup);
				showMessage.setTextColor(0xffff8400);
				OnInfoWindowClickListener listener = null;
				if (marker == locMarker) {
					showMessage.setText(locAddress); // 如果是定位marker的话就显示当前位置信息
					LatLng ll = marker.getPosition();
					mInfoWindow = new InfoWindow(BitmapDescriptorFactory
							.fromView(showMessage), ll, -47, listener);
					mBaiduMap.showInfoWindow(mInfoWindow);
				} else {
					for (Station station : stationList) {
						// Log.i("latitude:",
						// marker.getPosition().latitude+","+station.getAxis_x()+","+station.getAxis_y());
						if (marker.getPosition().latitude == Double
								.parseDouble(station.getAxis_y())) {
							showMessage.setText(station.getStationName());
						}
					}
					showMessage.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							mBaiduMap.hideInfoWindow();
						}
					});
					LatLng ll = marker.getPosition();
					mInfoWindow = new InfoWindow(showMessage, ll, -47);
					mBaiduMap.showInfoWindow(mInfoWindow);
				}
				return true;
			}
		});
		
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BusMapActivity.this.finish();
			}
		});
	}

	// 绘制线条
	@Override
	public void onMapDrawFrame(GL10 gl, MapStatus drawingMapStatus) {
		if (mBaiduMap.getProjection() != null) {
			OpenglUtils openGL = new OpenglUtils(mBaiduMap, latLngList);
			FloatBuffer vertexBuffer = openGL
					.calPolylinePoint(drawingMapStatus);
			openGL.drawPolyline(gl, Color.argb(255, 255, 0, 0), vertexBuffer,
					10, latLngList.size(), drawingMapStatus);
		}
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