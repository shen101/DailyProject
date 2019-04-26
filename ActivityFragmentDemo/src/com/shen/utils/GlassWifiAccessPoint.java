package com.shen.utils;

public class GlassWifiAccessPoint {

	public String wifi_name;
	public int wifi_singal;
	public String wifi_safety;
	public String wifi_address;

	public GlassWifiAccessPoint() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getWifi_name() {
		return wifi_name;
	}

	public void setWifi_name(String wifi_name) {
		this.wifi_name = wifi_name;
	}

	public int getWifi_singal() {
		return wifi_singal;
	}

	public void setWifi_singal(int wifi_singal) {
		this.wifi_singal = wifi_singal;
	}

	public String getWifi_safety() {
		return wifi_safety;
	}

	public void setWifi_safety(String wifi_safety) {
		this.wifi_safety = wifi_safety;
	}

	public String getWifi_address() {
		return wifi_address;
	}

	public void setWifi_address(String wifi_address) {
		this.wifi_address = wifi_address;
	}

	@Override
	public String toString() {
		return "GlassWifiAccessPoint [wifi_name=" + wifi_name + ", wifi_singal=" + wifi_singal + ", wifi_safety="
				+ wifi_safety + ", wifi_address=" + wifi_address + "]";
	}

}
