package com.vkclient.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.example.podkaifom.vkclient.R;
import com.vk.sdk.VKUIHelper;

public class ClientActivity extends VkSdkActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VKUIHelper.onCreate(this);
        setContentView(R.layout.activity_client);
        super.onCreateDrawer();
        findViewById(R.id.btGetUserInfo).setOnClickListener(clientClickListener);
        findViewById(R.id.btGetFriends).setOnClickListener(clientClickListener);
        findViewById(R.id.btGetDialogs).setOnClickListener(clientClickListener);
        getSupportActionBar().hide();
    }
    private void startApiCall(Class <?> cls){
        Intent i = new Intent(this, cls);
        startActivity(i);
    }
    private final View.OnClickListener clientClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btGetFriends:{
                    startApiCall(FriendsListActivity.class);
                    break;}
                case R.id.btGetUserInfo:{
                    startApiCall(ProfileActivity.class);
                    break;}
                case R.id.btGetDialogs:{
                    startApiCall(DialogsActivity.class);
                    break;}
            }
        }};

}
