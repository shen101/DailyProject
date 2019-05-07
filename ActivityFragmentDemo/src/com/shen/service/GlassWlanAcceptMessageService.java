package com.shen.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class GlassWlanAcceptMessageService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Socket socket = null;
				try {
					ServerSocket server = new ServerSocket(18888);
					while (true) {
						socket = server.accept();
						BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						String message = in.readLine();
						PrintWriter out = new PrintWriter(
								new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
						Intent intent = new Intent();
						intent.setAction("com.shen.serverSocket");
						intent.putExtra("message", message);
						sendBroadcast(intent);
						in.close();
						out.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (null != socket) {
						try {
							socket.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}).start();
	}
}
