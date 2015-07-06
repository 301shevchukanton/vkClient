package com.vkclient.supports;

import com.vkclient.entities.Dialog;
import com.vkclient.entities.Message;
import com.vkclient.entities.News;
import com.vkclient.entities.User;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.TimeZone;

public class JsonResponseParser {
    private JSONObject object;
    public JSONArray messagesArray;
    public JSONArray postsArray;

    public JsonResponseParser(JSONObject object) {
        this.object = object;
    }

    public void parseMessages() throws JSONException {
        this.messagesArray = this.object.getJSONObject("response").getJSONArray("items");
    }

    public void parsePosts() throws JSONException {
        this.postsArray = this.object.getJSONObject("response").getJSONArray("items");
    }

    public JSONObject getMessage(int i) throws JSONException {
        return this.messagesArray.getJSONObject(i).getJSONObject("message");
    }

    public JSONObject getPost(int i) throws JSONException {
        return this.postsArray.getJSONObject(i);
    }

    public String getUser(int i) throws JSONException {
        return String.valueOf(this.messagesArray.getJSONObject(i).getJSONObject("message").getString("user_id"));
    }

    public int length() {
        return this.messagesArray.length();
    }

    public int feedLength() {
        return this.postsArray.length();
    }

    public String getUserName() throws JSONException {
        return this.object.getJSONArray("response").getJSONObject(0).getString("first_name") + " " +
                this.object.getJSONArray("response").getJSONObject(0).getString("last_name");
    }

    public boolean photoAvailable() throws JSONException {
        return !this.object.getJSONArray("response").getJSONObject(0).getString("photo_200").isEmpty();
    }

    public String getPhoto() throws JSONException {
        return this.object.getJSONArray("response").getJSONObject(0).getString("photo_200");
    }

    public static Message parseMessageFromJSON(JSONObject messageJSON) throws JSONException {
        return new Message(Integer.parseInt(messageJSON.getString("id")),
                Integer.parseInt(messageJSON.getString("user_id")),
                Integer.parseInt(messageJSON.getString("from_id")),
                Long.parseLong(messageJSON.getString("date")),
                (messageJSON.getString("read_state").equals("1")),
                messageJSON.getString("body"));
    }

    public static Dialog parseDialog(JSONObject object) {
        Dialog result = null;
        try {
            result = new Dialog(Integer.parseInt(object.getString("id")),
                    Long.parseLong(object.getString("date")),
                    Integer.parseInt(object.getString("user_id")),
                    (object.getString("read_state").equals("1")),
                    object.getString("title"),
                    object.getString("body"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
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

    public static User parseUserFromJSON(JSONObject r) throws JSONException {
        User result = new User();
        result.setId(Integer.parseInt(r.getString("id")));
        if (r.getString("last_name") != null && r.getString("first_name") != null) {
            result.setName(r.getString("first_name") + " " + r.getString("last_name"));
        }
        if (r.has("status")) result.setStatus(r.getString("status"));
        if (r.has("bdate")) result.setBdDateString(r.getString("bdate"));
        if (r.has("city")) result.setCity(r.getJSONObject("city").getString("title"));
        if (r.has("relation"))
            result.setRelationship(User.relationshipStatus[Integer.parseInt(r.getString("relation"))]);
        if (r.has("universities") && !r.getJSONArray("universities").isNull(0))
            result.setUnivers(r.getJSONArray("universities").getJSONObject(0).getString("name"));
        else result.setUnivers("");
        if (r.has("photo_200")) result.setPhoto(r.getString("photo_200"));
        result.setLangs(User.getLangs(r));
        return result;
    }

    public static DateTime getParsedDate(long date) {
        DateTime dateTime = new DateTime(date * 1000L + TimeZone.getDefault().getRawOffset());
        return dateTime;
    }
}

