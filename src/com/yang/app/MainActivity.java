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
import com.yang.fragment.HelpFragment;
import com.yang.fragment.MyzoneFragment;
import com.yang.fragment.RouteQueryFragment;
import com.yang.mybus.R;

public class MainActivity extends ActionBarActivity {
	// 容器id
	private int content_id = R.id.r1;
	private FragmentManager fm;
	private FragmentTransaction ft;
	private TextView tab_routequery, tab_myzone, tab_help;
	
	RouteQueryFragment routeQueryFragment = new RouteQueryFragment();
	HelpFragment helpFragment = new HelpFragment();
	MyzoneFragment myzoneFragment = new MyzoneFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext  
        //注意该方法要再setContentView方法之前实现 
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        this.setTitle("主页面");
        
        tab_routequery = (TextView) findViewById(R.id.tab_route_query);
        tab_myzone = (TextView) findViewById(R.id.tab_myzone);
        tab_help = (TextView) findViewById(R.id.tab_help);
        
        tab_routequery.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				fm = getSupportFragmentManager();
				ft = fm.beginTransaction();
				tab_routequery.setBackgroundColor(Color.GRAY);
				tab_myzone.setBackgroundColor(Color.WHITE);
				tab_help.setBackgroundColor(Color.WHITE);
				ft.replace(content_id, routeQueryFragment);
				ft.commit();
			}
		});
        tab_myzone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				fm = getSupportFragmentManager();
				ft = fm.beginTransaction();
				tab_myzone.setBackgroundColor(Color.GRAY);
				tab_routequery.setBackgroundColor(Color.WHITE);
				tab_help.setBackgroundColor(Color.WHITE);;
				ft.replace(content_id, myzoneFragment);
				ft.commit();
			}
		});
        tab_help.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				fm = getSupportFragmentManager();
				ft = fm.beginTransaction();
				tab_help.setBackgroundColor(Color.GRAY);
				tab_routequery.setBackgroundColor(Color.WHITE);
				tab_myzone.setBackgroundColor(Color.WHITE);
				ft.replace(content_id, helpFragment);
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
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
