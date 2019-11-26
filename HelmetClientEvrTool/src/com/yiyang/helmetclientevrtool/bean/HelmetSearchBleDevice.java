package com.yiyang.helmetclientevrtool.bean;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

public class HelmetSearchBleDevice implements Parcelable{

	private BluetoothDevice ble_device;
	private String status;
	private boolean isConnect;
	private int isBinder;

	public HelmetSearchBleDevice() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HelmetSearchBleDevice(BluetoothDevice ble_device, String status, boolean isConnect, int isBinder) {
		super();
		this.ble_device = ble_device;
		this.status = status;
		this.isConnect = isConnect;
		this.isBinder = isBinder;
	}

	public BluetoothDevice getBle_device() {
		return ble_device;
	}

	public void setBle_device(BluetoothDevice ble_device) {
		this.ble_device = ble_device;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isConnect() {
		return isConnect;
	}

	public void setConnect(boolean isConnect) {
		this.isConnect = isConnect;
	}

	public int getIsBinder() {
		return isBinder;
	}

	public void setIsBinder(int isBinder) {
		this.isBinder = isBinder;
	}

	@Override
	public String toString() {
		return "HelmetSearchBleDevice [ble_device=" + ble_device + ", status=" + status + ", isConnect=" + isConnect
				+ ", isBinder=" + isBinder + "]";
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}
}
