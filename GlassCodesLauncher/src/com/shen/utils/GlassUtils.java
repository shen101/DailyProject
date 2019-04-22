package com.shen.utils;

import java.lang.reflect.Method;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.android.internal.telephony.ITelephony;

import android.app.backup.BackupManager;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.widget.Toast;

public class GlassUtils {

	private static final String TAG = "Utils";
	private static long lastClickTime;
	private static Toast toast;

	public static final String ACTION_PHONE_STATE_CHANGED = "android.intent.action.PHONE_STATE";
	public static final String ACTION_START_INCALLUI_TAG = "com.yiyang.glass.GLASS_INCALLUI_MAIN";
	public static final String ACTION_START_DIALPAD_TAG = "com.yiyang.glass.GLASS_DIALPAD_MAIN";
	public static final String ACTION_IS_CONNECTED_CALL_TAG = "com.yiyang.glass.ACTION_IS_CONNECTED";
	public static final String ACTION_FRAGMENT_ONBACK_CLICK_TAG = "com.shen.BACK_ONCLICK";
	public static final String INTENT_INCALL_UI_NUMBER = "intent_incall_ui_number";
	public static final String MMI_IMEI_DISPLAY = "*#06#";
	public static final int INTENT_INCALL_UI_NUM = 1000;
	public static final int DURATION = 50;

	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		Log.i(TAG, "timeD = " + timeD);
		if (0 < timeD && timeD < 400) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	public static void showToast(Context context, String content) {
		if (toast == null) {
			toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
		} else {
			toast.setText(content);
		}
		toast.show();
	}

	private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

	static {
		ORIENTATIONS.append(Surface.ROTATION_0, 90);
		ORIENTATIONS.append(Surface.ROTATION_90, 0);
		ORIENTATIONS.append(Surface.ROTATION_180, 270);
		ORIENTATIONS.append(Surface.ROTATION_270, 180);
	}

	public static int getOrientation(int rotation, int sensorOrientation) {
		return (ORIENTATIONS.get(rotation) + sensorOrientation + 270) % 360;
	}

	public static String getContact(Context mcontext, String mNumber) {
		String[] projection = { ContactsContract.PhoneLookup.DISPLAY_NAME,
				ContactsContract.CommonDataKinds.Phone.NUMBER };

		Cursor cursor = mcontext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				projection, ContactsContract.CommonDataKinds.Phone.NUMBER + " = '" + mNumber + "'", null, null);

		if (cursor == null) {
			return "";
		}
		Log.d(TAG, "getPeople cursor.getCount() = " + cursor.getCount());

		String name = "";

		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
			name = cursor.getString(nameFieldColumnIndex);
		}
		return name;
	}

	/**
	 * Determine if the entered phone number is qualified
	 */
	public static boolean isAllowTelePhoneNumber(String inputText) {
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(inputText);
		return m.matches();
	}

	/**
	 * EndPhone
	 */
	public static void endPhone(Context c, TelephonyManager tm) {
		try {
			ITelephony iTelephony;
			Method getITelephonyMethod = TelephonyManager.class.getDeclaredMethod("getITelephony", (Class[]) null);
			getITelephonyMethod.setAccessible(true);
			iTelephony = (ITelephony) getITelephonyMethod.invoke(tm, (Object[]) null);
			iTelephony.endCall();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * set current language
	 */
	public static void setCurrentLanguage(Locale locale) {
		try {
			Object objIActMag, objActMagNative;

			Class clzIActMag = Class.forName("android.app.IActivityManager");

			Class clzActMagNative = Class.forName("android.app.ActivityManagerNative");

			Method mtdActMagNative$getDefault = clzActMagNative.getDeclaredMethod("getDefault");

			objIActMag = mtdActMagNative$getDefault.invoke(clzActMagNative);

			Method mtdIActMag$getConfiguration = clzIActMag.getDeclaredMethod("getConfiguration");

			Configuration config = (Configuration) mtdIActMag$getConfiguration.invoke(objIActMag);

			config.locale = locale;

			Class clzConfig = Class.forName("android.content.res.Configuration");
			java.lang.reflect.Field userSetLocale = clzConfig.getField("userSetLocale");
			userSetLocale.set(config, true);

			// 此处需要声明权限:android.permission.CHANGE_CONFIGURATION
			// 会重新调用 onCreate();
			Class[] clzParams = { Configuration.class };

			Method mtdIActMag$updateConfiguration = clzIActMag.getDeclaredMethod("updateConfiguration", clzParams);

			mtdIActMag$updateConfiguration.invoke(objIActMag, config);

			BackupManager.dataChanged("com.android.providers.settings");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
