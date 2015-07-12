package com.vkclient.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.podkaifom.vkclient.R;
import com.vkclient.entities.User;
import com.vkclient.views.FriendsListItemView;

import java.util.List;

public class FriendListAdapter extends ArrayAdapter<User> {
    public interface OnPhotoClickListener {
        void onClick(String photoUrl);
    }

    private OnPhotoClickListener photoClickListener;

    public void setOnPhotoClickListener(OnPhotoClickListener photoClickListener) {
        this.photoClickListener = photoClickListener;
    }

    public FriendListAdapter(Context context, List<User> models) {
        super(context, R.layout.friends_list_item, R.id.tvFriendName, models);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new FriendsListItemView(getContext());
        }
        ((FriendsListItemView) convertView).setOnPhotoClickListener(photoClickListener);
        ((FriendsListItemView) convertView).setFriend(getItem(position));
        return convertView;
    }

}

