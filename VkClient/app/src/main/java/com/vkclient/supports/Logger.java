package com.vkclient.supports;

import android.util.Log;

public class Logger {
    public static void logDebug(String key, String message) {
        Log.d(key, message);
    }

    public static void logError(String key, String message) {
        Log.e(key, message);
    }
}
