package com.vkclient.activities;

import android.support.v4.app.Fragment;

import com.example.podkaifom.vkclient.R;
import com.vkclient.fragments.WallPostFragment;


public class WallPostActivity extends VkSdkActivity {
    protected Fragment createFragment() {
        return new WallPostFragment();
    }

    @Override
    int getLayoutResource() {
        return R.layout.activity_single_fragment;
    }
}
