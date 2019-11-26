package com.yiyang.helmetclientevrtool.activity;

import java.util.Date;

import com.yiyang.helmetclientevrtool.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class HelmetBaseActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.helmet_setting_base_main);

		Log.i("shen", "time = " + new Date().getTime());
	}
}
