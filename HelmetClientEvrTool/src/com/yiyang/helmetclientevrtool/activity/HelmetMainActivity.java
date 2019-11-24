package com.yiyang.helmetclientevrtool.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yiyang.helmetclientevrtool.R;
import com.yiyang.helmetclientevrtool.utils.HelmetToolsUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;

public class HelmetMainActivity extends HelmetBaseActivity {

	private GridView main_list;
	private ImageButton ble_connect_status;
	private int[] list_names = { R.string.helmet_main_navigation_title, R.string.helmet_main_rides_title,
			R.string.helmet_main_gallery_title, R.string.helmet_main_setting_title };
	private int[] list_icons = { R.drawable.helmet_main_navigation_icon, R.drawable.helmet_main_rides_icon,
			R.drawable.helmet_main_gallery_icon, R.drawable.helmet_main_setting_icon };
	private List<Map<String, Object>> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		HelmetToolsUtils.setStatusBarFullTransparent(getWindow());
		setContentView(R.layout.helmet_main_layout);
		getActionBar().hide();
		initGridData();
		initViews();
	}

	private void initViews() {
		// TODO Auto-generated method stub
		main_list = (GridView) findViewById(R.id.helmet_main_grilview);
		ble_connect_status = (ImageButton) findViewById(R.id.helmet_main_ble_connect_status_icon);
		ble_connect_status.setOnClickListener(clicklistener);
		SimpleAdapter adapter = new SimpleAdapter(this, initGridData(), R.layout.helmet_main_grilview_item_layout,
				new String[] { "icon", "name" }, new int[] { R.id.gridview_icons, R.id.gridview_title });
		main_list.setAdapter(adapter);
		main_list.setOnItemClickListener(itemListener);
	}

	private List<Map<String, Object>> initGridData() {
		list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < list_names.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("icon", list_icons[i]);
			map.put("name", getResources().getString(list_names[i]));
			list.add(map);
		}
		return list;
	}

	private OnClickListener clicklistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

		}
	};

	private OnItemClickListener itemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			// TODO Auto-generated method stub
			Intent intent_class = new Intent();
			if (arg2 == 3) {
				intent_class.setClass(HelmetMainActivity.this, HelmetSettingMainActivity.class);
			}
			startActivity(intent_class);
		}
	};
}
