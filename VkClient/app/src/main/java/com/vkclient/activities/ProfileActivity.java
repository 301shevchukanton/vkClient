package com.vkclient.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

import com.example.podkaifom.vkclient.R;
import com.vk.sdk.VKUIHelper;
import com.vkclient.fragments.ProfileFragment;
import com.vkclient.supports.Logger;

public class ProfileActivity extends VkSdkActivity {

    private static final String ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        profileId = getIntent().getStringExtra(ID);
        Logger.logDebug("profid", "profileid " + profileId);
        super.onCreate(savedInstanceState);
        VKUIHelper.onCreate(this);
        FragmentManager fragmentManager = getFragmentManager();
        Fragment profileFragment = fragmentManager.findFragmentById(R.id.container_profile);
        if (profileFragment == null) {
            profileFragment = new ProfileFragment();
            fragmentManager.beginTransaction().add(R.id.container_profile, profileFragment).commit();
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_profile;
    }
}
