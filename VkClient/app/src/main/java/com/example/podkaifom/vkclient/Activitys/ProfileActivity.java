package com.example.podkaifom.vkclient.Activitys;

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

import com.example.podkaifom.vkclient.Classes.User;
import com.example.podkaifom.vkclient.R;
import com.squareup.picasso.Picasso;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

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
        ((RelativeLayout)findViewById(R.id.relLayout)).setVisibility(View.VISIBLE);


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
        currentRequest = VKApi.users().get(VKParameters.from(VKApiConst.USER_IDS, profileId, VKApiConst.FIELDS,
                "id,first_name,last_name,bdate,city,photo_200,online," +
                        "online_mobile,lists,domain,has_mobile,contacts,connections,site,education," +
                        "universities,schools,can_post,can_see_all_posts,can_see_audio,can_write_private_message," +
                        "status,last_seen,common_count,relation,relatives,counters,langs,personal"));
        currentRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                Log.d("profid", "onComplete " + response);
                setUserInfo(response);
            }

            private void setUserInfo(VKResponse response) {
                try {
                    JSONObject r = response.json.getJSONArray("response").getJSONObject(0);
                    profileId=r.getString("id");
                    if (r.getString("last_name") != null && r.getString("first_name") != null)
                        ((TextView) findViewById(R.id.nameText)).setText(r.getString("first_name") + " " + r.getString("last_name"));
                    if (r.getString("status") != null)
                        ((TextView) findViewById(R.id.statusText)).setText(r.getString("status"));
                    try{((TextView) findViewById(R.id.bdText)).setText(r.getString("bdate"));}
                   catch(Exception e) {
                       findViewById(R.id.bdLayout).setVisibility(View.GONE);}
                    try{((TextView) findViewById(R.id.townText)).setText(r.getJSONObject("city").getString("title"));}
                        catch(Exception e) { findViewById(R.id.homeTownLayout).setVisibility(View.GONE);}
                    try{((TextView) findViewById(R.id.relationshipText)).setText(User.relationshipStatus[Integer.parseInt(r.getString("relation"))]);}
                        catch(Exception e) { findViewById(R.id.relationshipLayout).setVisibility(View.GONE);}
                    try{((TextView) findViewById(R.id.studiedAtText)).setText((r.getJSONArray("universities").getJSONObject(0).getString("name")));}
                        catch(Exception e) { findViewById(R.id.studiedAtLayout).setVisibility(View.GONE);}
                     try{((TextView) findViewById(R.id.languagesText)).setText(getLangs(r));}
                     catch(Exception e)    { findViewById(R.id.langLayout).setVisibility(View.GONE);}
                    try{  Picasso.with(getApplicationContext())
                            .load(r.getString("photo_200"))
                            .into((ImageView) findViewById(R.id.profilePhoto));}
                        catch(Exception e){};
                } catch (JSONException e) {
                    Log.e(e.getMessage(), e.toString());
                }
            }

            private String getLangs(JSONObject r) {
                try {
                    String langs = "";
                    JSONArray langsArray = r.getJSONObject("personal").getJSONArray("langs");
                    for (int i = 0; i < langsArray.length(); i++)
                        langs += langsArray.getString(i) + "; ";
                    return langs;
                } catch (JSONException e) {
                    return "-";
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