package com.yang.fragment;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.yang.app.LineShowActivity;
import com.yang.entity.Line;
import com.yang.entity.SerializableList;
import com.yang.mybus.R;
import com.yang.thread.LineQueryThread;

public class LineQueryFragment extends Fragment {
	private EditText begin_station;
	private EditText end_station;
	private Button btn_refresh;
	private Button btn_query;

	// 接收子线程返回的消息
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x127) {
				SerializableList list = new SerializableList();
				list.setLineList((List<Line>) msg.obj);
				// 传送页面输入信息到查询结果页面
				Bundle data = new Bundle();
				/*data.putSerializable("beginStation", begin_station.getText()
						.toString());
				data.putSerializable("end_station", end_station.getText()
						.toString());*/
				data.putSerializable("lineList", list);
				Intent intent = new Intent(getActivity(),
						LineShowActivity.class);
				intent.putExtras(data);
				startActivity(intent);
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle saveInstanceState) {
		View view = inflater.inflate(R.layout.fragment_routequery, null);
		begin_station = (EditText) view.findViewById(R.id.begin_name);
		end_station = (EditText) view.findViewById(R.id.end_name);
		btn_query = (Button) view.findViewById(R.id.btn_query);
		btn_refresh = (Button) view.findViewById(R.id.btn_exchange);
		btn_query.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!begin_station.getText().toString().equals("")
						&& !end_station.getText().toString().equals("")) {
					LineQueryThread thread = new LineQueryThread(handler,
							begin_station.getText().toString(), end_station
									.getText().toString());
					new Thread(thread).start();
				}

			}
		});

		btn_refresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				begin_station.setText("");
				end_station.setText("");
			}
		});
		return view;
	}

}
