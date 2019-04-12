package com.shen.activityfragmentdemo;

import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;
import com.shen.utils.GlassUtils;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class PhoneDialPadMainActivity extends BaseActivity implements OnClickListener, OnLongClickListener {

	private Button number_one, number_two, number_three, number_four, number_five, number_six, number_seven,
			number_eight, number_nine, number_zero, glass_asterisk, glass_hash;
	private TextView letter_one, letter_two, letter_three, letter_four, letter_five, letter_six, letter_seven,
			letter_eight, letter_nine, letter_zero;
	private ImageButton phone_call_btn, delete_call_number;
	private EditText input_number;
	private Chronometer phone_status;
	private Vibrator mVibrator;
	private Context mContext;
	private String number = "";
	private TelephonyManager mTelephonyManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phone_dialpad_layout);
		mContext = PhoneDialPadMainActivity.this;
		getActionBar().hide();
		initViews();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initTelephonyListener();
		mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		IntentFilter mRingFilter = new IntentFilter();
		mRingFilter.addAction(GlassUtils.ACTION_IS_CONNECTED_CALL_TAG);
		registerReceiver(ringReceiver, mRingFilter);
	}

	private BroadcastReceiver ringReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			Log.i("shen", "action = " + action);
			if (GlassUtils.ACTION_IS_CONNECTED_CALL_TAG.equals(action)) {
				Boolean isstatus = intent.getBooleanExtra("phonestatus", false);
				if (isstatus) {
					phone_status.stop();
					phone_status.start();
					phone_call_btn.setPressed(true);
				} else {
					phone_status.stop();
					phone_call_btn.setPressed(false);
				}
			}
		}
	};

	private void initViews() {
		number_one = (Button) findViewById(R.id.one_number);
		number_one.setOnClickListener(this);
		letter_one = (TextView) findViewById(R.id.one_letter);
		letter_one.setOnClickListener(this);

		number_two = (Button) findViewById(R.id.two_number);
		number_two.setOnClickListener(this);
		letter_two = (TextView) findViewById(R.id.two_letter);
		letter_two.setOnClickListener(this);

		number_three = (Button) findViewById(R.id.three_number);
		number_three.setOnClickListener(this);
		letter_three = (TextView) findViewById(R.id.three_letter);
		letter_three.setOnClickListener(this);

		number_four = (Button) findViewById(R.id.four_number);
		number_four.setOnClickListener(this);
		letter_four = (TextView) findViewById(R.id.four_letter);
		letter_four.setOnClickListener(this);

		number_five = (Button) findViewById(R.id.five_number);
		number_five.setOnClickListener(this);
		letter_five = (TextView) findViewById(R.id.five_letter);
		letter_five.setOnClickListener(this);

		number_six = (Button) findViewById(R.id.six_number);
		number_six.setOnClickListener(this);
		letter_six = (TextView) findViewById(R.id.six_letter);
		letter_six.setOnClickListener(this);

		number_seven = (Button) findViewById(R.id.seven_number);
		number_seven.setOnClickListener(this);
		letter_seven = (TextView) findViewById(R.id.seven_letter);
		letter_seven.setOnClickListener(this);

		number_eight = (Button) findViewById(R.id.eight_number);
		number_eight.setOnClickListener(this);
		letter_eight = (TextView) findViewById(R.id.eight_letter);
		letter_eight.setOnClickListener(this);

		number_nine = (Button) findViewById(R.id.nine_number);
		number_nine.setOnClickListener(this);
		letter_nine = (TextView) findViewById(R.id.nine_letters);
		letter_nine.setOnClickListener(this);

		number_zero = (Button) findViewById(R.id.zero_number);
		number_zero.setOnClickListener(this);
		letter_zero = (TextView) findViewById(R.id.zero_letter);
		letter_zero.setOnClickListener(this);

		glass_asterisk = (Button) findViewById(R.id.glass_asterisk);
		glass_asterisk.setOnClickListener(this);

		glass_hash = (Button) findViewById(R.id.glass_hash);
		glass_hash.setOnClickListener(this);

		phone_status = (Chronometer) findViewById(R.id.glass_phone_out_status);
		phone_status.setText("");
		phone_status.stop();

		phone_call_btn = (ImageButton) findViewById(R.id.glass_phone_call_btn);
		phone_call_btn.setOnClickListener(this);
		delete_call_number = (ImageButton) findViewById(R.id.deleteButton);
		delete_call_number.setOnClickListener(this);
		delete_call_number.setOnLongClickListener(this);
		input_number = (EditText) findViewById(R.id.phone_number);
		input_number.addTextChangedListener(number_text_listener);
	}

	private TextWatcher number_text_listener = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			Log.i("shen",
					"onTextChanged s = " + s + ",  start = " + start + ",  before = " + before + ",  count = " + count);
			number = s.toString().trim();
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			// TODO Auto-generated method stub
			Log.i("shen", "beforeTextChanged s = " + s + ",  start = " + start + ",  count = " + count);
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.one_number:
		case R.id.one_letter:
			keyPressed(KeyEvent.KEYCODE_1);
			break;
		case R.id.two_number:
		case R.id.two_letter:
			keyPressed(KeyEvent.KEYCODE_2);
			break;
		case R.id.three_number:
		case R.id.three_letter:
			keyPressed(KeyEvent.KEYCODE_3);
			break;
		case R.id.four_number:
		case R.id.four_letter:
			keyPressed(KeyEvent.KEYCODE_4);
			break;
		case R.id.five_number:
		case R.id.five_letter:
			keyPressed(KeyEvent.KEYCODE_5);
			break;
		case R.id.six_number:
		case R.id.six_letter:
			keyPressed(KeyEvent.KEYCODE_6);
			break;
		case R.id.seven_number:
		case R.id.seven_letter:
			keyPressed(KeyEvent.KEYCODE_7);
			break;
		case R.id.eight_number:
		case R.id.eight_letter:
			keyPressed(KeyEvent.KEYCODE_8);
			break;
		case R.id.nine_number:
		case R.id.nine_letters:
			keyPressed(KeyEvent.KEYCODE_9);
			break;
		case R.id.zero_number:
		case R.id.zero_letter:
			keyPressed(KeyEvent.KEYCODE_0);
			break;
		case R.id.glass_asterisk:
			keyPressed(KeyEvent.KEYCODE_STAR);
			break;
		case R.id.deleteButton:
			keyPressed(KeyEvent.KEYCODE_DEL);
			break;
		case R.id.glass_hash:
			keyPressed(KeyEvent.KEYCODE_POUND);
			break;
		case R.id.glass_phone_call_btn:

			if (status_text == TelephonyManager.CALL_STATE_OFFHOOK) {
				endPhone(mContext, mTelephonyManager);
			} else {
				dialNumber();
			}

			break;
		default:
			break;
		}
	}

	private int status_text = -1;

	private void keyPressed(int keyCode) {
		mVibrator.vibrate(GlassUtils.DURATION);
		KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
		input_number.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onLongClick(View view) {
		switch (view.getId()) {
		case R.id.deleteButton:
			Editable digits = input_number.getText();
			digits.clear();
			return true;
		}
		return false;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mVibrator.cancel();
		unregisterReceiver(ringReceiver);
	}

	public static void endPhone(Context c, TelephonyManager tm) {
		try {
			ITelephony iTelephony;
			Method getITelephonyMethod = TelephonyManager.class.getDeclaredMethod("getITelephony", (Class[]) null);
			getITelephonyMethod.setAccessible(true);
			iTelephony = (ITelephony) getITelephonyMethod.invoke(tm, (Object[]) null);
			iTelephony.endCall();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void dialNumber() {
		// if (GlassUtils.isAllowTelePhoneNumber(number)) {
		if (number.length() > 14) {
			Toast.makeText(mContext, R.string.glass_number_invalid, Toast.LENGTH_LONG).show();
		} else if (number.length() > 0 && number.length() <= 14) {
			startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number)));
			Log.i("shen", "21212222222222222222222222");
			phone_call_btn.setPressed(true);
		} else {
			Toast.makeText(mContext, R.string.glass_number_no_null, Toast.LENGTH_LONG).show();
		}
		// }else{
		// Toast.makeText(mContext, "you phone number is error",
		// Toast.LENGTH_LONG).show();
		// }
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
			Log.i("shen", "state 1111111 = " + state + ",  incomingNumber = " + incomingNumber);
			initInCallFragment(state, incomingNumber);
		}
	};

	private void initInCallFragment(int status, String incomingNumber) {

		if ("".equals(incomingNumber)) {
			return;
		}
		status_text = status;
		switch (status) {
		case TelephonyManager.CALL_STATE_RINGING: // Incoming call

			break;
		case TelephonyManager.CALL_STATE_OFFHOOK: // Outgoing
			phone_call_btn.setPressed(true);
			phone_status.setText("calling");
			break;
		case TelephonyManager.CALL_STATE_IDLE: // Hang up
			phone_call_btn.setPressed(false);
			phone_status.setText("");
			phone_status.stop();
			break;
		default:
			break;
		}
	}
}
