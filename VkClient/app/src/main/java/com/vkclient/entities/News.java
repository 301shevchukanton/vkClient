package com.vkclient.entities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class News extends AbstractContentEntity {
    private String sourceId;
    private String postId;
    private String postType;
    private String type;
    private String text;
    private String sourceName="";
    private String likesCount="";
    private String repostsCount="";
    public News(String type, String sourceId, long date, String postId, String postType) {
        this.sourceId=sourceId;
        this.postId=postId;
        this.date=date;
        this.type=type;
        this.postType=postType;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceId() {
        return sourceId;
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

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void getPostSourceDate(JSONObject newsFeedObject) {
        JSONArray source;
        if(this.getSourceId().contains("-")) {
            try {
                source = newsFeedObject.getJSONObject("response").getJSONArray("groups");
                this.setSourceId(this.getSourceId().replace("-", ""));
                for (int i = 0; i < source.length(); i++) {
                    if (source.getJSONObject(i).getString("id").equals(this.getSourceId()))
                    {
                        this.setSourceName(source.getJSONObject(i).getString("name"));
                        this.setUserPhotoLink_200(source.getJSONObject(i).getString("userPhotoLink_200"));
                    }
                }
            }
            catch (JSONException e) {
            e.printStackTrace();
            }
        }
        else {
            try {
            source = newsFeedObject.getJSONObject("response").getJSONArray("profiles");
            for(int i = 0; i < source.length(); i++){
                if(source.getJSONObject(i).getString("id").equals(this.getSourceId()))
                {
                    this.setSourceName(source.getJSONObject(i).getString("first_name") + " "
                            + source.getJSONObject(i).getString("last_name"));
                    this.setUserPhotoLink_200(source.getJSONObject(i).getString("photo_100"));
                }
            }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
}
