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

public class SettingsNotificationFragment extends BaseFragment {

	private View noti_view = null;
	private TextView noti_title;
	private ImageView noti_left_btn, noti_icon;
	private RelativeLayout noti_layout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		noti_view = inflater.inflate(R.layout.public_home_fragment, container, false);
		initViews();
		return noti_view;
	}

	private void initViews() {
		noti_title = (TextView) noti_view.findViewById(R.id.glass_public_home_title);
		noti_title.setText(R.string.glass_settings_noti_title);
		noti_icon = (ImageView) noti_view.findViewById(R.id.glass_public_home_icon);
//		noti_icon.setImageResource(R.drawable.ic_glass_setting);
		noti_left_btn = (ImageView) noti_view.findViewById(R.id.left_direction_icons);
		noti_left_btn.setOnClickListener(MyOnLinster);
		noti_layout = (RelativeLayout) noti_view.findViewById(R.id.glass_public_home_layout);
		noti_layout.setOnClickListener(MyOnLinster);
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
				startNotificationInfoFragment();
				break;
			default:
				break;
			}
		}
	};

	private void startNotificationInfoFragment() {
		// TODO Auto-generated method stub
		getRightTransaction(new SettingsNotificationInfoFragment()).hide(this);
	}
}
