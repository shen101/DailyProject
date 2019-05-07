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

public class PhoneBookFragment extends BaseFragment {

	private View phonebook_view = null;
	private TextView phonebook_title;
	private ImageView phonebook_right_btn, phonebook_left_btn, phonebook_icon;
	private RelativeLayout phonebook_layout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		phonebook_view = inflater.inflate(R.layout.public_home_fragment, container, false);
		initViews();
		return phonebook_view;
	}

	private void initViews() {
		phonebook_title = (TextView) phonebook_view.findViewById(R.id.glass_public_home_title);
		phonebook_title.setText(R.string.glass_phone_contacts_title);
		phonebook_icon = (ImageView) phonebook_view.findViewById(R.id.glass_public_home_icon);
		phonebook_icon.setImageResource(R.drawable.ic_glass_contacts);
		phonebook_right_btn = (ImageView) phonebook_view.findViewById(R.id.right_direction_icons);
		phonebook_right_btn.setOnClickListener(MyOnLinster);
		phonebook_left_btn = (ImageView) phonebook_view.findViewById(R.id.left_direction_icons);
		phonebook_left_btn.setOnClickListener(MyOnLinster);
		phonebook_layout = (RelativeLayout) phonebook_view.findViewById(R.id.glass_public_home_layout);
		phonebook_layout.setOnClickListener(MyOnLinster);
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
				startCallRecordsFragment();
				break;
			case R.id.glass_public_home_layout:

				break;

			default:
				break;
			}
		}
	};

	private void startCallRecordsFragment() {
		// TODO Auto-generated method stub
		getRightTransaction(new PhoneCallRecordsFragment()).hide(this);
	}
}
