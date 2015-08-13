package com.vkclient.entities;

import com.vkclient.activities.VkSdkActivity;

public class DrawerMenuItem {
    private String title;
    private VkSdkActivity.NavigationItem key;
    private int iconResourceId;

    public DrawerMenuItem(String title, int iconResourceId, VkSdkActivity.NavigationItem key) {
        this.title = title;
        this.key = key;
        this.iconResourceId = iconResourceId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setKey(VkSdkActivity.NavigationItem key) {
        this.key = key;
    }

    public VkSdkActivity.NavigationItem getKey() {
        return key;
    }

    public int getIconResourceId() {
        return iconResourceId;
    }
}
