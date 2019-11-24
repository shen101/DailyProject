package com.yiyang.helmetclientevrtool.adapter;

import java.util.ArrayList;

import com.yiyang.helmetclientevrtool.R;
import com.yiyang.helmetclientevrtool.bean.HelmetBleBluetoothInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HelmetBleDeviceAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<HelmetBleBluetoothInfo> list;
	private LayoutInflater inflater;

	public HelmetBleDeviceAdapter(Context context, ArrayList<HelmetBleBluetoothInfo> list) {
		inflater = LayoutInflater.from(context);
		this.list = list;
	};

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.helmet_setting_ble_connect_item, null);
			holder.name = (TextView) convertView.findViewById(R.id.device_name);
			holder.status = (TextView) convertView.findViewById(R.id.status);
			holder.address = (TextView) convertView.findViewById(R.id.device_address);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		HelmetBleBluetoothInfo device = list.get(arg0);
		holder.name.setText(device.getName());
		holder.status.setText(device.getStatus());
		holder.address.setText(device.getAddress());
		return convertView;
	}

	class ViewHolder {
		TextView name;
		TextView status;
		TextView address;
	}

	public void setDeviceList(ArrayList<HelmetBleBluetoothInfo> list) {
		this.list = list;
	}

}
