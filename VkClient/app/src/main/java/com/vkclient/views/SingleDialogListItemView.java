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
import com.vkclient.entities.Message;
import com.vkclient.entities.PhotoFeed;
import com.vkclient.supports.PhotoLoader;
import com.vkclient.views.External.HorizontalListView;

import java.util.ArrayList;
import java.util.List;

public class SingleDialogListItemView extends SuperView {
    private TextView nameText;
    private ImageView photo;
    private HorizontalListView messagePhotoListView;
    private TextView dateText;
    private TextView messageBody;
    private List<PhotoFeed> messagesPhotos;

    public SingleDialogListItemView(Context context) {
        this(context, null);
    }

    public SingleDialogListItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SingleDialogListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.single_dialog_list_item, this);
        findViews();
    }

    private void findViews() {
        this.nameText = ((TextView) findViewById(R.id.tvSingleDialogName));
        this.photo = ((ImageView) findViewById(R.id.tvSingleDialogPhoto));
        this.messagePhotoListView = (HorizontalListView) findViewById(R.id.lvMessagesPhoto);
        this.dateText = (TextView) findViewById(R.id.tvSingleDialogDate);
        this.messageBody = ((TextView) findViewById(R.id.tvSingleDialogText));
    }

    public void setMessagesListItem(Message message) {
        nameText.setText(message.getUser_id() == message.getFrom_id() ? message.getUsername() : message.getFromname());
        PhotoLoader.loadPhoto(getContext(),
                message.getUser_id() == message.getFrom_id() ? message.getUserPhotoLink_200() : message.getFromPhotoLink_200(), photo);
        this.dateText.setText(getParsedDate(message.getDate()).toString("dd.MM - HH:mm"));
        this.messageBody.setText(message.getBody());
        this.messageBody.setBackgroundColor(message.getBackgroundColor(getContext()));
        this.messagesPhotos = new ArrayList<>();
        PhotoFeedAdapter listAdapter = new PhotoFeedAdapter(getContext(), this.messagesPhotos, R.layout.photo_feed_item, R.id.ivPhotoFeedImage);
        if (!message.getMessagesPhotos().isEmpty()) {
            this.messagesPhotos.addAll(message.getMessagesPhotos());
            this.messagePhotoListView.setOnItemClickListener(this.photoFeedClickListener);
            this.messagePhotoListView.setAdapter(listAdapter);
            this.messagePhotoListView.setVisibility(View.VISIBLE);
            listAdapter.notifyDataSetChanged();
        } else {
            this.messagePhotoListView.setVisibility(View.GONE);
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
