package com.shen.utils;

import java.util.Set;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.ParcelUuid;

public class GlassBluetoothAdapter {

	private BluetoothAdapter mAdapter;
	private static GlassBluetoothAdapter sInstance;
	private static final int SCAN_EXPIRATION_MS = 5 * 60 * 1000;
	private long mLastScan;

	public GlassBluetoothAdapter(BluetoothAdapter mAdapter) {
		super();
		this.mAdapter = mAdapter;
	}

	static synchronized GlassBluetoothAdapter getInstance() {
		if (sInstance == null) {
			BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
			if (adapter != null) {
				sInstance = new GlassBluetoothAdapter(adapter);
			}
		}

		return sInstance;
	}

	void cancelDiscovery() {
		mAdapter.cancelDiscovery();
	}

	boolean enable() {
		return mAdapter.enable();
	}

	boolean disable() {
		return mAdapter.disable();
	}

	void getProfileProxy(Context context, BluetoothProfile.ServiceListener listener, int profile) {
		mAdapter.getProfileProxy(context, listener, profile);
	}

	String getName() {
		return mAdapter.getName();
	}

	int getScanMode() {
		return mAdapter.getScanMode();
	}

	int getState() {
		return mAdapter.getState();
	}

	boolean isDiscovering() {
		return mAdapter.isDiscovering();
	}

	boolean isEnabled() {
		return mAdapter.isEnabled();
	}

	void setName(String name) {
		mAdapter.setName(name);
	}

	void startScanning(boolean force) {
		// Only start if we're not already scanning
		if (!mAdapter.isDiscovering()) {
			if (!force) {
				// Don't scan more than frequently than SCAN_EXPIRATION_MS,
				// unless forced
				if (mLastScan + SCAN_EXPIRATION_MS > System.currentTimeMillis()) {
					return;
				}
			}

			if (mAdapter.startDiscovery()) {
				mLastScan = System.currentTimeMillis();
			}
		}
	}

	void stopScanning() {
		if (mAdapter.isDiscovering()) {
			mAdapter.cancelDiscovery();
		}
	}
}
