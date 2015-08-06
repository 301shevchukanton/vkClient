package com.vkclient.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.widget.ListView;

import com.example.podkaifom.vkclient.R;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKRequest;
import com.vkclient.adapters.NewsListAdapter;
import com.vkclient.entities.News;
import com.vkclient.fragments.NewsFragment;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends VkSdkActivity {
    private VKRequest currentRequest;
    private ListView listView;
    private List<News> news = new ArrayList<>();
    private NewsListAdapter listAdapter;

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
