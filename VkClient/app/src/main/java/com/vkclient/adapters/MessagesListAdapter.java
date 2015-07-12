package com.vkclient.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.podkaifom.vkclient.R;
import com.vkclient.entities.Message;
import com.vkclient.views.SingleDialogLsitItemView;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.List;

public class MessagesListAdapter extends ArrayAdapter<Message> {
Context context;
    public MessagesListAdapter(Context context, List<Message> models) {
        super(context, R.layout.single_dialog_list_item, R.id.tvSingleDialogName, models);
        JodaTimeAndroid.init(context);
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new SingleDialogLsitItemView(getContext());
        }
        ((SingleDialogLsitItemView) convertView).setMessagesListItem(getItem(position));
        return convertView;
    }
}
