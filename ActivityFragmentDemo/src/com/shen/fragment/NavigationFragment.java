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

public class NavigationFragment extends BaseFragment {

	private View navigation_view = null;
	private TextView navigation_title;
	private ImageView navigation_left_image, navigation_right_image, navigation_icon;
	private RelativeLayout navigation_layout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		navigation_view = inflater.inflate(R.layout.public_home_fragment, container, false);
		initViews();
		return navigation_view;
	}

	private void initViews() {
		navigation_title = (TextView) navigation_view.findViewById(R.id.glass_public_home_title);
		navigation_title.setText(R.string.glass_navigation_title);
		navigation_icon = (ImageView) navigation_view.findViewById(R.id.glass_public_home_icon);
		navigation_icon.setImageResource(R.drawable.ic_glass_navigation);
		navigation_left_image = (ImageView) navigation_view.findViewById(R.id.left_direction_icons);
		navigation_left_image.setOnClickListener(MyOnLinster);
		navigation_right_image = (ImageView) navigation_view.findViewById(R.id.right_direction_icons);
		navigation_right_image.setOnClickListener(MyOnLinster);
		navigation_layout = (RelativeLayout) navigation_view.findViewById(R.id.glass_public_home_layout);
		navigation_layout.setOnClickListener(MyOnLinster);
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
				startRecordVideoFragment();
				break;
			case R.id.glass_public_home_layout:
				
				break;
			default:
				break;
			}
		}
	};

	private void startRecordVideoFragment() {
		// TODO Auto-generated method stub
		getRightTransaction(new RecordVideoFragment()).hide(this);
	}
}
