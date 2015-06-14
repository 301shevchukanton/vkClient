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

import org.json.JSONException;
import org.json.JSONObject;


public class ProfileActivity extends VkSdkActivity {

    private VKRequest currentRequest;
    public final class ProfileRequestListener extends VKRequest.VKRequestListener {
        @Override
        public void onComplete(VKResponse response) {
            super.onComplete(response);
            Log.d("profid", "onComplete " + response);
            setUserInfo(response);
        }
        void setLayoutsVisibility(User u)
        {
            ((TextView) findViewById(R.id.nameText)).setText(u.getName());
            if(u.getStatus()!=null) ((TextView) findViewById(R.id.statusText)).setText(u.getStatus());
            if(u.getBdDateString()!=null) ((TextView) findViewById(R.id.bdText)).setText(u.getBdDateString());
            else  findViewById(R.id.bdLayout).setVisibility(View.GONE);
            if(u.getCity()!=null) ((TextView) findViewById(R.id.townText)).setText(u.getCity());
            else findViewById(R.id.homeTownLayout).setVisibility(View.GONE);
            if(u.getRelationship()!=null) ((TextView) findViewById(R.id.relationshipText)).setText(u.getRelationship());
            else findViewById(R.id.relationshipLayout).setVisibility(View.GONE);
            if(u.getUnivers()!=null) ((TextView)findViewById(R.id.studiedAtText)).setText(u.getUnivers());
            else findViewById(R.id.studiedAtLayout).setVisibility(View.GONE);
            if(u.getLangs()!=null) ((TextView)findViewById(R.id.languagesText)).setText(u.getLangs());
            else findViewById(R.id.langLayout).setVisibility(View.GONE);
        }
        private void setUserInfo(VKResponse response) {
            try {
                JSONObject r = response.json.getJSONArray("response").getJSONObject(0);
                User temp = User.parseUserFromJSON(r);
                setLayoutsVisibility(temp);
                profileId = r.getString("id");
                try {
                    Picasso.with(getApplicationContext())
                            .load(r.getString("photo_200"))
                            .into((ImageView) findViewById(R.id.profilePhoto));
                } catch (Exception e) {
                }
                ;
            } catch (JSONException e) {
                Log.e(e.getMessage(), e.toString());
            }
        }
        @Override
        public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
            super.attemptFailed(request, attemptNumber, totalAttempts);
            Log.d("VkDemoApp", "attemptFailed " + request + " " + attemptNumber + " " + totalAttempts);
        }

        @Override
        public void onError(VKError error) {
            super.onError(error);
            Log.d("VkDemoApp", "onError: " + error);
        }

        @Override
        public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
            super.onProgress(progressType, bytesLoaded, bytesTotal);
            Log.d("VkDemoApp", "onProgress " + progressType + " " + bytesLoaded + " " + bytesTotal);
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
        Log.d("profid", "profileid " + profileId);
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
        Log.d("profid", "onComplete " + profileId);
        currentRequest = RequestCreator.getFullUserById(profileId);
        currentRequest.executeWithListener(new ProfileRequestListener());
    }


}