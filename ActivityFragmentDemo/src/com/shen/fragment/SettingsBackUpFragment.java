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

public class SettingsBackUpFragment extends BaseFragment {

	private View backup_view = null;
	private ImageView backup_left_btn, backup_right_btn;
	private TextView backup_title;
	private RelativeLayout language_layout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		backup_view = inflater.inflate(R.layout.settings_language_fragment, container, false);
		initViews();
		return backup_view;
	}

	private void initViews() {
		backup_left_btn = (ImageView) backup_view.findViewById(R.id.left_direction_icons);
		backup_left_btn.setOnClickListener(MyOnLinster);
		backup_right_btn = (ImageView) backup_view.findViewById(R.id.right_direction_icons);
		backup_right_btn.setOnClickListener(MyOnLinster);
		backup_title = (TextView) backup_view.findViewById(R.id.glass_language_text);
		backup_title.setText(R.string.glass_backup_title);
		language_layout = (RelativeLayout) backup_view.findViewById(R.id.glass_language_layout);
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

				break;
			case R.id.glass_language_layout:
				startBackUpInfoFragment();
				break;
			default:
				break;
			}
		}
	};

	private void startBackUpInfoFragment() {
		// TODO Auto-generated method stub
		getRightTransaction(new SettingsBackUpInfoFragment()).hide(this);
	}
}
