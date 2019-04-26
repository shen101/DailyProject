package com.shen.activityfragmentdemo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.shen.activityfragmentdemo.R;
import com.shen.utils.GlassCameraSizeInfo;
import com.shen.utils.GlassCameraUtil;
import com.shen.utils.GlassUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class PearViewInfoActivity extends BaseActivity {

	private Button take_photo_btn, flash_btn, switch_btn;
	private Context mContext;
	private Camera mCamera;
	private SensorManager mSensorManager;
	private Sensor AccelerationSensor;
	private GlassCameraSizeInfo camera_preview_info, camera_picture_info;

	private int cameraPosition = 0;// 0代表前置摄像头,1代表后置摄像头,默认打开前置摄像头
	private static final int UPDATE_INTERVAL = 100;
	private long mLastUpdateTime;
	private float mLastX, mLastY, mLastZ;
	private int shakeThreshold = 100;
	private Button take_photo, flash_photo, switch_photo;
	private SurfaceHolder holder;
	private SurfaceView mSurfaceView;
	private int photo_type = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pear_view_info_layout);
		mContext = PearViewInfoActivity.this;
		getActionBar().hide();

		if (getIntent().getIntExtra(GlassUtils.ACTION_TAKE_PHOTO_TYPE_TAG, photo_type) != -1) {
			photo_type = getIntent().getIntExtra(GlassUtils.ACTION_TAKE_PHOTO_TYPE_TAG, photo_type);
		} else if (getIntent().getIntExtra(GlassUtils.ACTION_VIDEO_PHOTO_TYPE_TAG, photo_type) != -1) {
			photo_type = getIntent().getIntExtra(GlassUtils.ACTION_VIDEO_PHOTO_TYPE_TAG, photo_type);
		} else if (getIntent().getIntExtra(GlassUtils.ACTION_PREAVIEW_PHOTO_TYPE_TAG, photo_type) != -1) {
			photo_type = getIntent().getIntExtra(GlassUtils.ACTION_PREAVIEW_PHOTO_TYPE_TAG, photo_type);
		}
		
		Log.i("shen", "photo_type = "+photo_type);

		initViews();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getActionBar().hide();
	}

	private void initViews() {
		mSurfaceView = (SurfaceView) findViewById(R.id.glass_surface_Camera);
		take_photo_btn = (Button) findViewById(R.id.glass_takePhotoBtn);
		flash_btn = (Button) findViewById(R.id.glass_takeflashBtn);
		switch_btn = (Button) findViewById(R.id.glass_switchcameraBtn);

		take_photo_btn.setOnClickListener(MyOnLinster);
		flash_btn.setOnClickListener(MyOnLinster);
		switch_btn.setOnClickListener(MyOnLinster);

		if (photo_type == GlassUtils.ACTION_PREAVIEW_PHOTO_TYPE_NUM) {
			take_photo_btn.setVisibility(View.GONE);
			flash_btn.setVisibility(View.GONE);
			switch_btn.setVisibility(View.GONE);
		} else if (photo_type == GlassUtils.ACTION_VIDEO_PHOTO_TYPE_NUM) {
			take_photo_btn.setVisibility(View.GONE);
			flash_btn.setVisibility(View.GONE);
			switch_btn.setVisibility(View.GONE);
		}

		holder = mSurfaceView.getHolder();
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		holder.addCallback(mCallback); // 回调接口

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		AccelerationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorManager.registerListener(mSensorListener, AccelerationSensor, SensorManager.SENSOR_DELAY_NORMAL);

		initCameraInfo();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.fragment_slide_right_out, R.anim.fragment_slide_right_out);
	}

	private SurfaceHolder.Callback mCallback = new Callback() {

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			releaseCamera();
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			startPreview(mCamera, holder);
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			// TODO Auto-generated method stub
			mCamera.stopPreview();
			startPreview(mCamera, holder);
			autoFocus();
		}
	};

	private OnClickListener MyOnLinster = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			switch (v.getId()) {
			case R.id.glass_takePhotoBtn:
				mCamera.takePicture(null, null, mPictureCallback);
				break;
			case R.id.glass_takeflashBtn:
				GlassCameraUtil.setFlashStatus(mCamera, flash_photo);
				break;
			case R.id.glass_switchcameraBtn:
				releaseCamera();
				cameraPosition = (cameraPosition + 1) % mCamera.getNumberOfCameras();
				mCamera = getCamera(cameraPosition);
				if (holder != null) {
					startPreview(mCamera, holder);
				}
				break;
			default:
				break;
			}

		}
	};

	private void initCameraInfo() {
		// TODO Auto-generated method stub
		if (mCamera == null) {
			mCamera = getCamera(cameraPosition);
			if (holder != null) {
				startPreview(mCamera, holder);
			}
		}
	}

	private PictureCallback mPictureCallback = new PictureCallback() {

		@Override
		public void onPictureTaken(final byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						// 将照片改为竖直方向
						Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
						saveImageToGallery(mContext,
								GlassCameraUtil.setTakePicktrueOrientation(cameraPosition, bitmap));
						mCamera.stopPreview();
						mCamera.startPreview();

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	};

	public void saveImageToGallery(Context context, Bitmap bmp) {
		// 首先保存图片
		File appDir = new File(Environment.getExternalStorageDirectory(), "test");
		if (!appDir.exists()) {
			appDir.mkdir();
		}
		String fileName = System.currentTimeMillis() + ".jpg";
		File file = new File(appDir, fileName);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			Log.i("shen", "file.toString() = " + file.toString());
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(context, getResources().getString(R.string.glass_save_failed), Toast.LENGTH_SHORT).show();
		}

		// Must update local gallery
		context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file)));
	}

	private SensorEventListener mSensorListener = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				long currentTime = System.currentTimeMillis();
				long diffTime = currentTime - mLastUpdateTime;
				if (diffTime < UPDATE_INTERVAL) {
					return;
				}
				mLastUpdateTime = currentTime;
				float x = event.values[0];
				float y = event.values[1];
				float z = event.values[2];
				float deltaX = x - mLastX;
				float deltaY = y - mLastY;
				float deltaZ = z - mLastZ;
				mLastX = x;
				mLastY = y;
				mLastZ = z;

				float delta = (float) (Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) / diffTime
						* 10000);
				if (delta > shakeThreshold) {
					autoFocus();
				}
			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub

		}
	};

	private void startPreview(Camera camera, SurfaceHolder holder) {
		try {
			setupCamera(camera);
			camera.setPreviewDisplay(holder);
			GlassCameraUtil.setCameraDisplayOrientation(PearViewInfoActivity.this, camera);
			camera.startPreview();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void releaseCamera() {
		if (mCamera != null) {
			mCamera.setPreviewCallback(null);
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
	}

	private Camera getCamera(int id) {
		Camera camera = null;
		try {
			camera = Camera.open(id);
		} catch (Exception e) {

		}
		return camera;
	}

	private Pair<CameraInfo, Integer> getBackCamera() {
		CameraInfo cameraInfo = new CameraInfo();
		final int numberOfCameras = Camera.getNumberOfCameras();

		for (int i = 0; i < numberOfCameras; ++i) {
			Camera.getCameraInfo(i, cameraInfo);
			if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
				return new Pair<Camera.CameraInfo, Integer>(cameraInfo, Integer.valueOf(i));
			}
		}
		return null;
	}

	private void autoFocus() {
		new Thread() {
			@Override
			public void run() {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (mCamera == null) {
					return;
				}
				mCamera.autoFocus(new Camera.AutoFocusCallback() {
					@Override
					public void onAutoFocus(boolean success, Camera camera) {
						if (success) {
							setupCamera(camera);
							Log.i("shen", "success");
						} else {
							Log.i("shen", "failed");
							autoFocus();
						}
					}

				});
			}
		}.start();
	}

	private void setupCamera(Camera camera) {
		Camera.Parameters parameters = camera.getParameters();

		camera_preview_info = new GlassCameraSizeInfo();

		List<String> focusModes = parameters.getSupportedFocusModes();
		for (int i = 0; i < focusModes.size(); i++) {
			// Log.i("shen", "focusModes.get(i) = " + focusModes.get(i));
		}

		if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
			// Autofocus mode is supported 自动对焦
			parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
		}

		List<Size> previewSizes = parameters.getSupportedPreviewSizes();
		int preview_length = previewSizes.size();
		for (int i = 0; i < preview_length; i++) {
			if (previewSizes.get(i).width > camera_preview_info.getWight()) {
				camera_preview_info.setWight(previewSizes.get(i).width);
			}

			if (previewSizes.get(i).height > camera_preview_info.getHight()) {
				camera_preview_info.setHight(previewSizes.get(i).height);
			}
		}

		Log.i("shen", "cameraPosition = " + cameraPosition);

		if (cameraPosition == 0) { // REAR
			GlassCameraUtil.setCameraMaxInfoSize(mContext, GlassCameraUtil.CAMERA_REAR_PREVIEW_SIZE,
					camera_preview_info);
		} else { // FRONT
			GlassCameraUtil.setCameraMaxInfoSize(mContext, GlassCameraUtil.CAMERA_FRONT_PREVIEW_SIZE,
					camera_preview_info);
		}

		parameters.setPreviewSize(camera_preview_info.getWight(), camera_preview_info.getHight());
		camera_picture_info = new GlassCameraSizeInfo();

		List<Size> pictureSizes = parameters.getSupportedPictureSizes();
		int picture_length = pictureSizes.size();
		for (int i = 0; i < picture_length; i++) {
			if (pictureSizes.get(i).width > camera_picture_info.getWight()) {
				camera_picture_info.setWight(pictureSizes.get(i).width);
			}
			if (pictureSizes.get(i).height > camera_picture_info.getHight()) {
				camera_picture_info.setHight(pictureSizes.get(i).height);
			}
		}

		if (cameraPosition == 0) {
			GlassCameraUtil.setCameraMaxInfoSize(mContext, GlassCameraUtil.CAMERA_REAR_PRITURE_SIZE,
					camera_picture_info);
		} else {
			GlassCameraUtil.setCameraMaxInfoSize(mContext, GlassCameraUtil.CAMERA_FRONT_PRITURE_SIZE,
					camera_picture_info);
		}
		parameters.setPictureSize(camera_picture_info.getWight(), camera_picture_info.getHight());
		camera.setParameters(parameters);

	}

	@Override
	public void onPause() {
		super.onPause();
		if (null != mSensorManager && null != AccelerationSensor) {
			mSensorManager.unregisterListener(mSensorListener, AccelerationSensor);
		}
		releaseCamera();
	}
}
