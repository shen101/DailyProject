package com.shen.activityfragmentdemo;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class PhoneDialPadMainActivity extends GeneralFatherActivity implements OnClickListener {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phone_dialpad_layout);
		initViews();
	}

	private void initViews() {

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}
}
