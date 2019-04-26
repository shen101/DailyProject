package com.shen.fragment;

import com.shen.activityfragmentdemo.BaseFragment;
import com.shen.activityfragmentdemo.PearViewInfoActivity;
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

public class PearViewFragment extends BaseFragment {

	private View pear_view = null;
	private ImageView pear_left_btn, pear_right_btn;
	private RelativeLayout pearview_layout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		pear_view = inflater.inflate(R.layout.pear_view_fragment, container, false);
		initViews();
		return pear_view;
	}

	private void initViews() {
		pear_left_btn = (ImageView) pear_view.findViewById(R.id.left_direction_icons);
		pear_left_btn.setOnClickListener(MyOnLinster);
		pear_right_btn = (ImageView) pear_view.findViewById(R.id.right_direction_icons);
		pear_right_btn.setOnClickListener(MyOnLinster);
		pearview_layout = (RelativeLayout) pear_view.findViewById(R.id.glass_pearview_layout);
		pearview_layout.setOnClickListener(MyOnLinster);
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
				openSettingsMainFragment();
				break;
			case R.id.glass_pearview_layout:
				startPearViewInfoActivity();
				break;
			default:
				break;
			}

		}
	};

	private void openSettingsMainFragment() {
		// TODO Auto-generated method stub
		getRightTransaction(new SettingsMainFragment()).hide(this);
	}

	private void startPearViewInfoActivity() {
		// TODO Auto-generated method stub
		Intent pearview_intent = new Intent();
		pearview_intent.setAction(GlassUtils.ACTION_START_PEARVIEW_TAG);
		pearview_intent.putExtra(GlassUtils.ACTION_PREAVIEW_PHOTO_TYPE_TAG, GlassUtils.ACTION_PREAVIEW_PHOTO_TYPE_NUM);
		getActivity().startActivity(pearview_intent);
	}
}
