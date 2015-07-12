package com.vkclient.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.podkaifom.vkclient.R;
import com.vkclient.entities.News;
import com.vkclient.views.NewsListItemView;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.List;


public class NewsListAdapter extends ArrayAdapter<News> {
    public NewsListAdapter(Context context, List<News> models) {
        super(context, R.layout.news_list_item, R.id.tvPostSource, models);
        JodaTimeAndroid.init(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new NewsListItemView(getContext());
        }
        ((NewsListItemView) convertView).setPost(getItem(position));
        return convertView;
    }
}