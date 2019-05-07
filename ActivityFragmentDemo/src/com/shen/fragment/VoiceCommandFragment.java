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

public class VoiceCommandFragment extends BaseFragment {

	private View voice_view = null;
	private ImageView voice_right_btn, voice_left_btn, voice_icon;
	private TextView voice_title;
	private RelativeLayout voice_layout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		voice_view = inflater.inflate(R.layout.public_home_fragment, container, false);
		initViews();
		return voice_view;
	}

	private void initViews() {
		voice_title = (TextView) voice_view.findViewById(R.id.glass_public_home_title);
		voice_title.setText(R.string.glass_voicecommand_title);
		voice_icon = (ImageView) voice_view.findViewById(R.id.glass_public_home_icon);
		voice_icon.setImageResource(R.drawable.ic_glass_voice);
		voice_right_btn = (ImageView) voice_view.findViewById(R.id.right_direction_icons);
		voice_right_btn.setOnClickListener(MyOnLinster);
		voice_left_btn = (ImageView) voice_view.findViewById(R.id.left_direction_icons);
		voice_left_btn.setVisibility(View.GONE);
		voice_layout = (RelativeLayout) voice_view.findViewById(R.id.glass_public_home_layout);
		voice_layout.setOnClickListener(MyOnLinster);
	}

	private OnClickListener MyOnLinster = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.right_direction_icons:
				onFragmentBackClick();
				break;
			case R.id.glass_public_home_layout:
				break;
			default:
				break;
			}
		}
	};
}
