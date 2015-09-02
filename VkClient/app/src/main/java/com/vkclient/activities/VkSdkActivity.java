package com.vkclient.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;

import com.example.podkaifom.vkclient.R;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKApiConst;
import com.vkclient.fragments.NavigationPanelFragment;

public abstract class VkSdkActivity extends ActionBarActivity implements NavigationPanelFragment.Callbacks {
    private static final String ID = "id";
    protected String profileId = VKApiConst.OWNER_ID;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    abstract int getLayoutResource();

    abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VKUIHelper.onCreate(this);
        setContentView(getLayoutResource());
        profileId = getIntent().getStringExtra(ID);
        initializeFragment(new NavigationPanelFragment(), R.id.navigationPanelContainer);
        setupNavigationPanel();
        initializeFragment(createFragment(), R.id.fragmentContainer);
    }

    private void initializeFragment(Fragment fragment, int containerLayoutId) {
        FragmentManager manager = getSupportFragmentManager();
        Fragment drawerFragment = manager.findFragmentById(containerLayoutId);
        if (drawerFragment == null) {
            drawerFragment = fragment;
            manager.beginTransaction()
                    .add(containerLayoutId, drawerFragment)
                    .commit();
        }
    }

    private void setupNavigationPanel() {
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

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onDrawerItemSelected() {
        this.drawerLayout.closeDrawers();
    }

}
