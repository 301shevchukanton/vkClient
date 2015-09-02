package com.vkclient.entities;

import com.vkclient.fragments.NavigationPanelFragment;

public class DrawerMenuItem {
    private String title = "";
    private NavigationPanelFragment.NavigationItem key;
    private int iconResourceId;

    public DrawerMenuItem(String title, int iconResourceId, NavigationPanelFragment.NavigationItem key) {
        this.title = title;
        this.key = key;
        this.iconResourceId = iconResourceId;
    }

    public String getTitle() {
        return title;
    }

    public NavigationPanelFragment.NavigationItem getKey() {
        return key;
    }

    public int getIconResourceId() {
        return iconResourceId;
    }
}
