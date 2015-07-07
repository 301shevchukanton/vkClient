package com.vkclient.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.podkaifom.vkclient.R;
import com.vkclient.entities.News;
import com.vkclient.entities.PhotoFeed;
import com.vkclient.parsers.UserParser;
import com.vkclient.supports.PhotoLoader;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.List;

public class PhotoFeedAdapter extends ArrayAdapter<PhotoFeed> {

    public PhotoFeedAdapter(Context context, List<PhotoFeed> models) {
        super(context, R.layout.photo_feed_item, R.id.tvPhotoFeedText, models);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view = super.getView(position, convertView, parent);
        ImageView photo = ((ImageView) view.findViewById(R.id.ivPhotoFeedImage));
        final PhotoFeed photoFeed = getItem(position);
        if (!photoFeed.getPhotoLarge().isEmpty() && photoFeed.getPhotoLarge() != null) {
            PhotoLoader.loadPhoto(getContext(), photoFeed.getPhotoLarge(), photo);
        }

        return view;
    }

}
