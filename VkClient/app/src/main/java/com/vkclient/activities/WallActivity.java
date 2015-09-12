package com.vkclient.activities;

import android.support.v4.app.Fragment;

import com.example.podkaifom.vkclient.R;
import com.vkclient.fragments.WallFragment;

public class WallActivity extends VkSdkActivity {
    @Override
    protected Fragment createFragment() {
        return new WallFragment();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_single_fragment;
    }
}
