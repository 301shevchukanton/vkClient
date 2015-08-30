package com.vkclient.entities;

public class PhotoFeed {
    private String photoId = "";
    private String albumId = "";
    private String ownerId = "";
    private String postId = "";
    private String photoSmall = "";
    private String photoLarge = "";
    private String text = "";
    private String likes = "";

    public PhotoFeed() {

    }
    public PhotoFeed(String photoId, String albumId, String ownerId, String postId, String photoSmall, String photoLarge, String text, String likes) {
        this.photoId = photoId;
        this.albumId = albumId;
        this.ownerId = ownerId;
        this.postId = postId;
        this.photoSmall = photoSmall;
        this.photoLarge = photoLarge;
        this.text = text;
        this.likes = likes;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public void setPhotoSmall(String photoSmall) {
        this.photoSmall = photoSmall;
    }

    public void setPhotoLarge(String photoLarge) {
        this.photoLarge = photoLarge;
    }

    public String getPhotoLarge() {
        return photoLarge;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }
}
