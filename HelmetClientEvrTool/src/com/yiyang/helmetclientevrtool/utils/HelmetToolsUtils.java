package com.yiyang.helmetclientevrtool.utils;

import java.util.Locale;
import java.util.UUID;

import com.yiyang.helmetclientevrtool.R;
import com.yiyang.helmetclientevrtool.bean.HelmetSearchBleDevice;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.ScanCallback;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class HelmetToolsUtils {
	
	// Service UUID
	public static UUID CONNECTION_SERVICE_UUID = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
	// Read-only
	public static UUID CHARACTERISTIC_READ_UUID = UUID.fromString("275348FB-C14D-4FD5-B434-7C3F351DEA5F");
	// Read-write
	public static UUID CHARACTERISTIC_WRITE_UUID = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");
	// Read-write characteristic
	public static UUID CHARACTERISTIC_DEMO_UUID = UUID.fromString("BD28E457-4026-4270-A99F-F9BC20182E15");

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
    
	// BLE send data state
	public static final String BLE_SEND_DATA_RESULT_BROADCASE_ACTION = "ble_send_data_result_action";
	public static final String BLE_SEND_DATA_RESULT_BROADCASE_ERROR = "ble_send_data_result_error";
	public static final String BLE_SEND_DATA_RESULT_BROADCASE_TYPE = "ble_send_data_result_type";
    
	// fond BLE device
	public static final String FOND_BLE_DEVICES_ACTION = "find_ble_devices_action";

	public final static String ACTION_UPDATE_DEVICE_LIST = "action.update.device.list";// 更新设备列表
	public final static String ACTION_CONNECTED_ONE_DEVICE = "action.connected.one.device";// 连接上某个设备时发的广播
	public final static String ACTION_RECEIVE_MESSAGE_FROM_DEVICE = "action.receive.message";
	public final static String ACTION_STOP_CONNECT = "action.stop.connect";
	
	public static final String NOTIFICATION_MESSAGE_ACTION = "com.yiyang.notification.NOTIFICATION_MESSAGE";
	public static final String NOTIFICATION_MESSAGE_PACKAGE_NUM = "yiyang_notification_package";
	public static final String NOTIFICATION_MESSAGE_TITLE_NUM = "yiyang_notification_title";
	public static final String NOTIFICATION_MESSAGE_CONTENTS_NUM = "yiyang_notification_contents";
	
	// recover sms broadcase
	public static final String HELMET_RECOVER_NOTICE_PENDING_ACTION = "helmet_recevier_pendding_action";
	public static final String HELMET_CLIENT_MAIN_ACTION = "com.yiyang.helmet.client_MAIN";

	public static final String HELMET_DEFAULT_CHARSET = "utf-8";
	
	public static final String ZH_LOCALE = "zh";
	public static final String EN_LOCALE = "en";
	private static Toast toast;
	
	public static int HELMET_DEFAULT_SEND_SPLIT_LIMIT = 18;
	public static final int HELMET_DEFAULT_NULL_NUM = -1;

	public static final int HELMET_DEFAULT_TEXT_TYPE = 1;
	public static final int HELMET_DEFAULT_NOTICE_TYPE = 2;
	public static final int HELMET_DEFAULT_WLAN_TYPE = 3;
	public static final int HELMET_DEFAULT_CAMERA_TYPE = 4;
	public static final int HELMET_DEFAULT_ACCOUNT_TYPE = 5;
	public static final int HELMET_DEFAULT_SAFETY_TYPE = 6;

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
	 * CLOSE BLE SEARCH
	 */
	public static final int BLE_COMMAND_CODE_CLOSE_SEARCH = 6;
	/**
	 * DISCONNECT
	 */
	public static final int BLE_COMMAND_CODE_DISCONNECT = 7;
	
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
	 * return scan BLE error info
	 */
	public static String getScanError(Context mContext, int error) {
		if (error == ScanCallback.SCAN_FAILED_ALREADY_STARTED) {
			return mContext.getResources().getString(R.string.helmet_text_scan_error_one);
		} else if (error == ScanCallback.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED) {
			return mContext.getResources().getString(R.string.helmet_text_scan_error_two);
		} else if (error == ScanCallback.SCAN_FAILED_INTERNAL_ERROR) {
			return mContext.getResources().getString(R.string.helmet_text_scan_error_three);
		} else if (error == ScanCallback.SCAN_FAILED_FEATURE_UNSUPPORTED) {
			return mContext.getResources().getString(R.string.helmet_text_scan_error_four);
		}
		return mContext.getResources().getString(R.string.helmet_text_scan_error_five);
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
	
	/**
	 * get connected devices
	 */
	public static boolean isConnectedDevices(Context mContext, HelmetSearchBleDevice find_device) {
		BluetoothManager mManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
		for (BluetoothDevice device : mManager.getConnectedDevices(BluetoothProfile.GATT)) {
			if ((device.getAddress()).equals(find_device.getBle_device().getAddress())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * get the Bluetooth Adapter
	 */
	public static BluetoothAdapter getBluetoothAdapter(Context mContext) {
		BluetoothManager mManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
		return mManager.getAdapter().getDefaultAdapter();
	}
}
