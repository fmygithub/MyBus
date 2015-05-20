package com.yang.app;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
@SuppressLint("HandlerLeak")
public class LoginActivity extends Activity {
	private EditText username;
	private EditText password;
	private Button sign_in_btn;
	private TextView error_message;
	private LoginThread loginThread;
	private TextView go_register;
	// 接收子线程返回的消息
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 判断消息是否来自子线程
			if (msg.what == 0x123) {
				// 测试是否接收到消息
				System.out.println("子线程返回的消息msg.obj:"+msg.obj.toString());
				// 接收子线程从服务器拿到的消息
				if (msg.obj.toString().equals("login_success")) {
					// 保存登陆后的用户名
					Bundle data = new Bundle();
					data.putSerializable("username", username.getText().toString());
					Intent intent = new Intent(LoginActivity.this,
							MainActivity.class);
					intent.putExtras(data);
					
					startActivity(intent);
					finish();
				} else if (msg.obj.toString().equals("login_fail")) {
					// 页面提示用户名或密码错误
					error_message.setText("用户名或密码错误，请重新输入！");
					password.setText("");
				}
			}
		}
	};

	public void onCreate(Bundle saveInstanceState) {
		super.onCreate(saveInstanceState);
		setContentView(R.layout.activity_login);
		this.setTitle("登陆");
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		sign_in_btn = (Button) findViewById(R.id.sign_in_button);
		error_message = (TextView) findViewById(R.id.error_message);
		go_register = (TextView) findViewById(R.id.go_register);
		// 绑定事件
		sign_in_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("===========");
				if (username.getText().toString().equals("")) {
					error_message.setText("用户名不能为空！");
					password.setText("");
				} else if (password.getText().toString().equals("")) {
					error_message.setText("密码不能为空！");
				} else {
					
					/*// 调用子线程处理登陆请求，并接收返回的服务器数据
					loginThread = new LoginThread();
					new Thread(loginThread).start();*/
					Intent intent = new Intent(LoginActivity.this, MainActivity.class);
					startActivity(intent);
					finish();
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
	}

	class LoginThread extends Thread {
		HttpClient httpClient = new DefaultHttpClient();
		@Override
		public void run() {
			// 拿到服务返回的响应值
			String return_message = post(username.getText().toString(), password.getText().toString());
			System.out.println(return_message.toString());

			Message msg = new Message();
			msg.what = 0x123;
			msg.obj = return_message.toString();
			System.out.println(msg.obj.toString());
			handler.sendMessage(msg);

		}

		/**
		 * 
		 * @Description: 向服务器发出post请求,并接收响应
		 * @return String
		 * @throws
		 * @author: fengmengyang
		 * @date: 2015年4月6日
		 */
		public String post(String... params) {
			System.out.println("数组数据：" + params[0] + "--" + params[1]);
			HttpPost post = new HttpPost(Static.LOGIN + "?user.userName="
					+ params[0] + "&user.password=" + params[1]);
			try {
				// 发送post请求
				HttpResponse response = httpClient.execute(post);
				// 如果服务器成功返回相应
				System.out.println(response.getStatusLine().getStatusCode());
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					return HandleJson.parse_json(response).getString("message");
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}
	}
}
