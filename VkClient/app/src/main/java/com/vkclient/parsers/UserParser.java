package com.vkclient.parsers;

import com.vkclient.entities.User;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.TimeZone;

public class UserParser {
    private JSONObject object;

    public UserParser(JSONObject object) {
        this.object = object;
    }

    public String getUserName() throws JSONException {
        return this.object.getJSONArray("response").getJSONObject(0).getString("first_name") + " " +
                this.object.getJSONArray("response").getJSONObject(0).getString("last_name");
    }

    public boolean photoAvailable() throws JSONException {
        return !this.object.getJSONArray("response").getJSONObject(0).getString("photo_max_orig").isEmpty();
    }

    public String getPhoto() throws JSONException {
        return this.object.getJSONArray("response").getJSONObject(0).getString("photo_max_orig");
    }

    public User parse() throws JSONException {
        User result = new User();
        result.setId(Integer.parseInt(this.object.getString("id")));
        if (this.object.getString("last_name") != null && this.object.getString("first_name") != null) {
            result.setName(this.object.getString("first_name") + " " + this.object.getString("last_name"));
        }
        if (this.object.has("status")) result.setStatus(this.object.getString("status"));
        if (this.object.has("bdate")) result.setBdDateString(this.object.getString("bdate"));
        if (this.object.has("city"))
            result.setCity(this.object.getJSONObject("city").getString("title"));
        if (this.object.has("relation"))
            result.setRelationship(User.RELATIONSHIP_STATUS[Integer.parseInt(this.object.getString("relation"))]);
        if (this.object.has("universities") && !this.object.getJSONArray("universities").isNull(0))
            result.setUnivers(this.object.getJSONArray("universities").getJSONObject(0).getString("name"));
        else result.setUnivers("");
        if (this.object.has("photo_max_orig"))
            result.setPhoto(this.object.getString("photo_max_orig"));
        result.setLangs(User.getLangs(this.object));
        return result;
    }

    public static DateTime getParsedDate(long date) {
        DateTime dateTime = new DateTime(date * 1000L + TimeZone.getDefault().getRawOffset());
        return dateTime;
    }
}

