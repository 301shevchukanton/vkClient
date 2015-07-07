package com.vkclient.supports;

import android.content.Context;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class PhotoLoader {
    public static void loadPhoto(Context con, String url, ImageView view) {
          Picasso.with(con)
                  .load(url)
                  .into(view);

    }
}
