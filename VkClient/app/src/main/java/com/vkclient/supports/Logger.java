package com.vkclient.supports;

import android.util.Log;

public class Logger {
    public static void logDebug(String key, String message) {
        Log.d(key, message);
    }

    public static void logError(String key, String message) {
        Log.e(key, message);
    }

    public static void logWarning(String key, String message) {
        Log.w(key, message);
    }
}
