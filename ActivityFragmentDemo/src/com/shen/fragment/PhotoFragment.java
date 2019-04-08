package com.shen.fragment;

import com.shen.activityfragmentdemo.BaseFragment;
import com.shen.activityfragmentdemo.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class PhotoFragment extends BaseFragment {

	private View photo_view = null;
	private ImageView video_left_btn, video_right_btn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		photo_view = inflater.inflate(R.layout.photo_fragment, container, false);
		initViews();
		return photo_view;
	}

	private void initViews() {
		video_left_btn = (ImageView) photo_view.findViewById(R.id.left_direction_icons);
		video_left_btn.setOnClickListener(MyOnLinster);
		video_right_btn = (ImageView) photo_view.findViewById(R.id.right_direction_icons);
		video_right_btn.setOnClickListener(MyOnLinster);
	}

	private OnClickListener MyOnLinster = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			openOneFragment();
		}
	};

	private void openOneFragment() {
		// TODO Auto-generated method stub
		getRightTransaction(new MusicFragment()).hide(this);
	}
}
