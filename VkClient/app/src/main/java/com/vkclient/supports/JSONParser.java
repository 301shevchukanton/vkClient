package com.vkclient.supports;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONParser {
    private JSONObject object;
    public JSONArray messagesArray;
    public JSONParser(JSONObject object) {
        this.object=object;
    }
    public void parseMessages() throws JSONException {
        this.messagesArray=this.object.getJSONObject("response").getJSONArray("items");
    }
    public JSONObject getMessage(int i) throws JSONException {
        return this.messagesArray.getJSONObject(i).getJSONObject("message");
    }

    public String getUser(int i) throws JSONException {
        return String.valueOf(this.messagesArray.getJSONObject(i).getJSONObject("message").getString("user_id"));
    }
    public int length()
    {
        return this.messagesArray.length();
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
}

