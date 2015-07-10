package com.vkclient.parsers;


import com.vk.sdk.api.VKResponse;
import com.vkclient.entities.Dialog;
import com.vkclient.entities.Message;
import com.vkclient.entities.PhotoFeed;
import com.vkclient.supports.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MessageParser {

    private Dialog getDialog(JSONObject object, int index) throws JSONException {
        JSONObject dialogObject = object.getJSONObject("response").getJSONArray("items").getJSONObject(index).getJSONObject("message");
        return new Dialog(Integer.parseInt(dialogObject.getString("id")),
                Long.parseLong(dialogObject.getString("date")),
                Integer.parseInt(dialogObject.getString("user_id")),
                dialogObject.getString("read_state").equals("1"),
                dialogObject.getString("title"),
                dialogObject.getString("body"));
    }

    private Message getMessage(JSONObject object, int index) throws JSONException {
        JSONObject messageJSON = object.getJSONObject("response").getJSONArray("items").getJSONObject(index);
        List<PhotoFeed> messagesPhotos = new ArrayList<>();
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
        return new Message(Integer.parseInt(messageJSON.getString("id")),
                Integer.parseInt(messageJSON.getString("user_id")),
                Integer.parseInt(messageJSON.getString("from_id")),
                Long.parseLong(messageJSON.getString("date")),
                messageJSON.getString("read_state").equals("1"),
                messageJSON.getString("body"), messagesPhotos
        );
    }

    public List<Message> getMessagesList(VKResponse response) throws JSONException {
        JSONObject object = response.json;
        List<Message> result = new ArrayList<>();
        for (int i = 0; i < object.getJSONObject("response").getJSONArray("items").length(); i++) {
            result.add(this.getMessage(object, i));
        }
        return result;
    }

    public List<Dialog> getDialogsList(VKResponse response) throws JSONException {
        JSONObject object = response.json;
        List<Dialog> result = new ArrayList<>();
        for (int i = 0; i < object.getJSONObject("response").getJSONArray("items").length(); i++) {
            result.add(this.getDialog(object, i));
        }
        return result;
    }
}

