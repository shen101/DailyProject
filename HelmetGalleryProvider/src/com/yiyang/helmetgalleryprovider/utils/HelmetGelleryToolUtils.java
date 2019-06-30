package com.yiyang.helmetgalleryprovider.utils;

import java.io.File;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

public class HelmetGelleryToolUtils {

	public static void deletePicture(String localPath, Context context) {
		if (!TextUtils.isEmpty(localPath)) {
			Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
			ContentResolver contentResolver = context.getContentResolver();
			String url = MediaStore.Images.Media.DATA + "=?";
			int deleteRows = contentResolver.delete(uri, url, new String[] { localPath });
			if (deleteRows == 0) {
				File file = new File(localPath);
				while (file.exists()) {
					file.delete();
				}
			}
			Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			File file = new File(localPath);
			intent.setData(Uri.fromFile(file));
			context.sendBroadcast(intent);
		}

	}

	public static void playVideo(Context mContext, String file_path) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		File file = new File(file_path);
		Uri uri = null;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			uri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".fileprovider", file);
		} else {
			uri = Uri.fromFile(file);
		}
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		intent.putExtra("is_from_gallery", true);
		intent.setDataAndType(uri, "video/*");
		mContext.startActivity(intent);
	}
}
