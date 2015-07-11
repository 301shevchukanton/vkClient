package com.vkclient.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.podkaifom.vkclient.R;
import com.vkclient.entities.DrawerMenuItem;


public class DrawerMenuItemView extends LinearLayout {
    private TextView itemText;

    public DrawerMenuItemView(Context context) {
        this(context, null);
    }

    public DrawerMenuItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawerMenuItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.drawer_list_item, this);
        this.itemText = (TextView) findViewById(R.id.drawerText);
    }

    public void setDrawerItem(DrawerMenuItem drawerItem) {
        this.itemText.setText(drawerItem.getTitle());
    }
}
