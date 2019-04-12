package com.shen.activityfragmentdemo;

import com.shen.utils.GlassUtils;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;

@SuppressLint("Override")
public class BaseActivity extends FragmentActivity {

	private ActionBar mActionBar;
	private Context mContext;
	private TelephonyManager mTelephonyManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.base_activity_main);
		mContext = BaseActivity.this;
		setCustomActionBarStyle();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initTelephonyListener();
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == GlassUtils.INTENT_INCALL_UI_NUM && resultCode == Activity.RESULT_OK) {

		}
	}

	private void initInCallFragment(int status, String incomingNumber) {

		if ("".equals(incomingNumber)) {
			return;
		}

		// incomingNumber = GlassUtils.getContact(mContext, incomingNumber);
		// Log.i("shen", "get incomingNumber = " + incomingNumber);

		Intent incallui_intent = new Intent();
		incallui_intent.setAction(GlassUtils.ACTION_START_INCALLUI_TAG);
		incallui_intent.putExtra(GlassUtils.INTENT_INCALL_UI_NUMBER, incomingNumber);

		switch (status) {
		case TelephonyManager.CALL_STATE_RINGING: // Incoming call
			startActivityForResult(incallui_intent, GlassUtils.INTENT_INCALL_UI_NUM);
			break;
		case TelephonyManager.CALL_STATE_OFFHOOK: // Outgoing
			break;
		case TelephonyManager.CALL_STATE_IDLE: // Hang up
			break;
		default:
			break;
		}
	}
}
