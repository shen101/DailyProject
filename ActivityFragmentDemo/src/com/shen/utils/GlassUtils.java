package com.shen.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
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
	public static final String INTENT_INCALL_UI_NUMBER = "intent_incall_ui_number";
	public static final int INTENT_INCALL_UI_NUM = 1000;
	public static final int DURATION = 50;

	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		Log.i(TAG, "timeD = " + timeD);
		if (0 < timeD && timeD < 245) {
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

}
