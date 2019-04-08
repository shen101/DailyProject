package com.shen.aidlservicedemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class RemoteService extends Service {

	private Binder person = new Personimpl();

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return person;
	}

}
