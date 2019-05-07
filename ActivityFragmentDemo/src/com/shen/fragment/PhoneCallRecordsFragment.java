package com.shen.fragment;

import com.shen.activityfragmentdemo.BaseFragment;
import com.shen.activityfragmentdemo.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PhoneCallRecordsFragment extends BaseFragment {

	private View callRecord_view = null;
	private TextView callrecord_title;
	private ImageView callrecord_left_image, callrecord_right_image, callrecord_icon;
	private RelativeLayout callrecord_layout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		callRecord_view = inflater.inflate(R.layout.public_home_fragment, container, false);
		initViews();
		return callRecord_view;
	}

	private void initViews() {
		callrecord_title = (TextView) callRecord_view.findViewById(R.id.glass_public_home_title);
		callrecord_title.setText(R.string.glass_callrecord_title);
		callrecord_icon = (ImageView) callRecord_view.findViewById(R.id.glass_public_home_icon);
		callrecord_icon.setImageResource(R.drawable.ic_glass_recents);
		callrecord_left_image = (ImageView) callRecord_view.findViewById(R.id.left_direction_icons);
		callrecord_right_image = (ImageView) callRecord_view.findViewById(R.id.right_direction_icons);
		callrecord_layout = (RelativeLayout) callRecord_view.findViewById(R.id.glass_public_home_layout);
		callrecord_left_image.setOnClickListener(MyOnLinster);
		callrecord_right_image.setOnClickListener(MyOnLinster);
		callrecord_layout.setOnClickListener(MyOnLinster);
	}

	private OnClickListener MyOnLinster = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.left_direction_icons:
				onFragmentBackClick();
				break;
			case R.id.right_direction_icons:
				startPhoneDialPadFragment();
				break;
			case R.id.glass_public_home_layout:
				break;

			default:
				break;
			}
		}
	};

	private void startPhoneDialPadFragment() {
		// TODO Auto-generated method stub
		getRightTransaction(new PhoneDialPadFragment()).hide(this);
	}
}
