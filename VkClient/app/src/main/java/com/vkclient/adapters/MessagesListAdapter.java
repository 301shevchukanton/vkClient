package com.vkclient.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vkclient.entities.Message;
import com.example.podkaifom.vkclient.R;
import com.vkclient.parsers.UserParser;
import com.vkclient.supports.Logger;
import com.vkclient.supports.PhotoLoader;

import java.util.List;

public class MessagesListAdapter extends ArrayAdapter<Message> {
    public MessagesListAdapter(Context context, List<Message> models) {
        super(context, R.layout.single_dialog_list_item, R.id.tvSingleDialogName, models);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView nameText = ((TextView) view.findViewById(R.id.tvSingleDialogName));
        ImageView photo = ((ImageView) view.findViewById(R.id.tvSingleDialogPhoto));
        final Message msg = getItem(position);
        if (msg.getUser_id() == msg.getFrom_id()) {
            nameText.setText(msg.getUsername());
        } else {
            nameText.setText(msg.getFromname());
        }

        photo.setImageResource(R.drawable.ic_user100);
        try {
            PhotoLoader.loadPhoto(getContext(),
                    msg.getUser_id() == msg.getFrom_id() ? msg.getUserPhotoLink_200() : msg.getFromPhotoLink_200(), photo);
        } catch (Exception e) {
            Logger.logError("Image loading error", e.toString());
        }
        String Date = UserParser.getParsedDate(msg.getDate()).toString("dd.MM - HH:mm");
        ((TextView) view.findViewById(R.id.tvSingleDialogDate)).setText(Date);
        ((TextView) view.findViewById(R.id.tvSingleDialogText)).setText(msg.getBody());
        return view;

    }
}
