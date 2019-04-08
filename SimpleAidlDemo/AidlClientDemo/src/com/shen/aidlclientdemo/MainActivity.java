package com.shen.aidlclientdemo;

import com.shen.aidlservicedemo.Person;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Person iperson;

	private ServiceConnection conn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			iperson = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			iperson = Person.Stub.asInterface(service);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Intent intent = new Intent("com.shen.aidl.CONNECT_SERVER");
		intent.setPackage("com.shen.aidlservicedemo");
		bindService(intent, conn, Service.BIND_AUTO_CREATE);
	}

	public void getInfo(View view) {
		try {
			iperson.setAge(20);
			iperson.setName("67238");
			Toast.makeText(this, iperson.display(), 0).show();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
