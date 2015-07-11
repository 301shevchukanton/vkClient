package com.vkclient.parsers;

import com.vk.sdk.api.VKResponse;
import com.vkclient.entities.PhotoFeed;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PhotoFeedParser {
    private JSONArray photoFeedArray;


    private PhotoFeed getPhoto(int index) {
        try {
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
        } catch (JSONException e) {
            return null;
        }
    }

    public List<PhotoFeed> getPhotoFeedList(VKResponse response) {
        try {
            this.photoFeedArray = response.json.getJSONObject("response").getJSONArray("items");
            List<PhotoFeed> result = new ArrayList<>();
            for (int i = 0; i < this.photoFeedArray.length(); i++) {
                result.add(this.getPhoto(i));
            }
            return result;
        } catch (JSONException e) {
            return null;
        }
    }
}
