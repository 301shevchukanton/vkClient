package com.vkclient.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.podkaifom.vkclient.R;
import com.vkclient.activities.FriendsListActivity;
import com.vkclient.activities.PhotoViewActivity;
import com.vkclient.entities.User;
import com.squareup.picasso.Picasso;
import com.vkclient.supports.PhotoLoader;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class FriendListAdapter extends ArrayAdapter<User> {
    private List<User> mModels;

    public Object getItems() {
        return mModels;
    }

    public interface OnPhotoClickListener {
        void onClick(String photoUrl);
    }

    private OnPhotoClickListener photoClickListener;

    public void setOnPhotoClickListener(OnPhotoClickListener photoClickListener) {
        this.photoClickListener = photoClickListener;
    }

    public FriendListAdapter(Context context, List<User> models) {
        super(context, R.layout.friends_list_item, R.id.tvFriendName, models);
        mModels = models;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        ImageView friendPhoto = (ImageView) view.findViewById(R.id.ivFriendPhoto);
        final User user = getItem(position);
        ((TextView) view.findViewById(R.id.tvFriendName)).setText(user.getName());
        friendPhoto.setOnClickListener(
                new ImageView.OnClickListener() {
                    public void onClick(View v) {
                        if (FriendListAdapter.this.photoClickListener != null) {
                            photoClickListener.onClick(String.valueOf(user.getId()));
                        }
                    }
                }
        );
        friendPhoto.setImageResource(R.drawable.ic_user100);
        PhotoLoader.loadPhoto(getContext(), user.getPhoto(), friendPhoto);
        String birthDateStr = String.valueOf(R.string.not_set);
        DateTime dt = user.getBirthDate();
        if (dt != null) {
            birthDateStr = dt.toString(DateTimeFormat.forPattern(user.getDateFormat()));
        }
        ((TextView) view.findViewById(R.id.tvFriendBirthDate)).setText(birthDateStr);
        return view;
    }

}

