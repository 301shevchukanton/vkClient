package com.vkclient.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vkclient.entities.Dialog;
import com.example.podkaifom.vkclient.R;
import com.squareup.picasso.Picasso;
import com.vkclient.supports.JsonResponseParser;
import com.vkclient.supports.PhotoLoader;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.List;

public class DialogsListAdapter extends ArrayAdapter<Dialog> {
    public DialogsListAdapter(Context context, List<Dialog> models) {
        super(context, R.layout.dialogs_list_item, R.id.tvDialogName, models);
        JodaTimeAndroid.init(context);
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        final View view = super.getView(position, convertView, parent);
        TextView text = ((TextView) view.findViewById(R.id.tvDialogText));
        ImageView photo = ((ImageView) view.findViewById(R.id.ivDialogPhoto));
        final Dialog dialog = getItem(position);
        ((TextView) view.findViewById(R.id.tvDialogName)).setText(dialog.getUsername());
        ((TextView) view.findViewById(R.id.tvDialogDate)).setText(JsonResponseParser.getParsedDate(dialog.getDate()).toString("dd.MM - HH:mm"));
        photo.setImageResource(R.drawable.ic_user100);
        text.setText(dialog.getBody());
        text.setBackgroundColor(!dialog.getReadState() ? Color.LTGRAY : Color.TRANSPARENT);
        if ((!dialog.getUserPhotoLink_200().isEmpty()) && dialog.getUserPhotoLink_200() != null) {
            PhotoLoader.loadPhoto(getContext(), dialog.getUserPhotoLink_200(), photo);
        }
        return view;
    }
}
