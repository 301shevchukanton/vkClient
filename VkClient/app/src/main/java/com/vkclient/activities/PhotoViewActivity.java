package com.vkclient.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.podkaifom.vkclient.R;
import com.vkclient.fragments.PhotoViewFragment;

public class PhotoViewActivity extends VkSdkActivity {
    public static final String PHOTO_EXTRA = "photo";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fragmentManager = getFragmentManager();
        Fragment photoViewFragment = fragmentManager.findFragmentById(R.id.container_photo_view);
        if (photoViewFragment == null) {
            photoViewFragment = new PhotoViewFragment();
            fragmentManager.beginTransaction().add(R.id.container_photo_view, photoViewFragment).commit();
        }
    }

    public static Intent getPhotoViewIntent(Context context, String photoUrl) {
        Intent intent = new Intent(context, PhotoViewActivity.class);
        intent.putExtra(PHOTO_EXTRA, photoUrl);
        return intent;
    }

    @Override
    int getLayoutResource() {
        return R.layout.activity_photo_view;
    }
}
