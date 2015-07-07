package com.vkclient.parsers;

import android.util.Log;

import com.vkclient.entities.Message;
import com.vkclient.entities.PhotoFeed;
import com.vkclient.supports.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PhotoFeedParser {
    private JSONArray photoFeedArray;

    public PhotoFeedParser(JSONObject object) throws JSONException {
        this.photoFeedArray = object.getJSONObject("response").getJSONArray("items");
    }

    private PhotoFeed getPhoto(int index) throws JSONException {
        JSONObject photoFeedJSON = this.photoFeedArray.getJSONObject(index);
        PhotoFeed result = new PhotoFeed();
        result.setPhotoId(photoFeedJSON.getString("id"));
        result.setAlbumId(photoFeedJSON.getString("album_id"));
        result.setOwnerId(photoFeedJSON.getString("owner_id"));
        result.setPostId(photoFeedJSON.has("post_id") ? photoFeedJSON.getString("post_id") : "");
        result.setPhotoSmall(photoFeedJSON.getString("photo_130"));
        result.setPhotoLarge(photoFeedJSON.getString("photo_604"));
        result.setText(photoFeedJSON.getString("text"));
        result.setLikes(photoFeedJSON.getJSONObject("likes").getString("count"));
        return result;
    }

    public List<PhotoFeed> getPhotoFeedList() throws JSONException {
        List<PhotoFeed> result = new ArrayList<>();
        for (int i = 0; i < this.photoFeedArray.length(); i++) {
            result.add(this.getPhoto(i));
        }
        return result;
    }
}
