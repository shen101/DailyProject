package com.shen.custombluetoothconntectdemo;

import java.util.ArrayList;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyBluetoothAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<BluetoothDevice> devices = new ArrayList<BluetoothDevice>();

	public MyBluetoothAdapter() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MyBluetoothAdapter(Context mcontext, ArrayList<BluetoothDevice> devices) {
		super();
		this.mContext = mcontext;
		this.mInflater = LayoutInflater.from(mcontext);
		this.devices = devices;
	}

	public void addBluetoothDevice(BluetoothDevice device) {
		devices.add(device);
		notifyDataSetChanged();
	}

	public void updateBluetoothDeviceTop(BluetoothDevice device) {
		devices.remove(device);
		devices.add(0, device);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return devices.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return devices.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		MyHoloder holder = null;
		if (convertView == null) {
			holder = new MyHoloder();
			convertView = mInflater.inflate(R.layout.devices_items, null);
			holder.device_name = convertView.findViewById(R.id.blue_name);
			holder.device_address = convertView.findViewById(R.id.blue_address);
			holder.device_status = convertView.findViewById(R.id.blue_status);
			holder.device_type = convertView.findViewById(R.id.blue_type);
			convertView.setTag(holder);
		} else {
			holder = (MyHoloder) convertView.getTag();
		}

		if (devices.get(position).getName() == null) {
			holder.device_name.setVisibility(View.GONE);
		} else {
			holder.device_name.setVisibility(View.VISIBLE);
			holder.device_name.setText(devices.get(position).getName());
		}

		holder.device_address.setText(devices.get(position).getAddress());
		holder.device_status.setText(devices.get(position).getBondState() == BluetoothDevice.BOND_BONDED
				? R.string.connected : R.string.unconnected);
		holder.device_type.setBackgroundResource(
				devices.get(position).getBluetoothClass().getDeviceClass() == BluetoothClass.Device.Major.PERIPHERAL
						? R.drawable.ic_glass_bluetooth_wearable_helmet_type
						: R.drawable.ic_glass_bluetooth_smartphone_type);
		return convertView;
	}

	class MyHoloder {
		private TextView device_name;
		private TextView device_address;
		private ImageView device_type;
		private TextView device_status;
	}
}
