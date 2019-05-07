package com.shen.bean;

public class NotificationInfo {

	private String noti_title;
	private String noti_contents;
	private int noti_picture;

	public String getNoti_title() {
		return noti_title;
	}

	public void setNoti_title(String noti_title) {
		this.noti_title = noti_title;
	}

	public String getNoti_contents() {
		return noti_contents;
	}

	public void setNoti_contents(String noti_contents) {
		this.noti_contents = noti_contents;
	}

	public int getNoti_picture() {
		return noti_picture;
	}

	public void setNoti_picture(int noti_picture) {
		this.noti_picture = noti_picture;
	}

	public NotificationInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NotificationInfo(String noti_title, String noti_contents, int noti_picture) {
		super();
		this.noti_title = noti_title;
		this.noti_contents = noti_contents;
		this.noti_picture = noti_picture;
	}

	@Override
	public String toString() {
		return "NotificationInfo [noti_title=" + noti_title + ", noti_contents=" + noti_contents + ", noti_picture="
				+ noti_picture + "]";
	}

}
