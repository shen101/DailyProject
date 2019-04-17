package com.shen.activityfragmentdemo;

import com.shen.utils.GlassUtils;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.WindowManager;

@SuppressLint("Override")
public class BaseActivity extends FragmentActivity {

	private ActionBar mActionBar;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.base_activity_main);
		mContext = BaseActivity.this;
		setCustomActionBarStyle();
		
	}

	private void setCustomActionBarStyle() {
		// TODO Auto-generated method stub
		mActionBar = getActionBar();
		if (mActionBar == null) {
			return;
		} else {
			mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM,
					ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
			mActionBar.setCustomView(R.layout.custom_actionbar_layout);
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (GlassUtils.isFastDoubleClick()) {
				return true;
			}
		}
		return super.dispatchTouchEvent(event);
	}
}
