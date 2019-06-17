package com.shen.custombluetoothconntectdemo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.util.Log;

public class SendRemoteDeviceInfoTask extends AsyncTask<String, String, String> {

	private BluetoothSocket mSocket;

	public SendRemoteDeviceInfoTask(BluetoothSocket msocket) {
		super();
		// TODO Auto-generated constructor stub
		mSocket = msocket;
	}

	@Override
	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		if (mSocket == null) {
			return "Haven't created a connection yet";
		}

		try {
			mSocket.getOutputStream().write(arg0[0].getBytes("utf-8"));
			Log.i("shen", "Send " + Arrays.asList(arg0[0]) + " Success");
			return "Success to send";
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("shen", "Send Data Failed");
		}
		return "Failed to send";
	}
}
