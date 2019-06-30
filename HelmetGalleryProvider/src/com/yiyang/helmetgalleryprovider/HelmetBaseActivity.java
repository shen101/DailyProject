package com.yiyang.helmetgalleryprovider;

import com.yiyang.helmetgalleryprovider.utils.HelmetFileLoadManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class HelmetBaseActivity extends Activity {

	protected boolean isDestroy;
	private boolean clickable = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isDestroy = false;
		HelmetFileLoadManager.getAppManager().addActivity(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		isDestroy = true;
		HelmetFileLoadManager.getAppManager().finishActivity(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		clickable = true;
	}

	protected boolean isClickable() {
		return clickable;
	}

	protected void lockClick() {
		clickable = false;
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
		if (isClickable()) {
			lockClick();
			super.startActivityForResult(intent, requestCode, options);
		}
	}
}
