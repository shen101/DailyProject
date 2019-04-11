package com.shen.fragment;

import com.shen.activityfragmentdemo.BaseFragment;
import com.shen.activityfragmentdemo.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class TakePhotoFragment extends BaseFragment {

	private View photo_view = null;
	private ImageView photo_left_btn, photo_right_btn;

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
				startPhoneFragment();
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

	private void startRecordVideoFragment() {
		// TODO Auto-generated method stub
		getLeftTransaction(new RecordVideoFragment()).disallowAddToBackStack().hide(this);
	}
}
