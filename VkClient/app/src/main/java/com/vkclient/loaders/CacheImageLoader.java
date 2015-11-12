package com.vkclient.loaders;


import android.content.Context;
import android.os.Environment;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.vkclient.listeners.ImageLoaderListener;
import com.vkclient.supports.PhotoSaver;

import java.io.File;

public class CacheImageLoader implements ImageLoader {
    private ImageLoader imageLoader;

    public CacheImageLoader(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    @Override
    public void load(Context context, ImageLoaderListener imageLoaderListener, String url, ImageView view) {

        if (url != null) {
            String folderToSave = Environment.getExternalStorageDirectory().toString();
            String photoUrl = PhotoSaver.getBaseFileName(url);
            File photoFile = new File(folderToSave, photoUrl);
            if (photoFile.exists()) {
                loadPhotoFromFile(context, photoFile, view);
                imageLoaderListener.onLoad();
            } else {
                this.imageLoader.load(context, imageLoaderListener, url, view);
            }
        }
    }

    private static void loadPhotoFromFile(Context context, File file, ImageView view) {
        Picasso.with(context)
                .load(file)
                .into(view);
    }
}
