package com.example.podkaifom.vkclient.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.podkaifom.vkclient.Classes.Message;
import com.example.podkaifom.vkclient.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by pod kaifom on 03.06.2015.
 */
public class SingleDialogListViewAdapter extends ArrayAdapter<Message> {
    private List<Message> mModels;

    public Object getItems() {
        return mModels;
    }
    public SingleDialogListViewAdapter(Context context, List<Message> models)
    {
        super(context, R.layout.single_dialog_list_item, R.id.single_dialog_name, models);
        mModels = models;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        final Message msg = getItem(position);
         if(msg.getUser_id()==msg.getFrom_id())  ((TextView) view.findViewById(R.id.single_dialog_name)).setText(msg.getUsername());
         else ((TextView) view.findViewById(R.id.single_dialog_name)).setText(msg.getFromname());

        ((ImageView)view.findViewById(R.id.single_dialog_photo)).setImageResource(R.drawable.user_100);

        if(msg.getUser_id()==msg.getFrom_id())
        {
            try
            {
            Picasso.with(getContext())
                    .load(msg.getUserPhoto_200())
                    .into((ImageView)view.findViewById(R.id.single_dialog_photo));
            }
            catch (Exception e) {}
        }
        else {
            try {
                Picasso.with(getContext())
                        .load(msg.getFromPhoto_200())
                        .into((ImageView) view.findViewById(R.id.single_dialog_photo));
            } catch (Exception e) {
            }
        }
        String Date  = msg.getParsedDate().toString("dd.MM - HH:mm");
        ((TextView) view.findViewById(R.id.single_dialog_date)).setText(Date);
        ((TextView) view.findViewById(R.id.single_dialog_text)).setText(msg.getBody());
        return view;

    }
}
