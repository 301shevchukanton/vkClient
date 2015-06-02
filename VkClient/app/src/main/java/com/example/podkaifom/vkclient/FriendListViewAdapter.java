package com.example.podkaifom.vkclient;

/**
 * Created by pod kaifom on 01.06.2015.
 */
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class FriendListViewAdapter extends ArrayAdapter<User> {
    private List<User> mModels;

    public Object getItems() {
        return mModels;
    }
    public FriendListViewAdapter(Context context, List<User> models)
    {
        super(context, R.layout.friends_list_item, R.id.text1, models);
        mModels = models;
    }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            final User user = getItem(position);
            ((TextView) view.findViewById(R.id.text1)).setText(user.getName());
            ((ImageView)view.findViewById(R.id.friend_photo)).setImageResource(R.drawable.user_100);
            // try { new ImageManager().fetchImage(user.getPhoto(), (ImageView) findViewById(R.id.friend_photo));}
            try{    new DownloadImageTask((ImageView)view.findViewById(R.id.friend_photo)).execute(user.getPhoto());}
            catch (Exception e) {}
            String birthDateStr = "Не задано";

            DateTime dt = user.getBirthDate();

            if (dt != null) {
                birthDateStr = dt.toString(DateTimeFormat.forPattern(user.getDateFormat()));
            }
            ((TextView) view.findViewById(R.id.text2)).setText(birthDateStr);

            return view;

        }
    }

