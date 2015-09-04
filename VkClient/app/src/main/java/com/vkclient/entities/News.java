package com.vkclient.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class News extends AbstractContentEntity implements Parcelable {
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

    private News(Parcel inputParcel) {
        this.sourceId = inputParcel.readString();
        this.postId = inputParcel.readString();
        this.date = inputParcel.readLong();
        this.type = inputParcel.readString();
        this.postType = inputParcel.readString();
        inputParcel.readList(postPhotos, null);
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

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(sourceId);
        out.writeString(postId);
        out.writeLong(date);
        out.writeString(type);
        out.writeString(postType);
    }

    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<News> CREATOR = new Parcelable.Creator<News>() {
        public News createFromParcel(Parcel inputParcel) {
            return new News(inputParcel);
        }

        public News[] newArray(int size) {
            return new News[size];
        }
    };
}
