package com.yang.client;

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
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.yang.url.Static;
import com.yang.utils.HandleJson;

/**
 * 
 * ClassName: LoginThread
 * 
 * @Description: 建立网络连接，负责将客户端输入的内容发送给服务器
 * @author: fengmengyang
 * @date: 2015年4月5日
 */
@SuppressLint("HandlerLeak")
public class LoginThread implements Runnable {
	private Handler handler;
	public Handler revHandler;
	String return_message = "";
	HttpClient httpClient = new DefaultHttpClient();

	public LoginThread(Handler handler) {
		this.handler = handler;
	}

	@Override
	public void run() {
		// 为当前线程初始化looper
		Looper.prepare();
		// 接收UI消息的handler
		revHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// 接收UI用户输入的数据
				if (msg.what == 0x345) {
					// 将数据传输到服务器
					try {
						System.out.println("子线程接收的消息："+msg.obj.toString());
						return_message = LoginThread.this.post(msg.obj.toString());
						System.out.println(return_message.toString());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};
	
		
		// 启动looper
		Looper.loop();
		if (!"".equals(return_message)){
			System.out.println(return_message);
		}
		new Thread(){
			@Override
			public void run(){
				Message msg = new Message();
				msg.what = 0x123;
				msg.obj = return_message.toString();
				System.out.println(msg.obj.toString());
				handler.sendMessage(msg);
			}
		}.start();
		
	}

	/**
	 * 
	 * @Description: 向服务器发出post请求,并接收响应
	 * @return boolean
	 * @throws
	 * @author: fengmengyang
	 * @date: 2015年4月6日
	 */
	public String post(String obj) {
		String[] arr = obj.split("&");
		System.out.println("数组数据："+arr[0]+"--"+arr[1]);
		HttpPost post = new HttpPost(Static.LOGIN+"?user.userName="+arr[0]+"&user.password="+arr[1]);
		//HttpPost post = new HttpPost("http://192.168.191.6:8080/MyBus/user/login.action?user.userName=yang&user.password=yang");
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
