package com.shen.custombluetoothconntectdemo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ClientActivity extends Activity {

	private EditText edit_content;
	private Button send_btn, search_btn, connect_btn;
	private ListView devices_list;
	private BluetoothDevice unpair_device;
	private BluetoothDevice connected_device;
	private MyBluetoothAdapter myAdapter;
	private BluetoothAdapter mAdapter;
	private BluetoothManager mManager;
	private BluetoothSocket clientSocket;

	private AcceptThread mAcceptThread;

	private final String NAME = "Bluetooth_Socket";
	public static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	private ArrayList<BluetoothDevice> pair_list = new ArrayList<BluetoothDevice>();

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())) {
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
					myAdapter.addBluetoothDevice(device);
				}
			} else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(intent.getAction())) {
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				myAdapter.notifyDataSetChanged();
				switch (device.getBondState()) {
				case BluetoothDevice.BOND_NONE:
					break;
				case BluetoothDevice.BOND_BONDING:
					break;
				case BluetoothDevice.BOND_BONDED:
					myAdapter.updateBluetoothDeviceTop(device);
					break;
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.client_main);
		mManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);

		initViews();
	}

	private void initViews() {
		// TODO Auto-generated method stub
		edit_content = findViewById(R.id.edit_data);
		send_btn = findViewById(R.id.send_data_btn);
		send_btn.setOnClickListener(listener);
		search_btn = findViewById(R.id.search_devices_btn);
		search_btn.setOnClickListener(listener);
		connect_btn = findViewById(R.id.connect_remote_devices_btn);
		connect_btn.setOnClickListener(listener);
		devices_list = findViewById(R.id.blue_list);

		myAdapter = new MyBluetoothAdapter(this, pair_list);
		devices_list.setAdapter(myAdapter);

		devices_list.setOnItemClickListener(itemListener);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		mAdapter = mManager.getAdapter().getDefaultAdapter();

		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(BluetoothDevice.ACTION_FOUND);
		mFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		registerReceiver(mReceiver, mFilter);

		addAlealyPairDevice();

		mAcceptThread = new AcceptThread();
		mAcceptThread.start();
	}

	private void addAlealyPairDevice() {
		Set<BluetoothDevice> paireDevices = mAdapter.getBondedDevices();
		for (BluetoothDevice pair_devices : paireDevices) {
			myAdapter.addBluetoothDevice(pair_devices);
			connected_device = pair_devices;
		}
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.search_devices_btn:
				if (mAdapter != null) {
					mAdapter.startDiscovery();
				}

				pair_list.clear();
				addAlealyPairDevice();
				break;
			case R.id.send_data_btn:
				Log.i("shen", "clientSocket != null" + (clientSocket != null));
				if (clientSocket != null) {
					new SendRemoteDeviceInfoTask(clientSocket).execute(edit_content.getText().toString());
				}
				break;
			case R.id.connect_remote_devices_btn:
				new ConnectTask().execute(connected_device.getAddress());
				break;
			default:
				break;
			}
		}
	};

	private OnItemClickListener itemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			// TODO Auto-generated method stub
			if (pair_list.get(arg2).getBondState() != BluetoothDevice.BOND_BONDED) {
				Utils.PairDevie(pair_list.get(arg2));
				connected_device = pair_list.get(arg2);
			} else {
				unpair_device = pair_list.get(arg2);
				getUnpairDialog().show();
			}
			myAdapter.notifyDataSetChanged();
		}
	};

	private AlertDialog getUnpairDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("unpar !");
		builder.setNegativeButton(android.R.string.cancel, dialoglistener);
		builder.setPositiveButton(android.R.string.ok, dialoglistener);
		return builder.create();
	}

	private DialogInterface.OnClickListener dialoglistener = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			if (AlertDialog.BUTTON_POSITIVE == which) {
				Utils.unPairDevice(unpair_device);
			} else if (AlertDialog.BUTTON_NEGATIVE == which) {
				dialog.dismiss();
			}
		}
	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(mReceiver);
	}

	class ConnectTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			BluetoothDevice device = mAdapter.getRemoteDevice(params[0]);

			if (clientSocket != null) {
				return "clientSocket != null";
			}

			try {
				clientSocket = device.createRfcommSocketToServiceRecord(Utils.MY_UUID);
				clientSocket.connect();
				Log.i("shen", "Already connected " + device.getName());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				try {
					clientSocket.close();
					Log.i("shen", "Close Client Socket Success");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.i("shen", "Close Client Socket Failed");
				}
				Log.i("shen", "Unable to connect to " + device.getName());
			}

			mAdapter.cancelDiscovery();
			return "The Bluetooth connection is normal and the Socket is successfully created.";
		}
	}

	private class AcceptThread extends Thread {
		private BluetoothServerSocket serverSocket;
		private BluetoothSocket socket;
		private InputStream is;

		public AcceptThread() {
			try {
				serverSocket = mAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
				Log.e("shen", "Waiting for server information");
			} catch (IOException e) {
				e.printStackTrace();
				Log.e("shen", "Connection server exception");
			}
		}

		@Override
		public void run() {
			try {
				socket = serverSocket.accept();
				is = socket.getInputStream();
				while (true) {
					synchronized (this) {
						byte[] tt = new byte[is.available()];
						if (tt.length > 0) {
							is.read(tt, 0, tt.length);
							Message msg = new Message();
							msg.obj = new String(tt, "utf-8");
							mhandler.sendMessage(msg);
							Log.e("shen", "The receiving server information is: " + String.valueOf(msg.obj));
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private Handler mhandler = new Handler(Looper.getMainLooper()) {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Toast.makeText(ClientActivity.this, String.valueOf(msg.obj), Toast.LENGTH_LONG).show();
		}
	};
}
