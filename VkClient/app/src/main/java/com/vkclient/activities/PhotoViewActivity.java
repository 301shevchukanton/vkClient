package com.vkclient.activities;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.podkaifom.vkclient.R;
import com.vkclient.fragments.PhotoViewFragment;

public class PhotoViewActivity extends VkSdkActivity {
    public static final String PHOTO_EXTRA = "photo";

    @Override
    protected Fragment createFragment() {
        return new PhotoViewFragment();
    }

    public static Intent getPhotoViewIntent(Context context, String photoUrl) {
        Intent intent = new Intent(context, PhotoViewActivity.class);
        intent.putExtra(PHOTO_EXTRA, photoUrl);
        return intent;
    }

    @Override
    int getLayoutResource() {
        return R.layout.activity_single_fragment;
    }
}
