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

public class SettingsWlanFragment extends BaseFragment {

	private View wlan_view = null;
	private TextView wlan_title;
	private ImageView wlan_left_btn, wlan_right_btn, wlan_icon;
	private RelativeLayout wlan_layout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		wlan_view = inflater.inflate(R.layout.public_home_fragment, container, false);
		initViews();
		return wlan_view;
	}

	private void initViews() {
		wlan_title = (TextView) wlan_view.findViewById(R.id.glass_public_home_title);
		wlan_title.setText(R.string.glass_wlan_title);
		wlan_icon = (ImageView) wlan_view.findViewById(R.id.glass_public_home_icon);
		wlan_icon.setImageResource(R.drawable.ic_glass_rearview);
		wlan_left_btn = (ImageView) wlan_view.findViewById(R.id.left_direction_icons);
		wlan_left_btn.setOnClickListener(MyOnLinster);
		wlan_right_btn = (ImageView) wlan_view.findViewById(R.id.right_direction_icons);
		wlan_right_btn.setOnClickListener(MyOnLinster);
		wlan_layout = (RelativeLayout) wlan_view.findViewById(R.id.glass_public_home_layout);
		wlan_layout.setOnClickListener(MyOnLinster);
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
				startLanguageFragment();
				break;
			case R.id.glass_public_home_layout:
				startWlanInfoFragment();
				break;
			default:
				break;
			}
		}
	};

	private void startLanguageFragment() {
		// TODO Auto-generated method stub
		getRightTransaction(new SettingsLanguageFragment()).hide(this);
	}

	private void startWlanInfoFragment() {
		// TODO Auto-generated method stub
		getRightTransaction(new SettingsWlanInfoFragment()).hide(this);
	}
}
