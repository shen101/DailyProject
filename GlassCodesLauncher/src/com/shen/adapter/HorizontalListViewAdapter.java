package com.shen.adapter;

import java.util.ArrayList;
import java.util.List;

import com.shen.activityfragmentdemo.R;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HorizontalListViewAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private List<ScanResult> info_datas = new ArrayList<ScanResult>();

	public HorizontalListViewAdapter(Context mcontext, List<ScanResult> info_datas) {
		super();
		this.mContext = mcontext;
		this.mInflater = LayoutInflater.from(mcontext);
		this.info_datas = info_datas;
	}

	@Override
	public int getCount() {
		return info_datas.size();
	}

	@Override
	public Object getItem(int position) {
		return info_datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView == null) {
			holder = new ViewHolder();

			convertView = mInflater.inflate(R.layout.wlan_sanresult_horizontallistview_item, null);
			holder.wifi_name = (TextView) convertView.findViewById(R.id.wifi_name);
			holder.wifi_signal = (TextView) convertView.findViewById(R.id.wifi_signal_strength_text);
			holder.wifi_safety = (TextView) convertView.findViewById(R.id.wifi_safety_text);
			holder.wifi_info = (TextView) convertView.findViewById(R.id.wifi_info_text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		convertView.setMinimumWidth(600);

		holder.wifi_name.setText(info_datas.get(position).SSID);
		holder.wifi_signal.setText(getWifiLevel(info_datas.get(position).level));
		holder.wifi_safety.setText(info_datas.get(position).capabilities);
		holder.wifi_info.setText(info_datas.get(position).BSSID);
		return convertView;
	}

	private String getWifiLevel(int level) {
		level = Integer.parseInt(String.valueOf(level).substring(1));
		if (level > 0 && level < 25) {
			return mContext.getString(R.string.wifi_signal_level_very_strong);
		} else if (level >= 25 && level < 50) {
			return mContext.getString(R.string.wifi_signal_level_strong);
		} else if (level >= 50 && level < 75) {
			return mContext.getString(R.string.wifi_signal_level_general);
		} else if (level >= 75 && level < 100) {
			return mContext.getString(R.string.wifi_signal_level_very_weak);
		} else {
			return mContext.getString(R.string.wifi_signal_level_null);
		}
	}

	class ViewHolder {
		private TextView wifi_name, wifi_signal, wifi_safety, wifi_info;
	}
}