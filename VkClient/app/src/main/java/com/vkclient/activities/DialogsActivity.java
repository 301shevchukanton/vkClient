package com.vkclient.activities;

import android.support.v4.app.Fragment;

import com.example.podkaifom.vkclient.R;
import com.vkclient.fragments.DialogsFragment;

public class DialogsActivity extends VkSdkActivity {

    @Override
    protected Fragment createFragment() {
        Fragment dialogsFragment = new DialogsFragment();
        return dialogsFragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_single_fragment;
    }
}
