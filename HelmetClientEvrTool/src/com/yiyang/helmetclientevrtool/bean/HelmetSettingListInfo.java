package com.yiyang.helmetclientevrtool.bean;

public class HelmetSettingListInfo {

	private int name;
	private int icon;

	public int getName() {
		return name;
	}

	public void setName(int name) {
		this.name = name;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public HelmetSettingListInfo(int name, int icon) {
		super();
		this.name = name;
		this.icon = icon;
	}

	public HelmetSettingListInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "HelmetSettingListInfo [name=" + name + ", icon=" + icon + "]";
	}

}
