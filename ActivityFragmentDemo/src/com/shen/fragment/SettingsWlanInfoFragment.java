package com.shen.fragment;

import java.util.ArrayList;
import java.util.List;
import com.shen.activityfragmentdemo.BaseFragment;
import com.shen.activityfragmentdemo.R;
import com.shen.adapter.HorizontalListViewAdapter;
import com.shen.utils.GlassWifiAccessPoint;
import com.shen.widget.HorizontalListView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsWlanInfoFragment extends BaseFragment {

	private View wlan_view = null;
	private ImageView wlan_left_btn;
	private Switch wlan_switch_btn;
	private TextView wlan_switch_text, wlan_connect_info;
	private RelativeLayout wlan_layout;
	private IntentFilter mFilter;
	private WifiManager mWifiManager;
	private HorizontalListViewAdapter wlan_adapter;
	private HorizontalListView wlan_list;
	private List<GlassWifiAccessPoint> datas = new ArrayList<GlassWifiAccessPoint>();
	private Handler mhandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 100:
				datas.clear();
				wlan_adapter.notifyDataSetChanged();
				updateText();
				wlan_switch_btn.setChecked(false);
				break;
			case 200:
				datas.add((GlassWifiAccessPoint) msg.obj);
				wlan_adapter.refresh(datas);
				wlan_switch_btn.setChecked(true);
				break;
			default:
				break;
			}

		}
	};

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
		mWifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
		wlan_left_btn = (ImageView) wlan_view.findViewById(R.id.left_direction_icons);
		wlan_left_btn.setOnClickListener(MyOnLinster);
		wlan_switch_text = (TextView) wlan_view.findViewById(R.id.glass_wlan_on_off_txt);
		wlan_connect_info = (TextView) wlan_view.findViewById(R.id.glass_wlan_connecting_text);
		wlan_switch_btn = (Switch) wlan_view.findViewById(R.id.glass_wlan_switch);
		wlan_switch_btn.setOnCheckedChangeListener(mChangeListener);
		wlan_list = (HorizontalListView) wlan_view.findViewById(R.id.wlan_list);
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
			Log.i("shen", "datas.get(position).getWifi_name = " + datas.get(position).getWifi_name());
		}
	};

	private OnCheckedChangeListener mChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub
			if (isChecked) {
				mWifiManager.setWifiEnabled(true);
				updateText();
			} else {
				mWifiManager.setWifiEnabled(false);
				mhandler.sendEmptyMessage(100);
			}
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
			default:
				break;
			}
		}
	};

	private void updateText(NetworkInfo mInfo) {

		String text;

		WifiInfo info = mWifiManager.getConnectionInfo();

		String wifiName = info != null ? info.getSSID() : null;

		wifiName = wifiName.substring(1, wifiName.length() - 1);

		text = getActivity().getString(R.string.glass_wlan_connected_info, wifiName);

		if (mInfo.getState() == mInfo.getState().DISCONNECTED) {
			wlan_connect_info.setText(R.string.glass_wlan_disconnect_info);
		} else if (mInfo.getState() == mInfo.getState().CONNECTED) {
			wlan_connect_info.setText(text);
		}
		updateText();
	}

	private void updateText() {
		if (wlan_switch_btn.isChecked()) {
			wlan_switch_text.setText(R.string.glass_turn_on_title);
		} else {
			wlan_switch_text.setText(R.string.glass_shut_down_title);
		}
	}

	protected void handleEvent(Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
			updateWifiState(intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN));
		} else if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {
			Log.i("shen", "WifiManager.SCAN_RESULTS_AVAILABLE_ACTION 444444444444444");

			result = mWifiManager.getScanResults();
			wlan_adapter = new HorizontalListViewAdapter(getActivity(), datas);
			wlan_list.setAdapter(wlan_adapter);
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					for (int i = 0; i < result.size(); i++) {
						GlassWifiAccessPoint mPoint = new GlassWifiAccessPoint();
						mPoint.setWifi_name(result.get(i).SSID);
						mPoint.setWifi_singal(result.get(i).level);
						mPoint.setWifi_safety(result.get(i).capabilities);
						mPoint.setWifi_address(result.get(i).BSSID);
						Message msg = new Message();
						msg.obj = mPoint;
						msg.what = 200;
						mhandler.sendMessage(msg);
					}
				}
			}).start();

		} else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)) {
			NetworkInfo info = (NetworkInfo) intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
			updateText(info);
			Log.i("shen", "info.getState() = " + info.getState());
		} else if (WifiManager.RSSI_CHANGED_ACTION.equals(action)) {
			Log.i("shen", "WifiManager.RSSI_CHANGED_ACTION 00000000000000");
		}
	}

	private List<ScanResult> result;
	private List<WifiConfiguration> configs;

	private void updateWifiState(int state) {

		switch (state) {
		case WifiManager.WIFI_STATE_ENABLED:
			Log.i("shen", "WIFI_STATE_ENABLED 111111111111111");
			result = mWifiManager.getScanResults();
			wlan_adapter = new HorizontalListViewAdapter(getActivity(), datas);
			wlan_list.setAdapter(wlan_adapter);
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					for (int i = 0; i < result.size(); i++) {
						GlassWifiAccessPoint mPoint = new GlassWifiAccessPoint();
						mPoint.setWifi_name(result.get(i).SSID);
						mPoint.setWifi_singal(result.get(i).level);
						mPoint.setWifi_safety(result.get(i).capabilities);
						mPoint.setWifi_address(result.get(i).BSSID);
						Message msg = new Message();
						msg.obj = mPoint;
						mhandler.sendMessage(msg);
					}
				}
			}).start();
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
