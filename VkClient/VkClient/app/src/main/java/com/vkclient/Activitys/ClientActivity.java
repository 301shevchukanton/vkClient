package com.vkclient.Activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.podkaifom.vkclient.R;
import com.vk.sdk.VKUIHelper;

public class ClientActivity extends Activity {

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_client, menu);
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        VKUIHelper.onActivityResult(this, requestCode, resultCode, data);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
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
