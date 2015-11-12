package com.vkclient.supports;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class PhotoSaver {

    public static String SavePicture(Context context, ImageView image, String photoUrl) throws IOException {
        String folderToSave = Environment.getExternalStorageDirectory().toString();
        photoUrl = getBaseFileName(photoUrl);
        OutputStream fOut = null;
        try {
            File file = new File(folderToSave, photoUrl);
            fOut = new BufferedOutputStream(new FileOutputStream(file));
            Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            return e.getMessage();
        } finally {
            if (fOut != null) {
                fOut.flush();
                fOut.close();
            }
        }
        return "";
    }

    public static String getBaseFileName(String photoUrl) {
        try {
            String[] pars = photoUrl.split("/");
            photoUrl = pars[pars.length - 1];
        } finally {
            return photoUrl;
        }
    }
}
