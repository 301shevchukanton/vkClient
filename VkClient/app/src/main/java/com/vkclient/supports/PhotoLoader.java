package com.vkclient.supports;

import android.content.Context;
import android.widget.ImageView;

import com.example.podkaifom.vkclient.R;
import com.squareup.picasso.Picasso;

public class PhotoLoader {
    public static void loadPhoto(Context con, String url, ImageView view){
        Picasso.with(con)
                .load(url)
                .into(view);
    }
}
