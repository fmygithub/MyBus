package com.yang.thread;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.yang.entity.User;
import com.yang.url.Static;

/**
 * 
 * ClassName: LoginRegisterThread
 * 
 * @Description: 注册子线程
 * @author: fengmengyang
 * @date: 2015年4月9日
 */
public class LoginRegisterThread implements Runnable {
	private Handler handler;
	private User user;
	private String flag; //标记是登录请求还是注册请求

	public LoginRegisterThread(Handler handler, User user, String flag) {
		this.handler = handler;
		this.user = user;
		this.flag = flag;
	}


	@Override
	public void run() {
		// 接收服务器返回的消息
		String return_message = post(user);

		Message msg = new Message();
		if (flag.equals("register")) {
			msg.what = 0x124;
		} else if (flag.equals("login")) {
			msg.what = 0x123;
		}
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
	private String post(User user) {
		HttpClient client = new DefaultHttpClient();
		try {
			HttpPost post = null;
			if (flag.equals("register")) {
				post = new HttpPost(Static.REGISTER);
			} else if (flag.equals("login")) {
				post = new HttpPost(Static.LOGIN);
			}
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			Log.i("username", user.getUserName());
			params.add(new BasicNameValuePair("userName",
					user.getUserName()));
			params.add(new BasicNameValuePair("password",
					user.getPassword()));
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String json = EntityUtils.toString(response.getEntity(),
						"utf-8");
				JSONObject object = new JSONObject(json).getJSONObject("json");
				return object.getString("message");
			} else {
				System.out.println("响应异常，响应码："
						+ response.getStatusLine().getStatusCode());
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
