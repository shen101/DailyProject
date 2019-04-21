package com.shen.fragment;

import com.shen.activityfragmentdemo.BaseFragment;
import com.shen.activityfragmentdemo.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;

public class SettingsWlanInfoFragment extends BaseFragment {

	private View wlan_view = null;
	private ImageView wlan_left_btn;
	private Switch wlan_switch_btn;
	private ListView wlan_list;
	private RelativeLayout wlan_layout;
	private IntentFilter mFilter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		wlan_view = inflater.inflate(R.layout.settings_wlan_info_fragment, container, false);
		initViews();
		return wlan_view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mFilter = new IntentFilter();
		mFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		mFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		mFilter.addAction(WifiManager.NETWORK_IDS_CHANGED_ACTION);
		mFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
		mFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		mFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
		getActivity().registerReceiver(wlanReceiver, mFilter);
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		getActivity().unregisterReceiver(wlanReceiver);
	}

	private void initViews() {
		wlan_left_btn = (ImageView) wlan_view.findViewById(R.id.left_direction_icons);
		wlan_left_btn.setOnClickListener(MyOnLinster);
		wlan_switch_btn = (Switch) wlan_view.findViewById(R.id.wlan_switch);
		wlan_switch_btn.setOnClickListener(MyOnLinster);
		wlan_list = (ListView) wlan_view.findViewById(R.id.wlan_list);
		wlan_list.setOnItemClickListener(mItemListener);
	}

	private BroadcastReceiver wlanReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.i("shen", "intent.getAction() = " + intent.getAction());
			handleEvent(intent);
		}
	};

	private OnItemClickListener mItemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// TODO Auto-generated method stub

		}
	};

	private OnClickListener MyOnLinster = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.left_direction_icons:
				onFragmentBackClick();
				break;
			case R.id.wlan_switch:

				break;
			default:
				break;
			}
		}
	};

	protected void handleEvent(Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
			updateWifiState(intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN));
		} else if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {
			Log.i("shen", "WifiManager.SCAN_RESULTS_AVAILABLE_ACTION 444444444444444");
		} else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)) {
			NetworkInfo info = (NetworkInfo) intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
			Log.i("shen", "info.getTypeName() = " + info.getTypeName()+",  isConnected() = "+info.isConnected());
		} else if (WifiManager.RSSI_CHANGED_ACTION.equals(action)) {
			Log.i("shen", "WifiManager.RSSI_CHANGED_ACTION 00000000000000");
		}
	}

	private void updateWifiState(int state) {

		switch (state) {
		case WifiManager.WIFI_STATE_ENABLED:
			Log.i("shen", "WIFI_STATE_ENABLED 111111111111111");
			return; // not break, to avoid the call to pause() below

		case WifiManager.WIFI_STATE_ENABLING:
			Log.i("shen", "WIFI_STATE_ENABLING 2222222222222222");
			break;

		case WifiManager.WIFI_STATE_DISABLED:
			Log.i("shen", "WIFI_STATE_DISABLED 33333333333333333");
			break;
		}

	}
}
