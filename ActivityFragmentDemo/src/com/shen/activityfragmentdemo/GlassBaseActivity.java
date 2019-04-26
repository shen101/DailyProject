package com.shen.activityfragmentdemo;

import com.shen.broadcastreceiver.GeneralBroadcastReceiver;
import com.shen.fragment.MainTimeFragment;
import com.shen.service.MonitorPhoneStatusServer;
import com.shen.utils.GlassUtils;
import com.shen.widget.BatteryView;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings.SettingNotFoundException;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

@SuppressLint("Override")
public class GlassBaseActivity extends FragmentActivity {

	private MainTimeFragment main_time_fragment = null;
	private ActionBar mActionBar;
	private Context mContext;
	private BatteryView mBatteryView;
	private LinearLayout actionbar_left_layout, actionbar_right_layout, naim_layout;
	private GeneralBroadcastReceiver mBroadcastReceiver, mwifiReceiver;
	private ImageView wifi_view, mobile_view, gps_view, bluetooth_view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.base_activity_main);
		mContext = GlassBaseActivity.this;
		setCustomActionBarStyle();
		initFM();
		initPhoneService(true);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mBroadcastReceiver = new GeneralBroadcastReceiver(mBatteryView);
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
		register(mFilter);

		IntentFilter back_Filter = new IntentFilter();
		back_Filter.addAction(GlassUtils.ACTION_FRAGMENT_ONBACK_CLICK_TAG);
		back_Filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
		registerReceiver(back_Receiver, back_Filter);

		mwifiReceiver = new GeneralBroadcastReceiver(wifi_view, bluetooth_view);
		IntentFilter wifi_Filter = new IntentFilter();
		wifi_Filter.addAction(WifiManager.RSSI_CHANGED_ACTION);
		wifi_Filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		wifi_Filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		registerReceiver(mwifiReceiver, wifi_Filter);

		GlassUtils.getCurrentMobileSingal(mContext, mobile_view);
		GlassUtils.initGPSSignal(mContext, gps_view, true);
	}

	private BroadcastReceiver back_Receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if ((GlassUtils.ACTION_FRAGMENT_ONBACK_CLICK_TAG).equals(intent.getAction())) {
				onBackPressed();
			} else if (Intent.ACTION_AIRPLANE_MODE_CHANGED.equals(intent.getAction())) {
				try {
					if (!GlassUtils.isAirplaneMode(context)) {
						wifi_view.setVisibility(View.VISIBLE);
						mobile_view.setVisibility(View.VISIBLE);
					} else {
						wifi_view.setVisibility(View.GONE);
						mobile_view.setVisibility(View.GONE);
					}
				} catch (SettingNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	};

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		unregister();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		initPhoneService(false);
		GlassUtils.initGPSSignal(mContext, gps_view, false);
	}

	private void register(IntentFilter mFilter) {
		registerReceiver(mBroadcastReceiver, mFilter);
	}

	private void unregister() {
		unregisterReceiver(mBroadcastReceiver);
		unregisterReceiver(back_Receiver);
		unregisterReceiver(mwifiReceiver);
	}

	private void initFM() {
		FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
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
		wifi_view = (ImageView) mActionBar.getCustomView().findViewById(R.id.wifi_view);
		mobile_view = (ImageView) mActionBar.getCustomView().findViewById(R.id.mobile_view);
		gps_view = (ImageView) mActionBar.getCustomView().findViewById(R.id.gps_view);
		bluetooth_view = (ImageView) mActionBar.getCustomView().findViewById(R.id.bluetooth_view);
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

	private void initPhoneService(boolean mstatus) {
		Intent phone_service = new Intent();
		phone_service.setClass(GlassBaseActivity.this, MonitorPhoneStatusServer.class);
		if (mstatus) {
			startService(phone_service);
		} else {
			stopService(phone_service);
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
}
