package com.vkclient.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.podkaifom.vkclient.R;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKApiConst;

public abstract class VkSdkActivity extends ActionBarActivity {
    protected String profileId= VKApiConst.OWNER_ID;
    private String[] drawerTitles;
    private ListView drawerList;

    protected void onCreateDrawer() {
        drawerTitles = getResources().getStringArray(R.array.drawer_menu);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        drawerList.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
                R.layout.drawer_list_item, drawerTitles));
        drawerList.setOnItemClickListener(drawerListener);
        getSupportActionBar().hide();
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

    private void startApiCall(Class <?> cls){
        Intent i = new Intent(this, cls);
        startActivity(i);
    }

    private ListView.OnItemClickListener drawerListener = new ListView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            switch (((TextView) view).getText().toString()) {
                case "My Profile": {
                    startApiCall(ProfileActivity.class);
                    break;
                }
                case "Messages": {
                    startApiCall(DialogsActivity.class);
                    break;
                }
                case "Friends": {
                    startApiCall(FriendsListActivity.class);
                    break;
                }
                case "News feed": {
                    startApiCall(NewsActivity.class);
                    break;
                }
            }
        }
    };

}
