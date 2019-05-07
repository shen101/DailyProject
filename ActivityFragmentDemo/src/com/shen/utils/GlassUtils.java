package com.shen.utils;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.android.internal.telephony.ITelephony;
import com.shen.activityfragmentdemo.R;
import com.shen.bean.NotificationInfo;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Notification.Builder;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.ScanCallback;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class GlassUtils {

	private static final String TAG = "Utils";
	private static long lastClickTime;
	private static Toast toast;

	// Service UUID
	public static UUID CONNECTION_SERVICE_UUID = UUID.fromString("00007777-0000-1000-8000-00805f9b34fb");
	// Read-only
	public static UUID CHARACTERISTIC_READ_UUID = UUID.fromString("275348FB-C14D-4FD5-B434-7C3F351DEA5F");
	// Read-write
	public static UUID CHARACTERISTIC_WRITE_UUID = UUID.fromString("BD28E457-4026-4270-A99F-F9BC20182E15");
	// Read-write characteristic
	public static UUID CHARACTERISTIC_DEMO_UUID = UUID.fromString("BD28E457-4026-4270-A99F-F9BC20182E15");

	public static final int OPEN_BLUETOOTH_NUM = 10;
	public static final int REFLASH_STATUS_SUCCESSFUL_NUM = 11;
	public static final int REFLASH_STATUS_FAILED_NUM = 12;

	public static final String ACTION_PHONE_STATE_CHANGED = "android.intent.action.PHONE_STATE";
	public static final String ACTION_START_INCALLUI_TAG = "com.yiyang.glass.GLASS_INCALLUI_MAIN";
	public static final String ACTION_START_DIALPAD_TAG = "com.yiyang.glass.GLASS_DIALPAD_MAIN";
	public static final String ACTION_IS_CONNECTED_CALL_TAG = "com.yiyang.glass.ACTION_IS_CONNECTED";
	public static final String ACTION_FRAGMENT_ONBACK_CLICK_TAG = "com.shen.BACK_ONCLICK";
	public static final String INTENT_INCALL_UI_NUMBER = "intent_incall_ui_number";
	public static final String ACTION_START_PEARVIEW_TAG = "com.yiyang.glass.GLASS_PEARVIEW_MAIN";
	public static final String ACTION_WIFI_AP_STATE_CHANGED_TAG = "android.net.wifi.WIFI_AP_STATE_CHANGED";
	public static final String MMI_IMEI_DISPLAY = "*#06#";
	public static final int INTENT_INCALL_UI_NUM = 1000;
	public static final int DURATION = 50;
	public static final int GLASS_LOCATION_MODE_OFF = android.provider.Settings.Secure.LOCATION_MODE_OFF;
	public static final int GLASS_LOCATION_MODE_SENSORS_ONLY = android.provider.Settings.Secure.LOCATION_MODE_SENSORS_ONLY;
	public static final int GLASS_LOCATION_BATTERY_SAVING = android.provider.Settings.Secure.LOCATION_MODE_BATTERY_SAVING;
	public static final int GLASS_LOCATION_HIGH_ACCURACY = android.provider.Settings.Secure.LOCATION_MODE_HIGH_ACCURACY;

	public static final String ACTION_TAKE_PHOTO_TYPE_TAG = "take_photo";
	public static final int ACTION_TAKE_PHOTO_TYPE_NUM = 1;
	public static final String ACTION_VIDEO_PHOTO_TYPE_TAG = "video_photo";
	public static final int ACTION_VIDEO_PHOTO_TYPE_NUM = 2;
	public static final String ACTION_PREAVIEW_PHOTO_TYPE_TAG = "preaview_photo";
	public static final int ACTION_PREAVIEW_PHOTO_TYPE_NUM = 3;

	public static final String NOTIFICATION_TABLE_VALUES = "notification_table_values";
	public static final String NOTIFICATION_TABLE_TENCENT_MM = "notification_table_tencent_mm";
	public static final String NOTIFICATION_TABLE_TENCENT_QQ = "notification_table_tencent_qq";
	public static final String NOTIFICATION_TABLE_ANDROID_SMS = "notification_table_android_sms";
	public static final String NOTIFICATION_TABLE_INCALLUI = "notification_table_incallui";

	public static final String NOTIFICATION_MESSAGE_NUM = "com.shen.notification.NOTIFICATION_MESSAGE";
	public static final String NOTIFICATION_MESSAGE_PACKAGE_NUM = "notification_package";
	public static final String NOTIFICATION_MESSAGE_TITLE_NUM = "notification_title";
	public static final String NOTIFICATION_MESSAGE_CONTENTS_NUM = "notification_contents";

	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		Log.i(TAG, "timeD = " + timeD);
		if (0 < timeD && timeD < 400) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	public static void showToast(Context context, String content) {
		if (toast == null) {
			toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
		} else {
			toast.setText(content);
		}
		toast.show();
	}

	private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

	static {
		ORIENTATIONS.append(Surface.ROTATION_0, 90);
		ORIENTATIONS.append(Surface.ROTATION_90, 0);
		ORIENTATIONS.append(Surface.ROTATION_180, 270);
		ORIENTATIONS.append(Surface.ROTATION_270, 180);
	}

	public static int getOrientation(int rotation, int sensorOrientation) {
		return (ORIENTATIONS.get(rotation) + sensorOrientation + 270) % 360;
	}

	public static String getContact(Context mcontext, String mNumber) {
		String[] projection = { ContactsContract.PhoneLookup.DISPLAY_NAME,
				ContactsContract.CommonDataKinds.Phone.NUMBER };

		Cursor cursor = mcontext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				projection, ContactsContract.CommonDataKinds.Phone.NUMBER + " = '" + mNumber + "'", null, null);

		if (cursor == null) {
			return "";
		}
		Log.d(TAG, "getPeople cursor.getCount() = " + cursor.getCount());

		String name = "";

		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
			name = cursor.getString(nameFieldColumnIndex);
		}
		return name;
	}

	/**
	 * Determine if the entered phone number is qualified
	 */
	public static boolean isAllowTelePhoneNumber(String inputText) {
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(inputText);
		return m.matches();
	}

	/**
	 * EndPhone
	 */
	public static void endPhone(Context c, TelephonyManager tm) {
		try {
			ITelephony iTelephony;
			Method getITelephonyMethod = TelephonyManager.class.getDeclaredMethod("getITelephony", (Class[]) null);
			getITelephonyMethod.setAccessible(true);
			iTelephony = (ITelephony) getITelephonyMethod.invoke(tm, (Object[]) null);
			iTelephony.endCall();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get the strength of the current wifi signal
	 */
	public static int getWiFiSignal(Context context) {
		int strength = 0;
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifiManager.getConnectionInfo();
		if (info.getBSSID() != null) {
			strength = WifiManager.calculateSignalLevel(info.getRssi(), 5);
		}
		return strength;
	}

	/**
	 * Get the strength of the current mobile signal
	 */
	public static void initCurrentMobileSingal(final Context context, final ImageView mMobileSingalView,
			boolean status) {
		final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		PhoneStateListener phoneStateListener = new PhoneStateListener() {
			@Override
			public void onSignalStrengthsChanged(SignalStrength signalStrength) {
				super.onSignalStrengthsChanged(signalStrength);
				int gsmSignalStrength = signalStrength.getGsmSignalStrength();
				int dbm = -113 + 2 * gsmSignalStrength;
				if (dbm >= -75) {
					mMobileSingalView.setImageResource(R.drawable.ic_glass_mobile_signal_4);
				} else if (dbm >= -85) {
					mMobileSingalView.setImageResource(R.drawable.ic_glass_mobile_signal_3);
				} else if (dbm >= -95) {
					mMobileSingalView.setImageResource(R.drawable.ic_glass_mobile_signal_2);
				} else if (dbm >= -100) {
					mMobileSingalView.setImageResource(R.drawable.ic_glass_mobile_signal_1);
				} else {
					mMobileSingalView.setImageResource(R.drawable.ic_glass_mobile_signal_0);
				}
				Log.d("shen", dbm + "");
				try {
					if (isAirplaneMode(context)) {
						mMobileSingalView.setVisibility(View.GONE);
					} else {
						mMobileSingalView.setVisibility(View.VISIBLE);
					}
				} catch (SettingNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		if (status) {
			tm.listen(phoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
		} else {
			tm.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
		}
	}

	/**
	 * Determine if it is currently in flight mode
	 */
	public static boolean isAirplaneMode(Context mcontext) throws SettingNotFoundException {
		return Settings.Global.getInt(mcontext.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON) == 1 ? true
				: false;
	}

	/**
	 * Get current GPS status
	 */
	public static int getGpsStatusMode(Context context) {
		int gps_mode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE,
				Settings.Secure.LOCATION_MODE_OFF);
		if (gps_mode == GLASS_LOCATION_MODE_SENSORS_ONLY) {
			return GLASS_LOCATION_MODE_SENSORS_ONLY;
		} else if (gps_mode == GLASS_LOCATION_BATTERY_SAVING) {
			return GLASS_LOCATION_BATTERY_SAVING;
		} else if (gps_mode == GLASS_LOCATION_HIGH_ACCURACY) {
			return GLASS_LOCATION_HIGH_ACCURACY;
		} else {
			return GLASS_LOCATION_MODE_OFF;
		}
	}

	/**
	 * Initialize current GPS signal strength
	 */
	public static void initGPSSignal(final Context context, final ImageView gps_view, boolean status) {
		final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		GpsStatus.Listener gpsS = new GpsStatus.Listener() {
			@Override
			public void onGpsStatusChanged(int event) {
				int gpscount = 0;
				Log.d("shen", "event ========= " + event);
				if (event == GpsStatus.GPS_EVENT_FIRST_FIX) {

				} else if (event == GpsStatus.GPS_EVENT_SATELLITE_STATUS) {
					GpsStatus gpsStatus = locationManager.getGpsStatus(null);
					int maxStatellites = gpsStatus.getMaxSatellites();
					Iterator<GpsSatellite> it = gpsStatus.getSatellites().iterator();
					while (it.hasNext() && gpscount <= maxStatellites) {
						GpsSatellite s = it.next();
						if (s.usedInFix()) {
							gpscount++;
						}
					}
					Log.d("shen", "gpscount = " + gpscount);
					// showToast(context, "gpscount = "+gpscount);
					if (gpscount >= 4) {
						gps_view.setImageResource(R.drawable.ic_glass_gps_signal_1);
					} else if (gpscount >= 7) {
						gps_view.setImageResource(R.drawable.ic_glass_gps_signal_2);
					} else if (gpscount >= 10) {
						gps_view.setImageResource(R.drawable.ic_glass_gps_signal_3);
					} else {
						gps_view.setImageResource(R.drawable.ic_glass_gps_signal_0);
					}
				} else if (event == GpsStatus.GPS_EVENT_STARTED) {
				} else if (event == GpsStatus.GPS_EVENT_STOPPED) {
				}
			}
		};
		LocationListener locationListener = new LocationListener() {
			@Override
			public void onLocationChanged(Location location) {

			}

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {

			}

			@Override
			public void onProviderEnabled(String provider) {

			}

			@Override
			public void onProviderDisabled(String provider) {

			}
		};
		if (status) {
			locationManager.addGpsStatusListener(gpsS);
			// locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
			// 1000, 1, locationListener);
		} else {
			locationManager.removeGpsStatusListener(gpsS);
		}
	}

	/**
	 * Initialize Bluetooth status
	 */
	public static void initBluetoothStatus(BluetoothAdapter mAdapter, ImageView bluetooth_view) {
		GlassBluetoothAdapter mBluetoothAdapter = new GlassBluetoothAdapter(mAdapter);
		switch (mBluetoothAdapter.getState()) {
		case BluetoothAdapter.STATE_ON:
		case BluetoothAdapter.STATE_TURNING_ON:
			bluetooth_view.setVisibility(View.VISIBLE);
			bluetooth_view.setBackgroundResource(R.drawable.ic_glass_bluetooth);
			break;
		case BluetoothAdapter.STATE_OFF:
		case BluetoothAdapter.STATE_TURNING_OFF:
			bluetooth_view.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}

	/**
	 * Set notification app
	 */
	public static void setNotifiAppValues(Context mContext, String values_name, int values) {
		SharedPreferences share_data = mContext.getSharedPreferences(NOTIFICATION_TABLE_VALUES, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = share_data.edit();
		editor.putInt(values_name, values);
		editor.commit();
	}

	/**
	 * Get notification app values
	 */
	public static boolean getBotifiAppValues(Context mContext, String values_name) {
		SharedPreferences share_data = mContext.getSharedPreferences(NOTIFICATION_TABLE_VALUES, Activity.MODE_PRIVATE);
		return share_data.getInt(values_name, 0) == 0 ? false : true;
	}

	public static int getAdvError(int error) {

		if (error == AdvertiseCallback.ADVERTISE_FAILED_DATA_TOO_LARGE) {
			return R.string.system_text_adv_error_one;
		} else if (error == AdvertiseCallback.ADVERTISE_FAILED_TOO_MANY_ADVERTISERS) {
			return R.string.system_text_adv_error_two;
		} else if (error == AdvertiseCallback.ADVERTISE_FAILED_ALREADY_STARTED) {
			return R.string.system_text_adv_error_three;
		} else if (error == AdvertiseCallback.ADVERTISE_FAILED_FEATURE_UNSUPPORTED) {
			return R.string.system_text_adv_error_five;
		}
		return R.string.system_text_adv_error_four;
	}

	public static int getScanError(int error) {
		if (error == ScanCallback.SCAN_FAILED_ALREADY_STARTED) {
			return R.string.system_text_scan_error_one;
		} else if (error == ScanCallback.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED) {
			return R.string.system_text_scan_error_two;
		} else if (error == ScanCallback.SCAN_FAILED_INTERNAL_ERROR) {
			return R.string.system_text_scan_error_three;
		} else if (error == ScanCallback.SCAN_FAILED_FEATURE_UNSUPPORTED) {
			return R.string.system_text_scan_error_four;
		}
		return R.string.system_text_scan_error_five;
	}

	public static int getConnectionStatus(int status, int newsattus) {
		if (status == 0 && newsattus == 2) {
			return R.string.system_text_connect_success;
		} else {
			return R.string.system_text_connect_failed;
		}
	}

	public static boolean createBond(Class btClass, BluetoothDevice btDevice) throws Exception {
		Method createBondMethod = btClass.getMethod("createBond");
		Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);
		return returnValue.booleanValue();
	}
	
	public static void sendNotification(Activity mActivity, NotificationInfo infos) {
		// Intent intent = new Intent(mActivity, MainActivity.class);
		// PendingIntent pintent = PendingIntent.getActivity(this, 0, intent,
		// 0);
		NotificationManager manager = (NotificationManager) mActivity.getSystemService(Context.NOTIFICATION_SERVICE);
		Builder builder = new Builder(mActivity);
		builder.setSmallIcon(infos.getNoti_picture());
		builder.setTicker("hello");
		builder.setWhen(System.currentTimeMillis());
		builder.setContentTitle(infos.getNoti_title());
		builder.setContentText(infos.getNoti_contents());
		// builder.setContentIntent(pintent);// 点击后的意图
		builder.setDefaults(Notification.DEFAULT_ALL);// 给通知设置震动，声音，和提示灯三种效果
		Notification notification = builder.build();
		manager.notify(0, notification);
	}

}
