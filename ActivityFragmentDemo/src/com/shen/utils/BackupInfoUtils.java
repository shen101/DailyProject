package com.shen.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import com.shen.bean.SMS;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.provider.Telephony;
import android.util.Xml;

public class BackupInfoUtils {

	public static final String SAVE_MMS_FILE_NAME = "glass_backups_mms.xml";
	public static final String SAVE_BACKUP_FILES = "GlassBackUp";
	private static final String ENCODING_FORMAT = "utf-8";
	private static ArrayList<SMS> smsList = new ArrayList<SMS>();

	/**
	 * Backup SMS
	 */
	public static ArrayList<SMS> getSystemSms(Context context) {
		ArrayList<SMS> lists = new ArrayList<SMS>();
		Cursor cursor = context.getContentResolver().query(Telephony.Sms.CONTENT_URI,
				new String[] { Telephony.Sms._ID, Telephony.Sms.DATE, Telephony.Sms.ADDRESS, Telephony.Sms.BODY }, null,
				null, null);
		while (cursor.moveToNext()) {
			String id = cursor.getString(cursor.getColumnIndex(Telephony.Sms._ID));
			String date = cursor.getString(cursor.getColumnIndex(Telephony.Sms.DATE));
			String address = cursor.getString(cursor.getColumnIndex(Telephony.Sms.ADDRESS));
			String body = cursor.getString(cursor.getColumnIndex(Telephony.Sms.BODY));
			SMS sms = new SMS(body, Long.parseLong(id), date, address);
			lists.add(sms);
		}
		cursor.close();
		return lists;
	}

	@SuppressLint("SimpleDateFormat")
	public static String getDate(String data) {
		long temp_data = Long.parseLong(data);
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(temp_data));
	}

	/**
	 * Save system SMS to sdcard
	 */
	public static boolean saveSystemSmsToSdcard(Context context) {
		ArrayList<SMS> mms_lists = getSystemSms(context);
		XmlSerializer serializer = Xml.newSerializer();
		try {
			File pathFiles = new File(Environment.getExternalStorageDirectory() + File.separator + SAVE_BACKUP_FILES);
			if (!pathFiles.exists()) {
				pathFiles.mkdirs();
			}
			File savefile = new File(pathFiles.getAbsolutePath(), SAVE_MMS_FILE_NAME);
			FileOutputStream mms_os = new FileOutputStream(savefile);
			serializer.setOutput(mms_os, ENCODING_FORMAT);
			serializer.startDocument(ENCODING_FORMAT, true);
			serializer.startTag(null, "SMSS");
			for (SMS sms : mms_lists) {
				serializer.startTag(null, "sms");
				serializer.attribute(null, "id", sms.getId() + "");

				serializer.startTag(null, "body");
				serializer.text(sms.getBody());
				serializer.endTag(null, "body");

				serializer.startTag(null, "address");
				serializer.text(sms.getAddress() + "");
				serializer.endTag(null, "address");

				serializer.startTag(null, "date");
				serializer.text(sms.getDate() + "");
				serializer.endTag(null, "date");

				serializer.endTag(null, "sms");
			}
			serializer.endTag(null, "SMSS");

			serializer.endDocument();
			mms_os.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * parse xml mms to list
	 */
	public static void parsingXmlToArray(Context context) throws XmlPullParserException, IOException {
		File parsefile = new File(Environment.getExternalStorageDirectory() + File.separator + SAVE_BACKUP_FILES,
				SAVE_MMS_FILE_NAME);
		FileInputStream fis = new FileInputStream(parsefile);
		XmlPullParser pullParser = Xml.newPullParser();
		int type = 0;
		try {
			pullParser.setInput(fis, ENCODING_FORMAT);
			type = pullParser.getEventType();
			decoding(type, pullParser);
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
	}

	public static void decoding(int type, XmlPullParser pullParser) throws XmlPullParserException, IOException {
		SMS sms = null;
		while (type != XmlPullParser.END_DOCUMENT) {
			switch (type) {
			case XmlPullParser.START_TAG:
				String tagName = pullParser.getName();
				if ("SMSS".equals(tagName)) {
				} else if ("sms".equals(tagName)) {
					sms = new SMS();
					String idStr = pullParser.getAttributeValue(0);
					sms.setId(Long.parseLong(idStr));
				} else if ("body".equals(tagName)) {
					String body = null;
					body = pullParser.nextText();
					sms.setBody(body);
				} else if ("address".equals(tagName)) {
					String address = null;
					address = pullParser.nextText();
					sms.setAddress(address);
				} else if ("date".equals(tagName)) {
					String date = null;
					date = pullParser.nextText();
					sms.setDate(date);
				}
				break;
			case XmlPullParser.END_TAG:
				String endtagName = pullParser.getName();
				if ("sms".equals(endtagName)) {
					smsList.add(sms);
					sms = null;
				}
				break;
			}
			type = pullParser.next();
		}
	}

	/**
	 * Parse xml as list (Must be parsed before xml can be obtained otherwise
	 * return null)
	 */
	public static ArrayList<SMS> getBackUpMmsList() {
		return smsList.size() == 0 ? null : smsList;
	}

	/**
	 * Insert the backup SMS xml into the system SMS database
	 */
	public static void insertListToSystem(ArrayList<SMS> mms_datas, Context mcontext) {
		for (SMS sms : mms_datas) {
			ContentValues values = new ContentValues();
			values.put(Telephony.Sms.ADDRESS, sms.getAddress());
			values.put(Telephony.Sms.DATE, sms.getDate());
			values.put(Telephony.Sms.BODY, sms.getBody());
			mcontext.getContentResolver().insert(Telephony.Sms.CONTENT_URI, values);
		}
	}
}
