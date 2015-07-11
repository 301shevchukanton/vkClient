package com.vkclient.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.podkaifom.vkclient.R;
import com.vkclient.entities.Dialog;
import com.vkclient.views.DialogListItemView;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.List;

public class DialogsListAdapter extends ArrayAdapter<Dialog> {
    public DialogsListAdapter(Context context, List<Dialog> models) {
        super(context, R.layout.dialogs_list_item, R.id.tvDialogName, models);
        JodaTimeAndroid.init(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new DialogListItemView(getContext());
        }
        ((DialogListItemView) convertView).setDialog(getItem(position));
        return convertView;
    }
}
