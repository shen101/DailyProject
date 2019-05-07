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
import android.widget.TextView;

public class PhoneDialPadFragment extends BaseFragment {

	private View dialpad_view = null;
	private TextView dialpad_title;
	private ImageView dialpad_left_btn, dialpad_icon;
	private RelativeLayout glass_phone_dialpad_layout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		dialpad_view = inflater.inflate(R.layout.public_home_fragment, container, false);
		initViews();
		return dialpad_view;
	}

	private void initViews() {
		dialpad_title = (TextView) dialpad_view.findViewById(R.id.glass_public_home_title);
		dialpad_title.setText(R.string.glass_dialpad_title);
		dialpad_icon = (ImageView) dialpad_view.findViewById(R.id.glass_public_home_icon);
		dialpad_icon.setImageResource(R.drawable.ic_glass_dialpad);
		dialpad_left_btn = (ImageView) dialpad_view.findViewById(R.id.left_direction_icons);
		dialpad_left_btn.setOnClickListener(MyOnLinster);
		glass_phone_dialpad_layout = (RelativeLayout) dialpad_view.findViewById(R.id.glass_public_home_layout);
		glass_phone_dialpad_layout.setOnClickListener(MyOnLinster);
	}

	private OnClickListener MyOnLinster = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.left_direction_icons:
				onFragmentBackClick();
				break;
			case R.id.glass_public_home_layout:
				startPhoneDialPadMain();
				break;
			default:
				break;
			}
		}
	};

	private void startPhoneDialPadMain() {
		// TODO Auto-generated method stub
		Intent phoneDailPadMain_intent = new Intent();
		phoneDailPadMain_intent.setAction(GlassUtils.ACTION_START_DIALPAD_TAG);
		getActivity().startActivity(phoneDailPadMain_intent);
	}
}
