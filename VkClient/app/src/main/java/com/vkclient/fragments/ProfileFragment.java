package com.vkclient.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.podkaifom.vkclient.R;
import com.meetme.android.horizontallistview.HorizontalListView;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vkclient.activities.FriendsListActivity;
import com.vkclient.activities.PhotoViewActivity;
import com.vkclient.activities.SendMessageActivity;
import com.vkclient.activities.WallPostActivity;
import com.vkclient.adapters.PhotoFeedAdapter;
import com.vkclient.entities.PhotoFeed;
import com.vkclient.entities.User;
import com.vkclient.listeners.AbstractRequestListener;
import com.vkclient.parsers.PhotoFeedParser;
import com.vkclient.parsers.UserParser;
import com.vkclient.supports.Logger;
import com.vkclient.supports.PhotoLoader;
import com.vkclient.supports.RequestCreator;
import com.vkclient.views.ProfileInfoLayoutView;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    private List<PhotoFeed> usersPhoto = new ArrayList<>();
    private PhotoFeedAdapter listAdapter;
    private VKRequest currentRequest;
    private VKRequest profileInfoRequest;
    private VKRequest profilePhotoRequest;
    private String profileId;
    public HorizontalListView listView;
    private TextView userName;
    private TextView userStatus;
    private ProfileInfoLayoutView birthDateView;
    private ProfileInfoLayoutView townView;
    private ProfileInfoLayoutView relationshipsView;
    private ProfileInfoLayoutView educationView;
    private ProfileInfoLayoutView languagesView;
    private ImageView profilePhoto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewHierarchy = inflater.inflate(R.layout.fragment_profile, container, false);
        findProfileInfoViews(viewHierarchy);
        setUserInfoCaptions();
        this.listAdapter = new PhotoFeedAdapter(getActivity(), this.usersPhoto, R.layout.photo_feed_item, R.id.ivPhotoFeedImage);
        this.listView.setOnItemClickListener(photoFeedClickListener);
        this.listView.setAdapter(this.listAdapter);
        profileId = getActivity().getIntent().getStringExtra("id");
        startLoading();
        setupOnClickListeners(profileClickListener, viewHierarchy);
        return viewHierarchy;
    }

    private void findProfileInfoViews(View viewHierarchy) {
        this.listView = (HorizontalListView) viewHierarchy.findViewById(R.id.lvPhotoFeed);
        this.userName = (TextView) viewHierarchy.findViewById(R.id.tvProfileName);
        this.userStatus = (TextView) viewHierarchy.findViewById(R.id.tvProfileStatus);
        this.birthDateView = (ProfileInfoLayoutView) viewHierarchy.findViewById(R.id.piBirthDate);
        this.townView = (ProfileInfoLayoutView) viewHierarchy.findViewById(R.id.piTown);
        this.relationshipsView = (ProfileInfoLayoutView) viewHierarchy.findViewById(R.id.piRelationships);
        this.educationView = (ProfileInfoLayoutView) viewHierarchy.findViewById(R.id.piEducation);
        this.languagesView = (ProfileInfoLayoutView) viewHierarchy.findViewById(R.id.piLanguages);
        this.profilePhoto = (ImageView) viewHierarchy.findViewById(R.id.ivProfilePhoto);
    }

    private void setUserInfoCaptions() {
        this.birthDateView.setCaption(getString(R.string.birthday_profile_label));
        this.townView.setCaption(getString(R.string.hometown_profile_label));
        this.relationshipsView.setCaption(getString(R.string.relationship_status_label));
        this.educationView.setCaption(getString(R.string.studied_at_profile_label));
        this.languagesView.setCaption(getString(R.string.languages_profile_label));
    }

    private void setupOnClickListeners(View.OnClickListener profileClickListener, View container) {
        container.findViewById(R.id.btProfileFriends).setOnClickListener(profileClickListener);
        container.findViewById(R.id.btSendMessage).setOnClickListener(profileClickListener);
        container.findViewById(R.id.btWallPost).setOnClickListener(profileClickListener);
        container.findViewById(R.id.ivProfilePhoto).setOnClickListener(profileClickListener);
    }

    private final AdapterView.OnItemClickListener photoFeedClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {
            final PhotoFeed photoFeed = usersPhoto.get(position);
            photoViewCall(photoFeed.getPhotoLarge());
        }
    };

    private void photoViewCall(String photoUrl) {
        Intent i = new Intent(getActivity(), PhotoViewActivity.class);
        i.putExtra("photo", photoUrl);
        startActivity(i);
    }

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

    private void startActivityCall(Class<?> cls) {
        Intent i = new Intent(getActivity(), cls);
        i.putExtra("id", profileId);
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
    private final AbstractRequestListener profileRequestListener = new AbstractRequestListener() {
        @Override
        public void onComplete(VKResponse response) {
            super.onComplete(response);
            Logger.logDebug("profid", "onComplete " + response);
            setUserInfo(response);
        }

        private void setViewText(String data, ProfileInfoLayoutView profileInfoView) {
            if (!TextUtils.isEmpty(data)) {
                profileInfoView.setValue(data);
            } else {
                profileInfoView.setVisibility(View.GONE);
            }
        }

        private void setUserInfo(VKResponse response) {
            User user = new UserParser().parse(response);
            setViewsData(user);
            profileId = String.valueOf(user.getId());
            if (user.getPhoto() != null) {
                PhotoLoader.loadPhoto(getActivity().getApplicationContext(), user.getPhoto(), profilePhoto);
            }
        }

        private void setViewsData(User user) {
            userName.setText(user.getName());
            userStatus.setText(user.getStatus());
            setViewText(user.getBdDateString(), birthDateView);
            setViewText(user.getCity(), townView);
            setViewText(user.getRelationship(), relationshipsView);
            setViewText(user.getUnivers(), educationView);
            setViewText(user.getLangs(), languagesView);
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

}