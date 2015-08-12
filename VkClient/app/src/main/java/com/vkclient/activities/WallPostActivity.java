package com.vkclient.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

import com.example.podkaifom.vkclient.R;
import com.vkclient.fragments.WallPostFragment;


public class WallPostActivity extends VkSdkActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fragmentManager = getFragmentManager();
        Fragment wallPostFragment = fragmentManager.findFragmentById(R.id.container_wall_post);
        if (wallPostFragment == null) {
            wallPostFragment = new WallPostFragment();
            fragmentManager.beginTransaction().add(R.id.container_wall_post, wallPostFragment).commit();
        }
    }

    @Override
    int getLayoutResource() {
        return R.layout.activity_wall_post;
    }
}
