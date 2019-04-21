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

public class SettingsWlanFragment extends BaseFragment {

	private View wlan_view = null;
	private ImageView wlan_left_btn, wlan_right_btn;
	private RelativeLayout wlan_layout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		wlan_view = inflater.inflate(R.layout.settings_wlan_fragment, container, false);
		initViews();
		return wlan_view;
	}

	private void initViews() {
		wlan_left_btn = (ImageView) wlan_view.findViewById(R.id.left_direction_icons);
		wlan_left_btn.setOnClickListener(MyOnLinster);
		wlan_right_btn = (ImageView) wlan_view.findViewById(R.id.right_direction_icons);
		wlan_right_btn.setOnClickListener(MyOnLinster);
		wlan_layout = (RelativeLayout) wlan_view.findViewById(R.id.glass_wlan_layout);
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
			case R.id.glass_wlan_layout:
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
