package com.yiyang.helmetclientevrtool.activity;

import java.util.Arrays;
import java.util.List;

import com.yiyang.helmetclientevrtool.R;
import com.yiyang.helmetclientevrtool.utils.HelmetToolsUtils;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class HelmetSettingHotspotActivity extends HelmetBaseActivity {

	private static final String TAG = "HelmetSettingHotspotActivity";
	private EditText AccountsEd, PasswadEd;
	private TextView hotspot_btn;
	private Spinner SecuritySpiiner;
	private String cardNumber;
	private LinearLayout Password_layout;
	private String a = "{";
	private String b = "\"ap_name\":\"";
	private String c = "\",\"ap_pwd\":\"";
	private String d = "\",\"ap_type\":\"";
	private String e = "\"}";
	private String f = "\",\"hotSpot\":\"";
	private String g = "hotSpot";
	private ActionBar mActionBar;
	private TextView actionbar_title;
	private ImageView actionbar_back_btn, actionbar_save_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.helmet_setting_hotspot_main);

		hotspot_btn = (TextView) findViewById(R.id.helmet_hotspot_btn);
		hotspot_btn.setOnClickListener(clickListener);

		AccountsEd = (EditText) findViewById(R.id.accounts);
		PasswadEd = (EditText) findViewById(R.id.password);
		Password_layout = (LinearLayout) findViewById(R.id.Password_layout);

		SecuritySpiiner = (Spinner) findViewById(R.id.security_spinner);
		ArrayAdapter<String> cameraSpinnerAadapter = new ArrayAdapter<String>(this,
				R.layout.helmet_spinner_style_layout, getSecurityDataSource());
		cameraSpinnerAadapter.setDropDownViewResource(R.layout.helmet_custom_spinner_dropdown_item);
		SecuritySpiiner.setAdapter(cameraSpinnerAadapter);
		SecuritySpiiner.setOnItemSelectedListener(itemListener);

		setCustomActionBarStyle();
	}

	@Override
	protected void onResume() {
		super.onResume();
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
		actionbar_back_btn.setOnClickListener(clickListener);
		actionbar_save_btn = (ImageView) mActionBar.getCustomView().findViewById(R.id.helmet_actionbar_save_icon);
		actionbar_save_btn.setOnClickListener(clickListener);
		actionbar_title = (TextView) mActionBar.getCustomView().findViewById(R.id.helmet_actionbar_title);
		actionbar_title.setText(getTitle());
	}

	private AdapterView.OnItemSelectedListener itemListener = new AdapterView.OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			cardNumber = getSecurityDataSource().get(position);
			if (cardNumber.equals(getResources().getString(R.string.helmet_setting_hotspot_safety_type_none))) {
				Password_layout.setVisibility(View.GONE);
			} else {
				Password_layout.setVisibility(View.VISIBLE);
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}
	};

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.helmet_hotspot_btn:
				break;
			case R.id.helmet_actionbar_back_icon:
				finish();
				break;
			case R.id.helmet_actionbar_save_icon:
				break;
			default:
				break;
			}
		}
	};

	private void initApConnectInfo() {
		String ap_name = AccountsEd.getText().toString();
		String ap_pwd = PasswadEd.getText().toString();
		int ap_type = -1;
		StringBuffer buffer = new StringBuffer();

		if (SecuritySpiiner.getSelectedItemPosition() == 0) { // None
			ap_type = 1;
		} else if (SecuritySpiiner.getSelectedItemPosition() == 1) { // WEP
			ap_type = 2;
		} else if (SecuritySpiiner.getSelectedItemPosition() == 2) { // WPA2-PSK
			ap_type = 3;
		}

		// buffer.append(ap_type);
		if (SecuritySpiiner.getSelectedItemPosition() == 0) {
			buffer.append(a + b + ap_name + d + ap_type + f + g + e);
		} else {
			if (TextUtils.isEmpty(ap_pwd)) {
				HelmetToolsUtils.showToast(this, R.string.helmet_setting_hotspot_ap_pwd_null);
			} else {
				buffer.append(a + b + ap_name + c + ap_pwd + d + ap_type + f + g + e);
			}
		}
	}

	public List<String> getSecurityDataSource() {
		String[] safety_types = getResources().getStringArray(R.array.helmet_hotspot_safety_type_array);
		return Arrays.asList(safety_types);
	}

}
