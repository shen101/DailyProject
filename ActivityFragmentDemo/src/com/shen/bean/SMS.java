package com.shen.bean;

import com.shen.utils.BackupInfoUtils;

public class SMS {
	private String body;
	private long id;
	private String date;
	private String address;

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public SMS(String body, long id, String date, String address) {
		super();
		this.body = body;
		this.id = id;
		this.date = date;
		this.address = address;
	}

	public SMS() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "SMS [body=" + body + ", id=" + id + ", date=" + BackupInfoUtils.getDate(date) + ", address=" + address
				+ "]";
	}

}
