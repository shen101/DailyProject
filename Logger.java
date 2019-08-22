package com.shen.demo1;

import android.util.Log;
import android.os.SystemProperties;

public class Logger {

    public static boolean DEBUG = !("user".equals(SystemProperties.get("ro.build.type", "user"))) ;

    public static void i(String tag, String msg) {
        if (DEBUG) Log.i(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (DEBUG) Log.v(tag, msg);
    }

    public static void w(String tag, String string) {
        if (DEBUG) Log.w(tag, string);
    }

    public static void e(String tag, String msg) {
        if (DEBUG) Log.e(tag, msg);
    }
}