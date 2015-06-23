package com.yang.app;

import com.yang.mybus.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

public class TestLocation extends Activity{
	private Button locationBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_location);
		locationBtn = (Button) findViewById(R.id.location);
		
	}
}
