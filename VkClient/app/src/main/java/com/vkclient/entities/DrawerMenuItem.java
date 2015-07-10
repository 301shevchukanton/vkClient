package com.vkclient.entities;

import com.vkclient.activities.VkSdkActivity;

public class DrawerMenuItem {
    private String title;
    private VkSdkActivity.NavigationItem key;

    public DrawerMenuItem(String title, VkSdkActivity.NavigationItem key) {
        this.title = title;
        this.key = key;
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
}
