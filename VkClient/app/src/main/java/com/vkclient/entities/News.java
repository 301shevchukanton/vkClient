package com.vkclient.entities;

import java.util.ArrayList;
import java.util.List;

public class News extends AbstractContentEntity {
    private String sourceId = "";
    private String postId = "";
    private String postType = "";
    private String type = "";
    private String text = "";
    private String sourceName = "";
    private String likesCount = "";
    private String repostsCount = "";
    private List<PhotoFeed> postPhotos = new ArrayList<>();

    public News(String type, String sourceId, long date, String postId, String postType, List<PhotoFeed> postPhotos) {
        this.sourceId = sourceId;
        this.postId = postId;
        this.date = date;
        this.type = type;
        this.postType = postType;
        this.postPhotos.addAll(postPhotos);
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setLikesCount(String likesCount) {
        this.likesCount = likesCount;
    }

    public String getLikesCount() {
        return likesCount;
    }

    public void setRepostsCount(String repostsCount) {
        this.repostsCount = repostsCount;
    }

    public String getRepostsCount() {
        return repostsCount;
    }

    public List<PhotoFeed> getPostPhotos() {
        return postPhotos;
    }
}
