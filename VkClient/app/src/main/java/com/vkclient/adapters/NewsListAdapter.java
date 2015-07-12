package com.vkclient.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.podkaifom.vkclient.R;
import com.meetme.android.horizontallistview.HorizontalListView;
import com.vkclient.activities.PhotoViewActivity;
import com.vkclient.entities.News;
import com.vkclient.entities.PhotoFeed;
import com.vkclient.supports.PhotoLoader;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;


public class NewsListAdapter extends ArrayAdapter<News> {
    private Context context;

    public NewsListAdapter(Context context, List<News> models) {
        super(context, R.layout.news_list_item, R.id.tvPostSource, models);
        JodaTimeAndroid.init(context);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view = super.getView(position, convertView, parent);
        TextView text = ((TextView) view.findViewById(R.id.tvPostText));
        ImageView photo = ((ImageView) view.findViewById(R.id.ivPostSourcePhoto));
        final News post = getItem(position);
        ((TextView) view.findViewById(R.id.tvPostSource)).setText(post.getSourceName());
        ((TextView) view.findViewById(R.id.tvPostDate)).setText(getParsedDate(post.getDate()).toString("dd.MM - HH:mm"));
        ((TextView) view.findViewById(R.id.tvLikesCount)).setText(post.getLikesCount());
        ((TextView) view.findViewById(R.id.tvSharesCount)).setText(post.getRepostsCount());
        HorizontalListView newsPhotoListView = (HorizontalListView) view.findViewById(R.id.lvNewsPhoto);
        final List<PhotoFeed> messagesPhotos = new ArrayList<>();
        PhotoFeedAdapter listAdapter = new PhotoFeedAdapter(context, messagesPhotos, R.layout.photo_feed_item, R.id.ivPhotoFeedImage);
        if (!post.getPostPhotos().isEmpty()) {
            messagesPhotos.addAll(post.getPostPhotos());
            final AdapterView.OnItemClickListener photoFeedClickListener = new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    final PhotoFeed photoFeed = messagesPhotos.get(position);
                    photoViewCall(photoFeed.getPhotoLarge());
                }
            };
            newsPhotoListView.setOnItemClickListener(photoFeedClickListener);
            newsPhotoListView.setAdapter(listAdapter);
            newsPhotoListView.setVisibility(View.VISIBLE);
            listAdapter.notifyDataSetChanged();
        } else newsPhotoListView.setVisibility(View.GONE);

        photo.setImageResource(R.drawable.ic_user100);

        text.setText(post.getText());
        if ((!post.getUserPhotoLink_200().isEmpty()) && post.getUserPhotoLink_200() != null) {
            PhotoLoader.loadPhoto(getContext(), post.getUserPhotoLink_200(), photo);
        }
        return view;
    }

    private DateTime getParsedDate(long date) {
        DateTime dateTime = new DateTime(date * 1000L + TimeZone.getDefault().getRawOffset());
        return dateTime;
    }

    private void photoViewCall(String photoUrl) {
        Intent i = new Intent(context, PhotoViewActivity.class);
        i.putExtra("photo", photoUrl);
        context.startActivity(i);
    }
}