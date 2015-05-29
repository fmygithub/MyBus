package com.yang.app;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yang.mybus.R;
import com.yang.url.Static;
import com.yang.utils.HandleJson;

/**
 * 
 * ClassName: RegisterActivity
 * @Description: 注册activity
 * @author: fengmengyang
 * @date: 2015年4月9日
 */
public class RegisterActivity extends Activity{
	private TextView error_message;
	private EditText username;
	private EditText password;
	private EditText password_confire;
	private Button btn_register;
	private TextView go_login;
	// 注册处理的子线程
	RegisterThread registerThread;
	
	// 接收子线程处理后返回的响应值
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x124){
				if ("register_success".equals(msg.obj.toString())) {
					// 注册成功提示，并跳转到登陆页面
					Bundle data = new Bundle();
					data.putSerializable("username", username.getText().toString());
					Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
					intent.putExtras(data);
					startActivity(intent);
					finish();
				} else if ("register_fail".equals(msg.obj.toString())){
					error_message.setText("该用户名已被注册！");
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
		error_message = (TextView) findViewById(R.id.register_message);
		username = (EditText) findViewById(R.id.register_name);
		System.out.println(username);
		password = (EditText) findViewById(R.id.register_password);
		password_confire = (EditText) findViewById(R.id.register_confire);
		btn_register = (Button) findViewById(R.id.btn_register);
		go_login = (TextView) findViewById(R.id.go_login);
		
		// 注册按钮绑定事件
		btn_register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if ("".equals(username.getText().toString())){
					error_message.setText("请输入用户名！");
				}else if ("".equals(password.getText().toString())){
					error_message.setText("请输入密码！");
				}else if ("".equals(password_confire.getText().toString())){
					error_message.setText("请输入确认密码！");
				}else if (!password_confire.getText().toString().equals(password.getText().toString())){
					error_message.setText("两次密码不一致！");
				}else {
					registerThread = new RegisterThread();
					new Thread(registerThread).start();
				}
			}
		});
		
		// 去登陆链接绑定事件
		go_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
	
	/**
	 * 
	 * ClassName: RegisterThread
	 * @Description: 注册子线程
	 * @author: fengmengyang
	 * @date: 2015年4月9日
	 */
	class RegisterThread implements Runnable {
		HttpClient client = new DefaultHttpClient();
		@Override
		public void run() {
			// 接收服务器返回的消息
			String return_message = post(username.getText().toString(),password.getText().toString());
			
			Message msg = new Message();
			msg.what = 0x124;
			msg.obj = return_message;
			handler.sendMessage(msg);
		}
		
		/**
		 * 
		 * @Description: 处理与服务器的交互
		 * @return String
		 * @author: fengmengyang
		 * @date: 2015年4月9日
		 */
		private String post(String username, String password) {
			HttpPost post = new HttpPost(Static.REGISTER+"?user.userName="+username+"&user.password"+password);
			try {
				HttpResponse response = client.execute(post);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
					JSONObject json = HandleJson.parse_json(response);
					return json.getString("message");
				} else {
					System.out.println("响应异常，响应码："+response.getStatusLine().getStatusCode());
				}
			} catch (ClientProtocolException e) {
				System.out.println("连接超时"); 
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
		
	}

}
