package com.shen.fragment;

import com.shen.activityfragmentdemo.BaseFragment;
import com.shen.activityfragmentdemo.R;
import com.shen.service.GlassWlanAcceptMessageService;
import com.shen.utils.GlassUtils;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

public class SettingsNotificationInfoFragment extends BaseFragment {

	private View noti_view = null;
	private ImageView noti_left_btn;
	private Button notifi_settings_btn;
	private Switch tencent_mm_switch, tencent_qq_switch, android_mms_switch, incallui_switch;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		noti_view = inflater.inflate(R.layout.settings_notifi_info_fragment, container, false);
		initViews();
		return noti_view;
	}

	private void initViews() {
		notifi_settings_btn = (Button) noti_view.findViewById(R.id.glass_notifi_settings);
		notifi_settings_btn.setOnClickListener(MyOnLinster);
		noti_left_btn = (ImageView) noti_view.findViewById(R.id.left_direction_icons);
		noti_left_btn.setOnClickListener(MyOnLinster);
		tencent_mm_switch = (Switch) noti_view.findViewById(R.id.glass_notifi_tencent_mm_btn);
		tencent_qq_switch = (Switch) noti_view.findViewById(R.id.glass_notifi_tencent_qq_btn);
		android_mms_switch = (Switch) noti_view.findViewById(R.id.glass_notifi_android_mms_btn);
		incallui_switch = (Switch) noti_view.findViewById(R.id.glass_notifi_incallui_btn);
		tencent_mm_switch.setOnCheckedChangeListener(checkoutListener);
		tencent_qq_switch.setOnCheckedChangeListener(checkoutListener);
		android_mms_switch.setOnCheckedChangeListener(checkoutListener);
		incallui_switch.setOnCheckedChangeListener(checkoutListener);
		updateSwitchStatus();
	}

	private OnCheckedChangeListener checkoutListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub
			switch (buttonView.getId()) {
			case R.id.glass_notifi_tencent_mm_btn:
				if (isChecked) {
					GlassUtils.setNotifiAppValues(getActivity(), GlassUtils.NOTIFICATION_TABLE_TENCENT_MM, 1);
				} else {
					GlassUtils.setNotifiAppValues(getActivity(), GlassUtils.NOTIFICATION_TABLE_TENCENT_MM, 0);
				}
				break;
			case R.id.glass_notifi_tencent_qq_btn:
				if (isChecked) {
					GlassUtils.setNotifiAppValues(getActivity(), GlassUtils.NOTIFICATION_TABLE_TENCENT_QQ, 1);
				} else {
					GlassUtils.setNotifiAppValues(getActivity(), GlassUtils.NOTIFICATION_TABLE_TENCENT_QQ, 0);
				}

				break;
			case R.id.glass_notifi_android_mms_btn:
				if (isChecked) {
					GlassUtils.setNotifiAppValues(getActivity(), GlassUtils.NOTIFICATION_TABLE_ANDROID_SMS, 1);
				} else {
					GlassUtils.setNotifiAppValues(getActivity(), GlassUtils.NOTIFICATION_TABLE_ANDROID_SMS, 0);
				}

				break;
			case R.id.glass_notifi_incallui_btn:
				if (isChecked) {
					GlassUtils.setNotifiAppValues(getActivity(), GlassUtils.NOTIFICATION_TABLE_INCALLUI, 1);
				} else {
					GlassUtils.setNotifiAppValues(getActivity(), GlassUtils.NOTIFICATION_TABLE_INCALLUI, 0);
				}

				break;

			default:
				break;
			}
		}
	};

	private void updateSwitchStatus() {
		tencent_mm_switch
				.setChecked(GlassUtils.getBotifiAppValues(getActivity(), GlassUtils.NOTIFICATION_TABLE_TENCENT_MM));
		tencent_qq_switch
				.setChecked(GlassUtils.getBotifiAppValues(getActivity(), GlassUtils.NOTIFICATION_TABLE_TENCENT_QQ));
		android_mms_switch
				.setChecked(GlassUtils.getBotifiAppValues(getActivity(), GlassUtils.NOTIFICATION_TABLE_ANDROID_SMS));
		incallui_switch
				.setChecked(GlassUtils.getBotifiAppValues(getActivity(), GlassUtils.NOTIFICATION_TABLE_INCALLUI));

		if (tencent_mm_switch.isChecked() || tencent_qq_switch.isChecked() || android_mms_switch.isChecked()
				|| incallui_switch.isChecked()) {
			Intent intent = new Intent();
			intent.setClass(getActivity(), GlassWlanAcceptMessageService.class);
			getActivity().startService(intent);
		}
	}

	private OnClickListener MyOnLinster = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.left_direction_icons:
				onFragmentBackClick();
				break;
			case R.id.glass_notifi_settings:
				break;
			default:
				break;
			}
		}
	};
}
