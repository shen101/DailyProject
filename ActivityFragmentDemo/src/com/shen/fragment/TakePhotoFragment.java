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

public class TakePhotoFragment extends BaseFragment {

	private View photo_view = null;
	private ImageView photo_left_btn, photo_right_btn;
	private RelativeLayout glass_take_photo_layout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		photo_view = inflater.inflate(R.layout.photo_fragment, container, false);
		initViews();
		return photo_view;
	}

	private void initViews() {
		photo_left_btn = (ImageView) photo_view.findViewById(R.id.left_direction_icons);
		photo_left_btn.setOnClickListener(MyOnLinster);
		photo_right_btn = (ImageView) photo_view.findViewById(R.id.right_direction_icons);
		photo_right_btn.setOnClickListener(MyOnLinster);
		glass_take_photo_layout = (RelativeLayout) photo_view.findViewById(R.id.glass_take_photo_layout);
		glass_take_photo_layout.setOnClickListener(MyOnLinster);
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
				startPhoneFragment();
				break;
			case R.id.glass_take_photo_layout:
				startPearViewInfoActivity();
				break;

			default:
				break;
			}
		}
	};

	private void startPhoneFragment() {
		// TODO Auto-generated method stub
		getRightTransaction(new PhoneFragment()).hide(this);
	}

	private void startPearViewInfoActivity() {
		// TODO Auto-generated method stub
		Intent take_photo_intent = new Intent();
		take_photo_intent.setAction(GlassUtils.ACTION_START_PEARVIEW_TAG);
		take_photo_intent.putExtra(GlassUtils.ACTION_TAKE_PHOTO_TYPE_TAG, GlassUtils.ACTION_TAKE_PHOTO_TYPE_NUM);
		getActivity().startActivity(take_photo_intent);
	}
}
