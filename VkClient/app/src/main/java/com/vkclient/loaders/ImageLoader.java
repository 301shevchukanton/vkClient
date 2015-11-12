package com.vkclient.loaders;


import android.content.Context;
import android.widget.ImageView;

import com.vkclient.listeners.ImageLoaderListener;

public interface ImageLoader {
    void load(Context context, ImageLoaderListener imageLoaderListener, String url, ImageView view);
}
