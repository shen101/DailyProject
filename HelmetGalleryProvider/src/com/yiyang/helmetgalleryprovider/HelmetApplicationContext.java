package com.yiyang.helmetgalleryprovider;

import java.io.File;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.yiyang.helmetgalleryprovider.helper.HelmetLocalImageHelper;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

@SuppressLint("NewApi")
public class HelmetApplicationContext extends Application {
	// singleton
	private static HelmetApplicationContext helmetApplicontext = null;
	private DisplayMetrics dm;

	@Override
	public void onCreate() {
		super.onCreate();
		helmetApplicontext = this;
		init();
	}

	public static HelmetApplicationContext getInstance() {
		return helmetApplicontext;
	}

	private void init() {
		initImageLoader(getApplicationContext());
		HelmetLocalImageHelper.init(this);
		if (dm == null) {
			WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
			dm = new DisplayMetrics();
			wm.getDefaultDisplay().getMetrics(dm);
		}
	}

	public static void initImageLoader(Context context) {
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
		config.threadPriority(Thread.NORM_PRIORITY);
		config.denyCacheImageMultipleSizesInMemory();
		config.memoryCacheSize((int) Runtime.getRuntime().maxMemory() / 4);
		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
		config.diskCacheSize(100 * 1024 * 1024); // 100 MiB
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		config.imageDownloader(new BaseImageDownloader(helmetApplicontext, 5 * 1000, 5 * 1000));
		ImageLoader.getInstance().init(config.build());
	}

	public String getCachePath() {
		File cacheDir;
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
			cacheDir = getExternalCacheDir();
		else
			cacheDir = getCacheDir();
		if (!cacheDir.exists())
			cacheDir.mkdirs();
		return cacheDir.getAbsolutePath();
	}

	public int getWindowWidth() {
		return dm.widthPixels;
	}

	public int getWindowHeight() {
		return dm.heightPixels;
	}

	public int getHalfWidth() {
		return dm.widthPixels / 2;
	}

	public int getQuarterWidth() {
		return dm.heightPixels / 4;
	}
}
