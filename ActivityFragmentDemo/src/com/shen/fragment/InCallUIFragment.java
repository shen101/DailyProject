package com.shen.fragment;

import java.io.IOException;
import com.shen.activityfragmentdemo.BaseFragment;
import com.shen.activityfragmentdemo.R;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class InCallUIFragment extends BaseFragment implements OnClickListener {

	private static final String TAG = "InCallUIFragment";
	private View incallui_view = null;
	private TextView incomming_name, incomming_status;
	private ImageButton incomming_btn;
	private int status = -1;
	private String number = "";
	private Context mContext;

	public InCallUIFragment(Context mcontext, int phone_status, String incommingnumber) {
		super();
		mContext = mcontext;
		status = phone_status;
		number = incommingnumber;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		incallui_view = inflater.inflate(R.layout.incallui_fragment, container, false);
		initViews();
		return incallui_view;
	}

	private void initViews() {
		incomming_name = (TextView) incallui_view.findViewById(R.id.glass_incallui_phone_name);
		incomming_name.setText(number);
		incomming_status = (TextView) incallui_view.findViewById(R.id.glass_incallui_phone_status);
		incomming_btn = (ImageButton) incallui_view.findViewById(R.id.glass_incallui_btn);
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
