package com.yiyang.helmetclientevrtool.service;

import com.yiyang.helmetclientevrtool.utils.HelmetToolsUtils;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class HelmetNotificationCollectorService extends NotificationListenerService {

	private TelephonyManager telephonyManager;

	@Override
	public void onListenerConnected() {
		// TODO Auto-generated method stub
		super.onListenerConnected();
	}

	private PhoneStateListener mPhoneStateListener = new PhoneStateListener() {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			super.onCallStateChanged(state, incomingNumber);
			Log.i("shen", "state = " + state + ",   incomingNumber = " + incomingNumber);
		}

	};

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(TELEPHONY_SERVICE);
		telephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onNotificationPosted(StatusBarNotification sbn) {
		super.onNotificationPosted(sbn);
		Notification notification = sbn.getNotification();
		sbn.getPostTime();
		if (notification == null) {
			return;
		}
		Bundle extras = notification.extras;
		String noti_content = "";
		String noti_title = "";
		String noti_temp = "";
		if (extras != null) {
			noti_title = extras.getString(Notification.EXTRA_TITLE, "");
			noti_content = extras.getString(Notification.EXTRA_TEXT, "");
			noti_temp = extras.getString(Notification.EXTRA_SUMMARY_TEXT, "");
		}
		if (noti_title != null && noti_content != null) {
			sendBroadcaseToService(sbn.getPackageName(), noti_title + ":" + noti_temp, noti_content);
		}
	}

	private void sendBroadcaseToService(String packageName, String noti_title, String noti_content) {

		Log.i("shen", "packageName = " + packageName + ",  noti_title = " + noti_title + ",   noti_content = "
				+ noti_content);

		Intent intent_msg = new Intent();
		intent_msg.setAction(HelmetToolsUtils.NOTIFICATION_MESSAGE_ACTION);
		intent_msg.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent_msg.putExtra(HelmetToolsUtils.NOTIFICATION_MESSAGE_PACKAGE_NUM, packageName);
		intent_msg.putExtra(HelmetToolsUtils.NOTIFICATION_MESSAGE_TITLE_NUM, noti_title);
		intent_msg.putExtra(HelmetToolsUtils.NOTIFICATION_MESSAGE_CONTENTS_NUM, noti_content);
		if (noti_title != null && noti_content != null) {
			sendBroadcast(intent_msg);
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (telephonyManager != null) {
			telephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
		}
	}
}
