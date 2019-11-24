package com.yiyang.helmetclientevrtool.activity;

import java.util.ArrayList;

import com.yiyang.helmetclientevrtool.R;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class HelmetSettingCameraActivity extends HelmetBaseActivity {

	private ActionBar mActionBar;
	private TextView actionbar_title, front_text, rear_text, front_btn, rear_btn;
	private ViewPager mPager;
	private View pager_one_layout, pager_two_layout, reft_line, right_line;
	private ImageView actionbar_back_btn, actionbar_save_btn;
	private RadioButton front_video_hd_item, front_video_full_hd_item, front_pic_ordinary_item, front_pic_fine_item,
			front_pic_ultra_clear_item;
	private RadioButton rear_video_sd_item, rear_video_hd_item, rear_pic_ordinary_item, rear_pic_fine_item,
			rear_pic_ultra_clear_item;
	private RadioGroup front_video_group, front_pic_group, rear_video_group, rear_pic_group;
	private ArrayList<View> fragmnet_list = new ArrayList<View>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.helmet_setting_camera_main);

		initViews();

		setCustomActionBarStyle();
	}

	private void initViews() {
		// TODO Auto-generated method stub
		mPager = (ViewPager) findViewById(R.id.helmet_camera_viewpager);

		front_text = (TextView) findViewById(R.id.helmet_setting_camera_front_text);
		front_text.setOnClickListener(clicklistener);
		rear_text = (TextView) findViewById(R.id.helmet_setting_camera_rear_text);
		rear_text.setOnClickListener(clicklistener);

		reft_line = (View) findViewById(R.id.helmet_setting_camera_tab_left_line);
		right_line = (View) findViewById(R.id.helmet_setting_camera_tab_right_line);

		pager_one_layout = getLayoutInflater().inflate(R.layout.helmet_setting_camera_tab_one_layout, null);
		pager_two_layout = getLayoutInflater().inflate(R.layout.helmet_setting_camera_tab_two_layout, null);

		front_btn = (TextView) pager_one_layout.findViewById(R.id.helmet_camera_front_btn);
		front_btn.setOnClickListener(clicklistener);

		front_video_hd_item = (RadioButton) pager_one_layout
				.findViewById(R.id.helmet_setting_camera_front_hd_item_layout);
		front_video_full_hd_item = (RadioButton) pager_one_layout
				.findViewById(R.id.helmet_setting_camera_front_full_hd_item_layout);
		front_video_group = (RadioGroup) pager_one_layout.findViewById(R.id.helmet_setting_front_video_layout);
		front_video_group.setOnCheckedChangeListener(grouplistener);

		front_pic_ordinary_item = (RadioButton) pager_one_layout
				.findViewById(R.id.helmet_setting_camera_front_ordinary_pic_item_layout);
		front_pic_fine_item = (RadioButton) pager_one_layout
				.findViewById(R.id.helmet_setting_camera_front_fine_pic_item_layout);
		front_pic_ultra_clear_item = (RadioButton) pager_one_layout
				.findViewById(R.id.helmet_setting_camera_front_ultra_clear_pic_item_layout);
		front_pic_group = (RadioGroup) pager_one_layout.findViewById(R.id.helmet_setting_front_pic_layout);
		front_pic_group.setOnCheckedChangeListener(grouplistener);

		rear_btn = (TextView) pager_two_layout.findViewById(R.id.helmet_camera_rear_btn);
		rear_btn.setOnClickListener(clicklistener);

		rear_video_sd_item = (RadioButton) pager_two_layout
				.findViewById(R.id.helmet_setting_camera_rear_sd_item_layout);
		rear_video_hd_item = (RadioButton) pager_two_layout
				.findViewById(R.id.helmet_setting_camera_rear_hd_item_layout);
		rear_video_group = (RadioGroup) pager_two_layout.findViewById(R.id.helmet_setting_rear_video_layout);
		rear_video_group.setOnCheckedChangeListener(grouplistener);

		rear_pic_ordinary_item = (RadioButton) pager_two_layout
				.findViewById(R.id.helmet_setting_camera_rear_ordinary_pic_item_layout);
		rear_pic_fine_item = (RadioButton) pager_two_layout
				.findViewById(R.id.helmet_setting_camera_rear_fine_pic_item_layout);
		rear_pic_ultra_clear_item = (RadioButton) pager_two_layout
				.findViewById(R.id.helmet_setting_camera_rear_ultra_clear_pic_item_layout);
		rear_pic_group = (RadioGroup) pager_two_layout.findViewById(R.id.helmet_setting_rear_pic_layout);
		rear_pic_group.setOnCheckedChangeListener(grouplistener);

		fragmnet_list.add(pager_one_layout);
		fragmnet_list.add(pager_two_layout);

		mPager.setAdapter(pagerAdapter);
		mPager.addOnPageChangeListener(mPageChangeListener);
		updateCameraInfo(0);
	}

	private void setCustomActionBarStyle() {
		// TODO Auto-generated method stub
		mActionBar = getActionBar();
		if (mActionBar == null) {
			return;
		} else {
			mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM,
					ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
			mActionBar.setCustomView(R.layout.helmet_custom_actionbar_main);
			initActionBarViews(mActionBar);
		}
	}

	private void initActionBarViews(ActionBar mActionBar) {
		// TODO Auto-generated method stub
		actionbar_back_btn = (ImageView) mActionBar.getCustomView().findViewById(R.id.helmet_actionbar_back_icon);
		actionbar_back_btn.setOnClickListener(clicklistener);
		actionbar_save_btn = (ImageView) mActionBar.getCustomView().findViewById(R.id.helmet_actionbar_save_icon);
		actionbar_save_btn.setOnClickListener(clicklistener);
		actionbar_title = (TextView) mActionBar.getCustomView().findViewById(R.id.helmet_actionbar_title);
		actionbar_title.setText(R.string.helmet_setting_camera_title);
	}

	@SuppressLint("ResourceAsColor")
	private void updateCameraInfo(int num) {
		if (num == 0) {
			reft_line.setSelected(true);
			right_line.setSelected(false);
			front_text.setTextColor(0xFFFFFFFF);
			rear_text.setTextColor(0xFF89929C);
		} else if (num == 1) {
			reft_line.setSelected(false);
			right_line.setSelected(true);
			front_text.setTextColor(0xFF89929C);
			rear_text.setTextColor(0xFFFFFFFF);
		}
	}

	private OnPageChangeListener mPageChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			updateCameraInfo(arg0);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
		}
	};

	private PagerAdapter pagerAdapter = new PagerAdapter() {

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return fragmnet_list.size();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView(fragmnet_list.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			container.addView(fragmnet_list.get(position));
			return fragmnet_list.get(position);
		}
	};

	private OnCheckedChangeListener grouplistener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			if (checkedId == front_video_hd_item.getId()) {

			} else if (checkedId == front_video_full_hd_item.getId()) {

			} else if (checkedId == front_pic_ordinary_item.getId()) {

			} else if (checkedId == front_pic_fine_item.getId()) {

			} else if (checkedId == front_pic_ultra_clear_item.getId()) {

			} else if (checkedId == rear_video_sd_item.getId()) {

			} else if (checkedId == rear_video_hd_item.getId()) {

			} else if (checkedId == rear_pic_ordinary_item.getId()) {

			} else if (checkedId == rear_pic_fine_item.getId()) {

			} else if (checkedId == rear_pic_ultra_clear_item.getId()) {

			}
		}
	};

	private OnClickListener clicklistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.helmet_actionbar_back_icon:
				finish();
				break;
			case R.id.helmet_actionbar_save_icon:
				break;
			case R.id.helmet_setting_camera_front_text:
				mPager.setCurrentItem(0);
				break;
			case R.id.helmet_setting_camera_rear_text:
				mPager.setCurrentItem(1);
				break;
			case R.id.helmet_camera_front_btn:
				front_btn.setPressed(true);
				break;
			case R.id.helmet_camera_rear_btn:
				rear_btn.setPressed(true);
				break;

			default:
				break;
			}
		}
	};
}
