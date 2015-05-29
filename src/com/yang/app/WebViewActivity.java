package com.yang.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import com.yang.mybus.R;
import com.yang.url.Static;

public class WebViewActivity extends Activity{
	WebView show;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		show = (WebView) findViewById(R.id.show);
		show.loadUrl("http://192.168.191.8:8080/MyBus_Server_1/routeCheckAction_check.action?directRouteViewId.beginStationId=7&directRouteViewId.endStationId=10");
	/*	show.loadUrl(Static.ROUTEQUERY
				+ "?directRouteViewId.beginStationId=7&directRouteViewId.endStationId=10");*/
		//show.loadUrl("http://baidu.com");
		/*//接收数据
		Intent intent = getIntent();
		String html = (String) intent.getSerializableExtra("html");
		System.out.println("html : "+html.toString());
		show.loadDataWithBaseURL(null, html.toString(), "text/html", "utf-8", null);*/
	}
}
