package com.vkclient.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.podkaifom.vkclient.R;
import com.vkclient.supports.PhotoLoader;

public class PhotoViewActivity extends VkSdkActivity {
    public static final String PHOTO_EXTRA = "photo";
    private static String photoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.photoUrl = getIntent().getStringExtra("photo");
        PhotoLoader.loadPhoto(getApplicationContext(), photoUrl, (ImageView) findViewById(R.id.ivPhoto));
    }

    public static Intent getPhotoViewIntent(Context context, String photoUrl) {
        Intent intent = new Intent(context, PhotoViewActivity.class);
        intent.putExtra(PHOTO_EXTRA, photoUrl);
        return intent;
    }

    @Override
    int getLayoutResource()
    {
        return R.layout.activity_photo_view;
    }
}
