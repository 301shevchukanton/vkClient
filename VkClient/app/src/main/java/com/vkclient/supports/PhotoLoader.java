package com.vkclient.supports;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class PhotoLoader {
    public static void loadPhoto(Context context, String url, ImageView view) {
        Picasso.with(context)
                .load(url)
                .into(view);

    }
}
