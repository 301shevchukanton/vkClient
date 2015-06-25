package com.vkclient.supports;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;

import com.vkclient.activities.WallPostActivity;

public class AlertBuilder {

    static public void showErrorMessage(final Context con,String message)
    {
        new AlertDialog.Builder(con)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}
