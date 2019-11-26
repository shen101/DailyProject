package com.yiyang.helmetclientevrtool.activity;

import java.util.ArrayList;

import com.yiyang.helmetclientevrtool.R;
import com.yiyang.helmetclientevrtool.adapter.HelmetBleDeviceAdapter;
import com.yiyang.helmetclientevrtool.bean.HelmetSearchBleDevice;
import com.yiyang.helmetclientevrtool.interfaces.OnItemBackListener;
import com.yiyang.helmetclientevrtool.utils.HelmetToolsUtils;
import android.app.ActionBar;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

public class HelmetSettingBleConnectActivity extends HelmetBaseActivity implements OnItemBackListener{

	private ActionBar mActionBar;
	private ImageButton actionbar_back, actionbar_helmet_status;
	private TextView actionbar_title, on_off_status;
	private RelativeLayout on_off_layout;
	private Switch on_off_switch;
	private ImageView actionbar_back_btn, actionbar_save_btn;
	private boolean isExitsConnectedDevice = false;
	private Button scan_ble_devices_btn, disconnect_device_btn;
	private ListView connected_list_devices;
	private BluetoothAdapter mAdapter;
	private BluetoothManager mManager;
	private ProgressBar bluetooth_scan_status;
	private ArrayList<String> temp_names = new ArrayList<String>();
	private HelmetBleDeviceAdapter mScanAdapter;

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (HelmetToolsUtils.BLE_SERVICE_CONNECTED_CHANGE_ACTION.equals(intent.getAction())
					&& on_off_switch.isChecked()) {
				int status = intent.getIntExtra(HelmetToolsUtils.BLE_SERVICE_CONNECTED_CHANGE_VALUES, -1);
				Log.i("HelmetBleBluetoothConntectService", "status = " + status);
				BluetoothDevice connect_device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				if (on_off_switch.isChecked()) {
					if (status == 2) {
						bluetooth_scan_status.setVisibility(View.GONE);
						updateListDevice(connect_device, true);
						scan_ble_devices_btn.setEnabled(false);
						disconnect_device_btn.setEnabled(true);
					} else {
						bluetooth_scan_status.setVisibility(View.VISIBLE);
						updateListDevice(connect_device, false);
						scan_ble_devices_btn.setEnabled(true);
						disconnect_device_btn.setEnabled(false);
					}
				}
			} else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(intent.getAction())) {
				int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
				if (state == BluetoothAdapter.STATE_ON) {
					on_off_switch.setChecked(true);
				} else if (state == BluetoothAdapter.STATE_OFF) {
					on_off_switch.setChecked(false);
				}
			} else if (HelmetToolsUtils.FOND_BLE_DEVICES_ACTION.equals(intent.getAction())) {
				BluetoothDevice find_device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				HelmetSearchBleDevice ble_device = new HelmetSearchBleDevice();
				ble_device.setBle_device(find_device);
				int status = intent.getIntExtra(HelmetToolsUtils.BLE_SERVICE_CONNECTED_CHANGE_VALUES, -1);
				if (status == 100) {
					bluetooth_scan_status.setVisibility(View.GONE);
					scan_ble_devices_btn.setEnabled(false);
					disconnect_device_btn.setEnabled(true);
				} else {
					addNewDevicesToList(context,ble_device);
				}
			}
		}
	};

	private void updateListDevice(BluetoothDevice pair_device, boolean is_top) {
		Log.i("HelmetBleBluetoothConntectService", "mScanAdapter.getCount() = " + mScanAdapter.getCount());
		if (mScanAdapter.getCount() != 0) {
			HelmetSearchBleDevice ble_pair_device = new HelmetSearchBleDevice();
			ble_pair_device.setBle_device(pair_device);
			mScanAdapter.removeDevice(ble_pair_device);
			if (is_top) {
				mScanAdapter.addBluetoothTopDevice(ble_pair_device);
				isExitsConnectedDevice = true;
			} else {
				mScanAdapter.addBluetoothTopDevice(ble_pair_device);
				isExitsConnectedDevice = false;
			}
		}
	}

	private void addNewDevicesToList(Context mContext, HelmetSearchBleDevice find_device) {
		if (!temp_names.contains(find_device.getBle_device().getName())) {
			temp_names.add(find_device.getBle_device().getName());
			if (!HelmetToolsUtils.isConnectedDevices(mContext, find_device)) {
				mScanAdapter.addBluetoothDevice(find_device);
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.helmet_setting_ble_connect_main);
		initViews();
		
		setCustomActionBarStyle();
	}

	private void setCustomActionBarStyle() {
		// TODO Auto-generated method stub
		mActionBar = getActionBar();
		if (mActionBar == null) {
			return;
		} else {
			mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM,
					ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
			mActionBar.setCustomView(R.layout.helmet_custom_actionbar_main);
			initActionBarViews(mActionBar);
		}
	}

	private void initActionBarViews(ActionBar mActionBar2) {
		// TODO Auto-generated method stub
		actionbar_back_btn = (ImageView) mActionBar.getCustomView().findViewById(R.id.helmet_actionbar_back_icon);
		actionbar_back_btn.setOnClickListener(listener);
		actionbar_save_btn = (ImageView) mActionBar.getCustomView().findViewById(R.id.helmet_actionbar_save_icon);
		actionbar_save_btn.setVisibility(View.GONE);
		actionbar_title = (TextView) mActionBar.getCustomView().findViewById(R.id.helmet_actionbar_title);
		actionbar_title.setText(getTitle());
	}

	private void initViews() {
		mManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mAdapter = HelmetToolsUtils.getBluetoothAdapter(this);
		on_off_status = findViewById(R.id.helmet_bluetooth_on_off_txt);
		on_off_layout = findViewById(R.id.helmet_settings_bluetooth_on_off_layout);
		on_off_layout.setOnClickListener(listener);
		on_off_switch = findViewById(R.id.helmet_settings_bluetooth_on_off_switch);
		on_off_switch.setOnCheckedChangeListener(checkedListener);
		connected_list_devices = findViewById(R.id.connected_list_devices);
		bluetooth_scan_status = findViewById(R.id.bluetooth_connect_status_bar);
		bluetooth_scan_status.setVisibility(View.GONE);
		scan_ble_devices_btn = findViewById(R.id.helmet_settings_bluetoothble_scan_btn);
		scan_ble_devices_btn.setOnClickListener(listener);
		disconnect_device_btn = findViewById(R.id.helmet_settings_bluetoothble_disconnect_btn);
		disconnect_device_btn.setOnClickListener(listener);
		mScanAdapter = new HelmetBleDeviceAdapter(this);
		connected_list_devices.setAdapter(mScanAdapter);
		mScanAdapter.setOnItemBackListener(this);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		on_off_switch.setChecked(mAdapter.isEnabled() ? true : false);

		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(HelmetToolsUtils.BLE_SERVICE_CONNECTED_CHANGE_ACTION);
		mFilter.addAction(HelmetToolsUtils.FOND_BLE_DEVICES_ACTION);
		mFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		registerReceiver(mReceiver, mFilter);

		initConnectedDevice();

		if (on_off_switch.isChecked()) {
			if (isExitsConnectedDevice) {
				scan_ble_devices_btn.setEnabled(false);
				disconnect_device_btn.setEnabled(true);
			} else {
				scan_ble_devices_btn.setEnabled(true);
				disconnect_device_btn.setEnabled(false);
			}
		} else {
			scan_ble_devices_btn.setEnabled(false);
			disconnect_device_btn.setEnabled(false);
		}
	}

	private OnCheckedChangeListener checkedListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub
			on_off_status.setText(isChecked ? R.string.helmet_setting_ble_turn_on_title
					: R.string.helmet_setting_ble_shut_down_title);
			scan_ble_devices_btn.setEnabled(isChecked);
			disconnect_device_btn.setEnabled(isChecked);
			if (isChecked) {
				mAdapter.enable();
			} else {
				mScanAdapter.removeAll();
				temp_names.clear();
				mAdapter.disable();
				bluetooth_scan_status.setVisibility(View.GONE);
			}
		}
	};

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.helmet_actionbar_back_icon:
				finish();
				break;
			case R.id.helmet_actionbar_save_icon:
				break;
			case R.id.helmet_settings_bluetooth_on_off_layout:
				on_off_switch.setChecked(on_off_switch.isChecked() ? false : true);
				break;
			case R.id.helmet_settings_bluetoothble_scan_btn:
				updateBleSearchStatus(true);
				mScanAdapter.removeAll();
				temp_names.clear();
				break;
			case R.id.helmet_settings_bluetoothble_disconnect_btn:
				disconnectDevice();
				break;
			default:
				break;
			}
		}
	};

	private void disconnectDevice() {
		Intent disconnect_intent = new Intent();
		disconnect_intent.setAction(HelmetToolsUtils.BLE_BROADCASE_BLUETOOTH_COMMAND_ACTION);
		disconnect_intent.putExtra(HelmetToolsUtils.BLE_BROADCASE_BLUETOOTH_COMMAND_VALUES,
				HelmetToolsUtils.BLE_COMMAND_CODE_DISCONNECT);
		sendBroadcast(disconnect_intent);
		mScanAdapter.removeAll();
		temp_names.clear();
		disconnect_device_btn.setEnabled(false);
		scan_ble_devices_btn.setEnabled(true);
	}

	private void updateBleSearchStatus(boolean status) {
		Intent scan_intent = new Intent();
		scan_intent.setAction(HelmetToolsUtils.BLE_BROADCASE_BLUETOOTH_COMMAND_ACTION);
		if (status) {
			scan_intent.putExtra(HelmetToolsUtils.BLE_BROADCASE_BLUETOOTH_COMMAND_VALUES,
					HelmetToolsUtils.BLE_COMMAND_CODE_SCAN);
		} else {
			scan_intent.putExtra(HelmetToolsUtils.BLE_BROADCASE_BLUETOOTH_COMMAND_VALUES,
					HelmetToolsUtils.BLE_COMMAND_CODE_CLOSE_SEARCH);
		}
		sendBroadcast(scan_intent);
	}

	@Override
	public void listener(HelmetSearchBleDevice device) {
		// TODO Auto-generated method stub
		Intent ble_service_command = new Intent();
		ble_service_command.setAction(HelmetToolsUtils.BLE_BROADCASE_BLUETOOTH_COMMAND_ACTION);
		ble_service_command.putExtra(HelmetToolsUtils.BLE_BROADCASE_BLUETOOTH_COMMAND_VALUES,
				HelmetToolsUtils.BLE_COMMAND_CODE_CONNECT);
		ble_service_command.putExtra(BluetoothDevice.EXTRA_DEVICE, device);
		sendBroadcast(ble_service_command);
	}

	private void initConnectedDevice() {
		for (BluetoothDevice device : mManager.getConnectedDevices(BluetoothProfile.GATT)) {
			HelmetSearchBleDevice ble_connected_device = new HelmetSearchBleDevice();
			ble_connected_device.setBle_device(device);
			mScanAdapter.addBluetoothTopDevice(ble_connected_device);
			stopScanBroadcase();
			isExitsConnectedDevice = true;
		}
	}

	private void stopScanBroadcase() {
		Intent stop_intent = new Intent();
		stop_intent.setAction(HelmetToolsUtils.BLE_BROADCASE_BLUETOOTH_COMMAND_ACTION);
		stop_intent.putExtra(HelmetToolsUtils.BLE_BROADCASE_BLUETOOTH_COMMAND_VALUES,
				HelmetToolsUtils.BLE_COMMAND_CODE_CLOSE_SEARCH);
		sendBroadcast(stop_intent);
	}
}
