package com.shen.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.widget.Button;
import java.util.List;

import com.shen.activityfragmentdemo.R;

public class GlassCameraUtil {

	private static final String TAG = "CameraUtil";
	public static float scale;

	public static int densityDpi;

	public static float fontScale;

	public static int screenWidth;
	public static int screenHeight;

	public static final String CAMERA_FRONT_PREVIEW_SIZE = "camera_front_preview_size";
	public static final String CAMERA_FRONT_PRITURE_SIZE = "camera_front_priture_size";
	public static final String CAMERA_REAR_PREVIEW_SIZE = "camera_rear_preview_size";
	public static final String CAMERA_REAR_PRITURE_SIZE = "camera_rear_priture_size";
	private static final String CAMERA_HIGHT = "camera_hight";
	private static final String CAMERA_WIGHT = "camera_wight";
	private static final int DEFAULT_CAMERA_NUM = 0;

	private static GlassCameraUtil mCamPara = null;

	public static void init(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		scale = dm.density;
		densityDpi = dm.densityDpi;
		fontScale = dm.scaledDensity;
		if (dm.widthPixels < dm.heightPixels) {
			screenWidth = dm.widthPixels;
			screenHeight = dm.heightPixels;
		} else {
			screenWidth = dm.heightPixels;
			screenHeight = dm.widthPixels;
		}
		Log.e(TAG, "屏幕宽度是:" + screenWidth + " 高度是:" + screenHeight + " dp:" + scale + " fontScale:" + fontScale);
	}

	public static GlassCameraUtil getInstance() {
		if (mCamPara == null) {
			mCamPara = new GlassCameraUtil();
			return mCamPara;
		} else {
			return mCamPara;
		}
	}

	public static int getFrontCameraID() {
		int cameraId = -1;
		// Search for the back facing camera
		int numberOfCameras = Camera.getNumberOfCameras();
		for (int i = 0; i < numberOfCameras; i++) {
			Camera.CameraInfo info = new Camera.CameraInfo();
			Camera.getCameraInfo(i, info);
			if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
				Log.d(TAG, "Camera found");
				cameraId = i;
				break;
			}
		}
		return cameraId;
	}

	public static void setCameraDisplayOrientation(Activity activity, Camera camera) {
		Camera.CameraInfo info = new Camera.CameraInfo();
		Camera.getCameraInfo(getFrontCameraID(), info);
		int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
		Log.i("shen", "rotation = " + rotation);
		int degrees = 0;
		switch (rotation) {
		case Surface.ROTATION_0:
			degrees = 0;
			break;
		case Surface.ROTATION_90:
			degrees = 90;
			break;
		case Surface.ROTATION_180:
			degrees = 180;
			break;
		case Surface.ROTATION_270:
			degrees = 270;
			break;
		}

		int result;
		if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360; // compensate the mirror
		} else { // back-facing
			result = (info.orientation - degrees + 360) % 360;
		}
		Log.i("shen", "result = " + result);
		camera.setDisplayOrientation(result);
	}

	public static Bitmap setTakePicktrueOrientation(int cameraid, Bitmap bitmap) {
		Camera.CameraInfo info = new Camera.CameraInfo();
		Camera.getCameraInfo(cameraid, info);
		bitmap = rotaingImageView(cameraid, info.orientation, bitmap);
		return bitmap;
	}

	public static Bitmap rotaingImageView(int id, int angle, Bitmap bitmap) {
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		if (id == 1) {
			matrix.postScale(-1, 1);
		}
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	}

	public void turnLightOn(Camera mCamera) {
		if (mCamera == null) {
			return;
		}
		Camera.Parameters parameters = mCamera.getParameters();
		if (parameters == null) {
			return;
		}
		List<String> flashModes = parameters.getSupportedFlashModes();
		// Check if camera flash exists
		if (flashModes == null) {
			// Use the screen as a flashlight (next best thing)
			return;
		}
		String flashMode = parameters.getFlashMode();
		if (!Camera.Parameters.FLASH_MODE_ON.equals(flashMode)) {
			// Turn on the flash
			if (flashModes.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
				parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
				mCamera.setParameters(parameters);
			} else {
			}
		}
	}

	public void turnLightAuto(Camera mCamera) {
		if (mCamera == null) {
			return;
		}
		Camera.Parameters parameters = mCamera.getParameters();
		if (parameters == null) {
			return;
		}
		List<String> flashModes = parameters.getSupportedFlashModes();
		// Check if camera flash exists
		if (flashModes == null) {
			// Use the screen as a flashlight (next best thing)
			return;
		}
		String flashMode = parameters.getFlashMode();
		if (!Camera.Parameters.FLASH_MODE_AUTO.equals(flashMode)) {
			// Turn on the flash
			if (flashModes.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
				parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
				mCamera.setParameters(parameters);
			} else {
			}
		}
	}

	private static final int MIN_PREVIEW_PIXELS = 480 * 320;

	private static final double MAX_ASPECT_DISTORTION = 0.15;

	public void turnLightOff(Camera mCamera) {
		if (mCamera == null) {
			return;
		}
		Camera.Parameters parameters = mCamera.getParameters();
		if (parameters == null) {
			return;
		}
		List<String> flashModes = parameters.getSupportedFlashModes();
		String flashMode = parameters.getFlashMode();
		// Check if camera flash exists
		if (flashModes == null) {
			return;
		}
		if (!Camera.Parameters.FLASH_MODE_OFF.equals(flashMode)) {
			// Turn off the flash
			if (flashModes.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
				parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
				mCamera.setParameters(parameters);
			} else {
			}
		}
	}

	/**
	 * Get the camera's maximum parameters
	 */
	public static GlassCameraSizeInfo getCameraMaxInfoSize(Context mContext, String table_name) {
		GlassCameraSizeInfo info = new GlassCameraSizeInfo();
		SharedPreferences share_data = mContext.getSharedPreferences(table_name, Activity.MODE_PRIVATE);
		info.setWight(share_data.getInt(CAMERA_HIGHT, DEFAULT_CAMERA_NUM));
		info.setHight(share_data.getInt(CAMERA_WIGHT, DEFAULT_CAMERA_NUM));
		return info;
	}

	/**
	 * Storage camera maximum parameters
	 */
	public static void setCameraMaxInfoSize(Context mContext, String table_name, GlassCameraSizeInfo info) {
		SharedPreferences share_data = mContext.getSharedPreferences(table_name, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = share_data.edit();
		editor.putInt(CAMERA_HIGHT, info.getHight());
		editor.putInt(CAMERA_WIGHT, info.getWight());
		editor.commit();
	}

	/**
	 * Turn on the flash
	 */
	public static void setFlashStatus(Camera mCamera, Button openLight) {
		if (mCamera == null || mCamera.getParameters() == null
				|| mCamera.getParameters().getSupportedFlashModes() == null) {
			return;
		}
		Camera.Parameters parameters = mCamera.getParameters();
		String flashMode = mCamera.getParameters().getFlashMode();
		List<String> supportedModes = mCamera.getParameters().getSupportedFlashModes();
		if (Camera.Parameters.FLASH_MODE_OFF.equals(flashMode)
				&& supportedModes.contains(Camera.Parameters.FLASH_MODE_ON)) {// 关闭状态
			parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
			mCamera.setParameters(parameters);
			openLight.setBackgroundResource(R.drawable.ic_glass_camera_flash_on);
		} else if (Camera.Parameters.FLASH_MODE_ON.equals(flashMode)) {// 开启状态
			if (supportedModes.contains(Camera.Parameters.FLASH_MODE_AUTO)) {
				parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
				openLight.setBackgroundResource(R.drawable.ic_glass_camera_flash_auto);
				mCamera.setParameters(parameters);
			} else if (supportedModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
				parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
				openLight.setBackgroundResource(R.drawable.ic_glass_camera_flash_off);
				mCamera.setParameters(parameters);
			}
		} else if (Camera.Parameters.FLASH_MODE_AUTO.equals(flashMode)
				&& supportedModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
			parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
			mCamera.setParameters(parameters);
			openLight.setBackgroundResource(R.drawable.ic_glass_camera_flash_off);
		}
	}
}
