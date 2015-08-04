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

public class NewsParser {
    public static News parse(JSONObject object) {
        News result = null;
        List<PhotoFeed> messagesPhotos = new ArrayList<>();
        try {
            JSONObject messageJSON = object;
            if (messageJSON.has("attachments")) {
                JSONArray attachmentsJSON = messageJSON.getJSONArray("attachments");
                for (int i = 0; i < attachmentsJSON.length(); i++) {

                    if (attachmentsJSON.getJSONObject(i).has("photo")) {
                        JSONObject photoJson = attachmentsJSON.getJSONObject(i).getJSONObject("photo");
                        Logger.logDebug("Dialogs_Photos:", attachmentsJSON.getJSONObject(i).getString("photo"));
                        messagesPhotos.add(new PhotoFeed(
                                photoJson.getString("id"),
                                photoJson.getString("album_id"),
                                photoJson.getString("owner_id"),
                                "",
                                photoJson.getString("photo_75"),
                                photoJson.getString("photo_604"),
                                photoJson.getString("text"),
                                ""));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            result = new News(object.getString("type"),
                    object.getString("source_id"),
                    Long.parseLong(object.getString("date")),
                    object.getString("post_id"),
                    object.getString("post_type"), messagesPhotos);
            result.setText(object.getString("text"));
            result.setLikesCount(object.getJSONObject("likes").getString("count"));
            result.setRepostsCount(object.getJSONObject("reposts").getString("count"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<News> getNewsList(VKResponse response) {
        JSONObject object = response.json;
        try {
            List<News> result = new ArrayList<>();
            for (int i = 0; i < object.getJSONObject("response").getJSONArray("items").length(); i++) {
                result.add(this.parse(object.getJSONObject("response").getJSONArray("items").getJSONObject(i)));
                getPostSourceInfo(object, result.get(i));
            }
            return result;
        } catch (JSONException e) {
            return null;
        }
    }

    public void getPostSourceInfo(JSONObject object, News newsObject) {
        JSONArray source;
        if (newsObject.getSourceId().contains("-")) {
            try {
                source = object.getJSONObject("response").getJSONArray("groups");
                newsObject.setSourceId(newsObject.getSourceId().replace("-", ""));
                for (int i = 0; i < source.length(); i++) {
                    if (source.getJSONObject(i).getString("id").equals(newsObject.getSourceId())) {
                        newsObject.setSourceName(source.getJSONObject(i).getString("name"));
                        newsObject.setUserPhotoLink_200(source.getJSONObject(i).getString("photo_200"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            try {
                source = object.getJSONObject("response").getJSONArray("profiles");
                for (int i = 0; i < source.length(); i++) {
                    if (source.getJSONObject(i).getString("id").equals(newsObject.getSourceId())) {
                        newsObject.setSourceName(source.getJSONObject(i).getString("first_name") + " "
                                + source.getJSONObject(i).getString("last_name"));
                        newsObject.setUserPhotoLink_200(source.getJSONObject(i).getString("photo_100"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
