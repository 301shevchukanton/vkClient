package com.vkclient.parsers;

import com.vk.sdk.api.VKResponse;
import com.vkclient.entities.User;

import org.json.JSONException;
import org.json.JSONObject;

public class UserParser {

    public User parse(VKResponse response) {
        try {
            JSONObject object=response.json.getJSONArray("response").getJSONObject(0);
            User result = new User();
            result.setId(Integer.parseInt(object.getString("id")));
            if (object.getString("last_name") != null && object.getString("first_name") != null) {
                result.setName(object.getString("first_name") + " " + object.getString("last_name"));
            }
            if (object.has("status")) result.setStatus(object.getString("status"));
            if (object.has("bdate")) result.setBdDateString(object.getString("bdate"));
            if (object.has("city"))
                result.setCity(object.getJSONObject("city").getString("title"));
            if (object.has("relation"))
                result.setRelationship(User.RELATIONSHIP_STATUS[Integer.parseInt(object.getString("relation"))]);
            if (object.has("universities") && !object.getJSONArray("universities").isNull(0))
                result.setUnivers(object.getJSONArray("universities").getJSONObject(0).getString("name"));
            else result.setUnivers("");
            if (object.has("photo_max_orig"))
                result.setPhoto(object.getString("photo_max_orig"));
            result.setLangs(User.getLangs(object));
            return result;
        } catch (JSONException e) {
            return null;
        }
    }

    public User parseUserName(VKResponse response) {
        try {
            JSONObject object = response.json;
            User result = new User();
            result.setName(object.getJSONArray("response").getJSONObject(0).getString("first_name") + " " +
                    object.getJSONArray("response").getJSONObject(0).getString("last_name"));
            result.setPhotoMax(object.getJSONArray("response").getJSONObject(0).getString("photo_max_orig"));
            //result.setPhoto(object.getJSONArray("response").getJSONObject(0).getString("photo_200"));
            return result;
        } catch (JSONException e) {
            return null;
        }
    }

}

