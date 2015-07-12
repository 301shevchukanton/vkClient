package com.vkclient.views;


import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.podkaifom.vkclient.R;
import com.meetme.android.horizontallistview.HorizontalListView;
import com.vkclient.activities.PhotoViewActivity;
import com.vkclient.adapters.PhotoFeedAdapter;
import com.vkclient.entities.News;
import com.vkclient.entities.PhotoFeed;
import com.vkclient.supports.PhotoLoader;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class NewsListItemView extends LinearLayout {
    private Context context;
    private TextView text;
    private ImageView photo;
    private TextView postSource;
    private TextView postDate;
    private TextView likesCount;
    private TextView repostsCount;
    private HorizontalListView newsPhoto;

    public NewsListItemView(Context context) {
        this(context, null);
    }

    public NewsListItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NewsListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.news_list_item, this);
        this.context = context;
        findViews();
    }

    private void findViews() {
        this.text = ((TextView) findViewById(R.id.tvPostText));
        this.photo = ((ImageView) findViewById(R.id.ivPostSourcePhoto));
        this.postSource = (TextView) findViewById(R.id.tvPostSource);
        this.postDate = (TextView) findViewById(R.id.tvPostDate);
        this.likesCount = (TextView) findViewById(R.id.tvLikesCount);
        this.repostsCount = (TextView) findViewById(R.id.tvSharesCount);
        this.newsPhoto = (HorizontalListView) findViewById(R.id.lvNewsPhoto);
    }

    public void setPost(News post) {
        this.postSource.setText(post.getSourceName());
        this.postDate.setText(getParsedDate(post.getDate()).toString("dd.MM - HH:mm"));
        this.likesCount.setText(post.getLikesCount());
        this.repostsCount.setText(post.getRepostsCount());
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
            this.newsPhoto.setOnItemClickListener(photoFeedClickListener);
            this.newsPhoto.setAdapter(listAdapter);
            this.newsPhoto.setVisibility(View.VISIBLE);
            listAdapter.notifyDataSetChanged();
        } else this.newsPhoto.setVisibility(View.GONE);

        photo.setImageResource(R.drawable.ic_user100);

        text.setText(post.getText());
        if ((!post.getUserPhotoLink_200().isEmpty()) && post.getUserPhotoLink_200() != null) {
            PhotoLoader.loadPhoto(getContext(), post.getUserPhotoLink_200(), photo);
        }
    }

    public DateTime getParsedDate(long date) {
        DateTime dateTime = new DateTime(date * 1000L + TimeZone.getDefault().getRawOffset());
        return dateTime;
    }

    private void photoViewCall(String photoUrl) {
        Intent i = new Intent(context, PhotoViewActivity.class);
        i.putExtra("photo", photoUrl);
        context.startActivity(i);
    }
}
