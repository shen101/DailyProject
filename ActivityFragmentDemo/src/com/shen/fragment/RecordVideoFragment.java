package com.shen.fragment;

import com.shen.activityfragmentdemo.BaseFragment;
import com.shen.activityfragmentdemo.R;
import com.shen.utils.GlassUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RecordVideoFragment extends BaseFragment {

	private View video_view = null;
	private TextView video_title;
	private ImageView video_left_btn, video_right_btn, video_icon;
	private RelativeLayout glass_video_camera_layout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		video_view = inflater.inflate(R.layout.public_home_fragment, container, false);
		initViews();
		return video_view;
	}

	private void initViews() {
		video_title = (TextView) video_view.findViewById(R.id.glass_public_home_title);
		video_title.setText(R.string.glass_recordvideo_title);
		video_icon = (ImageView) video_view.findViewById(R.id.glass_public_home_icon);
		video_icon.setImageResource(R.drawable.ic_glass_video);
		video_right_btn = (ImageView) video_view.findViewById(R.id.right_direction_icons);
		video_right_btn.setOnClickListener(MyOnLinster);
		video_left_btn = (ImageView) video_view.findViewById(R.id.left_direction_icons);
		video_left_btn.setOnClickListener(MyOnLinster);
		glass_video_camera_layout = (RelativeLayout) video_view.findViewById(R.id.glass_public_home_layout);
		glass_video_camera_layout.setOnClickListener(MyOnLinster);
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
				startTakePhotoFragment();
				break;
			case R.id.glass_public_home_layout:
				startPearViewInfoActivity();
				break;

			default:
				break;
			}
		}
	};

	private void startTakePhotoFragment() {
		// TODO Auto-generated method stub
		getRightTransaction(new TakePhotoFragment()).hide(this);
	}

	private void startPearViewInfoActivity() {
		// TODO Auto-generated method stub
		Intent video_intent = new Intent();
		video_intent.setAction(GlassUtils.ACTION_START_PEARVIEW_TAG);
		video_intent.putExtra(GlassUtils.ACTION_VIDEO_PHOTO_TYPE_TAG, GlassUtils.ACTION_VIDEO_PHOTO_TYPE_NUM);
		getActivity().startActivity(video_intent);
	}
}
