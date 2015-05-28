package com.yang.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

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
	private TextView beginStation;
	private TextView endStation;
	private TextView lineBegin;
	private TextView lineEnd;
	private TextView lineBus;
	private TextView lineStationCount;
	private Button back;

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

		// 接收数据
		Intent intent = getIntent();
		Line line = (Line) intent.getSerializableExtra("line");

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
	}
}
