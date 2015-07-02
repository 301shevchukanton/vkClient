package com.vkclient.entities;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.TimeZone;

public class News {
    private String sourceId;
    private final long date;
    private String postId;
    private String postType;
    private String type;
    private String text;
    private String sourceName="";
    private String photoLink_200 ="";
    private String likesCount="";
    private String repostsCount="";
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

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceName() {
        return sourceName;
    }
    public void setPhoto(String photo){
        this.photoLink_200 =photo;
    }
    public String getGetPhoto(){
        return this.photoLink_200;
    }
   public static News parse(JSONObject object) {
       News result = null;
       try {
           result = new News(object.getString("type"),
                   object.getString("source_id"),
                   Long.parseLong(object.getString("date")),
                   object.getString("post_id"),
                   object.getString("post_type"));
           result.setText(object.getString("text"));
           result.setLikesCount(object.getJSONObject("likes").getString("count"));
           result.setRepostsCount(object.getJSONObject("reposts").getString("count"));

       } catch (JSONException e) {
           e.printStackTrace();
       }
       return result;
   }
    public void getPostSourceDate(JSONObject newsFeedObject)
    {
        JSONArray source;
        if(this.getSourceId().contains("-")) {
            try {
                source = newsFeedObject.getJSONObject("response").getJSONArray("groups");
                this.setSourceId(this.getSourceId().replace("-", ""));
                for (int i = 0; i < source.length(); i++) {
                    if (source.getJSONObject(i).getString("id").equals(this.getSourceId())) {
                        this.setSourceName(source.getJSONObject(i).getString("name"));
                        this.setPhoto(source.getJSONObject(i).getString("photoLink_200"));
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
                if(source.getJSONObject(i).getString("id").equals(this.getSourceId())) {
                    this.setSourceName(source.getJSONObject(i).getString("first_name") + " "
                            + source.getJSONObject(i).getString("last_name"));
                    this.setPhoto(source.getJSONObject(i).getString("photo_100"));
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
