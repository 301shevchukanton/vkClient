package com.vkclient.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vkclient.entities.User;
import com.example.podkaifom.vkclient.R;
import com.squareup.picasso.Picasso;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vkclient.entities.RequestCreator;
import com.vkclient.supports.Loger;

import org.json.JSONException;
import org.json.JSONObject;


public class ProfileActivity extends VkSdkActivity {

    private VKRequest currentRequest;
    public final class ProfileRequestListener extends VKRequest.VKRequestListener {
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
            setViewText(R.id.nameText,u.getName());
            setViewText(R.id.languagesText,u.getLangs());
            if(u.getStatus()!=null)  setViewText(R.id.statusText, u.getStatus());
            if(u.getBdDateString()!=null) setViewText(R.id.bdText, u.getBdDateString());
            else hideLayout(R.id.bdLayout);
            if(u.getCity()!=null) setViewText(R.id.townText, u.getCity());
            else hideLayout(R.id.homeTownLayout);
            if(u.getRelationship()!=null) setViewText(R.id.relationshipText, u.getRelationship());
            else hideLayout(R.id.relationshipLayout);
            if(u.getUnivers()!=null) setViewText(R.id.studiedAtText, u.getUnivers());
            else hideLayout(R.id.studiedAtLayout);
            if(u.getLangs()!=null) setViewText(R.id.languagesText, u.getLangs());
            else hideLayout(R.id.langLayout);
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
                            .into((ImageView) findViewById(R.id.profilePhoto));
                }
            } catch (JSONException e) {
                Log.e(e.getMessage(), e.toString());
            }
        }
        @Override
        public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
            super.attemptFailed(request, attemptNumber, totalAttempts);
             Loger.log("VkDemoApp", "attemptFailed " + request + " " + attemptNumber + " " + totalAttempts);
        }

        @Override
        public void onError(VKError error) {
            super.onError(error);
             Loger.log("VkDemoApp", "onError: " + error);
        }

        @Override
        public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
            super.onProgress(progressType, bytesLoaded, bytesTotal);
             Loger.log("VkDemoApp", "onProgress " + progressType + " " + bytesLoaded + " " + bytesTotal);
        }
    }
    public final class ProfileClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(final View v)
        {
            if(v==findViewById(R.id.friendsButton))    startActivityCall(FriendsListActivity.class);
            if(v==findViewById(R.id.sendMessageButton))startActivityCall(SendMessageActivity.class);
            if(v==findViewById(R.id.profileWallPost))  startActivityCall(WallPostActivity.class);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        profileId=getIntent().getStringExtra("id");
         Loger.log("profid", "profileid " + profileId);
        startLoading();
        super.onCreate(savedInstanceState);
        VKUIHelper.onCreate(this);
        setContentView(R.layout.activity_profile);
        findViewById(R.id.relLayout).setVisibility(View.VISIBLE);
        findViewById(R.id.friendsButton).setOnClickListener(new ProfileClickListener());
        findViewById(R.id.sendMessageButton).setOnClickListener(new ProfileClickListener());
        findViewById(R.id.profileWallPost).setOnClickListener(new ProfileClickListener());
    }

    private void startActivityCall(Class <?> cls)
    {
        Intent i = new Intent(this, cls);
        i.putExtra("id",profileId);
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


}