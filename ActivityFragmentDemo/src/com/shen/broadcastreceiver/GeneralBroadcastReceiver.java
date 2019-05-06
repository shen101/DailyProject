package com.shen.broadcastreceiver;

import com.shen.activityfragmentdemo.R;
import com.shen.utils.GlassUtils;
import com.shen.widget.BatteryView;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class GeneralBroadcastReceiver extends BroadcastReceiver {

	private int power = 0;
	private BatteryView mBatteryView;
	private ImageView wifi_singal_view, bluetooth_view, tether_view;

	public GeneralBroadcastReceiver(BatteryView view) {
		super();
		this.mBatteryView = view;
	}

	public GeneralBroadcastReceiver(ImageView wifi_singal, ImageView bluetooth_view) {
		super();
		this.wifi_singal_view = wifi_singal;
		this.bluetooth_view = bluetooth_view;
	}

	public GeneralBroadcastReceiver(ImageView wifi_singal, ImageView bluetooth_view, ImageView tether_view) {
		super();
		this.wifi_singal_view = wifi_singal;
		this.bluetooth_view = bluetooth_view;
		this.tether_view = tether_view;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String intent_action = intent.getAction();
		if (Intent.ACTION_BATTERY_CHANGED.equals(intent_action)) {
			int level = intent.getIntExtra("level", 0);
			int scale = intent.getIntExtra("scale", 100);
			power = level * 100 / scale;
			mBatteryView.setPower(power);
			// batteryTx.setText(power+"%");
			int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
			switch (status) {
			case BatteryManager.BATTERY_STATUS_CHARGING:

				break;
			case BatteryManager.BATTERY_STATUS_FULL:
				// ³äÂú
				break;
			case BatteryManager.BATTERY_STATUS_DISCHARGING:

				break;
			default:
				break;
			}
		} else if (WifiManager.RSSI_CHANGED_ACTION.equals(intent_action)) {
			int signal = GlassUtils.getWiFiSignal(context);
			try {
				if (!GlassUtils.isAirplaneMode(context)) {
					Log.i("shen", "signal = " + signal);
					if (signal == 0) {
						int wifistate = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
								WifiManager.WIFI_STATE_DISABLED);
						if (wifistate == WifiManager.WIFI_STATE_DISABLED) {
							wifi_singal_view.setVisibility(View.GONE);
						} else {
							wifi_singal_view.setVisibility(View.VISIBLE);
							wifi_singal_view.setBackgroundResource(R.drawable.ic_glass_wifi_signal_0);
						}
					} else if (signal == 1) {
						wifi_singal_view.setVisibility(View.VISIBLE);
						wifi_singal_view.setBackgroundResource(R.drawable.ic_glass_wifi_signal_1);
					} else if (signal == 2) {
						wifi_singal_view.setVisibility(View.VISIBLE);
						wifi_singal_view.setBackgroundResource(R.drawable.ic_glass_wifi_signal_2);
					} else {
						wifi_singal_view.setVisibility(View.VISIBLE);
						wifi_singal_view.setBackgroundResource(R.drawable.ic_glass_wifi_signal_3);
					}
				} else {
					Log.i("shen", "airplane is on");
					wifi_singal_view.setVisibility(View.GONE);
				}
			} catch (SettingNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if ((WifiManager.WIFI_STATE_CHANGED_ACTION).equals(intent_action)) {
			try {
				if (!GlassUtils.isAirplaneMode(context)) {
					updateWifiState(intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN));
				} else {
					Log.i("shen", "airplane is on");
					wifi_singal_view.setVisibility(View.GONE);
				}
			} catch (SettingNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if ((BluetoothAdapter.ACTION_STATE_CHANGED).equals(intent_action)) {
			int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
			Log.i("shen", "blueState = " + blueState);
			if (blueState == BluetoothAdapter.STATE_TURNING_ON || blueState == BluetoothAdapter.STATE_ON) {
				bluetooth_view.setVisibility(View.VISIBLE);
				bluetooth_view.setBackgroundResource(R.drawable.ic_glass_bluetooth);
			} else if (blueState == BluetoothAdapter.STATE_TURNING_OFF || blueState == BluetoothAdapter.STATE_OFF) {
				bluetooth_view.setVisibility(View.GONE);
			}
		} else if ((GlassUtils.ACTION_WIFI_AP_STATE_CHANGED_TAG).equals(intent_action)) {
			int state = intent.getIntExtra("wifi_state", 0);
			Log.i("shen", "state = "+state);
			switch (state) {
			case 10:
			case 11:
				tether_view.setVisibility(View.GONE);
				break;
			case 12:
			case 13:
				tether_view.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
		}
	}

	private void updateWifiState(int intExtra) {
		// TODO Auto-generated method stub
		Log.i("shen", "intExtra = " + intExtra);
		switch (intExtra) {
		case WifiManager.WIFI_STATE_ENABLED:
			wifi_singal_view.setVisibility(View.VISIBLE);
			return; // not break, to avoid the call to pause() below

		case WifiManager.WIFI_STATE_ENABLING:
			break;

		case WifiManager.WIFI_STATE_DISABLED:
			wifi_singal_view.setVisibility(View.GONE);
			break;
		}
	}

}
