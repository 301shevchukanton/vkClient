package com.vkclient.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.podkaifom.vkclient.R;
import com.vkclient.entities.PhotoFeed;
import com.vkclient.supports.PhotoLoader;

import java.util.List;

public class PhotoFeedAdapter extends ArrayAdapter<PhotoFeed> {
    public PhotoFeedAdapter(Context context, List<PhotoFeed> models) {
        super(context, 0, models);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = (convertView != null ? convertView : inflater.inflate(R.layout.photo_feed_item, null));
        final PhotoFeed photoFeed = getItem(position);
        ImageView photo = ((ImageView) view.findViewById(R.id.ivPhotoFeedImage));
        if (!photoFeed.getPhotoLarge().isEmpty() && photoFeed.getPhotoLarge() != null) {
            PhotoLoader.loadPhoto(getContext(), photoFeed.getPhotoLarge(), photo);
        }
        return view;
    }
}
