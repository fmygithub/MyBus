package com.yang.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.yang.entity.Bus;
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
		//初始化页面元素
		beginStation = (TextView)findViewById(R.id.bus_begin);
		endStation = (TextView)findViewById(R.id.bus_end);
		listView = (ListView)findViewById(R.id.bus_line_list);
		
		// 接收数据
		Intent intent = getIntent();
		SerializableList list = (SerializableList) intent
				.getSerializableExtra("lineList");
		List<Line> lineList = list.getLineList();
		
		int length = lineList.get(0).getStationList().size();
		beginStation.setText(lineList.get(0).getStationList().get(0).getStationName());
		endStation.setText(lineList.get(0).getStationList().get(length-1).getStationName());

		// 列表显示
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		for (Line line : lineList) {
			Map<String, Object> temp = new HashMap<String, Object>();
			temp.put("stationCount", "共 " + (length-1) + " 站");
			if (line.getBusList().size() == 1) {
				temp.put("routeName", "乘" + line.getBusList().get(0).getBusName() + "直达");
			} else {
				String str = "";
				List<Bus> busList = line.getBusList();
				for (int i = 0; i < busList.size(); i++) {
					str += busList.get(i).getBusName();
					if (i + 2 != busList.size()) {
						str += "→";
					}
				}
				temp.put("routeName", "换乘："+str);
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
	}

}
