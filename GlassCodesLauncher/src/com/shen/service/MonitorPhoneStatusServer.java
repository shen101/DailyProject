package com.shen.service;

import com.shen.utils.GlassUtils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class MonitorPhoneStatusServer extends Service {

	private TelephonyManager mTelephonyManager;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		initTelephonyListener();
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
			Log.i("shen", "state MonitorPhoneStatusServer = " + state + ",  incomingNumber = " + incomingNumber);
			initInCallFragment(state, incomingNumber);
		}
	};

	private void initInCallFragment(int status, String incomingNumber) {

		if ("".equals(incomingNumber)) {
			return;
		}

		Intent incallui_intent = new Intent();
		incallui_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		incallui_intent.setAction(GlassUtils.ACTION_START_INCALLUI_TAG);
		incallui_intent.putExtra(GlassUtils.INTENT_INCALL_UI_NUMBER, incomingNumber);

		switch (status) {
		case TelephonyManager.CALL_STATE_RINGING: // Incoming call
			getApplication().startActivity(incallui_intent);
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
