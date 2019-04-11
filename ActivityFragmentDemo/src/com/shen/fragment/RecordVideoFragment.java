package com.shen.fragment;

import com.shen.activityfragmentdemo.BaseFragment;
import com.shen.activityfragmentdemo.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class RecordVideoFragment extends BaseFragment {

	private View video_view = null;
	private ImageView video_left_btn, video_right_btn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		video_view = inflater.inflate(R.layout.recordvideo_fragment, container, false);
		initViews();
		return video_view;
	}

	private void initViews() {
		video_right_btn = (ImageView) video_view.findViewById(R.id.right_direction_icons);
		video_right_btn.setOnClickListener(MyOnLinster);
		video_left_btn = (ImageView) video_view.findViewById(R.id.left_direction_icons);
		video_left_btn.setOnClickListener(MyOnLinster);
	}

	private OnClickListener MyOnLinster = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.left_direction_icons:
				startRecordVideoFragment();
				break;
			case R.id.right_direction_icons:
				startTakePhotoFragment();
				break;

			default:
				break;
			}
		}
	};

	private void startRecordVideoFragment() {
		// TODO Auto-generated method stub
		getLeftTransaction(new RecordVideoFragment()).disallowAddToBackStack().hide(this);
	}

	private void startTakePhotoFragment() {
		// TODO Auto-generated method stub
		getRightTransaction(new TakePhotoFragment()).hide(this);
	}
}
