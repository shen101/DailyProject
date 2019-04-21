package com.shen.adapter;

import java.util.ArrayList;

import com.shen.activityfragmentdemo.R;
import com.shen.utils.GlassLanguageInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GlassLanguageListAdapter extends BaseAdapter {

	private Context mcontext;
	private LayoutInflater mInflater;
	private ArrayList<GlassLanguageInfo> infos = new ArrayList<GlassLanguageInfo>();

	public GlassLanguageListAdapter(Context mContext, ArrayList<GlassLanguageInfo> infos) {
		super();
		this.mcontext = mContext;
		this.mInflater = mInflater.from(mContext);
		this.infos = infos;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return infos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return infos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LanguageHolder holder = null;
		if (convertView == null) {
			holder = new LanguageHolder();
			convertView = mInflater.inflate(R.layout.language_list_items, null);
			holder.name = (TextView) convertView.findViewById(R.id.language_list_items_name);
			holder.country = (TextView) convertView.findViewById(R.id.language_list_items_country);
			convertView.setTag(holder);
		} else {
			holder = (LanguageHolder) convertView.getTag();
		}

		convertView.setMinimumHeight(120);

		holder.name.setText(infos.get(position).getLanguage_name());
		holder.country.setText(infos.get(position).getLanguage_country());

		return convertView;
	}

	class LanguageHolder {
		TextView name, country;
	}
}
