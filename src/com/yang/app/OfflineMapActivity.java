package com.yang.app;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.yang.fragment.OfflineAllFragment;
import com.yang.fragment.OfflineDownloadFragment;
import com.yang.mybus.R;

public class OfflineMapActivity extends FragmentActivity{
	private TextView tab_all;
	private TextView tab_download;
	private int content_id = R.id.content;
	
	private FragmentManager fm;
	private FragmentTransaction ft;
	
	OfflineAllFragment allFragment = new OfflineAllFragment();
	OfflineDownloadFragment downloadFragment = new OfflineDownloadFragment();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_offline_map);
		
		tab_all = (TextView) findViewById(R.id.tab_all);
		tab_download = (TextView) findViewById(R.id.tab_download);
		
		tab_all.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tab_all.setBackgroundColor(Color.GRAY);
				tab_download.setBackgroundColor(Color.WHITE);
				fm = getSupportFragmentManager();
				ft = fm.beginTransaction();
				ft.replace(content_id, allFragment);
				ft.commit();
			}
		});
		tab_download.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tab_all.setBackgroundColor(Color.WHITE);
				tab_download.setBackgroundColor(Color.GRAY);
				fm = getSupportFragmentManager();
				ft = fm.beginTransaction();
				ft.replace(content_id, downloadFragment);
				ft.commit();
			}
		});
	}
}
