package com.shen.fragment;

import java.util.ArrayList;
import java.util.Locale;
import com.shen.activityfragmentdemo.BaseFragment;
import com.shen.activityfragmentdemo.R;
import com.shen.adapter.GlassLanguageListAdapter;
import com.shen.utils.GlassLanguageInfo;
import com.shen.utils.GlassUtils;
import com.android.internal.app.LocalePicker;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class SettingsLanguageInfoFragment extends BaseFragment
		implements com.android.internal.app.LocalePicker.LocaleSelectionListener {

	private View language_view = null;
	private ImageView language_info_left_btn;
	private TextView current_language;
	private ListView language_list;
	private ArrayList<GlassLanguageInfo> language_infos = null;
	private GlassLanguageListAdapter mAdapter;
	private LocalePicker mPicker;
	private Locale selected_locale = null;
	private Handler mhandler = new Handler(Looper.getMainLooper());

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		language_view = inflater.inflate(R.layout.settings_language_info_fragment, container, false);
		initViews();
		return language_view;
	}

	private void initViews() {
		current_language = (TextView) language_view.findViewById(R.id.current_language_title);
		language_info_left_btn = (ImageView) language_view.findViewById(R.id.left_direction_icons);
		language_info_left_btn.setOnClickListener(MyOnLinster);
		language_list = (ListView) language_view.findViewById(R.id.language_list);

		Locale[] lg = Locale.getAvailableLocales();

		updateCurrentLanguage();

		language_infos = new ArrayList<GlassLanguageInfo>();

		for (Locale s : lg) {
			GlassLanguageInfo info = new GlassLanguageInfo();
			info.setLanguage_name(s.getDisplayName());
			info.setLanguage_country(s.getDisplayCountry());
			info.setLanguage_codes(s.getLanguage());
			language_infos.add(info);
		}

		mAdapter = new GlassLanguageListAdapter(getActivity(), language_infos);
		language_list.setAdapter(mAdapter);
		language_list.setOnItemClickListener(mItemClickListener);
	}

	private void updateCurrentLanguage() {
		current_language.setText(Locale.getDefault().getDisplayName());
	}

	private OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// TODO Auto-generated method stub
			Log.i("shen", "position = "+position);
			selected_locale = Locale.forLanguageTag(language_infos.get(position).getLanguage_codes());
			mhandler.post(new updateLocaleRunnable());
		}
	};
	
	class updateLocaleRunnable implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			onLocaleSelected(selected_locale);
			updateCurrentLanguage();
		}
		
	}

	private OnClickListener MyOnLinster = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			onFragmentBackClick();
		}
	};

	@Override
	public void onLocaleSelected(final Locale locale) {
		getActivity().onBackPressed();
		LocalePicker.updateLocale(locale);
	}
}
