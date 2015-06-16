package com.vkclient.adapter;

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

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.List;

public class DialogsListViewAdapter extends ArrayAdapter<Dialog> {
    public DialogsListViewAdapter(Context context, List<Dialog> models)
    {
        super(context, R.layout.dialogs_list_item, R.id.dialog_name, models);
        JodaTimeAndroid.init(context);
    }
    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        final View view = super.getView(position, convertView, parent);

        TextView text = ((TextView) view.findViewById(R.id.dialog_text));
        ImageView photo = ((ImageView) view.findViewById(R.id.dialog_photo));
        final Dialog dialog = getItem(position);
        ((TextView) view.findViewById(R.id.dialog_name)).setText(dialog.getUsername());
        ((TextView) view.findViewById(R.id.dialog_date)).setText(dialog.getParsedDate().toString("dd.MM - HH:mm"));
        photo.setImageResource(R.drawable.ic_user100);
        text.setText(dialog.getBody());
        text.setBackgroundColor(!dialog.getReadState() ? Color.LTGRAY : Color.TRANSPARENT);
    if ((!dialog.getGetPhoto().isEmpty()) && dialog.getGetPhoto() != null)
    {
        Picasso.with(getContext())
                .load(dialog.getGetPhoto())
                .into(((ImageView) view.findViewById(R.id.dialog_photo)));
    }
        return view;

    }

}
