package com.vkclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.meetme.android.horizontallistview.HorizontalListView;
import com.vk.sdk.VKSdk;
import com.vkclient.adapters.FriendListAdapter;
import com.vkclient.adapters.PhotoFeedAdapter;
import com.vkclient.entities.AbstractRequestListener;
import com.vkclient.entities.PhotoFeed;
import com.vkclient.entities.User;
import com.example.podkaifom.vkclient.R;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vkclient.entities.RequestCreator;
import com.vkclient.parsers.PhotoFeedParser;
import com.vkclient.parsers.UserParser;
import com.vkclient.supports.Logger;
import com.vkclient.supports.PhotoLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ProfileActivity extends VkSdkActivity {
    private HorizontalListView listView;
    private List<PhotoFeed> usersPhoto = new ArrayList<>();
    private PhotoFeedAdapter listAdapter;
    private VKRequest currentRequest;
    private String photoUrl = "";
    private VKRequest profileInfoRequest;
    private VKRequest profilePhotoRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        profileId = getIntent().getStringExtra("id");
        Logger.logDebug("profid", "profileid " + profileId);
        startLoading();
        super.onCreate(savedInstanceState);
        VKUIHelper.onCreate(this);
        setContentView(R.layout.activity_profile);
        super.onCreateDrawer();
        listView = (HorizontalListView) findViewById(R.id.lvPhotoFeed);

        Object items = getLastNonConfigurationInstance();
        if (items != null) {
            this.usersPhoto = ((List<PhotoFeed>) items);
            this.listAdapter = new PhotoFeedAdapter(this, this.usersPhoto);
            listAdapter.setOnPhotoClickListener(photoClickListener);
            this.listView.setAdapter(this.listAdapter);
            this.listAdapter.notifyDataSetChanged();
        } else if (VKSdk.wakeUpSession()) {
            startLoading();
        }
        this.listAdapter = new PhotoFeedAdapter(this, this.usersPhoto);
        listAdapter.setOnPhotoClickListener(photoClickListener);
        this.listView.setAdapter(this.listAdapter);

        findViewById(R.id.drawer_layout).setVisibility(View.VISIBLE);
        findViewById(R.id.btProfileFriends).setOnClickListener(profileClickListener);
        findViewById(R.id.btSendMessage).setOnClickListener(profileClickListener);
        findViewById(R.id.btWallPost).setOnClickListener(profileClickListener);
        findViewById(R.id.ivProfilePhoto).setOnClickListener(profileClickListener);

    }

    private void startActivityCall(Class<?> cls) {
        Intent i = new Intent(this, cls);
        i.putExtra("id", profileId);
        startActivity(i);
    }

    public void startActivityCall(Class<?> cls, String date) {
        Intent i = new Intent(this, cls);
        i.putExtra("photo", date);
        startActivity(i);
    }

    private void startLoading() {
        if (profileInfoRequest != null) {
            profileInfoRequest.cancel();
        }
        Logger.logDebug("profid", "onComplete " + profileId);
        profileInfoRequest = RequestCreator.getFullUserById(profileId);
        profileInfoRequest.executeWithListener(profileRequestListener);
        profilePhotoRequest = RequestCreator.getPhotosOfUser(profileId);
        profilePhotoRequest.executeWithListener(getPhotoFeedRequestListener);
    }

    private final AbstractRequestListener profileRequestListener = new AbstractRequestListener() {
        @Override
        public void onComplete(VKResponse response) {
            super.onComplete(response);
            Logger.logDebug("profid", "onComplete " + response);
            setUserInfo(response);
        }

        private void setViewText(int id, String text) {
            ((TextView) findViewById(id)).setText(text);
        }

        private void hideLayout(int id) {
            findViewById(id).setVisibility(View.GONE);
        }

        private void setLayoutState(String data, int viewId, int layoutId) {
            if (data != null && !data.equals("")) setViewText(viewId, data);
            else hideLayout(layoutId);
        }

        private void setLayoutState(String data, int viewId) {
            if (data != null) setViewText(viewId, data);
        }

        private void setLayoutsVisibility(User u) {
            setLayoutState(u.getName(), R.id.tvProfileName);
            setLayoutState(u.getStatus(), R.id.tvProfileStatus);
            setLayoutState(u.getBdDateString(), R.id.tvProfileBirthDate, R.id.llBirthDate);
            setLayoutState(u.getCity(), R.id.tvProfileTown, R.id.llHomeTown);
            setLayoutState(u.getRelationship(), R.id.tvRelationship, R.id.llRelationships);
            setLayoutState(u.getUnivers(), R.id.tvStudiedAt, R.id.llStudiedAt);
            setLayoutState(u.getLangs(), R.id.tvLanguages, R.id.llLanguages);
        }

        private void setUserInfo(VKResponse response) {
            try {
                JSONObject r = response.json.getJSONArray("response").getJSONObject(0);
                User user = new UserParser(r).parse();
                setLayoutsVisibility(user);
                profileId = String.valueOf(user.getId());
                if (user.getPhoto() != null) {
                    PhotoLoader.loadPhoto(getApplicationContext(), user.getPhoto(), (ImageView) findViewById(R.id.ivProfilePhoto));
                }
            } catch (JSONException e) {
                Log.e(e.getMessage(), e.toString());
            }
        }
    };

    private final AbstractRequestListener getPhotoFeedRequestListener = new AbstractRequestListener() {
        @Override
        public void onComplete(final VKResponse response) {
            super.onComplete(response);
            usersPhoto.clear();
            try {
                usersPhoto.addAll(new PhotoFeedParser(response.json).getPhotoFeedList());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            listAdapter.notifyDataSetChanged();
        }
    };

    private final View.OnClickListener profileClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            Class activityClass = getActivityClassForId(v.getId());
            if (activityClass != null)
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
                    startPhotoViewCall(profileId);
                    return null;
                }
                default:
                    return null;
            }
        }
    };

    public void startPhotoViewCall(String userId) {

        if (currentRequest != null) {
            currentRequest.cancel();
        }
        currentRequest = RequestCreator.getBigUserPhoto(userId);
        currentRequest.executeWithListener(bigPhotoRequestListener);

    }

    private AbstractRequestListener bigPhotoRequestListener = new AbstractRequestListener() {
        @Override
        public void onComplete(VKResponse response) {
            super.onComplete(response);
            setPhoto(response);
        }

        private void setPhoto(VKResponse response) {
            try {
                JSONObject r = response.json.getJSONArray("response").getJSONObject(0);
                if (r.getString("photo_max_orig") != null) {
                    photoUrl = r.getString("photo_max_orig");
                    Intent i = new Intent(getApplicationContext(), PhotoViewActivity.class);
                    i.putExtra("photo", photoUrl);
                    startActivity(i);
                }
            } catch (JSONException e) {
                Log.e(e.getMessage(), e.toString());
            }
        }
    };
    private final PhotoFeedAdapter.OnPhotoClickListener photoClickListener = new PhotoFeedAdapter.OnPhotoClickListener() {
        @Override
        public void onClick(String userId) {
            Intent i = new Intent(getApplicationContext(), PhotoViewActivity.class);
            i.putExtra("photo", userId);
            startActivity(i);
        }
    };
}
