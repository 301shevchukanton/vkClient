package com.vkclient.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vkclient.Classes.Dialog;
import com.example.podkaifom.vkclient.R;
import com.squareup.picasso.Picasso;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.List;

/**
 * Created by pod kaifom on 02.06.2015.
 */
public class DialogsListViewAdapter extends ArrayAdapter<Dialog> {
    private List<Dialog> mModels;
    public Object getItems() {
        return mModels;
    }
    public DialogsListViewAdapter(Context context, List<Dialog> models)
    {
        super(context, R.layout.dialogs_list_item, R.id.dialog_name, models);
        mModels = models;
        JodaTimeAndroid.init(context);
    }
    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {

        final View view = super.getView(position, convertView, parent);
        final Dialog dialog = getItem(position);
        ((TextView) view.findViewById(R.id.dialog_name)).setText(dialog.getUsername());
        ((TextView) view.findViewById(R.id.dialog_date)).setText(dialog.getParsedDate().toString("dd.MM - HH:mm"));
        ((ImageView) view.findViewById(R.id.dialog_photo)).setImageResource(R.drawable.user_100);
        ((TextView) view.findViewById(R.id.dialog_text)).setText(dialog.getBody());
        if (dialog.getReadState() == false)
             view.findViewById(R.id.dialog_text).setBackgroundColor(Color.LTGRAY);
        else
            view.findViewById(R.id.dialog_text).setBackgroundColor(Color.TRANSPARENT);

      try {
          if(!dialog.getGetphoto().isEmpty())       Picasso.with(getContext())
                                                    .load(dialog.getGetphoto())
                                                    .into((ImageView)view.findViewById(R.id.dialog_photo));
        }
      catch (Exception e){}
        return view;

    }

}
