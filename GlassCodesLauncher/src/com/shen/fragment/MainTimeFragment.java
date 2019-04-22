package com.shen.fragment;

import com.shen.activityfragmentdemo.BaseFragment;
import com.shen.activityfragmentdemo.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MainTimeFragment extends BaseFragment {

	private View main_time_view = null;
	private ImageView main_time_left_image, main_time_right_image;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		main_time_view = inflater.inflate(R.layout.main_time_fragment, container, false);
		initViews();
		return main_time_view;
	}

	private void initViews() {
		main_time_left_image = (ImageView) main_time_view.findViewById(R.id.left_direction_icons);
		main_time_right_image = (ImageView) main_time_view.findViewById(R.id.right_direction_icons);
		main_time_left_image.setOnClickListener(MyOnLinster);
		main_time_right_image.setOnClickListener(MyOnLinster);
	}

	private OnClickListener MyOnLinster = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.left_direction_icons:
				openVoiceFragment();
				break;
			case R.id.right_direction_icons:
				openNavigationFragment();
				break;

			default:
				break;
			}
		}
	};

	private void openNavigationFragment() {
		// TODO Auto-generated method stub
		getRightTransaction(new NavigationFragment()).hide(this);
	}

	private void openVoiceFragment() {
		// TODO Auto-generated method stub
		getLeftTransaction(new VoiceCommandFragment()).hide(this);
	}
}
