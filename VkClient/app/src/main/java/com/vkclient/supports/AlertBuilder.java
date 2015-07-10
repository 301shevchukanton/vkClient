package com.vkclient.supports;

import android.app.AlertDialog;
import android.content.Context;


public class AlertBuilder {

    static public void showErrorMessage(final Context context, String message) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
}
