package com.vkclient.activities;

import android.support.v4.app.Fragment;

import com.example.podkaifom.vkclient.R;
import com.vkclient.fragments.NewsFragment;

public class NewsActivity extends VkSdkActivity {

    @Override
    protected Fragment createFragment() {
        return new NewsFragment();
    }

    @Override
    int getLayoutResource() {
        return R.layout.activity_single_fragment;
    }
}
