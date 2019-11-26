package com.yiyang.helmetclientevrtool.adapter;

import java.util.ArrayList;
import java.util.List;
import com.yiyang.helmetclientevrtool.R;
import com.yiyang.helmetclientevrtool.bean.HelmetSearchBleDevice;
import com.yiyang.helmetclientevrtool.interfaces.OnItemBackListener;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HelmetBleDeviceAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<HelmetSearchBleDevice> list = new ArrayList<HelmetSearchBleDevice>();
	private LayoutInflater inflater;
	private OnItemBackListener onItemBackListener;

	public HelmetBleDeviceAdapter(Context context) {
		inflater = LayoutInflater.from(context);
	}

	public void addBluetoothDevice(HelmetSearchBleDevice bluetoothDevice) {
		list.add(bluetoothDevice);
		notifyDataSetChanged();
	}

	public void addBluetoothTopDevice(HelmetSearchBleDevice bluetoothDevice) {
		list.add(0, bluetoothDevice);
		notifyDataSetChanged();
	}

	public void setOnItemBackListener(OnItemBackListener onItemBackListener) {
		this.onItemBackListener = onItemBackListener;
	}

	public void removeAll() {
		if (null != list && !list.isEmpty()) {
			list.clear();
		}
		notifyDataSetChanged();
	}

	public void removeDevice(HelmetSearchBleDevice device) {
		if (null != list && !list.isEmpty()) {
			if (list.contains(device)) {
				list.remove(device);
			}
		}
		notifyDataSetChanged();
	}

	public List<HelmetSearchBleDevice> getAllDevices() {
		return list;
	}

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
	public View getView(final int arg0, View convertView, ViewGroup arg2) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.helmet_setting_ble_connect_item, null);
			holder.name = (TextView) convertView.findViewById(R.id.device_name);
			holder.status = (TextView) convertView.findViewById(R.id.status);
			holder.address = (TextView) convertView.findViewById(R.id.device_address);
			holder.item = (RelativeLayout) convertView.findViewById(R.id.layout);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		HelmetSearchBleDevice device = list.get(arg0);
		holder.name.setText(device.getBle_device().getName());
		holder.status.setText(device.getStatus());
		holder.address.setText(device.getBle_device().getAddress());

		holder.item.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (onItemBackListener != null) {
					onItemBackListener.listener(list.get(arg0));
				}
			}
		});

		return convertView;
	}

	class ViewHolder {
		TextView name;
		TextView status;
		TextView address;
		RelativeLayout item;
	}

	public void setDeviceList(ArrayList<HelmetSearchBleDevice> list) {
		this.list = list;
	}

}
