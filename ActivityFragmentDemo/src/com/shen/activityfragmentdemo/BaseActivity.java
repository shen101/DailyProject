package com.shen.activityfragmentdemo;

import com.shen.broadcastreceiver.GeneralBroadcastReceiver;
import com.shen.fragment.InCallUIFragment;
import com.shen.fragment.MainTimeFragment;
import com.shen.utils.GlassUtils;
import com.shen.widget.BatteryView;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.LinearLayout;

@SuppressLint("Override")
public class BaseActivity extends FragmentActivity {

	private FragmentManager mFragmentManager = null;
	private FragmentTransaction mFragmentTransaction = null;
	private MainTimeFragment main_time_fragment = null;
	private ActionBar mActionBar;
	private Context mContext;
	private BatteryView mBatteryView;
	private LinearLayout actionbar_left_layout, actionbar_right_layout;
	private GeneralBroadcastReceiver mBroadcastReceiver;
	private TelephonyManager mTelephonyManager;
	private InCallUIFragment incalluifragment = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.base_activity_main);
		mContext = BaseActivity.this;
		initTelephonyListener();
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
		mFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
		mFilter.addAction(GlassUtils.ACTION_PHONE_STATE_CHANGED);
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

	@Override
	public void onStateNotSaved() {
		// TODO Auto-generated method stub
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

	public void initTelephonyListener() {
		mTelephonyManager = (TelephonyManager) getSystemService(Service.TELEPHONY_SERVICE);
		mTelephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
	}

	private PhoneStateListener phoneListener = new PhoneStateListener() {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			super.onCallStateChanged(state, incomingNumber);
			Log.i("shen", "state = " + state + ",  incomingNumber = " + incomingNumber);
			initInCallFragment(state, incomingNumber);
		}
	};

	private void initInCallFragment(int status, String incomingNumber) {

		if ("".equals(incomingNumber)) {
			return;
		}

		// if (!"".equals(GlassUtils.getContact(mContext, incomingNumber))) {
		incomingNumber = GlassUtils.getContact(mContext, incomingNumber);
		Log.i("shen", "get incomingNumber = " + incomingNumber);
		// }

		if (incalluifragment == null) {
			incalluifragment = new InCallUIFragment(mContext, status, incomingNumber);
		}

		switch (status) {
		case TelephonyManager.CALL_STATE_RINGING: // Incoming call
			mActionBar.hide();
			getSupportFragmentManager().beginTransaction().add(R.id.id_content, incalluifragment)
					.disallowAddToBackStack().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
			break;
		case TelephonyManager.CALL_STATE_OFFHOOK: // Outgoing
			mActionBar.hide();
			break;
		case TelephonyManager.CALL_STATE_IDLE: // Hang up
			mActionBar.show();
			getSupportFragmentManager().beginTransaction().remove(incalluifragment).disallowAddToBackStack()
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
			break;
		default:
			break;
		}

	}
}
