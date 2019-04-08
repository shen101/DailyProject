package com.shen.aidlservicedemo;

import android.os.IBinder;
import android.os.RemoteException;

public class Personimpl extends Person.Stub {

	private String name;
	private int age;

	@Override
	public void setName(String name) throws RemoteException {
		// TODO Auto-generated method stub
		this.name = name;
	}

	@Override
	public void setAge(int age) throws RemoteException {
		// TODO Auto-generated method stub
		this.age = age;
	}

	@Override
	public String display() throws RemoteException {
		// TODO Auto-generated method stub
		return "name = " + name + ",  age = " + age;
	}

	@Override
	public IBinder asBinder() {
		// TODO Auto-generated method stub
		return null;
	}

}
