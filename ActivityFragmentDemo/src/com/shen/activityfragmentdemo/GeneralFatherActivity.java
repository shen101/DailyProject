package com.shen.activityfragmentdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

public class GeneralFatherActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getActionBar().hide();
	}
}
