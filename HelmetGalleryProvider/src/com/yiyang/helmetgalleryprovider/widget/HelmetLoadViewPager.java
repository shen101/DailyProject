package com.yiyang.helmetgalleryprovider.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

public class HelmetLoadViewPager extends ViewPager {

	public final static String TAG = "AlbumViewPager";

	DisplayImageOptions localOptions;

	public HelmetLoadViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		localOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(false)
				.bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.EXACTLY)
				.displayer(new SimpleBitmapDisplayer()).build();
	}

}