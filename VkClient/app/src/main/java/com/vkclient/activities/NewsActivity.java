package com.vkclient.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.podkaifom.vkclient.R;
import com.vk.sdk.VKUIHelper;

public class NewsActivity extends VkSdkActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VKUIHelper.onCreate(this);
        setContentView(R.layout.activity_news);
        super.onCreateDrawer();

    }

}
