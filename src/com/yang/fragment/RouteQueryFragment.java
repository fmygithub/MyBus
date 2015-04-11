package com.yang.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.yang.app.MainActivity;
import com.yang.app.RouteShowActivity;
import com.yang.mybus.R;

public class RouteQueryFragment extends Fragment {
	private EditText begin_name;
	private EditText end_name;
	private Button btn_check;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
		View view = inflater.inflate(R.layout.fragment_routequery, null);
		begin_name = (EditText) view.findViewById(R.id.begin_name);
		end_name = (EditText) view.findViewById(R.id.end_name);
		btn_check = (Button) view.findViewById(R.id.btn_check);
		btn_check.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 传送页面输入信息到查询结果页面
				Bundle data = new Bundle();
				data.putSerializable("begin_name", begin_name.getText().toString());
				data.putSerializable("end_name", end_name.getText().toString());
				Intent intent = new Intent(getActivity(), RouteShowActivity.class);
				intent.putExtras(data);
				startActivity(intent);
			}
		});
		return view;
	}
}
