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

public class MusicFragment extends BaseFragment {

	private View music_view = null;
	private ImageView music_left_btn, music_right_btn;
	private RelativeLayout music_layout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		music_view = inflater.inflate(R.layout.music_fragment, container, false);
		initViews();
		return music_view;
	}

	private void initViews() {
		music_left_btn = (ImageView) music_view.findViewById(R.id.left_direction_icons);
		music_left_btn.setOnClickListener(MyOnLinster);
		music_right_btn = (ImageView) music_view.findViewById(R.id.right_direction_icons);
		music_right_btn.setOnClickListener(MyOnLinster);
		music_layout = (RelativeLayout) music_view.findViewById(R.id.glass_music_layout);
		music_layout.setOnClickListener(MyOnLinster);
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
				startPearViewFragment();
				break;
			case R.id.glass_music_layout:

				break;
			default:
				break;
			}
		}
	};

	private void startPearViewFragment() {
		// TODO Auto-generated method stub
		getRightTransaction(new PearViewFragment()).hide(this);
	}

}
