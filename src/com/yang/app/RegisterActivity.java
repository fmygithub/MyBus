package com.yang.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yang.entity.User;
import com.yang.mybus.R;
import com.yang.thread.LoginRegisterThread;

/**
 * 
 * ClassName: RegisterActivity
 * 
 * @Description: 注册activity
 * @author: fengmengyang
 * @date: 2015年4月9日
 */
public class RegisterActivity extends Activity {
	private EditText username;
	private EditText password;
	private EditText password_confire;
	private Button btn_register;
	private TextView go_login;
	private Button btnBack;

	// 接收子线程处理后返回的响应值
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x124) {
				if ("register_success".equals(msg.obj.toString())) {
					// 注册成功提示，并跳转到登陆页面
					Bundle data = new Bundle();
					data.putSerializable("username", username.getText()
							.toString());
					Intent intent = new Intent(RegisterActivity.this,
							LoginActivity.class);
					intent.putExtras(data);
					startActivity(intent);
					finish();
				} else if ("register_fail".equals(msg.obj.toString())) {
					Toast toast = Toast.makeText(getApplicationContext(), "注册失败！",
							Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				} else if ("user_existed".equals(msg.obj.toString())) {
					Toast toast = Toast.makeText(getApplicationContext(), "该用户名已被占用！",
							Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		this.setTitle("注册");
		// 初始化页面组件
		username = (EditText) findViewById(R.id.register_name);
		System.out.println(username);
		password = (EditText) findViewById(R.id.register_password);
		password_confire = (EditText) findViewById(R.id.register_comfire);
		btn_register = (Button) findViewById(R.id.btn_register);
		go_login = (TextView) findViewById(R.id.go_login);
		btnBack = (Button) findViewById(R.id.btn_back);

		// 注册按钮绑定事件
		btn_register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("".equals(username.getText().toString())) {
					Toast toast = Toast.makeText(getApplicationContext(), "请输入用户名！",
							Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				} else if ("".equals(password.getText().toString())) {
					Toast toast = Toast.makeText(getApplicationContext(), "请输入密码！",
							Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				} else if ("".equals(password_confire.getText().toString())) {
					Toast toast = Toast.makeText(getApplicationContext(), "请输入确认密码！",
							Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				} else if (!password_confire.getText().toString()
						.equals(password.getText().toString())) {
					Toast toast = Toast.makeText(getApplicationContext(), "两次密码不一致！",
							Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				} else {
					User user = new User();
					user.setUserName(username.getText().toString());
					user.setPassword(password.getText().toString());
					LoginRegisterThread registerThread = new LoginRegisterThread(handler, user, "register");
					new Thread(registerThread).start();
				}
			}
		});

		// 去登陆链接绑定事件
		go_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RegisterActivity.this,
						LoginActivity.class);
				startActivity(intent);
				finish();
			}
		});
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				RegisterActivity.this.finish();
			}
		});
	}

	
}
