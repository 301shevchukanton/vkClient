package com.vkclient.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.podkaifom.vkclient.R;
import com.vkclient.adapters.FriendListAdapter;
import com.vkclient.entities.User;
import com.vkclient.supports.PhotoLoader;

import org.joda.time.format.DateTimeFormat;

public class FriendsListItemView extends LinearLayout {
    private ImageView friendPhoto;
    private FriendListAdapter.OnPhotoClickListener photoClickListener;
    private TextView friendName;
    private TextView friendBirthDate;
    private TextView friendCity;
    private User currentFriend;

    public FriendsListItemView(Context context) {
        this(context, null);
    }

    public FriendsListItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FriendsListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.friends_list_item, this);
        findViews();
    }

    public void setOnPhotoClickListener(FriendListAdapter.OnPhotoClickListener photoClickListener) {
        this.photoClickListener = photoClickListener;
    }

    private void findViews() {
        this.friendName = (TextView) findViewById(R.id.tvFriendName);
        this.friendPhoto = (ImageView) findViewById(R.id.ivFriendPhoto);
        this.friendBirthDate = (TextView) findViewById(R.id.tvFriendBirthDate);
        this.friendCity = (TextView) findViewById(R.id.tvFriendCity);
    }

    public void setFriend(final User friend) {
        this.friendName.setText(friend.getName());
        this.currentFriend = friend;
        this.friendPhoto.setOnClickListener(photoViewListener);
        if (friend.getCity() != null) {
            this.friendCity.setText(friend.getCity() + ", ");
        }
        friendPhoto.setImageResource(R.drawable.ic_user);
        PhotoLoader.loadPhoto(getContext(), friend.getPhoto(), friendPhoto);
        String birthDateString = friend.getBirthDate() != null ? friend.getBirthDate().toString(DateTimeFormat.forPattern(friend.getDateFormat())) :
                String.valueOf(R.string.not_set);
        if (birthDateString.contains(".")) {
            this.friendBirthDate.setText(birthDateString);
        }
    }

    private ImageView.OnClickListener photoViewListener = new ImageView.OnClickListener() {
        public void onClick(View v) {
            if (FriendsListItemView.this.photoClickListener != null) {
                photoClickListener.onClick(String.valueOf(currentFriend.getId()));
            }
        }
    };

}
