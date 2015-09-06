package com.vkclient.views;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.podkaifom.vkclient.R;
import com.vkclient.adapters.PhotoFeedAdapter;
import com.vkclient.entities.News;
import com.vkclient.entities.PhotoFeed;
import com.vkclient.supports.PhotoLoader;
import com.vkclient.views.external.HorizontalListView;

import java.util.ArrayList;
import java.util.List;

public class NewsListItemView extends ListItemView {
    private static final String DATE_FORMAT = "dd.MM - HH:mm";
    private TextView text;
    private ImageView photo;
    private TextView postSource;
    private TextView postDate;
    private TextView likesCount;
    private TextView repostsCount;
    private HorizontalListView newsPhoto;
    private List<PhotoFeed> messagesPhotos;

    public NewsListItemView(Context context) {
        this(context, null);
    }

    public NewsListItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NewsListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.news_list_item, this);
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
        this.postDate.setText(getParsedDate(post.getDate()).toString(this.DATE_FORMAT));
        this.likesCount.setText(post.getLikesCount());
        this.repostsCount.setText(post.getRepostsCount());
        this.messagesPhotos = new ArrayList<>();
        PhotoFeedAdapter listAdapter = new PhotoFeedAdapter(getContext(), this.messagesPhotos, R.layout.photo_feed_item, R.id.ivPhotoFeedImage);
        if (!post.getPostPhotos().isEmpty()) {
            this.messagesPhotos.addAll(post.getPostPhotos());
            this.newsPhoto.setOnItemClickListener(this.photoFeedClickListener);
            this.newsPhoto.setAdapter(listAdapter);
            this.newsPhoto.setVisibility(View.VISIBLE);
            listAdapter.notifyDataSetChanged();
        } else {
            this.newsPhoto.setVisibility(View.GONE);
        }
        this.photo.setImageResource(R.drawable.ic_user);
        this.text.setText(post.getText());
        if ((!post.getUserPhotoLink().isEmpty()) && post.getUserPhotoLink() != null) {
            PhotoLoader.loadPhoto(getContext(), post.getUserPhotoLink(), this.photo);
        }
    }

    final AdapterView.OnItemClickListener photoFeedClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {
            final PhotoFeed photoFeed = messagesPhotos.get(position);
            photoViewCall(photoFeed.getPhotoLarge());
        }
    };
}
