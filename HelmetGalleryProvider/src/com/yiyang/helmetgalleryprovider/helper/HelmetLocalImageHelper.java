package com.yiyang.helmetgalleryprovider.helper;

import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.yiyang.helmetgalleryprovider.HelmetApplicationContext;

public class HelmetLocalImageHelper {
	private static HelmetLocalImageHelper instance;
	private final HelmetApplicationContext context;
	final List<LocalFile> checkedItems = new ArrayList<>();

	public int getCurrentSize() {
		return currentSize;
	}

	public void setCurrentSize(int currentSize) {
		this.currentSize = currentSize;
	}

	private int currentSize;

	public String getCameraImgPath() {
		return CameraImgPath;
	}

	public String setCameraImgPath() {
		String foloder = HelmetApplicationContext.getInstance().getCachePath() + "/PostPicture/";
		File savedir = new File(foloder);
		if (!savedir.exists()) {
			savedir.mkdirs();
		}
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

		String picName = timeStamp + ".jpg";

		CameraImgPath = foloder + picName;
		return CameraImgPath;
	}

	private String CameraImgPath;

	private static final String[] ORIGINAL_IMAGES = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA,
			MediaStore.Images.Media.ORIENTATION };

	private static final String[] ORIGINAL_VIDEOS = { MediaStore.Video.Media._ID, MediaStore.Video.Media.DATA,
			MediaStore.Video.Media.DATE_TAKEN };

	private static final String[] THUMBNAIL_IMAGES = { MediaStore.Images.Thumbnails._ID,
			MediaStore.Images.Thumbnails.DATA };

	private static final String[] THUMBNAIL_VIDEO = { MediaStore.Video.Thumbnails._ID,
			MediaStore.Video.Thumbnails.DATA };

	private int old_file_size = -1;
	private int new_file_size = -1;

	final List<LocalFile> local_file_list = new ArrayList<>();

	private HelmetLocalImageHelper(HelmetApplicationContext context) {
		this.context = context;
	}

	public List<LocalFile> getFolderMap() {
		return local_file_list;
	}

	public static HelmetLocalImageHelper getInstance() {
		return instance;
	}

	public static void init(HelmetApplicationContext context) {
		instance = new HelmetLocalImageHelper(context);
		new Thread(new Runnable() {
			@Override
			public void run() {
				instance.initImage();
			}
		}).start();
	}

	public boolean isInitFiled() {
		return new_file_size == old_file_size;
	}

	public List<LocalFile> getCheckedItems() {
		return checkedItems;
	}

	private boolean resultOk;

	public boolean isResultOk() {
		return resultOk;
	}

	public void setResultOk(boolean ok) {
		resultOk = ok;
	}

	public synchronized void initImage() {

		// InIt image
		Cursor original_image_cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				ORIGINAL_IMAGES, null, null, MediaStore.Images.Media.DATE_TAKEN + " DESC");

		// InIt video
		Cursor original_video_cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
				ORIGINAL_VIDEOS, null, null, MediaStore.Video.Media._ID + " DESC");

		if (original_image_cursor == null || original_video_cursor == null)
			return;

		new_file_size = original_image_cursor.getCount() + original_video_cursor.getCount();

		if (isInitFiled())
			return;

		old_file_size = original_image_cursor.getCount() + original_video_cursor.getCount();
		local_file_list.clear();

		while (original_video_cursor.moveToNext()) {
			int original_video_id = original_video_cursor.getInt(0);
			String original_video_path = original_video_cursor.getString(1);
			File video_file = new File(original_video_path);

			if (video_file.exists()) {
				String video_uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI.buildUpon()
						.appendPath(Integer.toString(original_video_id)).build().toString();

				LocalFile videolocalFile = new LocalFile();
				videolocalFile.setOriginalUri(video_uri);
				videolocalFile.setCreate_time(new File(original_video_path).lastModified());
				videolocalFile.setFile_path(original_video_path);
				videolocalFile.setImage(false);
				local_file_list.add(videolocalFile);
			}
		}

		while (original_image_cursor.moveToNext()) {
			int id = original_image_cursor.getInt(0);
			String path = original_image_cursor.getString(1);
			File image_file = new File(path);

			if (image_file.exists()) {

				String thumbUri = getThumbnail(id, path);

				String uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.buildUpon().appendPath(Integer.toString(id))
						.build().toString();
				if (isEmpty(uri))
					continue;
				if (isEmpty(thumbUri))
					thumbUri = uri;

				String folder = image_file.getParentFile().getName();

				LocalFile localFile = new LocalFile();
				localFile.setOriginalUri(uri);
				localFile.setThumbnailUri(thumbUri);
				localFile.setCreate_time(new File(path).lastModified());
				localFile.setFile_path(path);
				localFile.setImage(true);
				int degree = original_image_cursor.getInt(2);
				if (degree != 0) {
					degree = degree + 180;
				}
				localFile.setOrientation(360 - degree);

				local_file_list.add(localFile);
			}
		}

		Collections.sort(local_file_list);
	}

	private String getThumbnail(int id, String path) {

		Cursor cursor = context.getContentResolver().query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
				THUMBNAIL_IMAGES, MediaStore.Images.Thumbnails.IMAGE_ID + " = ?", new String[] { id + "" }, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			int thumId = cursor.getInt(0);
			String uri = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI.buildUpon()
					.appendPath(Integer.toString(thumId)).build().toString();
			cursor.close();
			return uri;
		}
		cursor.close();
		return null;
	}

	public static boolean isEmpty(String input) {
		if (input == null || "".equals(input))
			return true;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}

	public void clear() {
		checkedItems.clear();
		currentSize = (0);
		String foloder = HelmetApplicationContext.getInstance().getCachePath() + "/PostPicture/";
		File savedir = new File(foloder);
		if (savedir.exists()) {
			deleteFile(savedir);
		}
	}

	public void deleteFile(File file) {

		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					deleteFile(files[i]);
				}
			}
		} else {
		}
	}

	public static class LocalFile implements Comparable<LocalFile> {
		private String originalUri;
		private String thumbnailUri;
		private String video_uri;
		private String file_path;
		private boolean isImage;
		private int orientation;
		private long create_time;
		private int order;
		
		public boolean isImage() {
			return isImage;
		}

		public void setImage(boolean isImage) {
			this.isImage = isImage;
		}

		public String getFile_path() {
			return file_path;
		}

		public void setFile_path(String file_path) {
			this.file_path = file_path;
		}

		public int getOrder() {
			return order;
		}

		public void setOrder(int order) {
			this.order = order;
		}

		public long getCreate_time() {
			return create_time;
		}

		public void setCreate_time(long create_time) {
			this.create_time = create_time;
		}

		public String getVideo_uri() {
			return video_uri;
		}

		public void setVideo_uri(String video_uri) {
			this.video_uri = video_uri;
		}

		public String getThumbnailUri() {
			return thumbnailUri;
		}

		public void setThumbnailUri(String thumbnailUri) {
			this.thumbnailUri = thumbnailUri;
		}

		public String getOriginalUri() {
			return originalUri;
		}

		public void setOriginalUri(String originalUri) {
			this.originalUri = originalUri;
		}

		public int getOrientation() {
			return orientation;
		}

		public void setOrientation(int exifOrientation) {
			orientation = exifOrientation;
		}

		@Override
		public int compareTo(LocalFile another) {
			// TODO Auto-generated method stub
			long i = another.getCreate_time() - this.getCreate_time();
			if (i == 0) {
				return this.order - another.getOrder();
			}
			return (int) i;
		}
	}
}
