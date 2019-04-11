package com.shen.fragment;

import com.shen.activityfragmentdemo.BaseFragment;
import com.shen.activityfragmentdemo.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class NavigationFragment extends BaseFragment {

	private View navigation_view = null;
	private ImageView navigation_left_image, navigation_right_image;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		navigation_view = inflater.inflate(R.layout.navigation_fragment, container, false);
		initViews();
		return navigation_view;
	}

	private void initViews() {
		navigation_left_image = (ImageView) navigation_view.findViewById(R.id.left_direction_icons);
		navigation_left_image.setOnClickListener(MyOnLinster);
		navigation_right_image = (ImageView) navigation_view.findViewById(R.id.right_direction_icons);
		navigation_right_image.setOnClickListener(MyOnLinster);
	}

	private OnClickListener MyOnLinster = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.left_direction_icons:
				startMianTimeFragment();
				break;
			case R.id.right_direction_icons:
				startRecordVideoFragment();
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
	private void startMianTimeFragment() {
		// TODO Auto-generated method stub
		getLeftTransaction(new MainTimeFragment()).disallowAddToBackStack().hide(this);
	}
}
