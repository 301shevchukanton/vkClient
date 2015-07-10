package com.vkclient.adapters;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.podkaifom.vkclient.R;
import com.vkclient.entities.DrawerMenuItem;
import com.vkclient.views.DrawerMenuItemView;

import java.util.List;

public class DrawerAdapter extends ArrayAdapter<DrawerMenuItem> {
    public DrawerAdapter(Context context, List<DrawerMenuItem> models) {
        super(context, R.layout.drawer_list_item, R.id.drawerText, models);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new DrawerMenuItemView(getContext());
        }
        ((DrawerMenuItemView) convertView).setDrawerItem(getItem(position));
        return convertView;
    }
}
