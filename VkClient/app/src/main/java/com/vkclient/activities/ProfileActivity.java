package com.vkclient.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

import com.example.podkaifom.vkclient.R;
import com.vk.sdk.VKUIHelper;
import com.vkclient.fragments.ProfileFragment;
import com.vkclient.supports.Logger;

public class ProfileActivity extends VkSdkActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        profileId = getIntent().getStringExtra("id");
        Logger.logDebug("profid", "profileid " + profileId);
        super.onCreate(savedInstanceState);
        VKUIHelper.onCreate(this);
        setContentView(R.layout.activity_profile);
        super.onCreateDrawer();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        ProfileFragment myFragment = new ProfileFragment();
        fragmentTransaction.add(R.id.container_profile, myFragment);
        fragmentTransaction.commit();
        findViewById(R.id.drawer_layout).setVisibility(View.VISIBLE);
    }
}
