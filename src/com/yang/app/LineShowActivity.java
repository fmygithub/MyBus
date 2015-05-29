package com.yang.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.yang.entity.BusStations;
import com.yang.entity.Line;
import com.yang.entity.SerializableList;
import com.yang.mybus.R;

public class LineShowActivity extends Activity {
	/**
	 * 起始站
	 */
	private TextView beginStation;
	/**
	 * 目的站点
	 */
	private TextView endStation;
	/**
	 * 路线列表
	 */
	private ListView listView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_bus_line);
		// 初始化页面元素
		beginStation = (TextView) findViewById(R.id.bus_begin);
		endStation = (TextView) findViewById(R.id.bus_end);
		listView = (ListView) findViewById(R.id.bus_line_list);

		// 接收数据
		Intent intent = getIntent();
		SerializableList list = (SerializableList) intent
				.getSerializableExtra("lineList");
		final List<Line> lineList = list.getLineList();

		if (lineList != null) {
			beginStation.setText(lineList.get(0).getBeginStation()
					.getStationName());
			endStation
					.setText(lineList.get(0).getEndStation().getStationName());

			/*
			 * //按站点数量排序 List<Line> newLineList = new ArrayList<Line>(); int
			 * length = lineList.size(); for (int i = 0; i<length; i++) { if (i
			 * + 1 < length) { if (lineList.get(i).getStationCount() >
			 * lineList.get(i+1).getStationCount()) { newLineList.add(object) }
			 * } }
			 */

			// 列表显示
			List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
			for (Line line : lineList) {
				Map<String, Object> temp = new HashMap<String, Object>();
				if (line.getBusStationsList().size() == 1) {
					temp.put("stationCount", "共 "
							+ (line.getBusStationsList().get(0)
									.getStationList().size() - 1) + " 站");
					temp.put("routeName", "乘"
							+ line.getBusStationsList().get(0).getBusName()
							+ "直达");
				} else {
					int stationCount = 0;
					String str = "";
					List<BusStations> busStationsList = line
							.getBusStationsList();
					for (int i = 0; i < busStationsList.size(); i++) {
						stationCount += (busStationsList.get(i)
								.getStationList().size() - 1);
						str += busStationsList.get(i).getBusName();
						if (i + 1 != busStationsList.size()) {
							str += "→";
						}
					}
					temp.put("stationCount", "共 " + stationCount + " 站");
					temp.put("routeName", "一次换乘：" + str);
				}
				listItems.add(temp);
			}
			// 创建SimpleAdapter
			SimpleAdapter adapter = new SimpleAdapter(this, listItems,
					R.layout.item_line, new String[] { "routeName",
							"stationCount" }, new int[] { R.id.routeName,
							R.id.stationCount });
			// 初始化listView
			listView.setAdapter(adapter);
			// 为list绑定点击事件
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Adapter adapter = parent.getAdapter();
					@SuppressWarnings("unchecked")
					Map<String, String> lineMap = (Map<String, String>) adapter
							.getItem(position);
					String temp = lineMap.get("routeName");
					Bundle bundle = new Bundle();
					if (temp.contains("一次换乘：")) {
						int jiantou = temp.indexOf("→");
						String busName1 = temp.substring(5, jiantou);
						String busName2 = temp.substring(jiantou + 1,
								temp.length());
						for (Line line : lineList) {
							if (line.getBusStationsList().get(0).getBusName()
									.equals(busName1)
									&& line.getBusStationsList().get(1)
											.getBusName().equals(busName2)) {
								bundle.putSerializable("line", line);
							}
							Intent intent = new Intent(LineShowActivity.this,
									OneChangeLineInfoShowActivity.class);
							intent.putExtras(bundle);
							startActivity(intent);
						}
					} else if (temp.contains("直达")) {
						String busName = temp.substring(1, temp.length() - 2);
						for (Line line : lineList) {
							if (line.getBusStationsList().get(0).getBusName()
									.equals(busName)) {
								bundle.putSerializable("line", line);
							}
							Intent intent = new Intent(LineShowActivity.this,
									DirectLineInfoShowActivity.class);
							intent.putExtras(bundle);
							startActivity(intent);
						}
					}
				}
			});
		}

	}

	private void paixu(int a[], int b[]) {
		int c[] = new int[a.length + b.length];
		int temp = 0;
		// a,b都是有序的数组
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < b.length; j++) {
				if (a[i] < b[j]) {
					c[temp] = a[i];
					temp++;
					break;
				} else if (a[i] == b[j]) {
					c[temp] = a[i];
					c[temp + 1] = b[j];
					temp += 2;
					j++;
					break;
				} else {
					c[temp] = b[j];
					temp++;
				}
			}
		}
	}
}
