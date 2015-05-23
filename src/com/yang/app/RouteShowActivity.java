package com.yang.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.yang.entity.Route;
import com.yang.entity.SerializableList;
import com.yang.mybus.R;

public class RouteShowActivity extends Activity {
	private ListView list;
	private ActionBar actionBar;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_routeshow);
		// 获取应用程序图标
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true); //设置图标显示
		// actionBar.setHomeButtonEnabled(true); //设置图标可点击
		actionBar.setDisplayHomeAsUpEnabled(true); //设置图标可点击，并在图标上添加向左箭头
		
		//初始化listView
		list = (ListView)findViewById(R.id.routeList);
		
		// 接收数据
		Intent intent = getIntent();
		String begin_name = (String) intent.getSerializableExtra("begin_station");
		String end_name = (String) intent.getSerializableExtra("end_station");
		SerializableList list = (SerializableList) intent.getSerializableExtra("routeList");
		List<Route> routeList = list.getList();
		
		Map<String, Object> temp = new HashMap<String,Object>();
		for(Route route : routeList) {
			route.getStationCount();
			route.getStationList();
		}
		//创建SimpleAdapter
		/*SimpleAdapter adapter = new SimpleAdapter(this, routeList, R.layout.simple_item, from, to);*/
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.isCheckable()){
			item.setChecked(true);
		}
		switch (item.getItemId()){
		
		case android.R.id.home:
			Intent intent = new Intent(RouteShowActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
