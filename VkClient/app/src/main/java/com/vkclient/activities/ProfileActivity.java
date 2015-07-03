package com.vkclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vkclient.entities.AbstractRequestListener;
import com.vkclient.entities.User;
import com.example.podkaifom.vkclient.R;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vkclient.entities.RequestCreator;
import com.vkclient.supports.JsonResponseParser;
import com.vkclient.supports.Loger;
import com.vkclient.supports.PhotoLoader;

import org.json.JSONException;
import org.json.JSONObject;


public class ProfileActivity extends VkSdkActivity {

    private VKRequest currentRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        profileId=getIntent().getStringExtra("id");
        Loger.logDebug("profid", "profileid " + profileId);
        startLoading();
        super.onCreate(savedInstanceState);
        VKUIHelper.onCreate(this);
        setContentView(R.layout.activity_profile);
        super.onCreateDrawer();
        findViewById(R.id.drawer_layout).setVisibility(View.VISIBLE);
        findViewById(R.id.btProfileFriends).setOnClickListener(profileClickListener);
        findViewById(R.id.btSendMessage).setOnClickListener(profileClickListener);
        findViewById(R.id.btWallPost).setOnClickListener(profileClickListener);
        findViewById(R.id.ivProfilePhoto).setOnClickListener(profileClickListener);
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
         Loger.logDebug("profid", "onComplete " + profileId);
        currentRequest = RequestCreator.getFullUserById(profileId);
        currentRequest.executeWithListener(new ProfileRequestListener());
    }
    public final class ProfileRequestListener extends AbstractRequestListener {
        @Override
        public void onComplete(VKResponse response) {
            super.onComplete(response);
            Loger.logDebug("profid", "onComplete " + response);
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
        private void setLayoutState(String data, int viewId, int layoutId){
            if(data!=null&&!data.equals("")) setViewText(viewId, data);
            else hideLayout(layoutId);
        }
        private void setLayoutState(String data, int viewId){
            if(data!=null) setViewText(viewId, data);
        }
        private void setLayoutsVisibility(User u)
        {
            setLayoutState(u.getName(),R.id.tvProfileName);
            setLayoutState(u.getStatus(),R.id.tvProfileStatus);
            setLayoutState(u.getBdDateString(),R.id.tvProfileBirthDate,R.id.llBirthDate);
            setLayoutState(u.getCity(),R.id.tvProfileTown,R.id.llHomeTown);
            setLayoutState(u.getRelationship(),R.id.tvRelationship,R.id.llRelationships);
            setLayoutState(u.getUnivers(), R.id.tvStudiedAt, R.id.llStudiedAt);
            setLayoutState(u.getLangs(), R.id.tvLanguages, R.id.llLanguages);
        }
        private void setUserInfo(VKResponse response) {
            try {
                JSONObject r = response.json.getJSONArray("response").getJSONObject(0);
                Loger.logDebug("responseshit",response.json.getString("response"));
                User user = JsonResponseParser.parseUserFromJSON(r);
                setLayoutsVisibility(user);
                profileId = String.valueOf(user.getId());
                if(user.getPhoto()!=null) {
                    PhotoLoader.loadPhoto(getApplicationContext(), user.getPhoto(), (ImageView) findViewById(R.id.ivProfilePhoto));
                }
            } catch (JSONException e) {
                Log.e(e.getMessage(), e.toString());
            }
        }
    }
    private final View.OnClickListener profileClickListener = new View.OnClickListener(){
        @Override
        public void onClick(final View v) {
            Class activityClass = getActivityClassForId(v.getId());
            if(activityClass != null)
                startActivityCall(getActivityClassForId(v.getId()));
        }
        private Class getActivityClassForId(int id) {
            switch (id) {
                case R.id.btProfileFriends:
                    return FriendsListActivity.class;
                case R.id.btSendMessage:
                    return SendMessageActivity.class;
                case R.id.btWallPost:
                    return WallPostActivity.class;
                case R.id.ivProfilePhoto: {
                    startActivityCall(PhotoViewActivity.class, profileId);
                    return null;
                }
                default:
                    return null;
            }
        }
    };
}
