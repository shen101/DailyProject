package com.yiyang.helmetclientevrtool.bean;

public class HelmetBleBluetoothInfo {
	private String name;
	private String address;
	private String status;
	private boolean isConnect;
	private String wlanaddress;
	private int isBinder;

	public HelmetBleBluetoothInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HelmetBleBluetoothInfo(String name, String address, String status, boolean isConnect, String wlanaddress,
			int isBinder) {
		super();
		this.name = name;
		this.address = address;
		this.status = status;
		this.isConnect = isConnect;
		this.wlanaddress = wlanaddress;
		this.isBinder = isBinder;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getWlanaddress() {
		return wlanaddress;
	}

	public void setWlanaddress(String wlanaddress) {
		this.wlanaddress = wlanaddress;
	}

	public int getIsBinder() {
		return isBinder;
	}

	public void setIsBinder(int isBinder) {
		this.isBinder = isBinder;
	}

	@Override
	public String toString() {
		return "HelmetBleBluetoothInfo [name=" + name + ", address=" + address + ", status=" + status + ", isConnect="
				+ isConnect + ", wlanaddress=" + wlanaddress + ", isBinder=" + isBinder + "]";
	}

}
