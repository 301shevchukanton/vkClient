package com.vkclient.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.vkclient.entities.Message;
import com.vkclient.entities.PhotoFeed;
import com.vkclient.supports.Logger;
import com.vkclient.supports.PhotoLoader;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class SingleDialogLsitItemView extends LinearLayout {
    private TextView nameText;
    private ImageView photo;
    private HorizontalListView messagePhotoListView;
    private TextView dateText;
    private TextView messageBody;
    private Context context;
    public SingleDialogLsitItemView(Context context) {
        this(context, null);
    }

    public SingleDialogLsitItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SingleDialogLsitItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.single_dialog_list_item, this);
        this.context=context;
        findViews();
    }

    private void findViews() {
        this.nameText = ((TextView) findViewById(R.id.tvSingleDialogName));
        this.photo = ((ImageView) findViewById(R.id.tvSingleDialogPhoto));
        this.messagePhotoListView = (HorizontalListView)findViewById(R.id.lvMessagesPhoto);
        this.dateText =(TextView) findViewById(R.id.tvSingleDialogDate);
        this.messageBody = ((TextView) findViewById(R.id.tvSingleDialogText));
    }
    public void setMessagesListItem(Message message){

        if (message.getUser_id() == message.getFrom_id()) {
            nameText.setText(message.getUsername());
        } else {
            nameText.setText(message.getFromname());
        }
        try {
            PhotoLoader.loadPhoto(getContext(),
                    message.getUser_id() == message.getFrom_id() ? message.getUserPhotoLink_200() : message.getFromPhotoLink_200(), photo);
        } catch (Exception e) {
            Logger.logError("Image loading error", e.toString());
        }
        String Date = getParsedDate(message.getDate()).toString("dd.MM - HH:mm");
        this.dateText.setText(Date);
        messageBody.setText(message.getBody());
        messageBody.setBackgroundColor(!message.getReadState() ? Color.LTGRAY : Color.TRANSPARENT);

        final List<PhotoFeed> messagesPhotos = new ArrayList<>();
        PhotoFeedAdapter listAdapter = new PhotoFeedAdapter(context, messagesPhotos, R.layout.photo_feed_item, R.id.ivPhotoFeedImage);
        if (!message.getMessagesPhotos().isEmpty()) {
            messagesPhotos.addAll(message.getMessagesPhotos());
            final AdapterView.OnItemClickListener photoFeedClickListener = new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    final PhotoFeed photoFeed = messagesPhotos.get(position);
                    photoViewCall(photoFeed.getPhotoLarge());
                }
            };
            messagePhotoListView.setOnItemClickListener(photoFeedClickListener);
            messagePhotoListView.setAdapter(listAdapter);
            messagePhotoListView.setVisibility(View.VISIBLE);
            listAdapter.notifyDataSetChanged();
        } else messagePhotoListView.setVisibility(View.GONE);
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
