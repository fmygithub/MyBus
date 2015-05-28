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
public class OneChangeLineInfoShowActivity extends Activity {
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
	private Button	back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_one_change_bus_line_info);
		// 初始化元素
		beginStation = (TextView) findViewById(R.id.businfo_begin);
		endStation = (TextView) findViewById(R.id.businfo_end);
		lineChange1 = (TextView) findViewById(R.id.view_line_one_change_info)
				.findViewById(R.id.one_change_change_station);
		lineChange2 = (TextView) findViewById(R.id.view_line_one_change_info)
				.findViewById(R.id.one_change_change_station1);
		lineBegin = (TextView) findViewById(R.id.view_line_one_change_info)
				.findViewById(R.id.one_change_begin);
		lineEnd = (TextView) findViewById(R.id.view_line_one_change_info)
				.findViewById(R.id.one_change_end);
		lineBus1 = (TextView) findViewById(R.id.view_line_one_change_info)
				.findViewById(R.id.one_change_bus1);
		lineBus2 = (TextView) findViewById(R.id.view_line_one_change_info)
				.findViewById(R.id.one_change_bus2);
		lineStationCount1 = (TextView) findViewById(
				R.id.view_line_one_change_info).findViewById(
				R.id.one_change_stationcount1);
		lineStationCount2 = (TextView) findViewById(
				R.id.view_line_one_change_info).findViewById(
				R.id.one_change_stationcount2);
		back = (Button) findViewById(R.id.btn_back);

		// 接收数据
		Intent intent = getIntent();
		Line line = (Line) intent.getSerializableExtra("line");

		beginStation.setText(line.getBeginStation().getStationName());
		lineBegin.setText(line.getBeginStation().getStationName());
		endStation.setText(line.getEndStation().getStationName());
		lineEnd.setText(line.getEndStation().getStationName());
		lineChange1.setText(line.getChangeStation().getStationName());
		lineChange2.setText(line.getChangeStation().getStationName());
		lineBus1.setText(line.getBusStationsList().get(0).getBusName());
		lineBus2.setText(line.getBusStationsList().get(1).getBusName());
		lineStationCount1.setText((line.getBusStationsList().get(0)
				.getStationList().size() - 1) + "");
		lineStationCount2.setText((line.getBusStationsList().get(1)
				.getStationList().size() - 1) + "");
		
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				OneChangeLineInfoShowActivity.this.finish();
			}
		});
	}
}
