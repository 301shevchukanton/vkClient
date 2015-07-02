package com.vkclient.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.podkaifom.vkclient.R;
import com.squareup.picasso.Picasso;
import com.vkclient.activities.FriendsListActivity;
import com.vkclient.activities.PhotoViewActivity;
import com.vkclient.entities.Dialog;
import com.vkclient.entities.News;
import com.vkclient.entities.User;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.List;


public class NewsListAdapter extends ArrayAdapter<News> {

    public NewsListAdapter(Context context, List<News> models)
    {
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
        ((TextView) view.findViewById(R.id.tvPostDate)).setText(post.getParsedDate().toString("dd.MM - HH:mm"));
        ((TextView)view.findViewById(R.id.tvLikesCount)).setText(post.getLikesCount());
        ((TextView)view.findViewById(R.id.tvSharesCount)).setText(post.getRepostsCount());
        photo.setImageResource(R.drawable.ic_user100);
        text.setText(post.getText());
        if ((!post.getGetPhoto().isEmpty()) && post.getGetPhoto() != null) {
            Picasso.with(getContext())
                    .load(post.getGetPhoto())
                    .into(((ImageView) view.findViewById(R.id.ivPostSourcePhoto)));
        }
        return view;
    }
}