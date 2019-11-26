package com.yiyang.helmetclientevrtool.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.yiyang.helmetclientevrtool.R;
import com.yiyang.helmetclientevrtool.receiver.HelmetGeneralBroadcast;
import com.yiyang.helmetclientevrtool.utils.HelmetToolsUtils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.ParcelUuid;
import android.util.Log;

@TargetApi(26)
@SuppressLint({ "HandlerLeak", "UseSparseArrays" })
public class HelmetBleBluetoothConntectService extends Service {

	private static final String TAG = "HelmetBleBluetoothConntectService";
	private BluetoothDevice connect_device;
	private BluetoothGatt mGatt;
	private BluetoothManager mManager;
	private BluetoothLeScanner mLeScanner;
	private static final String CHANNEL_ID = "BLE_Service_Id";
	private static final String CHANNEL_DATA = "BLE_Service_Data";
	private byte[] ble_use_content_text = null;
	private byte[] ble_use_content_notice = null;
	private static final int START_SCAN_BLE_NUM = 100;
	private HashMap<Integer, Boolean> bool_result = new HashMap<Integer, Boolean>();
	private Handler mhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case START_SCAN_BLE_NUM:
				if (isBluetoothEnable()) {
					start_scan();
				}
				break;
			default:
				break;
			}
		}
	};

	private MyBinder binder = new MyBinder();

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

			if (HelmetToolsUtils.BLE_BROADCASE_BLUETOOTH_COMMAND_ACTION.equals(intent.getAction())
					&& isBluetoothEnable()) {
				int values = intent.getIntExtra(HelmetToolsUtils.BLE_BROADCASE_BLUETOOTH_COMMAND_VALUES,
						HelmetToolsUtils.HELMET_DEFAULT_NULL_NUM);
				if (values == HelmetToolsUtils.BLE_COMMAND_CODE_START) {
					mhandler.sendEmptyMessageDelayed(START_SCAN_BLE_NUM, 300);
				} else if (values == HelmetToolsUtils.BLE_COMMAND_CODE_SCAN) {
					mhandler.sendEmptyMessageDelayed(START_SCAN_BLE_NUM, 300);
				} else if (values == HelmetToolsUtils.BLE_COMMAND_CODE_DATA) {
					try {
						ble_use_content_text = intent
								.getStringExtra(HelmetToolsUtils.BLE_BROADCASE_BLUETOOTH_COMMAND_CONTENT)
								.getBytes(HelmetToolsUtils.HELMET_DEFAULT_CHARSET);
						send_data(ble_use_content_text, HelmetToolsUtils.HELMET_DEFAULT_TEXT_TYPE);
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else if (values == HelmetToolsUtils.BLE_COMMAND_CODE_CONNECT) {
					connect_device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					if (connect_device != null) {
						mGatt = connect_device.connectGatt(getApplicationContext(), true, mGattCallback);
					}
				} else if (values == HelmetToolsUtils.BLE_COMMAND_CODE_CLOSE_SEARCH) {
					if (mLeScanner != null) {
						mLeScanner.stopScan(mScanCallback);
					}
				} else if (values == HelmetToolsUtils.BLE_COMMAND_CODE_DISCONNECT) {
					disconnection();
				}
			} else if (HelmetToolsUtils.NOTIFICATION_MESSAGE_ACTION.equals(intent.getAction())) {
				String data_package = intent.getStringExtra(HelmetToolsUtils.NOTIFICATION_MESSAGE_PACKAGE_NUM);
				String data_title = intent.getStringExtra(HelmetToolsUtils.NOTIFICATION_MESSAGE_TITLE_NUM);
				String data_content = intent.getStringExtra(HelmetToolsUtils.NOTIFICATION_MESSAGE_CONTENTS_NUM);
				if (!"搜狗输入法".equals(data_content) && !"com.sohu.inputmethod.sogou".equals(data_package)) {
					try {
						ble_use_content_notice = (data_package + ":" + data_title + ":" + data_content)
								.getBytes(HelmetToolsUtils.HELMET_DEFAULT_CHARSET);
						if (data_title != null && data_content != null) {
							send_data(ble_use_content_notice, HelmetToolsUtils.HELMET_DEFAULT_NOTICE_TYPE);
						}
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(intent.getAction())) {
				int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
				if (state == BluetoothAdapter.STATE_OFF) {
					HelmetToolsUtils.showToast(getApplicationContext(), R.string.helmet_text_bluetooth_is_no_open);
				}
			} else if (HelmetToolsUtils.HELMET_RECOVER_NOTICE_PENDING_ACTION.equals(intent.getAction())) {
				Log.i("shen", "values =********************** ");
			}
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return binder;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(HelmetToolsUtils.BLE_BROADCASE_BLUETOOTH_COMMAND_ACTION);
		mFilter.addAction(HelmetToolsUtils.NOTIFICATION_MESSAGE_ACTION);
		mFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		mFilter.addAction(HelmetToolsUtils.HELMET_RECOVER_NOTICE_PENDING_ACTION);
		registerReceiver(mReceiver, mFilter);
		mhandler.sendEmptyMessageDelayed(START_SCAN_BLE_NUM, 300);
		initNotification();

		return super.onStartCommand(intent, flags, startId);
	}

	private void initNotification() {
		// TODO Auto-generated method stub
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			NotificationChannel Channel = new NotificationChannel(CHANNEL_ID, CHANNEL_DATA,
					NotificationManager.IMPORTANCE_HIGH);
			Channel.enableLights(true);
			Channel.setLightColor(Color.RED);
			Channel.setShowBadge(true);
			Channel.setDescription("ytzn");
			Channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
			manager.createNotificationChannel(Channel);

			Intent broadcase_intent = new Intent();
			broadcase_intent.setAction(HelmetToolsUtils.HELMET_RECOVER_NOTICE_PENDING_ACTION);
			broadcase_intent.setClass(getApplicationContext(), HelmetGeneralBroadcast.class);

			PendingIntent action_intent = PendingIntent.getBroadcast(getApplicationContext(), 0, broadcase_intent,
					PendingIntent.FLAG_UPDATE_CURRENT);

			Notification notification = new Notification.Builder(getApplicationContext()).setChannelId(CHANNEL_ID)
					.setContentTitle("bles service").setContentText("运行中...").setWhen(System.currentTimeMillis())
					.setContentIntent(action_intent).setSmallIcon(R.drawable.ic_launcher).build();
			startForeground(1, notification);
		}
	}

	private boolean isBluetoothEnable() {
		if (mManager == null) {
			mManager = (BluetoothManager) getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE);
		}
		return mManager.getAdapter().isEnabled();
	}

	private void start_scan() {
		if (mManager == null) {
			mManager = (BluetoothManager) getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE);
		}
		if (mLeScanner == null) {
			mLeScanner = mManager.getAdapter().getDefaultAdapter().getBluetoothLeScanner();
		}
		mLeScanner.stopScan(mScanCallback);
		ScanFilter scanFilter = new ScanFilter.Builder()
				.setServiceUuid(new ParcelUuid(HelmetToolsUtils.CONNECTION_SERVICE_UUID)).build();
		ArrayList<ScanFilter> filters = new ArrayList<ScanFilter>();
		filters.add(scanFilter);
		ScanSettings settings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_BALANCED).build();
		mLeScanner.startScan(filters, settings, mScanCallback);
	}

	private void disconnection() {
		if (mGatt != null) {
			mGatt.disconnect();
			mGatt.close();
			mGatt = null;
		}
		mhandler.sendEmptyMessageDelayed(START_SCAN_BLE_NUM, 300);
//		HelmetToolsUtils.removeDisconnectDevice(getApplicationContext(), connect_device);
//		HelmetToolsUtils.setHelmetDefaultStatusValues(getApplicationContext(),
//				HelmetToolsUtils.BLE_CONNECTED_DEVICES_TABLE, HelmetToolsUtils.BLE_CONNECTED_DEVICES_STATUS, "-1");
	}

	private ScanCallback mScanCallback = new ScanCallback() {

		@Override
		public void onScanResult(int callbackType, ScanResult result) {
			// TODO Auto-generated method stub
			super.onScanResult(callbackType, result);

//			List<String> save_devices = Arrays
//					.asList(HelmetToolsUtils.getHelmetDefaultStatusValues(getApplicationContext(),
//							HelmetToolsUtils.BLE_CONNECTED_DEVICES_TABLE, HelmetToolsUtils.BLE_CONNECTED_DEVICES_NAME));
			Intent find_helmet_device_intent = new Intent();
			find_helmet_device_intent.setAction(HelmetToolsUtils.FOND_BLE_DEVICES_ACTION);
			find_helmet_device_intent.putExtra(BluetoothDevice.EXTRA_DEVICE, result.getDevice());
//			Log.i(TAG, "onScanResult DEVIE = " + result.getDevice().getName());
//			Log.i(TAG, "save_devices.toString() = " + save_devices.toString());
//			if (save_devices != null && save_devices.contains(result.getDevice().getName())) {
//				connect_device = result.getDevice();
//				mGatt = result.getDevice().connectGatt(getApplicationContext(), true, mGattCallback);
//			} else {
//			}

			if ((mManager.getConnectedDevices(BluetoothProfile.GATT)).size() != 0) {
				find_helmet_device_intent.putExtra(HelmetToolsUtils.BLE_SERVICE_CONNECTED_CHANGE_VALUES, 100);
				sendBroadcast(find_helmet_device_intent);
				mLeScanner.stopScan(mScanCallback);
				return;
			}

			sendBroadcast(find_helmet_device_intent);
		}

		@Override
		public void onScanFailed(int errorCode) {
			// TODO Auto-generated method stub
			super.onScanFailed(errorCode);
			Log.i(TAG, "scan failed info : " + HelmetToolsUtils.getScanError(getApplicationContext(), errorCode));
		}
	};

	private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
			Intent conntect_status_change = new Intent();
			conntect_status_change.setAction(HelmetToolsUtils.BLE_SERVICE_CONNECTED_CHANGE_ACTION);
			conntect_status_change.putExtra(BluetoothDevice.EXTRA_DEVICE, connect_device);

			Log.i(TAG, "status = " + status + ",   newState = " + newState);
			if (newState == BluetoothProfile.STATE_CONNECTED) {
				gatt.discoverServices();
//				HelmetToolsUtils.setHelmetDefaultStatusValues(getApplicationContext(),
//						HelmetToolsUtils.BLE_CONNECTED_DEVICES_TABLE, HelmetToolsUtils.BLE_CONNECTED_DEVICES_NAME,
//						HelmetToolsUtils.addNewConnectedDevice(getApplicationContext(), connect_device.getName()));
//				Log.i("shen", "saved devices == "
//						+ HelmetToolsUtils.addNewConnectedDevice(getApplicationContext(), connect_device.getName()));
//				HelmetToolsUtils.setHelmetDefaultStatusValues(getApplicationContext(),
//						HelmetToolsUtils.BLE_CONNECTED_DEVICES_TABLE, HelmetToolsUtils.BLE_CONNECTED_DEVICES_STATUS,
//						"1");
				mLeScanner.stopScan(mScanCallback);
				Log.i(TAG, "auto connect ble = " + connect_device.getName());
				mGatt = gatt;
			} else {
				mhandler.sendEmptyMessageDelayed(START_SCAN_BLE_NUM, 300);
//				HelmetToolsUtils.setHelmetDefaultStatusValues(getApplicationContext(),
//						HelmetToolsUtils.BLE_CONNECTED_DEVICES_TABLE, HelmetToolsUtils.BLE_CONNECTED_DEVICES_STATUS,
//						"-1");
				Log.i(TAG, "auto disconnect ble");
				mGatt.close();
			}
			conntect_status_change.putExtra(HelmetToolsUtils.BLE_SERVICE_CONNECTED_CHANGE_VALUES, newState);
			sendBroadcast(conntect_status_change);
		};

		@Override
		public void onCharacteristicRead(BluetoothGatt gatt,
				android.bluetooth.BluetoothGattCharacteristic characteristic, int status) {
			if (HelmetToolsUtils.CHARACTERISTIC_WRITE_UUID.equals(characteristic.getUuid())) {
				gatt.setCharacteristicNotification(characteristic, true);
			}
		};

		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			for (BluetoothGattService service : gatt.getServices()) {
				if (HelmetToolsUtils.CONNECTION_SERVICE_UUID.equals(service.getUuid())) {
					gatt.setCharacteristicNotification(
							service.getCharacteristic(HelmetToolsUtils.CHARACTERISTIC_WRITE_UUID), true);
				}
			}
		};

		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
			// TODO Auto-generated method stub
			super.onCharacteristicChanged(gatt, characteristic);
			final int charValue = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT32, 0);
			Log.i(TAG, "charValue = " + charValue);
		}

		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
			// TODO Auto-generated method stub
			super.onCharacteristicWrite(gatt, characteristic, status);
			if (status == BluetoothGatt.GATT_SUCCESS) {
				Log.i(TAG, "write success");
			} else if (status == BluetoothGatt.GATT_FAILURE) {
				Log.i(TAG, "write failed");
			} else if (status == BluetoothGatt.GATT_WRITE_NOT_PERMITTED) {
				Log.i(TAG, "No permission");
			}
		}
	};

	private boolean send_data(byte[] data, int type) {
		if (mGatt != null) {
			return writeCharacteristic(data, type);
		} else {
			Log.i(TAG, "mGatt == null");
			return false;
		}
	}

	/**
	 * Send Regularized data and send a broadcast of the transmission result
	 */
	private boolean writeCharacteristic(byte[] data_content, int type) {

		int index = HelmetToolsUtils.HELMET_DEFAULT_NULL_NUM;

		if (mGatt == null) {
			Log.i(TAG, "mGatt not initialized");
			return sendBroadcase("mGatt not initialized", index, false);
		}

		BluetoothGattService service = mGatt.getService(HelmetToolsUtils.CONNECTION_SERVICE_UUID);
		if (service == null) {
			Log.w(TAG, "service not initialized");
			return sendBroadcase("service not initialized", index, false);
		}

		BluetoothGattCharacteristic mGattCharacteristic = service
				.getCharacteristic(HelmetToolsUtils.CHARACTERISTIC_WRITE_UUID);
		if (mGattCharacteristic == null) {
			Log.w(TAG, "characteristic not initialized");
			return sendBroadcase("characteristic not initialized", index, false);
		}

		mGatt.beginReliableWrite();

		Log.i(TAG, "limit = " + HelmetToolsUtils.HELMET_DEFAULT_SEND_SPLIT_LIMIT);

		bool_result.clear();

		byte[] buffer_byte = null;
		String head_content = "";

		if (type == HelmetToolsUtils.HELMET_DEFAULT_TEXT_TYPE) { // text
			head_content = HelmetToolsUtils.HELMET_DEFAULT_TEXT_TYPE + ":" + data_content.length;
		} else if (type == HelmetToolsUtils.HELMET_DEFAULT_NOTICE_TYPE) { // notification
			head_content = HelmetToolsUtils.HELMET_DEFAULT_NOTICE_TYPE + ":" + data_content.length;
		} else if (type == HelmetToolsUtils.HELMET_DEFAULT_WLAN_TYPE) { // wifi_ap
			head_content = HelmetToolsUtils.HELMET_DEFAULT_WLAN_TYPE + ":" + data_content.length;
		} else if (type == HelmetToolsUtils.HELMET_DEFAULT_CAMERA_TYPE) { // camera
			head_content = HelmetToolsUtils.HELMET_DEFAULT_CAMERA_TYPE + ":" + data_content.length;
		} else if (type == HelmetToolsUtils.HELMET_DEFAULT_SAFETY_TYPE) { // safety
			head_content = HelmetToolsUtils.HELMET_DEFAULT_SAFETY_TYPE + ":" + data_content.length;
		} else if (type == HelmetToolsUtils.HELMET_DEFAULT_ACCOUNT_TYPE) { // account
			head_content = HelmetToolsUtils.HELMET_DEFAULT_ACCOUNT_TYPE + ":" + data_content.length;
		}
		try {
			buffer_byte = head_content.getBytes(HelmetToolsUtils.HELMET_DEFAULT_CHARSET);
			ByteArrayInputStream head_bais = new ByteArrayInputStream(buffer_byte);
			buffer_byte = new byte[buffer_byte.length];
			head_bais.read(buffer_byte);
			writeSendCharacteristic(mGatt, mGattCharacteristic, buffer_byte, 1);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ByteArrayInputStream bais = new ByteArrayInputStream(data_content);

		try {
			if (data_content.length < HelmetToolsUtils.HELMET_DEFAULT_SEND_SPLIT_LIMIT && data_content.length != 0) {
				buffer_byte = new byte[data_content.length];
				bais.read(buffer_byte);
				writeSendCharacteristic(mGatt, mGattCharacteristic, buffer_byte, 2);
			} else if (data_content.length > HelmetToolsUtils.HELMET_DEFAULT_SEND_SPLIT_LIMIT) {
				buffer_byte = new byte[HelmetToolsUtils.HELMET_DEFAULT_SEND_SPLIT_LIMIT];
				for (int i = 0; i < data_content.length / HelmetToolsUtils.HELMET_DEFAULT_SEND_SPLIT_LIMIT; i++) {
					bais.read(buffer_byte);
					writeSendCharacteristic(mGatt, mGattCharacteristic, buffer_byte, 3 + i);
				}
				buffer_byte = new byte[data_content.length % HelmetToolsUtils.HELMET_DEFAULT_SEND_SPLIT_LIMIT];
				bais.read(buffer_byte);
				writeSendCharacteristic(mGatt, mGattCharacteristic, buffer_byte,
						4 + data_content.length / HelmetToolsUtils.HELMET_DEFAULT_SEND_SPLIT_LIMIT);
			}
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sendBroadcase("", index, (index == HelmetToolsUtils.HELMET_DEFAULT_NULL_NUM) ? true : false);
	}

	private boolean writeSendCharacteristic(BluetoothGatt mGatt, BluetoothGattCharacteristic mGattCharacteristic,
			byte[] data_byte, int index) {
		boolean send_result = false;

		try {
			mGattCharacteristic.setValue(data_byte);
			send_result = mGatt.writeCharacteristic(mGattCharacteristic);
			bool_result.put(index, send_result);
			Thread.sleep(200);
			if (!send_result) {
				return sendBroadcase("", index, true);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sendBroadcase("", HelmetToolsUtils.HELMET_DEFAULT_NULL_NUM, false);
	}

	private boolean sendBroadcase(String error_content, int error_type, boolean status) {
		Intent send_result_broad = new Intent();
		send_result_broad.setAction(HelmetToolsUtils.BLE_SEND_DATA_RESULT_BROADCASE_ACTION);
		send_result_broad.putExtra(HelmetToolsUtils.BLE_SEND_DATA_RESULT_BROADCASE_ERROR, error_content);
		send_result_broad.putExtra(HelmetToolsUtils.BLE_SEND_DATA_RESULT_BROADCASE_TYPE, error_type);
		sendBroadcast(send_result_broad);
		return status;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i(TAG, "onDestroy");
	}

	public class MyBinder extends Binder {

		public HelmetBleBluetoothConntectService getService() {
			return HelmetBleBluetoothConntectService.this;
		}
	}
}
