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

public class SettingsMainFragment extends BaseFragment {

	private View settings_view = null;
	private ImageView settings_left_btn;
	private RelativeLayout settings_layout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		settings_view = inflater.inflate(R.layout.settings_main_fragment, container, false);
		initViews();
		return settings_view;
	}

	private void initViews() {
		settings_left_btn = (ImageView) settings_view.findViewById(R.id.left_direction_icons);
		settings_left_btn.setOnClickListener(MyOnLinster);
		settings_layout = (RelativeLayout) settings_view.findViewById(R.id.glass_settings_layout);
		settings_layout.setOnClickListener(MyOnLinster);
	}

	private OnClickListener MyOnLinster = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.left_direction_icons:
				onFragmentBackClick();
				break;
			case R.id.glass_settings_layout:
				startLanguageFragment();
				break;
			default:
				break;
			}
		}
	};

	private void startLanguageFragment() {
		// TODO Auto-generated method stub
		getRightTransaction(new SettingsWlanFragment()).hide(this);
	}
}
