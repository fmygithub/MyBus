package com.yang.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.yang.mybus.R;

public class HelpFragment extends Fragment {
	private Button btn_call110;
	private Button btn_help_for_friend;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_help, null);
		btn_call110 = (Button) view.findViewById(R.id.call110);
		btn_help_for_friend = (Button) view.findViewById(R.id.help_for_friend);
		
		// 绑定事件
		btn_call110.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_CALL);
				intent.setData(Uri.parse("tel:110"));
				startActivity(intent);
			}
		});
		btn_help_for_friend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_DIAL);
				startActivity(intent);
			}
		});
		return view;
	}
	@Override
	public void onPause() {
		super.onPause();
	}
}
