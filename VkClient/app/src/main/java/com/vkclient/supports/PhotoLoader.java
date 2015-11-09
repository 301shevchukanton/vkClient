package com.vkclient.supports;

import android.content.Context;
import android.os.Environment;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

public class PhotoLoader {

    public static void loadPhoto(final Context context, final String url, final ImageView view) {
        if (url != null) {
            String folderToSave = Environment.getExternalStorageDirectory().toString();
            String photoUrl = PhotoSaver.getBaseFileName(url);
            File photoFile = new File(folderToSave, photoUrl);
            if (photoFile.exists()) {
                PhotoLoader.loadPhotoFromFile(context, photoFile, view);
            } else {
                PhotoLoader.loadPhotoFromWeb(context, url, view, new Callback() {
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
                });
            }
        }
    }

    private static void loadPhotoFromWeb(Context context, String url, ImageView view, Callback callback) {
        Picasso.with(context)
                .load(url)
                .into(view, callback);
    }

    private static void loadPhotoFromFile(Context context, File file, ImageView view) {
        Picasso.with(context)
                .load(file)
                .into(view);
    }
}
