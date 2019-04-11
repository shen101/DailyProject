package com.shen.fragment;

import com.shen.activityfragmentdemo.BaseFragment;
import com.shen.activityfragmentdemo.R;
import com.shen.utils.GlassUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class PhoneDialPadFragment extends BaseFragment {

	private View dialpad_view = null;
	private ImageView dialpad_left_btn;
	private RelativeLayout glass_phone_dialpad_layout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		dialpad_view = inflater.inflate(R.layout.phone_dialpad_fragment, container, false);
		initViews();
		return dialpad_view;
	}

	private void initViews() {
		dialpad_left_btn = (ImageView) dialpad_view.findViewById(R.id.left_direction_icons);
		dialpad_left_btn.setOnClickListener(MyOnLinster);
		glass_phone_dialpad_layout = (RelativeLayout) dialpad_view.findViewById(R.id.glass_phone_dialpad_onclick);
		glass_phone_dialpad_layout.setOnClickListener(MyOnLinster);
	}

	private OnClickListener MyOnLinster = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.left_direction_icons:
				PhoneCallRecordsFragment();
				break;
			case R.id.glass_phone_dialpad_onclick:
				startPhoneDialPadMain();
				break;
			default:
				break;
			}
		}
	};

	private void PhoneCallRecordsFragment() {
		// TODO Auto-generated method stub
		getLeftTransaction(new PhoneCallRecordsFragment()).hide(this);
	}

	private void startPhoneDialPadMain() {
		// TODO Auto-generated method stub
		Intent phoneDailPadMain_intent = new Intent();
		phoneDailPadMain_intent.setAction(GlassUtils.ACTION_START_DIALPAD_TAG);
		getActivity().startActivity(phoneDailPadMain_intent);
	}
}
