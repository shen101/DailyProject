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

public class PhoneFragment extends BaseFragment {

	private View phone_view = null;
	private TextView phone_title;
	private ImageView phone_left_btn, phone_right_btn, phone_icon;
	private RelativeLayout phone_layout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		phone_view = inflater.inflate(R.layout.public_home_fragment, container, false);
		initViews();
		return phone_view;
	}

	private void initViews() {
		phone_title = (TextView) phone_view.findViewById(R.id.glass_public_home_title);
		phone_title.setText(R.string.glass_phone_contacts_title);
		phone_icon = (ImageView) phone_view.findViewById(R.id.glass_public_home_icon);
		phone_icon.setImageResource(R.drawable.ic_glass_phone);
		phone_left_btn = (ImageView) phone_view.findViewById(R.id.left_direction_icons);
		phone_left_btn.setOnClickListener(MyOnLinster);
		phone_right_btn = (ImageView) phone_view.findViewById(R.id.right_direction_icons);
		phone_right_btn.setOnClickListener(MyOnLinster);
		phone_layout = (RelativeLayout) phone_view.findViewById(R.id.glass_public_home_layout);
		phone_layout.setOnClickListener(MyOnLinster);
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
				startMusicFragment();
				break;
			case R.id.glass_public_home_layout:
				startPhoneInfosFragment();
				break;
			default:
				break;
			}
		}
	};

	private void startMusicFragment() {
		// TODO Auto-generated method stub
		getRightTransaction(new MusicFragment()).hide(this);
	}

	private void startPhoneInfosFragment() {
		// TODO Auto-generated method stub
		getRightTransaction(new PhoneBookFragment()).hide(this);
	}
}
