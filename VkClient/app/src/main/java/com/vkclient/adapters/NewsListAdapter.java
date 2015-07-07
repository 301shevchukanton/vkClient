package com.vkclient.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.podkaifom.vkclient.R;
import com.vkclient.entities.News;
import com.vkclient.parsers.UserParser;
import com.vkclient.supports.PhotoLoader;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.List;


public class NewsListAdapter extends ArrayAdapter<News> {

    public NewsListAdapter(Context context, List<News> models) {
        super(context, R.layout.news_list_item, R.id.tvPostSource, models);
        JodaTimeAndroid.init(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view = super.getView(position, convertView, parent);
        TextView text = ((TextView) view.findViewById(R.id.tvPostText));
        ImageView photo = ((ImageView) view.findViewById(R.id.ivPostSourcePhoto));
        final News post = getItem(position);
        ((TextView) view.findViewById(R.id.tvPostSource)).setText(post.getSourceName());
        ((TextView) view.findViewById(R.id.tvPostDate)).setText(UserParser.getParsedDate(post.getDate()).toString("dd.MM - HH:mm"));
        ((TextView) view.findViewById(R.id.tvLikesCount)).setText(post.getLikesCount());
        ((TextView) view.findViewById(R.id.tvSharesCount)).setText(post.getRepostsCount());
        photo.setImageResource(R.drawable.ic_user100);
        text.setText(post.getText());
        if ((!post.getUserPhotoLink_200().isEmpty()) && post.getUserPhotoLink_200() != null) {
            PhotoLoader.loadPhoto(getContext(), post.getUserPhotoLink_200(), photo);
        }
        return view;
    }
}