package com.vkclient.parsers;


import com.vk.sdk.api.VKResponse;
import com.vkclient.entities.News;
import com.vkclient.entities.PhotoFeed;
import com.vkclient.supports.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WallParser {
    private static final String ATTACHMENTS = "attachments";
    private static final String PHOTO = "photo";
    private static final String ID = "id";
    private static final String ALBUM_ID = "album_id";
    private static final String OWNER_ID = "owner_id";
    private static final String PHOTO_SMALL = "photo_75";
    private static final String PHOTO_BIG = "photo_604";
    private static final String TEXT = "text";
    private static final String EMPTY_PARAM = "";
    private static final String TYPE = "post_type";
    private static final String SOURCE_ID = "owner_id";
    private static final String DATE = "date";
    private static final String POST_TYPE = "post_type";
    private static final String POST_ID = "id";
    private static final String LIKES = "likes";
    private static final String COUNT = "count";
    private static final String REPOSTS = "reposts";
    private static final String RESPONSE = "response";
    private static final String ITEMS = "items";
    private static final String GROUPS = "groups";
    private static final String NAME = "name";
    private static final String PHOTO_NORMAL = "photo_200";
    private static final String PROFILES = "profiles";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String PHOTO_MIDDLE = "photo_100";

    public static News parse(JSONObject object) {
        News result = null;
        List<PhotoFeed> messagesPhotos = new ArrayList<>();
        try {
            JSONObject messageJSON = object;
            if (messageJSON.has(ATTACHMENTS)) {
                JSONArray attachmentsJSON = messageJSON.getJSONArray(ATTACHMENTS);
                for (int i = 0; i < attachmentsJSON.length(); i++) {

                    if (attachmentsJSON.getJSONObject(i).has(PHOTO)) {
                        JSONObject photoJson = attachmentsJSON.getJSONObject(i).getJSONObject(PHOTO);
                        messagesPhotos.add(new PhotoFeed(
                                photoJson.getString(ID),
                                photoJson.getString(ALBUM_ID),
                                photoJson.getString(OWNER_ID),
                                EMPTY_PARAM,
                                photoJson.getString(PHOTO_SMALL),
                                photoJson.getString(PHOTO_BIG),
                                photoJson.getString(TEXT),
                                EMPTY_PARAM));
                    }
                }
            }
        } catch (JSONException e) {
            Logger.logDebug("FUK IT", e.toString());
            e.printStackTrace();
        }
        try {
            result = new News(object.getString(TYPE),
                    object.getString(SOURCE_ID),
                    Long.parseLong(object.getString(DATE)),
                    object.getString(POST_ID),
                    object.getString(POST_TYPE), messagesPhotos);
            result.setText(object.getString(TEXT));
            result.setLikesCount(object.getJSONObject(LIKES).getString(COUNT));
            result.setRepostsCount(object.getJSONObject(REPOSTS).getString(COUNT));

        } catch (JSONException e) {
            Logger.logDebug("FUK IT", e.toString());
            e.printStackTrace();
        }
        Logger.logDebug("FUK IT", result.getText());
        return result;
    }

    public List<News> getNewsList(VKResponse response) {
        JSONObject object = response.json;
        try {
            List<News> result = new ArrayList<>();
            for (int i = 0; i < object.getJSONObject(RESPONSE).getJSONArray(ITEMS).length(); i++) {
                result.add(this.parse(object.getJSONObject(RESPONSE).getJSONArray(ITEMS).getJSONObject(i)));
                getPostSourceInfo(object, result.get(i));
            }
            return result;
        } catch (JSONException e) {
            Logger.logDebug("FUK IT", e.toString());
            return null;
        }
    }

    public void getPostSourceInfo(JSONObject object, News newsObject) {
        JSONArray source;
        if (newsObject.getSourceId().contains("-")) {
            try {
                source = object.getJSONObject(RESPONSE).getJSONArray(GROUPS);
                newsObject.setSourceId(newsObject.getSourceId().replace("-", EMPTY_PARAM));
                for (int i = 0; i < source.length(); i++) {
                    if (source.getJSONObject(i).getString(ID).equals(newsObject.getSourceId())) {
                        newsObject.setSourceName(source.getJSONObject(i).getString(NAME));
                        newsObject.setUserPhotoLink(source.getJSONObject(i).getString(PHOTO_NORMAL));
                    }
                }
                Logger.logDebug("FUK IT", "done");
            } catch (JSONException e) {
                Logger.logDebug("FUK IT", e.toString());
                e.printStackTrace();
            }
        } else {
            try {
                source = object.getJSONObject(RESPONSE).getJSONArray(PROFILES);
                for (int i = 0; i < source.length(); i++) {
                    if (source.getJSONObject(i).getString(ID).equals(newsObject.getSourceId())) {
                        newsObject.setSourceName(source.getJSONObject(i).getString(FIRST_NAME) + " "
                                + source.getJSONObject(i).getString(LAST_NAME));
                        newsObject.setUserPhotoLink(source.getJSONObject(i).getString(PHOTO_MIDDLE));
                    }
                }
            } catch (JSONException e) {
                Logger.logDebug("FUK IT", e.toString());
                e.printStackTrace();
            }
        }
    }
}

