package com.vkclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.podkaifom.vkclient.R;
import com.vk.sdk.VKUIHelper;

public class ClientActivity extends VkSdkActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VKUIHelper.onCreate(this);
        setContentView(R.layout.activity_client);
       findViewById(R.id.btGetUserInfo).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startApiCall(ProfileActivity.class);
           }
       });

        findViewById(R.id.btGetFriends).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startApiCall(FriendsListActivity.class);
            }
        });
        findViewById(R.id.btGetDialogs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startApiCall(DialogsActivity.class);
            }
        });

    }

    private void startApiCall(Class <?> cls){
        Intent i = new Intent(this, cls);
        startActivity(i);

    }
}
