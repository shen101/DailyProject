package com.shen.utils;

import android.content.Context;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.widget.Toast;

public class GlassUtils {

	private static final String TAG = "Utils";
	private static long lastClickTime;
	private static Toast toast;

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

}
