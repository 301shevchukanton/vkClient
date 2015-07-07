package com.vkclient.parsers;

import com.vkclient.entities.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewsParser {
    private JSONArray newsArray;
    private JSONObject newsFeedObject;

    public NewsParser(JSONObject object) throws JSONException {
        this.newsArray = object.getJSONObject("response").getJSONArray("items");
        this.newsFeedObject = object;
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

    public List<News> getNewsList() throws JSONException {
        List<News> result = new ArrayList<>();
        for (int i = 0; i < this.newsArray.length(); i++) {
            result.add(this.parse(newsArray.getJSONObject(i)));
            getPostSourceInfo(result.get(i));
        }
        return result;
    }

    public void getPostSourceInfo(News newsObject) {
        JSONArray source;
        if (newsObject.getSourceId().contains("-")) {
            try {
                source = this.newsFeedObject.getJSONObject("response").getJSONArray("groups");
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
                source = this.newsFeedObject.getJSONObject("response").getJSONArray("profiles");
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
