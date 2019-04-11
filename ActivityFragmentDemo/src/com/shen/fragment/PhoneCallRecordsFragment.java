package com.shen.fragment;

import com.shen.activityfragmentdemo.BaseFragment;
import com.shen.activityfragmentdemo.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class PhoneCallRecordsFragment extends BaseFragment {

	private View callRecord_view = null;
	private ImageView callrecord_left_image, callrecord_right_image;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		callRecord_view = inflater.inflate(R.layout.phone_callrecord_fragment, container, false);
		initViews();
		return callRecord_view;
	}

	private void initViews() {
		callrecord_left_image = (ImageView) callRecord_view.findViewById(R.id.left_direction_icons);
		callrecord_right_image = (ImageView) callRecord_view.findViewById(R.id.right_direction_icons);
		callrecord_left_image.setOnClickListener(MyOnLinster);
		callrecord_right_image.setOnClickListener(MyOnLinster);
	}

	private OnClickListener MyOnLinster = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.left_direction_icons:
				startPhoneBookFragment();
				break;
			case R.id.right_direction_icons:
				startPhoneDialPadFragment();
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

	private void startPhoneBookFragment() {
		// TODO Auto-generated method stub
		getLeftTransaction(new PhoneBookFragment()).hide(this);
	}

}
