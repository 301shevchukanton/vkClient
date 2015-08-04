package com.vkclient.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.podkaifom.vkclient.R;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKApiConst;
import com.vkclient.adapters.DrawerAdapter;
import com.vkclient.entities.DrawerMenuItem;

import java.util.ArrayList;
import java.util.List;

public abstract class VkSdkActivity extends ActionBarActivity {
    protected String profileId = VKApiConst.OWNER_ID;
    private List<DrawerMenuItem> drawerItems = new ArrayList<>();
    private ListView drawerList;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private DrawerAdapter drawerAdapter;

    public enum NavigationItem {
        PROFILE_INFO,
        NEWS,
        DIALOGS,
        FRIENDS_LIST
    }
    abstract int getLayoutResource();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VKUIHelper.onCreate(this);
        setContentView(getLayoutResource());
        onCreateDrawer();
    }

    protected void onCreateDrawer() {
        this.drawerItems.add(new DrawerMenuItem(getString(R.string.drawer_profile), NavigationItem.PROFILE_INFO));
        this.drawerItems.add(new DrawerMenuItem(getString(R.string.drawer_friends), NavigationItem.FRIENDS_LIST));
        this.drawerItems.add(new DrawerMenuItem(getString(R.string.drawer_messages), NavigationItem.DIALOGS));
        this.drawerItems.add(new DrawerMenuItem(getString(R.string.drawer_news), NavigationItem.NEWS));
        this.drawerList = (ListView) findViewById(R.id.left_drawer);
        this.drawerAdapter = new DrawerAdapter(this, this.drawerItems);
        this.drawerList.setAdapter(this.drawerAdapter);
        this.drawerList.setOnItemClickListener(this.drawerListener);
        this.drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        this.toggle = new ActionBarDrawerToggle(this, this.drawerLayout, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(getTitle());
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(R.string.drawer_open);
            }
        };
        this.drawerLayout.setDrawerListener(toggle);
        setupActionBar();
    }

    private void setupActionBar() {
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(true);
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_drawer);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.toggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        super.onResume();
        VKUIHelper.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VKUIHelper.onDestroy(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        VKUIHelper.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (this.toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startApiCall(Class<?> cls) {
        Intent i = new Intent(this, cls);
        startActivity(i);
    }

    private ListView.OnItemClickListener drawerListener = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (drawerAdapter.getItem(position).getKey()) {
                case PROFILE_INFO: {
                    startApiCall(ProfileActivity.class);
                    break;
                }
                case NEWS: {
                    startApiCall(NewsActivity.class);
                    break;
                }
                case DIALOGS: {
                    startApiCall(DialogsActivity.class);
                    break;
                }
                case FRIENDS_LIST: {
                    startApiCall(FriendsListActivity.class);
                    break;
                }
            }
        }
    };

}
