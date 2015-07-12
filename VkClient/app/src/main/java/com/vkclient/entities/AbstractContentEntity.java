package com.vkclient.entities;

public abstract class AbstractContentEntity {
    protected int id;
    protected long date;
    protected int user_id;
    protected String body;
    protected String username = "";
    protected String userPhotoLink_200 = "";
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
            this.userPhotoLink_200 = user.getPhoto();
        }
    }

    public void setUserPhotoLink_200(String userPhotoLink_200) {
        this.userPhotoLink_200 = userPhotoLink_200;
    }

    public String getUserPhotoLink_200() {
        return userPhotoLink_200;
    }

    public boolean getReadState() {
        return this.readState;
    }

}
