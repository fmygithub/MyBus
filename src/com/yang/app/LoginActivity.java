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
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
@SuppressLint("HandlerLeak")
public class LoginActivity extends Activity {
	private EditText username;
	private EditText password;
	private Button sign_in_btn;
	private TextView go_register;
	private Button btnBack;
	// 接收子线程返回的消息
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 判断消息是否来自子线程
			if (msg.what == 0x123) {
				// 测试是否接收到消息
				System.out.println("子线程返回的消息msg.obj:"+msg.obj.toString());
				// 接收子线程从服务器拿到的消息
				switch (msg.obj.toString()) {
				case "login_success":
					// 保存登陆后的用户名
					Bundle data = new Bundle();
					data.putSerializable("username", username.getText().toString());
					Intent intent = new Intent(LoginActivity.this,
							MainActivity.class);
					intent.putExtras(data);
					
					startActivity(intent);
					finish();
					break;
				case "username_wrong":
					Toast toast = Toast.makeText(getApplicationContext(), "用户名不存在！",
							Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					password.setText("");
					break;
				case "password_wrong":
					Toast toast1 = Toast.makeText(getApplicationContext(), "密码不正确！",
							Toast.LENGTH_SHORT);
					toast1.setGravity(Gravity.CENTER, 0, 0);
					toast1.show();
					password.setText("");
				}
			}
		}
	};

	public void onCreate(Bundle saveInstanceState) {
		super.onCreate(saveInstanceState);
		setContentView(R.layout.activity_login);
		this.setTitle("登陆");
		username = (EditText) findViewById(R.id.login_name);
		password = (EditText) findViewById(R.id.login_password);
		sign_in_btn = (Button) findViewById(R.id.btn_login);
		go_register = (TextView) findViewById(R.id.go_register);
		btnBack = (Button) findViewById(R.id.btn_back);
		
		//接收注册页传过来的用户名
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			username.setText(bundle.getString("username"));
		}
		// 绑定事件
		sign_in_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("===========");
				if (username.getText().toString().equals("")) {
					Toast toast = Toast.makeText(getApplicationContext(), "用户名不能为空！",
							Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					password.setText("");
				} else if (password.getText().toString().equals("")) {
					Toast toast = Toast.makeText(getApplicationContext(), "密码不能为空！",
							Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				} else {
					User user = new User();
					user.setUserName(username.getText().toString());
					user.setPassword(password.getText().toString());
					// 调用子线程处理登陆请求，并接收返回的服务器数据
					LoginRegisterThread loginThread = new LoginRegisterThread(handler, user, "login");
					new Thread(loginThread).start();
				}
			}
		});
		go_register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
				startActivity(intent);
				finish();
			}
		});
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, IndexActivity.class);
				startActivity(intent);
			}
		});
	}
	
}
