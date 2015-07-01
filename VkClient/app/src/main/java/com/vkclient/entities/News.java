package com.vkclient.entities;

import org.joda.time.DateTime;

import java.util.TimeZone;

public class News {
    private String sourceId;
    private final long date;
    private String postId;
    private String postType;
    private String type;
    private String text;
    public News(String type, String sourceId, long date, String postId, String postType)
    {
        this.sourceId=sourceId;
        this.postId=postId;
        this.date=date;
        this.type=type;
        this.postType=postType;
    }
    public News()
    {
        this.sourceId="";
        this.postId="";
        this.date=0;
        this.type="";
        this.postType="";
    }


    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public long getDate() {
        return date;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getPostType() {
        return postType;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    public DateTime getParsedDate()
    {
        DateTime dateTime = new DateTime( ( this.date * 1000L + TimeZone.getDefault().getRawOffset()));

        return dateTime;
    }
}
