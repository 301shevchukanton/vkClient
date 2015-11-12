package com.vkclient.loaders;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.vkclient.listeners.ImageLoaderListener;
import com.vkclient.supports.Logger;
import com.vkclient.supports.PhotoSaver;

import java.io.IOException;

public class NetworkImageLoader implements ImageLoader {

    @Override
    public void load(final Context context, ImageLoaderListener imageLoaderListener, final String url, final ImageView view) {
        Callback callback = new Callback() {
            @Override
            public void onSuccess() {
                try {
                    PhotoSaver.SavePicture(context, view, url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError() {
                Logger.logError("Photo loading error:", "Loading from web failed");
            }
        };
        this.loadPhotoFromWeb(context, url, view, callback);
        imageLoaderListener.onLoad();
    }

    private void loadPhotoFromWeb(Context context, String url, ImageView view, Callback callback) {
        Picasso.with(context)
                .load(url)
                .into(view, callback);
    }
}
