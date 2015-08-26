package com.vkclient.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

import com.example.podkaifom.vkclient.R;
import com.vk.sdk.VKUIHelper;
import com.vkclient.fragments.NewsFragment;

public class NewsActivity extends VkSdkActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VKUIHelper.onCreate(this);
        FragmentManager fragmentManager = getFragmentManager();
        Fragment newsFragment = fragmentManager.findFragmentById(R.id.container_news);
        if (newsFragment == null) {
            newsFragment = new NewsFragment();
            fragmentManager.beginTransaction().add(R.id.container_news, newsFragment).commit();
        }
    }

    @Override
    int getLayoutResource() {
        return R.layout.activity_news;
    }
}
