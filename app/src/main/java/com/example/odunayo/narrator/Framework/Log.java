package com.example.odunayo.narrator.Framework;

public class Log {
    public static final boolean LOG = false;
    public static final boolean ONLY_SPECIAL = false;
    public static final boolean APPLAUSE_VERSION = false;

    public static void i(String tag, String string) {
        if (LOG && !ONLY_SPECIAL) android.util.Log.i(tag, string);
    }
    public static void e(String tag, String string) {
        if (LOG && !ONLY_SPECIAL) android.util.Log.e(tag, string);
    }
    public static void d(String tag, String string) {
        if (LOG && !ONLY_SPECIAL) android.util.Log.d(tag, string);
    }
    public static void v(String tag, String string) {
        if (LOG && !ONLY_SPECIAL) android.util.Log.v(tag, string);
    }
    public static void w(String tag, String string) {
        if (LOG && !ONLY_SPECIAL) android.util.Log.w(tag, string);
    }

    public static void dSpecial(String tag, String string) {
        if (LOG) android.util.Log.d(tag, string);
    }

    // util
    public static void timeSince(String TAG, long start, int number) {
        long elapsed = System.currentTimeMillis() - start;
        Log.d(TAG, number + " elapsed: " + elapsed + "ms");
    }

    public static void nanoTimeSince(String TAG, long start, String message) {
        double elapsed = (System.nanoTime() - start)/1000000.0f;
        Log.d(TAG, message + " elapsed: " + elapsed + "ms");
    }
}