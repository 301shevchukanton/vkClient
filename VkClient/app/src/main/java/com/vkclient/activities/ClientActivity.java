package com.vkclient.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
       findViewById(R.id.users_get).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startApiCall(ProfileActivity.class);
           }
       });

        findViewById(R.id.friends_get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startApiCall(FriendsListActivity.class);
            }
        });
        findViewById(R.id.getDialogsButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startApiCall(DialogsActivity.class);
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void startApiCall(Class <?> cls){
        Intent i = new Intent(this, cls);
        startActivity(i);
    }
}
