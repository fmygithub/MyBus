package com.yang.app;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.yang.fragment.BusQueryFragment;
import com.yang.fragment.HelpFragment;
import com.yang.fragment.RouteQueryFragment;
import com.yang.fragment.StationQueryFragment;
import com.yang.mybus.R;

public class MainActivity extends ActionBarActivity {
	// 容器id
	private int content_id = R.id.r1;
	private FragmentManager fm;
	private FragmentTransaction ft;
	private TextView tab_routequery, tab_busquery, tab_stationquery;
	
	RouteQueryFragment routeQueryFragment = new RouteQueryFragment();
	HelpFragment helpFragment = new HelpFragment();
	StationQueryFragment stationQueryFragment = new StationQueryFragment();
	BusQueryFragment busQueryFragment = new BusQueryFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext  
        //注意该方法要再setContentView方法之前实现 
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        this.setTitle("主页面");
        
        tab_routequery = (TextView) findViewById(R.id.tab_route_query);
        tab_busquery = (TextView) findViewById(R.id.tab_bus_query);
        tab_stationquery = (TextView) findViewById(R.id.tab_station_query);
        
        tab_routequery.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				fm = getSupportFragmentManager();
				ft = fm.beginTransaction();
				tab_routequery.setBackgroundColor(Color.GRAY);
				tab_busquery.setBackgroundColor(Color.WHITE);
				tab_stationquery.setBackgroundColor(Color.WHITE);
				ft.replace(content_id, routeQueryFragment);
				ft.commit();
			}
		});
        tab_busquery.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				fm = getSupportFragmentManager();
				ft = fm.beginTransaction();
				tab_busquery.setBackgroundColor(Color.GRAY);
				tab_routequery.setBackgroundColor(Color.WHITE);
				tab_stationquery.setBackgroundColor(Color.WHITE);;
				ft.replace(content_id, busQueryFragment);
				ft.commit();
			}
		});
        tab_stationquery.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				fm = getSupportFragmentManager();
				ft = fm.beginTransaction();
				tab_stationquery.setBackgroundColor(Color.GRAY);
				tab_routequery.setBackgroundColor(Color.WHITE);
				tab_busquery.setBackgroundColor(Color.WHITE);
				ft.replace(content_id, stationQueryFragment);
				ft.commit();
			}
		});
        
        // 设置默认显示页面
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        tab_routequery.setBackgroundColor(Color.GRAY);
        ft.replace(content_id, routeQueryFragment);
        ft.commit();
        
        // 获取登陆后的用户名。判断登陆
        Intent intent = getIntent();
        String username = (String) intent.getSerializableExtra("username");
        this.setTitle(username);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_stationquery, container, false);
            return rootView;
        }
    }

}
