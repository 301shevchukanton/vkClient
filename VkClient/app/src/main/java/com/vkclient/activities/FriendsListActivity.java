package com.vkclient.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

import com.example.podkaifom.vkclient.R;
import com.vkclient.fragments.FriendsListFragment;
import com.vkclient.supports.Logger;


public class FriendsListActivity extends VkSdkActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        profileId = getIntent().getStringExtra("id");
        Logger.logDebug("profid", "profile id taked" + profileId);
        super.onCreate(savedInstanceState);
        FragmentManager fragmentManager = getFragmentManager();
        Fragment friendsListFragment = fragmentManager.findFragmentById(R.id.container_friends_list);
        if (friendsListFragment == null) {
            friendsListFragment = new FriendsListFragment();
            fragmentManager.beginTransaction().add(R.id.container_friends_list, friendsListFragment).commit();
        }
    }

    @Override
    int getLayoutResource() {
        return R.layout.activity_friends_list;
    }
}
