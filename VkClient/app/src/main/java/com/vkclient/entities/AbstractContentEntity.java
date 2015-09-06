package com.vkclient.entities;

import android.content.Context;
import android.graphics.Color;

import com.example.podkaifom.vkclient.R;

public abstract class AbstractContentEntity {
    protected int id;
    protected long date;
    protected int user_id;
    protected String body = "";
    protected String username = "";
    protected String userPhotoLink = "";
    protected boolean readState;

    protected AbstractContentEntity() {
        this.id = 0;
        this.date = 0;
        this.user_id = 0;
        this.body = null;
        this.readState = false;
    }

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public long getDate() {
        return date;
    }

    public String getBody() {
        return body;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDialogUserInfo(User user) {
        this.username = user.getName();
        if (user.getPhoto() != null) {
            this.userPhotoLink = user.getPhoto();
        }
    }

    public void setUserPhotoLink(String userPhotoLink) {
        this.userPhotoLink = userPhotoLink;
    }

    public String getUserPhotoLink() {
        return this.userPhotoLink;
    }

    public boolean getReadState() {
        return this.readState;
    }

    public int getBackgroundColor(Context context) {
        return !getReadState() ? context.getResources().getColor(R.color.unread_message_color) : Color.TRANSPARENT;
    }
}
