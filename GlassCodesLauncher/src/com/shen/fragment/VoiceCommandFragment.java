package com.shen.fragment;

import com.shen.activityfragmentdemo.BaseFragment;
import com.shen.activityfragmentdemo.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class VoiceCommandFragment extends BaseFragment {

	private View voice_view = null;
	private ImageView voice_right_btn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		voice_view = inflater.inflate(R.layout.voice_command_fragment, container, false);
		initViews();
		return voice_view;
	}

	private void initViews() {
		voice_right_btn = (ImageView) voice_view.findViewById(R.id.right_direction_icons);
		voice_right_btn.setOnClickListener(MyOnLinster);
	}

	private OnClickListener MyOnLinster = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.right_direction_icons:
				onFragmentBackClick();
				break;

			default:
				break;
			}
		}
	};
}
