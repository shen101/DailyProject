package com.shen.activityfragmentdemo;

import java.io.IOException;
import com.shen.utils.GlassUtils;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;

public class InCallUIActivity extends BaseActivity implements OnClickListener {

	private TextView incomming_name;
	private Chronometer incomming_status;
	private ImageButton incomming_btn;
	private Context mContext;
	private String incomming_number = "";
	private Intent incomming_intent;
	private TelephonyManager mTelephonyManager;
	private Vibrator vibrator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.incallui_layout);
		mContext = InCallUIActivity.this;
		getActionBar().hide();
		initTelephonyListener();
		incomming_intent = getIntent();
		incomming_number = incomming_intent.getStringExtra(GlassUtils.INTENT_INCALL_UI_NUMBER);
		Log.i("shen", "incomming_number == " + incomming_number);
		initViews();

		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		long[] patter = {1000, 1000, 2000, 50};
		vibrator.vibrate(patter,-1);
	}

	private void initViews() {
		incomming_name = (TextView) findViewById(R.id.glass_incallui_phone_name);
		incomming_name.setText(incomming_number);
		incomming_status = (Chronometer) findViewById(R.id.glass_incallui_phone_status);
		incomming_status.setText("Calling");
		incomming_status.stop();
		incomming_btn = (ImageButton) findViewById(R.id.glass_incallui_btn);
		incomming_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.glass_incallui_btn:
			answerRingingCall(mContext);

			break;

		default:
			break;
		}
	}

	public void initTelephonyListener() {
		mTelephonyManager = (TelephonyManager) getSystemService(Service.TELEPHONY_SERVICE);
		mTelephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
	}

	private PhoneStateListener phoneListener = new PhoneStateListener() {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			super.onCallStateChanged(state, incomingNumber);
			Log.i("shen", "state = " + state + ",  incomingNumber = " + incomingNumber);
			initInCallFragment(state, incomingNumber);
		}
	};

	private void initInCallFragment(int status, String incomingNumber) {

		if ("".equals(incomingNumber)) {
			return;
		}

		switch (status) {
		case TelephonyManager.CALL_STATE_IDLE: // Hang up
			setResult(Activity.RESULT_OK);
			vibrator.cancel();
			finish();
			break;
		case TelephonyManager.CALL_STATE_OFFHOOK: // answer the phone
			incomming_status.start();
			vibrator.cancel();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mTelephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_NONE);
	}

	private void answerRingingCall(Context context) {
		AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		boolean broadcastConnected = audioManager.isBluetoothScoAvailableOffCall() && audioManager.isBluetoothA2dpOn();
		if (broadcastConnected) {
			broadcastHeadsetConnected(context);
		}

		try {
			try {
				Runtime.getRuntime().exec("input keyevent " + Integer.toString(KeyEvent.KEYCODE_HEADSETHOOK));
			} catch (IOException e) {
				// Runtime.exec(String) had an I/O problem, try to fall back
				String enforcedPerm = "android.permission.CALL_PRIVILEGED";
				Intent btnDown = new Intent(Intent.ACTION_MEDIA_BUTTON).putExtra(Intent.EXTRA_KEY_EVENT,
						new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK));
				Intent btnUp = new Intent(Intent.ACTION_MEDIA_BUTTON).putExtra(Intent.EXTRA_KEY_EVENT,
						new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
				context.sendOrderedBroadcast(btnDown, enforcedPerm);
				context.sendOrderedBroadcast(btnUp, enforcedPerm);
			}
		} finally {
			if (broadcastConnected) {
				broadcastHeadsetConnected(context);
			}
		}
	}

	private void broadcastHeadsetConnected(Context context) {
		Intent i = new Intent(Intent.ACTION_HEADSET_PLUG);
		i.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY);
		i.putExtra("state", 0);
		i.putExtra("name", "mysms");
		try {
			context.sendOrderedBroadcast(i, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
