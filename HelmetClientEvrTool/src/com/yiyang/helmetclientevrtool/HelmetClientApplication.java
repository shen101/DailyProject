package com.yiyang.helmetclientevrtool;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class HelmetClientApplication extends Application {

	private static Context context;
	private WindowManager wManager;
	private DisplayMetrics outMetrics;
	private HelmetClientApplication mApplication;

	public HelmetClientApplication getInstance() {
		if (mApplication == null) {
			mApplication = new HelmetClientApplication();
		}
		return mApplication;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();

		wManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		outMetrics = new DisplayMetrics();
		wManager.getDefaultDisplay().getMetrics(new DisplayMetrics());
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	public static Context getContext() {
		return context;
	}

	public int getScreenWidth() {
		return outMetrics.widthPixels;
	}

	public int getScreenHight() {
		return outMetrics.heightPixels;
	}
}
