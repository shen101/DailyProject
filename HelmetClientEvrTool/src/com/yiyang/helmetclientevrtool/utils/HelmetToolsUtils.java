package com.yiyang.helmetclientevrtool.utils;

import java.util.Locale;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class HelmetToolsUtils {

	// BLE bluetooth service num
	public static final String BLE_BROADCASE_BLUETOOTH_COMMAND_ACTION = "com.yiyang.helmet.BLE_BROADCASE_COMMAND";
	public static final String BLE_BROADCASE_BLUETOOTH_COMMAND_VALUES = "ble_broadcase_command_values";
	public static final String BLE_BROADCASE_BLUETOOTH_COMMAND_CONTENT = "ble_broadcase_command_content";
	public static final String BLE_BROADCASE_BLUETOOTH_COMMAND_TYPE = "ble_broadcase_command_type";
	
    // BLE service connect state
    public static final String BLE_SERVICE_CONNECTED_CHANGE_ACTION = "com.yiyang.helmet.BLE_SERVICE_CHANGE";
    public static final String BLE_SERVICE_CONNECTED_CHANGE_VALUES = "ble_broadcase_service_change";

    // BLE connected devices
    public static final String BLE_CONNECTED_DEVICES_TABLE = "ble_connected_service_table";
    public static final String BLE_CONNECTED_DEVICES_NAME = "ble_connected_service_name";
    public static final String BLE_CONNECTED_DEVICES_STATUS = "ble_connected_service_status";

	public final static String ACTION_UPDATE_DEVICE_LIST = "action.update.device.list";// 更新设备列表
	public final static String ACTION_CONNECTED_ONE_DEVICE = "action.connected.one.device";// 连接上某个设备时发的广播
	public final static String ACTION_RECEIVE_MESSAGE_FROM_DEVICE = "action.receive.message";
	public final static String ACTION_STOP_CONNECT = "action.stop.connect";

	public static final String ZH_LOCALE = "zh";
	public static final String EN_LOCALE = "en";
	private static Toast toast;

	/**
	 * START BLE SERVICE
	 */
	public static final int BLE_COMMAND_CODE_START = 1;
	/**
	 * SCAN BLE DEVICES
	 */
	public static final int BLE_COMMAND_CODE_SCAN = 2;
	/**
	 * SEND BLE DATA
	 */
	public static final int BLE_COMMAND_CODE_DATA = 3;
	/**
	 * CONNECT BLE DEVICE
	 */
	public static final int BLE_COMMAND_CODE_CONNECT = 4;
	/**
	 * CLOSE BLE SERVICE
	 */
	public static final int BLE_COMMAND_CODE_CLOSE = 5;

	/**
	 * Use unified Toast
	 */
	public static void showToast(Context context, int content_id) {
		if (toast == null) {
			toast = Toast.makeText(context, content_id, Toast.LENGTH_SHORT);
		} else {
			toast.setText(content_id);
		}
		toast.show();
	}

	/**
	 * The status bar is completely transparent
	 */
	public static void setStatusBarFullTransparent(Window mWindow) {
		if (Build.VERSION.SDK_INT >= 21) {
			mWindow.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			mWindow.getDecorView()
					.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
			mWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			mWindow.setStatusBarColor(Color.TRANSPARENT);
		} else if (Build.VERSION.SDK_INT <= 19) {
			mWindow.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
	}

	/*
	 * Get the current system default language
	 */
	public static String getCurrentLanguage() {
		return Locale.getDefault().getLanguage();
	}

	/**
	 * dp to px
	 */
	public static int dpTopx(Context mContext, int dp) {
		final float scale = mContext.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	/**
	 * px to dp
	 */
	public static int pxTodp(Context mContext, int px) {
		final float scale = mContext.getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}
}
