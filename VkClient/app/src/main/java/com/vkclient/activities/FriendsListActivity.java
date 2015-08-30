package com.vkclient.activities;

import android.support.v4.app.Fragment;

import com.example.podkaifom.vkclient.R;
import com.vkclient.fragments.FriendsListFragment;


public class FriendsListActivity extends VkSdkActivity {

    @Override
    protected Fragment createFragment() {
        return new FriendsListFragment();
    }

    @Override
    int getLayoutResource() {
        return R.layout.activity_single_fragment;
    }
}
