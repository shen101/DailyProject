package com.yiyang.helmetclientevrtool.adapter;

import java.util.ArrayList;

import com.yiyang.helmetclientevrtool.R;
import com.yiyang.helmetclientevrtool.bean.HelmetSettingListInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HelmetSettingListAdapter extends BaseAdapter {

	private Context mContext;
	private int selectItem;
	private LayoutInflater mInflater;
	private ArrayList<HelmetSettingListInfo> infos = new ArrayList<HelmetSettingListInfo>();

	public HelmetSettingListAdapter(Context mcontext, ArrayList<HelmetSettingListInfo> infos) {
		super();
		this.mContext = mcontext;
		this.mInflater = LayoutInflater.from(mcontext);
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

	public int getSelectNumber(int position) {
		return infos.get(position).getName();
	}

	public void setSelectItem(int selectItem) {
		if (this.selectItem != selectItem) {
			this.selectItem = selectItem;
			notifyDataSetChanged();
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		mHolder mholder = null;
		if (convertView == null) {
			mholder = new mHolder();
			convertView = mInflater.inflate(R.layout.helmet_setting_list_item_layout, null);
			mholder.item_name = (TextView) convertView.findViewById(R.id.helmet_setting_list_item_name);
			mholder.item_icon = (ImageView) convertView.findViewById(R.id.helmet_setting_list_item_icon);
			mholder.item_layout = (RelativeLayout) convertView.findViewById(R.id.helmet_setting_list_item_layout);
			convertView.setTag(mholder);
		} else {
			mholder = (mHolder) convertView.getTag();
		}

		// mholder.item_layout.setMinimumHeight(200);

		mholder.item_name.setText(infos.get(position).getName());
		mholder.item_icon.setImageResource(infos.get(position).getIcon());

		return convertView;
	}

	class mHolder {
		private TextView item_name;
		private ImageView item_icon;
		private RelativeLayout item_layout;
	}
}
