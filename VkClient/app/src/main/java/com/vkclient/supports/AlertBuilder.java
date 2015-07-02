package com.vkclient.supports;

import android.app.AlertDialog;
import android.content.Context;


public class AlertBuilder {

    static public void showErrorMessage(final Context con,String message)
    {
        new AlertDialog.Builder(con)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
}
