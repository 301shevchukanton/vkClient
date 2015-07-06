package com.vkclient.entities;

public abstract class AbstractContentEntity {
    protected int id;
    protected long date;
    protected int user_id;
    protected String body;
    protected String username = "";
    protected String userPhotoLink_200 = "";
    protected boolean readState;

    protected AbstractContentEntity(int id, long date, int user_id, String body, boolean readState) {
        this.id = id;
        this.date = date;
        this.user_id = user_id;
        this.body = body;
        this.readState = readState;
    }

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

    public boolean isReadState() {
        return readState;
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
