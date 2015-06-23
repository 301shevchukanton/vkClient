package com.vkclient.adapters;

import java.util.List;

import android.content.Context;
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

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class FriendListViewAdapter extends ArrayAdapter<User> {
    private List<User> mModels;
    public Object getItems() {
        return mModels;
    }
    public FriendListViewAdapter(Context context, List<User> models)
    {
        super(context, R.layout.friends_list_item, R.id.tvFriendName, models);
        mModels = models;
    }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            final User user = getItem(position);
            ((TextView) view.findViewById(R.id.tvFriendName)).setText(user.getName());
            view.findViewById(R.id.ivFriendPhoto).setOnClickListener(new ImageView.OnClickListener() {
            public void onClick(View v) {
                ((FriendsListActivity)getContext()).startApiCall(PhotoViewActivity.class, String.valueOf(user.getId()));
            }
            }
            );
                    ((ImageView) view.findViewById(R.id.ivFriendPhoto)).setImageResource(R.drawable.ic_user100);
             Picasso.with(getContext())
                    .load(user.getPhoto())
                    .into((ImageView)view.findViewById(R.id.ivFriendPhoto));
            String birthDateStr = "Не задано";
            DateTime dt = user.getBirthDate();
            if (dt != null) {
                birthDateStr = dt.toString(DateTimeFormat.forPattern(user.getDateFormat()));
            }
            ((TextView) view.findViewById(R.id.tvFriendBirthDate)).setText(birthDateStr);
            return view;

        }

    }

