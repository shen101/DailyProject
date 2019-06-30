package com.yiyang.helmetgalleryprovider;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.yiyang.helmetgalleryprovider.helper.HelmetLocalImageHelper;
import com.yiyang.helmetgalleryprovider.utils.HelmetGelleryToolUtils;
import com.yiyang.helmetgalleryprovider.widget.HelmetLoadViewPager;

@SuppressLint({ "HandlerLeak", "InflateParams" })
public class MainActivity extends HelmetBaseActivity {

	private HelmetLoadViewPager viewpager;
	private TextView counts_text;
	private List<HelmetLocalImageHelper.LocalFile> currentFile = null;
	private static final int REFLASH_ADAPTER = 200;
	private LocalViewPagerAdapter pagerAdapter;
	private RelativeLayout empty_layout;
	private ImageButton to_camera, to_video;
	private ImageView video_play_icon;
	private int file_position = -1;
	private Timer timer;
	private String[] image_options_items, vedio_options_items;

	private HelmetLocalImageHelper helper = null;

	private Handler mhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case REFLASH_ADAPTER:
				initPagerLaoyutAndEmptyStatus();
				viewpager.setCurrentItem(file_position);
				pagerAdapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.helmet_main_layout);
		if (!HelmetLocalImageHelper.getInstance().isInitFiled()) {
			finish();
			return;
		}

		initViews();

		localOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(false)
				.bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.EXACTLY)
				.displayer(new SimpleBitmapDisplayer()).build();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initPagerLaoyutAndEmptyStatus();
	}

	@SuppressWarnings("deprecation")
	private void initViews() {
		image_options_items = getResources().getStringArray(R.array.image_operating_options);
		vedio_options_items = getResources().getStringArray(R.array.video_operating_options);

		counts_text = (TextView) findViewById(R.id.size_counts);
		viewpager = (HelmetLoadViewPager) findViewById(R.id.albumviewpager);
		empty_layout = (RelativeLayout) findViewById(R.id.empty_layout);
		to_camera = (ImageButton) findViewById(R.id.to_camera);
		to_camera.setOnClickListener(listener);
		to_video = (ImageButton) findViewById(R.id.to_video);
		to_video.setOnClickListener(listener);

		video_play_icon = (ImageView) findViewById(R.id.video_play_icon);

		viewpager.setOnPageChangeListener(pageChangeListener);
	}

	private void initPagerLaoyutAndEmptyStatus() {
		helper = HelmetLocalImageHelper.getInstance();
		HelmetLocalImageHelper.getInstance().initImage();
		currentFile = helper.getFolderMap();
		pagerAdapter = new LocalViewPagerAdapter(this, currentFile);
		viewpager.setAdapter(pagerAdapter);
		counts_text.setText(1 + "/" + viewpager.getAdapter().getCount());

		if (currentFile.size() == 0) { // Empty
			viewpager.setVisibility(View.GONE);
			counts_text.setVisibility(View.GONE);
			empty_layout.setVisibility(View.VISIBLE);
		} else {
			viewpager.setVisibility(View.VISIBLE);
			counts_text.setVisibility(View.VISIBLE);
			empty_layout.setVisibility(View.GONE);
		}
	}

	private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {

		@Override
		public void onPageSelected(int position) {
			file_position = position;
			if (viewpager.getAdapter() != null) {
				String text = (position + 1) + "/" + viewpager.getAdapter().getCount();
				counts_text.setText(text);
			} else {
				counts_text.setText("0/0");
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			if (arg0 == ViewPager.SCROLL_STATE_DRAGGING) { // 1 Ongoing
				video_play_icon.setVisibility(View.GONE);
			} else if (arg0 == ViewPager.SCROLL_STATE_IDLE) { // 0 end
				video_play_icon.setVisibility(currentFile.get(file_position).isImage() ? View.GONE : View.VISIBLE);
				Log.i("shen", "file_position = " + file_position);
			}
		}
	};

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.to_camera:
				Intent to_camera_intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
				startActivity(to_camera_intent);
				break;
			case R.id.to_video:
				Intent to_video_intent = new Intent(MediaStore.INTENT_ACTION_VIDEO_CAMERA);
				startActivity(to_video_intent);
				break;
			default:
				break;
			}
		}
	};

	public class LocalViewPagerAdapter extends PagerAdapter {
		private List<HelmetLocalImageHelper.LocalFile> paths;
		private LayoutInflater mInflater;
		private Context mContext;

		public LocalViewPagerAdapter(Context mcontext, List<HelmetLocalImageHelper.LocalFile> paths) {
			this.paths = paths;
			this.mContext = mcontext;
			this.mInflater = LayoutInflater.from(mcontext);
		}

		@Override
		public int getCount() {
			return paths.size();
		}

		@Override
		public Object instantiateItem(ViewGroup viewGroup, int position) {
			View imageLayout = mInflater.inflate(R.layout.loading_items, null);
			viewGroup.addView(imageLayout);
			assert imageLayout != null;
			ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
			HelmetLocalImageHelper.LocalFile path = paths.get(position);
			ImageLoader.getInstance().displayImage(path.getOriginalUri(), new ImageViewAware(imageView), localOptions,
					loadingListenerr, new ProcressListener(imageLayout), path.getOrientation());
			imageView.setOnClickListener(
					new ImageListener(currentFile.get(position).getFile_path(), currentFile.get(position).isImage()));
			return imageLayout;
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public void destroyItem(ViewGroup container, int arg1, Object object) {
			((ViewPager) container).removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}

	private ImageLoadingListener loadingListenerr = new ImageLoadingListener() {

		@Override
		public void onLoadingCancelled(String arg0, View arg1) {
		}

		@Override
		public void onLoadingComplete(String arg0, View view, Bitmap arg2) {
			view.setVisibility(View.VISIBLE);
			view.getParent().bringChildToFront(view);
		}

		@Override
		public void onLoadingFailed(String arg0, View view, FailReason arg2) {
			// view.setVisibility(View.VISIBLE);
			// view.getParent().bringChildToFront(view);
		}

		@Override
		public void onLoadingStarted(String arg0, View arg1) {
		}

	};

	private class ProcressListener implements ImageLoadingProgressListener {
		private View mView = null;

		public ProcressListener(View view) {
			this.mView = view;
		}

		@Override
		public void onProgressUpdate(String arg0, View view, int current, int total) {
			TextView loadText = (TextView) mView.findViewById(R.id.current_procress);
			loadText.setText(String.valueOf(100 * current / total) + "%");
			loadText.bringToFront();
		}

	}

	DisplayImageOptions options;

	DisplayImageOptions localOptions;

	class ImageListener implements OnClickListener {

		private String images_path;
		private boolean isimages;

		public ImageListener(String image_path, boolean isimage) {
			super();
			// TODO Auto-generated constructor stub
			images_path = image_path;
			isimages = isimage;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			createImageDialog(images_path, isimages).show();
		}
	}

	private AlertDialog.Builder createImageDialog(final String image_path, final boolean isimage) {
		AlertDialog.Builder imageDialog = new AlertDialog.Builder(this);
		imageDialog.setTitle(R.string.operating_image_dialog_title);
		imageDialog.setSingleChoiceItems(isimage == true ? image_options_items : vedio_options_items, 0,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if (which == 0) {
							HelmetGelleryToolUtils.deletePicture(image_path, MainActivity.this);
						} else if (which == 1) {
							if (isimage) {
								startAuto();
							} else {
								HelmetGelleryToolUtils.playVideo(MainActivity.this, image_path);
							}
						} else if (which == 2) {
							if (isimage) {
								// image share
							} else {
								startAuto();
							}
						} else if (which == 3) {
							// video share
						}

						dialog.dismiss();
					}
				});
		imageDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				mhandler.sendEmptyMessage(REFLASH_ADAPTER);
			}
		});
		return imageDialog;
	}

	public void startAuto() {
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {

					}
				});

			}
		}, 3000, 3000);
	}

	public void stop() {
		if (timer != null) {
			timer.cancel();
		}
	}
}