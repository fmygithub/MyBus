package com.yang.app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.TextView;

import com.yang.mybus.R;

public class RouteShowActivity extends Activity {
	private TextView show;
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
		
		show = (TextView) findViewById(R.id.show);
		
		// 接收数据
		Intent intent = getIntent();
		String begin_name = (String) intent.getSerializableExtra("begin_name");
		String end_name = (String) intent.getSerializableExtra("end_name");
		show.setText(begin_name+"&"+end_name);
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
