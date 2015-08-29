package com.vkclient.parsers;

import com.vk.sdk.api.VKResponse;
import com.vkclient.entities.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserParser {

    private static final String LAST_NAME = "last_name";
    private static final String FIRST_NAME = "first_name";
    private static final String STATUS = "status";
    private static final String BDATE = "bdate";
    private static final String CITY = "city";
    private static final String ID = "id";
    private static final String RESPONSE = "response";
    private static final String TITLE = "title";
    private static final String RELATION = "relation";
    private static final String UNIVERSITIES = "universities";
    private static final String NAME = "name";
    private static final String PHOTO_MAX_ORIG = "photo_max_orig";
    private static final String PHOTO_DEFAULT = "photo_200";
    private static final String PERSONAL = "personal";
    private static final String LANGS = "langs";

    public User parse(VKResponse response) {
        try {
            JSONObject object = response.json.getJSONArray(RESPONSE).getJSONObject(0);
            User result = new User();
            result.setId(Integer.parseInt(object.getString(ID)));
            if (object.getString(LAST_NAME) != null && object.getString(FIRST_NAME) != null) {
                result.setName(object.getString(FIRST_NAME) + " " + object.getString(LAST_NAME));
            }
            if (object.has(STATUS)) {
                result.setStatus(object.getString(STATUS));
            }
            if (object.has(BDATE)) {
                result.setBdDateString(object.getString(BDATE));
            }
            if (object.has(CITY)) {
                result.setCity(object.getJSONObject(CITY).getString(TITLE));
            }
            if (object.has(RELATION)) {
                result.setRelationship(User.RELATIONSHIP_STATUS[Integer.parseInt(object.getString(RELATION))]);
            }
            result.setUnivers(object.has(UNIVERSITIES) && !object.getJSONArray(UNIVERSITIES).isNull(0)
                    ? object.getJSONArray(UNIVERSITIES).getJSONObject(0).getString(NAME) : "");
            if (object.has(PHOTO_MAX_ORIG)) {
                result.setPhoto(object.getString(PHOTO_MAX_ORIG));
            }
            result.setLangs(UserParser.getLangs(object));
            return result;
        } catch (JSONException e) {
            return null;
        }
    }

    public User parseUserName(VKResponse response) {
        try {
            JSONObject object = response.json;
            User result = new User();
            result.setName(object.getJSONArray(RESPONSE).getJSONObject(0).getString(FIRST_NAME) + " " +
                    object.getJSONArray(RESPONSE).getJSONObject(0).getString(LAST_NAME));
            result.setPhotoMax(object.getJSONArray(RESPONSE).getJSONObject(0).getString(PHOTO_MAX_ORIG));
            if (object.getJSONArray(RESPONSE).getJSONObject(0).has(PHOTO_DEFAULT)) {
                result.setPhoto(object.getJSONArray(RESPONSE).getJSONObject(0).getString(PHOTO_DEFAULT));
            }
            return result;
        } catch (JSONException e) {
            return null;
        }
    }

    public static String getLangs(JSONObject r) {
        try {
            String langs = "";
            JSONArray langsArray = r.getJSONObject(PERSONAL).getJSONArray(LANGS);
            for (int i = 0; i < langsArray.length(); i++)
                langs += langsArray.getString(i) + "; ";
            return langs;
        } catch (JSONException e) {
            return "";
        }
    }

}

