package com.example.podkaifom.vkclient;

import android.content.Context;
import android.widget.ArrayAdapter;

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
    }
}
