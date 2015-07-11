package com.vkclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.podkaifom.vkclient.R;
import com.meetme.android.horizontallistview.HorizontalListView;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vkclient.adapters.PhotoFeedAdapter;
import com.vkclient.entities.PhotoFeed;
import com.vkclient.entities.User;
import com.vkclient.listeners.AbstractRequestListener;
import com.vkclient.parsers.PhotoFeedParser;
import com.vkclient.parsers.UserParser;
import com.vkclient.supports.Logger;
import com.vkclient.supports.PhotoLoader;
import com.vkclient.supports.RequestCreator;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends VkSdkActivity {
    private HorizontalListView listView;
    private List<PhotoFeed> usersPhoto = new ArrayList<>();
    private PhotoFeedAdapter listAdapter;
    private VKRequest currentRequest;
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
            this.listAdapter.notifyDataSetChanged();
        } else if (VKSdk.wakeUpSession()) {
            startLoading();
        }
        this.listAdapter = new PhotoFeedAdapter(this, this.usersPhoto, R.layout.photo_feed_item, R.id.ivPhotoFeedImage);
        this.listView.setOnItemClickListener(this.photoFeedClickListener);
        this.listView.setAdapter(this.listAdapter);
        findViewById(R.id.drawer_layout).setVisibility(View.VISIBLE);
        setupOnClickListeners(profileClickListener);
    }

    private void setupOnClickListeners(View.OnClickListener profileClickListener) {
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

    private void startLoading() {
        if (this.profileInfoRequest != null) {
            this.profileInfoRequest.cancel();
        }
        Logger.logDebug("profid", "onComplete " + profileId);
        this.profileInfoRequest = RequestCreator.getFullUserById(profileId);
        this.profileInfoRequest.executeWithListener(this.profileRequestListener);
        this.profilePhotoRequest = RequestCreator.getPhotosOfUser(profileId);
        this.profilePhotoRequest.executeWithListener(this.getPhotoFeedRequestListener);
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
            User user = new UserParser().parse(response);
            setLayoutsVisibility(user);
            profileId = String.valueOf(user.getId());
            if (user.getPhoto() != null) {
                PhotoLoader.loadPhoto(getApplicationContext(), user.getPhoto(), (ImageView) findViewById(R.id.ivProfilePhoto));
            }
        }
    };

    private final AbstractRequestListener getPhotoFeedRequestListener = new AbstractRequestListener() {
        @Override
        public void onComplete(final VKResponse response) {
            super.onComplete(response);
            usersPhoto.clear();
            usersPhoto.addAll(new PhotoFeedParser().getPhotoFeedList(response));
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
                    startUserPhotoViewCall(profileId);
                    return null;
                }
                default:
                    return null;
            }
        }
    };

    public void startUserPhotoViewCall(String userId) {
        if (this.currentRequest != null) {
            this.currentRequest.cancel();
        }
        this.currentRequest = RequestCreator.getUserById(userId);
        this.currentRequest.executeWithListener(this.bigPhotoRequestListener);
    }

    private void photoViewCall(String photoUrl) {
        Intent i = new Intent(getApplicationContext(), PhotoViewActivity.class);
        i.putExtra("photo", photoUrl);
        startActivity(i);
    }

    private AbstractRequestListener bigPhotoRequestListener = new AbstractRequestListener() {
        @Override
        public void onComplete(VKResponse response) {
            super.onComplete(response);
            setPhoto(response);
        }

        private void setPhoto(VKResponse response) {
            photoViewCall(new UserParser().parseUserName(response).getPhotoMax());
        }
    };
    private final AdapterView.OnItemClickListener photoFeedClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {
            final PhotoFeed photoFeed = usersPhoto.get(position);
            photoViewCall(photoFeed.getPhotoLarge());
        }
    };


}
