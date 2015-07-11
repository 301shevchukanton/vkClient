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
import com.vkclient.entities.Message;
import com.vkclient.entities.PhotoFeed;
import com.vkclient.supports.Logger;
import com.vkclient.supports.PhotoLoader;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class MessagesListAdapter extends ArrayAdapter<Message> {
    private Context context;

    public MessagesListAdapter(Context context, List<Message> models) {
        super(context, R.layout.single_dialog_list_item, R.id.tvSingleDialogName, models);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView nameText = ((TextView) view.findViewById(R.id.tvSingleDialogName));
        ImageView photo = ((ImageView) view.findViewById(R.id.tvSingleDialogPhoto));
        HorizontalListView messagePhotoListView = (HorizontalListView) view.findViewById(R.id.lvDialogsPhoto);

        final Message message = getItem(position);
        if (message.getUser_id() == message.getFrom_id()) {
            nameText.setText(message.getUsername());
        } else {
            nameText.setText(message.getFromname());
        }

        photo.setImageResource(R.drawable.ic_user100);
        try {
            PhotoLoader.loadPhoto(getContext(),
                    message.getUser_id() == message.getFrom_id() ? message.getUserPhotoLink_200() : message.getFromPhotoLink_200(), photo);
        } catch (Exception e) {
            Logger.logError("Image loading error", e.toString());
        }
        String Date = getParsedDate(message.getDate()).toString("dd.MM - HH:mm");
        ((TextView) view.findViewById(R.id.tvSingleDialogDate)).setText(Date);
        ((TextView) view.findViewById(R.id.tvSingleDialogText)).setText(message.getBody());
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

        return view;
    }

    private void photoViewCall(String photoUrl) {
        Intent i = new Intent(context, PhotoViewActivity.class);
        i.putExtra("photo", photoUrl);
        context.startActivity(i);
    }

    private DateTime getParsedDate(long date) {
        DateTime dateTime = new DateTime(date * 1000L + TimeZone.getDefault().getRawOffset());
        return dateTime;
    }

}
