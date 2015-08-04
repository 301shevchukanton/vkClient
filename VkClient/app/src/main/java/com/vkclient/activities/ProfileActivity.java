package com.vkclient.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;

import com.example.podkaifom.vkclient.R;
import com.vkclient.fragments.ProfileFragment;
import com.vkclient.supports.Logger;

public class ProfileActivity extends VkSdkActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        profileId = getIntent().getStringExtra("id");
        Logger.logDebug("profid", "profileid " + profileId);
        super.onCreate(savedInstanceState);
        FragmentManager fragmentManager = getFragmentManager();
        Fragment profileFragment =  fragmentManager.findFragmentById(R.id.container_profile);
        if (profileFragment == null) {
            profileFragment = new ProfileFragment();
            fragmentManager.beginTransaction().add(R.id.container_profile, profileFragment).commit();
            findViewById(R.id.drawer_layout).setVisibility(View.VISIBLE);
        }
    }
    @Override
    int getLayoutResource()
    {
        return R.layout.activity_profile;
    }
}
