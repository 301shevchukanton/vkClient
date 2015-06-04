package com.vkclient.Activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vkclient.Classes.User;
import com.example.podkaifom.vkclient.R;
import com.squareup.picasso.Picasso;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vkclient.Classes.myRequests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ProfileActivity extends Activity {
    private String profileId=null;
    private VKRequest currentRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        profileId=getIntent().getStringExtra("id");
       Log.d("profid", "profileid " + profileId);
        startLoading();
        super.onCreate(savedInstanceState);
        VKUIHelper.onCreate(this);
        setContentView(R.layout.activity_profile);
        findViewById(R.id.relLayout).setVisibility(View.VISIBLE);


        findViewById(R.id.friendsButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startFriendsApiCall();
            }
        });
        findViewById(R.id.sendMessageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSendMessageApiCall();
            }
        });
        findViewById(R.id.profileWallPost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startWallPostApiCall();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        VKUIHelper.onResume(this);
        //....
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        VKUIHelper.onActivityResult(this, requestCode, resultCode, data);
        //....
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VKUIHelper.onDestroy(this);
        //....
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
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
    private void startFriendsApiCall() {
        Intent i = new Intent(this, FriendsListActivity.class);
        i.putExtra("id",profileId);
        Log.d("profid", "profileidSended " + profileId);
        startActivity(i);
    }
    private void startSendMessageApiCall() {
        Intent i = new Intent(this, SendMessageActivity.class);
        i.putExtra("id",profileId);
        Log.d("profid", "profileidSended " + profileId);
        startActivity(i);
    }
    private void startWallPostApiCall() {
        Intent i = new Intent(this, WallPostActivity.class);
        i.putExtra("id",profileId);
        Log.d("profid", "profileidSended " + profileId);
        startActivity(i);
    }

    private void startLoading() {
        if (currentRequest != null) {
            currentRequest.cancel();
        }
        Log.d("profid", "onComplete " + profileId);
        currentRequest = myRequests.getFullUserById(profileId);
        currentRequest.executeWithListener(new VKRequest.VKRequestListener() {
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
        });
    }


}