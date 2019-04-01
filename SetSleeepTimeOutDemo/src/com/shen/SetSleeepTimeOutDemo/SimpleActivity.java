package com.shen.SetSleeepTimeOutDemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.System;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SimpleActivity extends Activity {

	private TextView show_text;
	private Button set_btn;
	private Context mContext;
	private CharSequence[] items;
	private AlertDialog.Builder mdialog;
	private long default_values = 60000;
	private int[] times_values;
	private int index = 0;
	private ContentResolver mResolver;
	private AlertDialog mAlertDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_layout);
		mContext = SimpleActivity.this;

		mResolver = getContentResolver();

		show_text = (TextView) findViewById(R.id.show_time);
		set_btn = (Button) findViewById(R.id.start_set);
		set_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openDialog().show();
			}
		});
		times_values = getResources().getIntArray(R.array.times_list_values);
		updateText(getCurrentTimeOut());
	}

	private AlertDialog openDialog() {
		items = getResources().getStringArray(R.array.times_list_name);
		mdialog = new AlertDialog.Builder(mContext);
		mdialog.setSingleChoiceItems(items, index, mListener);
		mAlertDialog = mdialog.create();
		return mAlertDialog;
	}

	private DialogInterface.OnClickListener mListener = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			setCurrentTimeOut(times_values[which]);
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					updateText(getCurrentTimeOut());
				}
			});
			mAlertDialog.dismiss();
		}
	};

	void updateText(long time_long) {
		show_text.setText(String.valueOf(time_long));
		for (int i = 0; i < times_values.length; i++) {
			if (times_values[i] == getCurrentTimeOut()) {
				index = i;
			}
		}
	}

	long getCurrentTimeOut() {
		return Settings.System.getLong(mResolver, System.SCREEN_OFF_TIMEOUT, default_values);
	}

	boolean setCurrentTimeOut(long time) {
		return Settings.System.putLong(mResolver, System.SCREEN_OFF_TIMEOUT, time);
	}

}
