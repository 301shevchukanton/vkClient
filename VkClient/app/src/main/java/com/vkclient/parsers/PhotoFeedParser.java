package com.vkclient.parsers;

import com.vk.sdk.api.VKResponse;
import com.vkclient.entities.PhotoFeed;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PhotoFeedParser {
    private static final String ID = "id";
    private static final String ALBUM_ID = "album_id";
    private static final String OWNER_ID = "owner_id";
    private static final String POST_ID = "post_id";
    private static final String EMPTY_PARAM = "";
    private static final String PHOTO_SMALL = "photo_130";
    private static final String PHOTO_LARGE = "photo_604";
    private static final String TEXT_PARAM = "text";
    private static final String LIKES = "likes";
    private static final String COUNT = "count";
    private static final String RESPONSE_PARAM = "response";
    private static final String ITEMS_PARAM = "items";
    private JSONArray photoFeedArray;


    private PhotoFeed getPhoto(int index) {
        try {
            JSONObject photoFeedJSON = this.photoFeedArray.getJSONObject(index);
            PhotoFeed result = new PhotoFeed();
            result.setPhotoId(photoFeedJSON.getString(ID));
            result.setAlbumId(photoFeedJSON.getString(ALBUM_ID));
            result.setOwnerId(photoFeedJSON.getString(OWNER_ID));
            result.setPostId(photoFeedJSON.has(POST_ID) ? photoFeedJSON.getString(POST_ID) : EMPTY_PARAM);
            result.setPhotoSmall(photoFeedJSON.getString(PHOTO_SMALL));
            result.setPhotoLarge(photoFeedJSON.getString(PHOTO_LARGE));
            result.setText(photoFeedJSON.getString(TEXT_PARAM));
            result.setLikes(photoFeedJSON.getJSONObject(LIKES).getString(COUNT));
            return result;
        } catch (JSONException e) {
            return null;
        }
    }

    public List<PhotoFeed> getPhotoFeedList(VKResponse response) {
        try {
            this.photoFeedArray = response.json.getJSONObject(RESPONSE_PARAM).getJSONArray(ITEMS_PARAM);
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
