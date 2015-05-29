package com.yang.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.yang.mybus.R;

/**
 * 
 * ClassName: IndexActivity
 * @Description: 软件首页的activity
 * @author: fengmengyang
 * @date: 2015年4月9日
 */
public class IndexActivity extends Activity{
	private Button btn_login;
	private Button btn_register;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_index);
		
		// 初始化组件
		btn_login = (Button) findViewById(R.id.index_login);
		btn_register = (Button) findViewById(R.id.index_register);
		
		// 绑定事件
		btn_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(IndexActivity.this, LoginActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		btn_register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(IndexActivity.this, RegisterActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

}
