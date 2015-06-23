package com.vkclient.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vkclient.entities.Message;
import com.example.podkaifom.vkclient.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessagesAdapter extends ArrayAdapter<Message> {
    public MessagesAdapter(Context context, List<Message> models)
    {
        super(context, R.layout.single_dialog_list_item, R.id.tvSingleDialogName, models);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView nameText=((TextView) view.findViewById(R.id.tvSingleDialogName));
        ImageView photo=((ImageView)view.findViewById(R.id.tvSingleDialogPhoto));
        final Message msg = getItem(position);
         if(msg.getUser_id()==msg.getFrom_id())  nameText.setText(msg.getUsername());
         else nameText.setText(msg.getFromname());

       photo.setImageResource(R.drawable.ic_user100);
        try {
        if(msg.getUser_id()==msg.getFrom_id())
        {
            Picasso.with(getContext())
                    .load(msg.getUserPhoto_200())
                    .into(photo);
        }
        else
        {
                Picasso.with(getContext())
                        .load(msg.getFromPhoto_200())
                        .into(photo);

        }}
        catch (Exception e) {

        }
        String Date  = msg.getParsedDate().toString("dd.MM - HH:mm");
        ((TextView) view.findViewById(R.id.tvSingleDialogDate)).setText(Date);
        ((TextView) view.findViewById(R.id.tvSingleDialogText)).setText(msg.getBody());
        return view;

    }
}
