package com.shen.activityfragmentdemo;

import com.shen.broadcastreceiver.GeneralBroadcastReceiver;
import com.shen.fragment.MainTimeFragment;
import com.shen.utils.GlassUtils;
import com.shen.widget.BatteryView;
import android.app.ActionBar;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

public class BaseActivity extends FragmentActivity {

	private FragmentManager mFragmentManager = null;
	private FragmentTransaction mFragmentTransaction = null;
	private MainTimeFragment main_time_fragment = null;
	private ActionBar mActionBar;
	private BatteryView mBatteryView;
	private LinearLayout actionbar_left_layout, actionbar_right_layout;
	private GeneralBroadcastReceiver mBroadcastReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.base_activity_main);
		setCustomActionBarStyle();
		initFM();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mBroadcastReceiver = new GeneralBroadcastReceiver(mBatteryView);
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
		register(mFilter);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		unregister();
	}

	private void register(IntentFilter mFilter) {
		registerReceiver(mBroadcastReceiver, mFilter);
	}

	private void unregister() {
		unregisterReceiver(mBroadcastReceiver);
	}

	private void initFM() {
		mFragmentManager = getSupportFragmentManager();
		mFragmentTransaction = mFragmentManager.beginTransaction();
		if (main_time_fragment == null) {
			main_time_fragment = new MainTimeFragment();
			mFragmentTransaction.add(R.id.id_content, main_time_fragment);
		} else {
			mFragmentTransaction.show(main_time_fragment);
		}
		mFragmentTransaction.commit();
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
			initActionBarViews(mActionBar);
		}
	}

	private void initActionBarViews(ActionBar mActionBar) {
		// TODO Auto-generated method stub
		actionbar_left_layout = (LinearLayout) mActionBar.getCustomView().findViewById(R.id.actionbar_left_layout);
		actionbar_right_layout = (LinearLayout) mActionBar.getCustomView().findViewById(R.id.actionbar_right_layout);
		mBatteryView = (BatteryView) mActionBar.getCustomView().findViewById(R.id.actionbar_battery_view);
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
