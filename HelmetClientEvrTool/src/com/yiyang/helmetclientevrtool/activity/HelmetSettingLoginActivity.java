package com.yiyang.helmetclientevrtool.activity;

import java.util.ArrayList;

import com.yiyang.helmetclientevrtool.R;
import com.yiyang.helmetclientevrtool.utils.HelmetToolsUtils;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class HelmetSettingLoginActivity extends Activity {

	private TextView sign_in_btn, sign_up_btn, signin_pwd_forgot, signup_pwd_forgot;
	private EditText signin_username_edit, signin_userpwd_edit, signup_username_edit, signup_userpwd_edit;
	private ImageButton signin_btn, signup_btn;
	private ImageView facebook_btn, google_btn;
	private ProgressBar login_progressbar;
	private static final int SELECTED_TEXT_SIZE = 28;
	private static final int UNSELECTED_TEXT_SIZE = 16;
	private static final int ZH_SIGN_IN_PRESS = 18;
	private static final int ZH_SIGN_UP_PRESS = 81;
	private static final int EN_SIGN_IN_PRESS = 31;
	private static final int EN_SIGN_UP_PRESS = 67;
	private View signin_layout, signup_layout;
	private ViewPager login_pager;
	private ArrayList<View> fragmnet_list = new ArrayList<View>();

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		HelmetToolsUtils.setStatusBarFullTransparent(getWindow());
		setContentView(R.layout.helmet_setting_login_main);
		getActionBar().hide();
		initViews();
	}

	private void initViews() {
		signin_layout = getLayoutInflater().inflate(R.layout.helmet_setting_signin_pager_layout, null);
		signup_layout = getLayoutInflater().inflate(R.layout.helmet_setting_signup_pager_layout, null);
		fragmnet_list.add(signin_layout);
		fragmnet_list.add(signup_layout);

		login_pager = (ViewPager) findViewById(R.id.helmet_setting_login_viewpager);
		login_pager.setAdapter(pagerAdapter);
		login_pager.addOnPageChangeListener(mPageChangeListener);

		sign_in_btn = (TextView) findViewById(R.id.helmet_settings_login_signin_text);
		sign_in_btn.setOnClickListener(listener);
		sign_up_btn = (TextView) findViewById(R.id.helmet_settings_login_signup_text);
		sign_up_btn.setOnClickListener(listener);

		login_progressbar = (ProgressBar) findViewById(R.id.helmet_settings_login_progress);

		signin_btn = (ImageButton) signin_layout.findViewById(R.id.helmet_setting_signin_btn);
		signin_btn.setOnClickListener(listener);
		signup_btn = (ImageButton) signup_layout.findViewById(R.id.helmet_setting_signup_btn);
		signup_btn.setOnClickListener(listener);

		signin_username_edit = (EditText) signin_layout.findViewById(R.id.helmet_setting_signin_name_text);
		signin_userpwd_edit = (EditText) signin_layout.findViewById(R.id.helmet_setting_signin_pwd_text);

		signup_username_edit = (EditText) signup_layout.findViewById(R.id.helmet_setting_signup_name_text);
		signup_userpwd_edit = (EditText) signup_layout.findViewById(R.id.helmet_setting_signup_pwd_text);
		//
		signin_pwd_forgot = (TextView) findViewById(R.id.helmet_setting_signin_pwd_forgot);
		signup_pwd_forgot = (TextView) findViewById(R.id.helmet_setting_signup_pwd_forgot);
		// pwd_forgot.setOnClickListener(listener);

		// enter_btn = (ImageButton)
		// findViewById(R.id.helmet_settings_login_sign_btn);
		// enter_btn.setOnClickListener(listener);

		facebook_btn = (ImageView) findViewById(R.id.helmet_settings_login_facebook_btn);
		facebook_btn.setOnClickListener(listener);
		google_btn = (ImageView) findViewById(R.id.helmet_settings_login_google_btn);
		google_btn.setOnClickListener(listener);

		updateProgressBar(true);
	}

	private View.OnClickListener listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.helmet_settings_login_signin_text:
				updateProgressBar(true);
				break;
			case R.id.helmet_settings_login_signup_text:
				updateProgressBar(false);
				break;
			case R.id.helmet_setting_signin_btn:
				signinUserInfo();
				break;
			case R.id.helmet_setting_signup_btn:
				signupUserInfo();
				break;
			case R.id.helmet_settings_login_facebook_btn:
				break;
			case R.id.helmet_settings_login_google_btn:
				break;
			default:
				break;
			}
		}
	};

	private OnPageChangeListener mPageChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			updateProgressBar(arg0 == 0 ? true : false);
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

	private void updateProgressBar(boolean isSignIn) {
		if (HelmetToolsUtils.getCurrentLanguage().equals(HelmetToolsUtils.ZH_LOCALE)) {
			login_progressbar.setProgress(isSignIn ? ZH_SIGN_IN_PRESS : ZH_SIGN_UP_PRESS);
		} else if (HelmetToolsUtils.getCurrentLanguage().equals(HelmetToolsUtils.EN_LOCALE)) {
			login_progressbar.setProgress(isSignIn ? EN_SIGN_IN_PRESS : EN_SIGN_UP_PRESS);
		} else {
			login_progressbar.setProgress(isSignIn ? EN_SIGN_IN_PRESS : EN_SIGN_UP_PRESS);
		}

		if (isSignIn) {
			login_progressbar.setProgressDrawable(ContextCompat.getDrawable(HelmetSettingLoginActivity.this,
					R.drawable.helmet_progressbar_landscape_background_in));
			sign_in_btn.setTextSize(SELECTED_TEXT_SIZE);
			sign_up_btn.setTextSize(UNSELECTED_TEXT_SIZE);
			login_pager.setCurrentItem(0);
		} else {
			login_progressbar.setProgressDrawable(ContextCompat.getDrawable(HelmetSettingLoginActivity.this,
					R.drawable.helmet_progressbar_landscape_background_up));
			sign_in_btn.setTextSize(UNSELECTED_TEXT_SIZE);
			sign_up_btn.setTextSize(SELECTED_TEXT_SIZE);
			login_pager.setCurrentItem(1);
		}
	}

	private void signinUserInfo() {
		String signin_name = signin_username_edit.getText().toString();
		String signin_pwd = signin_userpwd_edit.getText().toString();

		if (TextUtils.isEmpty(signin_name)) {
			HelmetToolsUtils.showToast(this, R.string.helmet_setting_login_name_null);
			return;
		} else if (TextUtils.isEmpty(signin_pwd)) {
			HelmetToolsUtils.showToast(this, R.string.helmet_setting_login_pwd_null);
			return;
		}
	}

	private void signupUserInfo() {
		String signup_name = signup_username_edit.getText().toString();
		String signup_pwd = signup_userpwd_edit.getText().toString();

		if (TextUtils.isEmpty(signup_name)) {
			HelmetToolsUtils.showToast(this, R.string.helmet_setting_login_name_null);
			return;
		} else if (TextUtils.isEmpty(signup_pwd)) {
			HelmetToolsUtils.showToast(this, R.string.helmet_setting_login_pwd_null);
			return;
		}
	}
}
