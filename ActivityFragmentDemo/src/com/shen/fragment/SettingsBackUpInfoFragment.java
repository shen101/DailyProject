package com.shen.fragment;

import com.shen.activityfragmentdemo.BaseFragment;
import com.shen.activityfragmentdemo.R;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Switch;

public class SettingsBackUpInfoFragment extends BaseFragment {

	private View backup_info_view = null;
	private Switch backup_info_left_btn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		backup_info_view = inflater.inflate(R.layout.settings_backup_info_fragment, container, false);
		initViews();
		return backup_info_view;
	}

	private void initViews() {
		backup_info_left_btn = (Switch) backup_info_view.findViewById(R.id.glass_backup_contacts_btn);
		backup_info_left_btn.setOnClickListener(MyOnLinster);
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

				break;
			case R.id.glass_backup_contacts_btn:
				Intent export_contacts_intent = new Intent();
				ComponentName cn = new ComponentName("com.android.contacts", "com.android.contacts.common.vcard.ExportVCardActivity");
//				export_contacts_intent.putExtra("CALLING_ACTIVITY",
//                        callingActivity);
				export_contacts_intent.setComponent(cn);
                getActivity().startActivity(export_contacts_intent);
				break;
			default:
				break;
			}
		}
	};

}
