package com.yiyang.helmetclientevrtool.activity;

import java.util.ArrayList;

import com.yiyang.helmetclientevrtool.R;
import com.yiyang.helmetclientevrtool.adapter.HelmetSettingListAdapter;
import com.yiyang.helmetclientevrtool.bean.HelmetSettingListInfo;
import com.yiyang.helmetclientevrtool.widget.HelmetCircleImageView;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class HelmetSettingMainActivity extends HelmetBaseActivity {

	private ActionBar mActionBar;
	private TextView actionbar_title, user_name;
	private HelmetCircleImageView user_image;
	private ListView setting_list;
	private ImageView actionbar_back_btn, actionbar_save_btn;
	private HelmetSettingListAdapter mAdapter;
	private ArrayList<HelmetSettingListInfo> infos = new ArrayList<HelmetSettingListInfo>();
	private int[] list_icons = { R.drawable.helmet_setting_bluetooth, R.drawable.helmet_setting_wifi,
			R.drawable.helmet_setting_camera, R.drawable.helmet_setting_login, R.drawable.helmet_setting_notice,
			R.drawable.helmet_setting_contact };
	private int[] list_names = { R.string.helmet_setting_item_connect_helmet, R.string.helmet_setting_item_sync_wifi,
			R.string.helmet_setting_item_sync_camera, R.string.helmet_setting_item_sync_login,
			R.string.helmet_setting_item_sync_notice, R.string.helmet_setting_item_sync_contact };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.helmet_setting_main);
		initViews();
		setCustomActionBarStyle();
	}

	private void initViews() {
		// TODO Auto-generated method stub
		user_image = (HelmetCircleImageView) findViewById(R.id.helmet_setting_usericon);
		user_name = (TextView) findViewById(R.id.helmet_setting_username);
		user_name.setText("Silence");
		user_image.setImageResource(R.drawable.qie);
		setting_list = (ListView) findViewById(R.id.helmet_setting_main_list);
		for (int i = 0; i < list_icons.length; i++) {
			HelmetSettingListInfo info = new HelmetSettingListInfo();
			info.setName(list_names[i]);
			info.setIcon(list_icons[i]);
			infos.add(info);
		}
		mAdapter = new HelmetSettingListAdapter(this, infos);
		setting_list.setAdapter(mAdapter);
		setting_list.setOnItemClickListener(itemclicklistener);
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
		actionbar_save_btn.setVisibility(View.GONE);
		actionbar_title = (TextView) mActionBar.getCustomView().findViewById(R.id.helmet_actionbar_title);
		actionbar_title.setText(getTitle());
	}

	private OnItemClickListener itemclicklistener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			// TODO Auto-generated method stub
			Intent intent_class = new Intent();
			if (arg2 == 0) {
				intent_class.setClass(HelmetSettingMainActivity.this, HelmetSettingBleConnectActivity.class);
			} else if (arg2 == 1) {
				intent_class.setClass(HelmetSettingMainActivity.this, HelmetSettingHotspotActivity.class);
			} else if (arg2 == 2) {
				intent_class.setClass(HelmetSettingMainActivity.this, HelmetSettingCameraActivity.class);
			} else if (arg2 == 3) {
				intent_class.setClass(HelmetSettingMainActivity.this, HelmetSettingLoginActivity.class);
			}
			startActivity(intent_class);
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
			default:
				break;
			}
		}
	};
}
