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

public class SettingsLanguageFragment extends BaseFragment {

	private View language_view = null;
	private TextView language_title;
	private ImageView language_left_btn, language_right_btn, language_icon;
	private RelativeLayout language_layout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		language_view = inflater.inflate(R.layout.public_home_fragment, container, false);
		initViews();
		return language_view;
	}

	private void initViews() {
		language_title = (TextView) language_view.findViewById(R.id.glass_public_home_title);
		language_title.setText(R.string.glass_language_title);
		language_icon = (ImageView) language_view.findViewById(R.id.glass_public_home_icon);
		language_icon.setImageResource(R.drawable.ic_glass_rearview);
		language_left_btn = (ImageView) language_view.findViewById(R.id.left_direction_icons);
		language_left_btn.setOnClickListener(MyOnLinster);
		language_right_btn = (ImageView) language_view.findViewById(R.id.right_direction_icons);
		language_right_btn.setOnClickListener(MyOnLinster);
		language_layout = (RelativeLayout) language_view.findViewById(R.id.glass_public_home_layout);
		language_layout.setOnClickListener(MyOnLinster);
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
				startBackUpFragment();
				break;
			case R.id.glass_public_home_layout:
				startLanguageInfoFragment();
				break;
			default:
				break;
			}
		}
	};

	private void startLanguageInfoFragment() {
		// TODO Auto-generated method stub
		getRightTransaction(new SettingsLanguageInfoFragment()).hide(this);
	}

	private void startBackUpFragment() {
		// TODO Auto-generated method stub
		getRightTransaction(new SettingsBackUpFragment()).hide(this);
	}
}
