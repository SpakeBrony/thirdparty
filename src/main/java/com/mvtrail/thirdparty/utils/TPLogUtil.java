package com.mvtrail.thirdparty.utils;

import android.util.Log;

/**
 * Created by jun.liu on 2016/11/22.
 */


public class TPLogUtil {
    public static boolean Debug = true;
    public static final String COMMON_TAG = "thirdparty_tag";

    public static void setDebug(boolean debug) {
        Debug = debug;
    }

    public static void d(String msg, Throwable r) {
        d(COMMON_TAG, msg, r);
    }

    public static void d(String tag, String msg, Throwable r) {
        if (Debug)
            Log.d(tag, msg, r);
    }

    public static void d(String msg) {
        d(COMMON_TAG, msg);
    }

    public static void d(String tag, String msg) {
        if (Debug)
            Log.d(tag, msg);
    }


    public static void w(String msg, Throwable r) {
        Log.w(COMMON_TAG, msg, r);
    }

    public static void w(String tag, String msg, Throwable r) {
        Log.w(tag, msg, r);
    }

    public static void w(String msg) {
        Log.w(COMMON_TAG, msg);
    }

    public static void w(String tag, String msg) {
        Log.w(tag, msg);
    }

    public static void i(String msg, Throwable r) {
        Log.i(COMMON_TAG, msg, r);
    }

    public static void i(String tag, String msg) {
        Log.i(tag, msg);
    }

    public static void i(String msg) {
        Log.i(COMMON_TAG, msg);
    }

    public static void i(String tag, String msg, Throwable r) {
        Log.i(tag, msg, r);
    }

    public static void e(String msg, Throwable r) {
        Log.e(COMMON_TAG, msg, r);
    }

    public static void e(String tag, String msg) {
        Log.e(tag, msg);
    }

    public static void e(String msg) {
        Log.e(COMMON_TAG, msg);
    }

    public static void e(String tag, String msg, Throwable r) {
        Log.e(tag, msg, r);
    }

    public static void v(String msg, Throwable r) {
        Log.v(COMMON_TAG, msg, r);
    }

    public static void v(String tag, String msg) {
        Log.v(tag, msg);
    }

    public static void v(String msg) {
        Log.v(COMMON_TAG, msg);
    }

    public static void v(String tag, String msg, Throwable r) {
        Log.v(tag, msg, r);
    }

}
