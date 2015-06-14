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

import com.vkclient.entities.Message;
import com.example.podkaifom.vkclient.R;
import com.squareup.picasso.Picasso;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vkclient.entities.RequestCreator;

import org.json.JSONException;
import org.json.JSONObject;


public class SendMessageActivity extends VkSdkActivity {

    private VKRequest currentRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        profileId=getIntent().getStringExtra("id");
        Log.d("profid", "profileidSended " + profileId);
        startLoading();
        super.onCreate(savedInstanceState);
        VKUIHelper.onCreate(this);
        setContentView(R.layout.activity_send_message);
        findViewById(R.id.sendMessage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message.sendMessage((TextView) findViewById(R.id.msg), profileId);
            }
        });

    }
    private void startLoading() {
        if (currentRequest != null) {
            currentRequest.cancel();
        }
        Log.d("profid", "onComplete " + profileId);
           currentRequest = RequestCreator.getFullUserById(profileId);
        currentRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                Log.d("profid", "onComplete " + response);
                setUserInfo(response);
            }

            private void setUserInfo(VKResponse response) {
                try {
                    Log.d("profid", "seting inf " + profileId);
                    JSONObject r = response.json.getJSONArray("response").getJSONObject(0);
                    if (r.getString("last_name") != null && r.getString("first_name") != null)
                        ((TextView) findViewById(R.id.msg_name)).setText(r.getString("first_name") + " " + r.getString("last_name"));
                    try{ Picasso.with(getApplicationContext())
                            .load(r.getString("photo_200"))
                            .into((ImageView)findViewById(R.id.msg_photo));}
                    catch(Exception e){};
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
        });
    }

}

