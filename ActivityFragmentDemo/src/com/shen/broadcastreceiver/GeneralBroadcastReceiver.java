package com.shen.broadcastreceiver;

import com.shen.widget.BatteryView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

public class GeneralBroadcastReceiver extends BroadcastReceiver {
	
	private int power = 0;
	private BatteryView mBatteryView;
	
	public GeneralBroadcastReceiver(BatteryView view) {
		super();
		this.mBatteryView = view;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
			int level = intent.getIntExtra("level", 0);
			int scale = intent.getIntExtra("scale", 100);
			power = level * 100 / scale;
			mBatteryView.setPower(power);
			// batteryTx.setText(power+"%");
			int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
			switch (status) {
			case BatteryManager.BATTERY_STATUS_CHARGING:

				break;
			case BatteryManager.BATTERY_STATUS_FULL:
				// ³äÂú
				break;
			case BatteryManager.BATTERY_STATUS_DISCHARGING:

				break;
			default:
				break;
			}
		}
	}

}
