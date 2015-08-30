package com.vkclient.activities;

import android.support.v4.app.Fragment;

import com.example.podkaifom.vkclient.R;
import com.vkclient.fragments.ProfileFragment;

public class ProfileActivity extends VkSdkActivity {

    @Override
    protected Fragment createFragment() {
        return new ProfileFragment();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_single_fragment;
    }
}
