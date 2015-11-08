package com.vkclient.supports;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

public class PhotoLoader {
    public static void loadPhoto(Context context, String url, ImageView view) {
        Picasso.with(context)
                .load(url)
                .into(view);
    }

    public static void loadPhoto(Context context, String url, ImageView view, Callback callback) {
        Picasso.with(context)
                .load(url)
                .into(view, callback);
    }

    public static void loadPhotoFromFile(Context context, File file, ImageView view) {
        Picasso.with(context)
                .load(file)
                .into(view);
    }
}
