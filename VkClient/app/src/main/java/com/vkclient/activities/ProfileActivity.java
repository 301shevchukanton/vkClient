package com.vkclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vkclient.entities.RequestListenerMaster;
import com.vkclient.entities.User;
import com.example.podkaifom.vkclient.R;
import com.squareup.picasso.Picasso;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vkclient.entities.RequestCreator;
import com.vkclient.supports.Loger;

import org.json.JSONException;
import org.json.JSONObject;


public class ProfileActivity extends VkSdkActivity {

    private VKRequest currentRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        profileId=getIntent().getStringExtra("id");
         Loger.log("profid", "profileid " + profileId);
        startLoading();
        super.onCreate(savedInstanceState);
        VKUIHelper.onCreate(this);
        setContentView(R.layout.activity_profile);
        findViewById(R.id.relLayout).setVisibility(View.VISIBLE);
        findViewById(R.id.btProfileFriends).setOnClickListener(new ProfileClickListener());
        findViewById(R.id.btSendMessage).setOnClickListener(new ProfileClickListener());
        findViewById(R.id.btWallPost).setOnClickListener(new ProfileClickListener());
        findViewById(R.id.ivProfilePhoto).setOnClickListener(new ProfileClickListener());
    }

    private void startActivityCall(Class <?> cls)
    {
        Intent i = new Intent(this, cls);
        i.putExtra("id", profileId);
        startActivity(i);
    }
    public void startActivityCall(Class <?> cls,String date){
        Intent i = new Intent(this, cls);
        i.putExtra("photo",date);
        startActivity(i);
    }
    private void startLoading() {
        if (currentRequest != null) {
            currentRequest.cancel();
        }
         Loger.log("profid", "onComplete " + profileId);
        currentRequest = RequestCreator.getFullUserById(profileId);
        currentRequest.executeWithListener(new ProfileRequestListener());
    }
    public final class ProfileRequestListener extends RequestListenerMaster {
        @Override
        public void onComplete(VKResponse response) {
            super.onComplete(response);
            Loger.log("profid", "onComplete " + response);
            setUserInfo(response);
        }
        private void setViewText(int id, String text)
        {
            ((TextView) findViewById(id)).setText(text);
        }
        private void hideLayout(int id)
        {
            findViewById(id).setVisibility(View.GONE);
        }
        private void setLayoutsVisibility(User u)
        {
            setViewText(R.id.tvProfileName, u.getName());
            setViewText(R.id.tvLanguages, u.getLangs());
            if(u.getStatus()!=null)  setViewText(R.id.tvProfileStatus, u.getStatus());
            if(u.getBdDateString()!=null) setViewText(R.id.tvProfileBirthDate, u.getBdDateString());
            else hideLayout(R.id.llBirthDate);
            if(u.getCity()!=null) setViewText(R.id.tvProfileTown, u.getCity());
            else hideLayout(R.id.llHomeTown);
            if(u.getRelationship()!=null) setViewText(R.id.tvRelationship, u.getRelationship());
            else hideLayout(R.id.llRelationships);
            if(u.getUnivers()!=null) setViewText(R.id.tvStudiedAt, u.getUnivers());
            else hideLayout(R.id.llStudiedAt);
            if(u.getLangs()!=null) setViewText(R.id.tvLanguages, u.getLangs());
            else hideLayout(R.id.llLanguages);
        }
        private void setUserInfo(VKResponse response) {
            try {
                JSONObject r = response.json.getJSONArray("response").getJSONObject(0);
                User temp = User.parseUserFromJSON(r);
                setLayoutsVisibility(temp);
                profileId = r.getString("id");
                if(r.getString("photo_200")!=null) {
                    Picasso.with(getApplicationContext())
                            .load(r.getString("photo_200"))
                            .into((ImageView) findViewById(R.id.ivProfilePhoto));
                }
            } catch (JSONException e) {
                Log.e(e.getMessage(), e.toString());
            }
        }
    }
    public final class ProfileClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(final View v)
        {
            if(v==findViewById(R.id.btProfileFriends))    startActivityCall(FriendsListActivity.class);
            if(v==findViewById(R.id.btSendMessage))startActivityCall(SendMessageActivity.class);
            if(v==findViewById(R.id.btWallPost))  startActivityCall(WallPostActivity.class);
            if(v==findViewById(R.id.ivProfilePhoto)) startActivityCall(PhotoViewActivity.class, profileId);
        }
    }


}