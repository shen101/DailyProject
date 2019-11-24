package com.yiyang.helmetclientevrtool.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import com.yiyang.helmetclientevrtool.R;
import com.yiyang.helmetclientevrtool.adapter.HelmetBleDeviceAdapter;
import com.yiyang.helmetclientevrtool.bean.HelmetBleBluetoothInfo;
import com.yiyang.helmetclientevrtool.utils.HelmetToolsUtils;

public class HelmetSettingBleConnectActivity extends HelmetBaseActivity {

	private static final String TAG = "LinkBluetoothLEActivity";
	private ActionBar mActionBar;
	private Button search;
	// ADD_S by jiangc@2019/5/28 临时添加，取消设备绑定，后面根据新UI添加到指定的地方
	private Button unbind;
	// ADD_E by jiangc@2019/5/28
	private ListView listview;
	private ArrayList<HelmetBleBluetoothInfo> list = new ArrayList<HelmetBleBluetoothInfo>();
	private HelmetBleDeviceAdapter adapter;

	private MsgReceiver receiver;
	// ADD_S by jiangc@2019/5/10 //已连接列表的list，用于更新连接状态
	List<BluetoothDevice> connectDevices = new ArrayList<>();
	List<String> cDevices = new ArrayList<>();
	private ProgressBar progressbarl;
	private TextView connectedDevice, bluetooth_on_off_text;

	public static boolean isVisible = false;
	private TextView actionbar_title;
	private boolean mBound = false;
	private ImageView actionbar_back_btn, actionbar_save_btn;
	private CheckBox switchView;
	private RelativeLayout on_off_layout;
	private ImageView back_image;
	private BluetoothAdapter blueAdapter;
	private BluetoothManager blueManager;
	private static final int UPDATE_SCAN_STATUS = 100;
	private static final int UPDATE_SCAN_INTERVAL_TIME = 300;
	private static final int HANDLE_COMM_BLE_CONNECT_DISCONNECT = 0xba0b;

	private ArrayList<String> temp_address = new ArrayList<String>();

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.helmet_setting_ble_connect_main);
		initView();
		initData();
		registerReceiver();
		isVisible = true;
	}

	private void initView() {
		blueManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);

		listview = (ListView) findViewById(R.id.list_devices);
		search = (Button) findViewById(R.id.helmet_settings_ble_search_btn);
		// ADD_S by jiangc@2019/5/28 临时添加
		unbind = (Button) findViewById(R.id.helmet_settings_ble_unbind_btn);
		// ADD_E by jinagc@2019/5/28
		search.setOnClickListener(clicklistener);
		connectedDevice = (TextView) findViewById(R.id.connected_device);

		bluetooth_on_off_text = findViewById(R.id.startble);
		switchView = findViewById(R.id.helmet_settings_ble_on_off);
		on_off_layout = findViewById(R.id.two_layout);
		on_off_layout.setOnClickListener(clicklistener);

		progressbarl = findViewById(R.id.progressbarl);
		switchView.setOnCheckedChangeListener(checkedListener);

		// ADD_S by jiangc@2019/5/28 临时添加
		unbind.setOnClickListener(clicklistener);
		// ADD_E by jinagc@2019/5/28

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

	private void initActionBarViews(ActionBar mActionBar) {
		// TODO Auto-generated method stub
		actionbar_back_btn = (ImageView) mActionBar.getCustomView().findViewById(R.id.helmet_actionbar_back_icon);
		actionbar_back_btn.setOnClickListener(clicklistener);
		actionbar_save_btn = (ImageView) mActionBar.getCustomView().findViewById(R.id.helmet_actionbar_save_icon);
		actionbar_save_btn.setVisibility(View.GONE);
		actionbar_title = (TextView) mActionBar.getCustomView().findViewById(R.id.helmet_actionbar_title);
		actionbar_title.setText(getTitle());
	}

	private CompoundButton.OnCheckedChangeListener checkedListener = new CompoundButton.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if (buttonView.isPressed()) {
				updateOnOffView(isChecked);
				updateBleUtilsService(isChecked);
				if (isChecked) {
					// handler.sendEmptyMessageDelayed(UPDATE_SCAN_STATUS,
					// UPDATE_SCAN_INTERVAL_TIME);
				}
			}
			search.setEnabled(isChecked);
			listview.setVisibility(isChecked ? View.VISIBLE : View.GONE);
		}
	};

	private void updateOnOffView(boolean status) {
		if (status) {
			bluetooth_on_off_text.setText(R.string.helmet_setting_ble_turn_on_title);
		} else {
			list.clear();
			connectedDevice.setText("");
			adapter.notifyDataSetChanged();
			bluetooth_on_off_text.setText(R.string.helmet_setting_ble_shut_down_title);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		blueAdapter = blueManager.getAdapter();
	}

	private void registerReceiver() {
		receiver = new MsgReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(HelmetToolsUtils.ACTION_UPDATE_DEVICE_LIST);
		intentFilter.addAction(HelmetToolsUtils.ACTION_CONNECTED_ONE_DEVICE);
		intentFilter.addAction(HelmetToolsUtils.ACTION_RECEIVE_MESSAGE_FROM_DEVICE);
		intentFilter.addAction(HelmetToolsUtils.ACTION_STOP_CONNECT);
		intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		intentFilter.addAction(HelmetToolsUtils.BLE_SERVICE_CONNECTED_CHANGE_ACTION);
		registerReceiver(receiver, intentFilter);
	}

	private void initData() {
		adapter = new HelmetBleDeviceAdapter(this, list);
		listview.setAdapter(adapter);
	}

	protected void updateBleUtilsService(boolean status) {
		Intent service_intent = new Intent();
		// service_intent.putExtra(Util.BLE_BROADCASE_BLUETOOTH_COMMAND_VALUES,
		// Util.BLE_COMMAND_CODE_START);
		// service_intent.setClass(this,
		// HelmetBleBluetoothConntectService.class);
		if (status) {
			// if (!getBleServiceIsExisted()) {
			// startService(service_intent);
			// }
		} else {
			stopService(service_intent);
		}
	}

	// private boolean getBleServiceIsExisted() {
	//// return Util.isServiceExisted(this,
	// HelmetBleBluetoothConntectService.class.getName());
	// }

	/**
	 * 开启扫描，在打开这个界面时就开始扫描
	 */
	// ADD_S by jiangc@2019/5/13 增加单独的扫描方法
	private void startScan() {
		// list.clear();
		// connectedDevice.setText("");
		// adapter.notifyDataSetChanged();
		// if (!binder.init()) {//手机不支持蓝牙
		// Toast.makeText(HelmetSettingBleConnectActivity.this,
		// getResources().getString(R.string.not_supported_bluetooth),
		// Toast.LENGTH_SHORT).show();
		// return;//手机不支持蓝牙就啥也不用干了
		// }
		// if (!binder.isBleEnable()) {// 如果蓝牙还没有打开
		// Toast.makeText(HelmetSettingBleConnectActivity.this,
		// getResources().getString(R.string.open_bluetooth),
		// Toast.LENGTH_SHORT).show();
		// return;
		// }
		// binder.startScan();
	}
	// ADD_E by jiangc@2019/5/13

	private OnClickListener clicklistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.two_layout:
				if (switchView.isChecked()) {
					switchView.setChecked(false);
					updateOnOffView(false);
					updateBleUtilsService(false);
				} else {
					switchView.setChecked(true);
					updateOnOffView(true);
					updateBleUtilsService(true);
					// handler.sendEmptyMessageDelayed(UPDATE_SCAN_STATUS,
					// UPDATE_SCAN_INTERVAL_TIME);
				}
				break;
			case R.id.helmet_actionbar_back_icon:
				finish();
				break;

			default:
				break;
			}
		}
	};

	private void scan_ble_devices() {
		list.clear();// 清空上次的搜索结果
		// connectedDevice.setText("");
		// adapter.notifyDataSetChanged();
		// if (!binder.init()) {// 手机不支持蓝牙
		// Toast.makeText(HelmetSettingBleConnectActivity.this,
		// getResources().getString(R.string.not_supported_bluetooth),
		// Toast.LENGTH_SHORT).show();
		// return;// 手机不支持蓝牙就啥也不用干了
		// }
		//
		// if (!binder.isBleEnable()) {// 如果蓝牙还没有打开
		// Toast.makeText(HelmetSettingBleConnectActivity.this,
		// getResources().getString(R.string.open_bluetooth),
		// Toast.LENGTH_SHORT).show();
		// return;
		// }
		// progressbarl.setVisibility(View.VISIBLE);
		// // new GetDataTask().execute();// 搜索任务
		// // add by yintao start 解除绑定顺便把BLE连接也断掉
		// binder.disconnect();
		// // add by yintao end 解除绑定顺便把BLE连接也断掉
		// binder.startScan();

		// ADD_S by jiangc@2019/5/28 临时添加
	}

	// DEL_S by jiangc@2019/5/23 没用，删掉
	/*
	 * private void scanBleDeviceService() { Intent scan_intent = new Intent();
	 * scan_intent.setAction(Util.BLE_BROADCASE_BLUETOOTH_COMMAND_ACTION);
	 * scan_intent.putExtra(Util.BLE_BROADCASE_BLUETOOTH_COMMAND_VALUES,
	 * Util.BLE_COMMAND_CODE_SCAN); sendBroadcast(scan_intent); }
	 */
	// DEL_E by jiangc@2019/5/23
	/**
	 * First connection to a Bluetooth device that needs to transfer clicks
	 */
	private void connectBleDeviceService(String address) {
		Intent connect_intent = new Intent();
		connect_intent.setAction(HelmetToolsUtils.BLE_BROADCASE_BLUETOOTH_COMMAND_ACTION);
		connect_intent.putExtra(BluetoothDevice.EXTRA_DEVICE, blueAdapter.getRemoteDevice(address));
		connect_intent.putExtra(HelmetToolsUtils.BLE_BROADCASE_BLUETOOTH_COMMAND_VALUES,
				HelmetToolsUtils.BLE_COMMAND_CODE_CONNECT);
		sendBroadcast(connect_intent);
	}

	// private class GetDataTask extends AsyncTask<Void, Void, String[]> {
	//
	// @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	// @Override
	// protected String[] doInBackground(Void... params) {
	// if (BluetoothController.getInstance().isBleOpen()) {
	// BluetoothController.getInstance().startScanBLE();
	// }
	// ;// 开始扫描
	// return null;
	// }
	//
	// @Override
	// protected void onPostExecute(String[] result) {
	// super.onPostExecute(result);
	// }
	// }

	public class MsgReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equalsIgnoreCase(HelmetToolsUtils.ACTION_UPDATE_DEVICE_LIST)) {
				String name = intent.getStringExtra("name");
				String address = intent.getStringExtra("address");
				int isBinder = intent.getIntExtra("isbinder", -1);
				// ADD_S by jiangc@2019/5/20 增加设备唯一标识
				String wlanaddress = intent.getStringExtra("wlanaddress");
				// ADD_E by jiangc@2019/5/20
				if (address == null) {
					Log.e(TAG, "onReceive: address  ============================== null");
					return;
				}

				if (!temp_address.contains(wlanaddress)) {

					Log.i(TAG, "name = " + name + ",  address = " + address + ",   wlanaddress = " + wlanaddress
							+ ",  isBinder = " + isBinder);

					HelmetBleBluetoothInfo temp = new HelmetBleBluetoothInfo();
					temp.setName(name);
					temp.setAddress(address);
					temp.setWlanaddress(wlanaddress);
					temp.setIsBinder(isBinder);

					if (cDevices.contains(wlanaddress)) {
						// temp.setStatus(getString(R.string.connected));
						// MOD_S by jiangc@2019/5/28
						// 增加状态标识，避免来到手动连接界面点击已连接的设备会发生重新连接的情况
						temp.setConnect(true);
						// add by yintao start
						search.setEnabled(false);
						// add by yintao end
					} else {
						// temp.setStatus(getString(R.string.not_connected));
						temp.setConnect(false);
						// MOD_E by jiangc@2019/5/28
					}

					list.add(temp);
					// ADD_E by jiangc@2019/6/6
				}
				adapter.setDeviceList(list);
				adapter.notifyDataSetChanged();
				temp_address.add(wlanaddress);
			} else if (intent.getAction().equalsIgnoreCase(HelmetToolsUtils.ACTION_CONNECTED_ONE_DEVICE)) {
				Log.e(TAG, "onReceive: 收到状态改变广播");
				// connectedDevice.setText(getString(R.string.connected_bluetooth)
				// + intent.getStringExtra("name") + " mac:" +
				// intent.getStringExtra("address"));
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getAddress().equals(intent.getStringExtra("address"))) {
						// list.get(i).setStatus(getString(R.string.connected));
						list.get(i).setConnect(true);
						// add by yintao start
						// search.setText(getResources().getString(R.string.connected_devices));
						search.setEnabled(false);
						// add by yintao end
					}
					// ADD_S by jiangc@2019/6/10 删除之前连接过的那个item
					// if
					// ((list.get(i).getWlanaddress().equals(BleSharedPreferences.instance().getBleServiceMac()))
					// &&
					// !(list.get(i).getAddress().equals(intent.getStringExtra("address"))))
					// {
					// list.remove(i);
					// }
					// ADD_E by jiangc@2019/6/10
				}
				adapter.setDeviceList(list);
				adapter.notifyDataSetChanged();
			}

			// else if
			// (intent.getAction().equalsIgnoreCase(HelmetToolsUtils.ACTION_STOP_CONNECT))
			// {
			// connectedDevice.setText("");
			// toast(getResources().getString(R.string.connection_dropped));
			// } else if
			// (intent.getAction().equalsIgnoreCase(HelmetToolsUtils.ACTION_RECEIVE_MESSAGE_FROM_DEVICE))
			// {
			// //
			// receivedMessage.append("\n\r"+intent.getStringExtra("message"));
			// } else if
			// (Util.BLE_SERVICE_CONNECTED_CHANGE_ACTION.equals(intent.getAction()))
			// {
			// int status =
			// intent.getIntExtra(Util.BLE_SERVICE_CONNECTED_CHANGE_VALUES, -1);
			// if (status == 0) { // disconnect
			// for (int i = 0; i < list.size(); i++) {
			// list.get(i).setStatus(getString(R.string.connected));
			// list.get(i).setConnect(false);
			// }
			// }
			//
			// // DEL_S by jiangc@2019/5/24 删除重复的地方
			// /*
			// * else if (status == 2) { //connected BluetoothDevice
			// * connected_device =
			// * intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE); for
			// * (int i = 0; i < list.size(); i++) { if (null != list.get(i))
			// * { if
			// * (list.get(i).getAddress().equals(connected_device.getAddress(
			// * ))) { list.get(i).setStatus(getString(R.string.connected));
			// * list.get(i).setConnect(true); } } } }
			// */
			// // DEL_E by jiangc@2019/5/24
			// adapter.notifyDataSetChanged();
			// }

			else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(intent.getAction())) {
				int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
				if (state == BluetoothAdapter.STATE_ON) {
					search.setEnabled(true);
					unbind.setEnabled(true);
					switchView.setChecked(true);
					isUnregisterReceiver = false;
					// handler.sendEmptyMessageDelayed(UPDATE_SCAN_STATUS,
					// UPDATE_SCAN_INTERVAL_TIME);
				} else if (state == BluetoothAdapter.STATE_OFF) {
					search.setEnabled(false);
					unbind.setEnabled(false);
					switchView.setChecked(false);
					isUnregisterReceiver = true;
				}
			}
		}
	}

	private boolean isUnregisterReceiver = false;

	private void toast(String str) {
		Toast.makeText(HelmetSettingBleConnectActivity.this, str, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.e(TAG, "onDestroy: ondestroy");
		if (!isUnregisterReceiver) {
			unregisterReceiver(receiver);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		isVisible = false;
	}

}
