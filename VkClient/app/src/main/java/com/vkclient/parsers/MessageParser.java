package com.vkclient.parsers;


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
    private JSONArray messagesArray;

    public MessageParser(JSONObject object) throws JSONException {
        this.messagesArray = object.getJSONObject("response").getJSONArray("items");
    }

    private Dialog getDialog(int index) throws JSONException {
        JSONObject object = this.messagesArray.getJSONObject(index).getJSONObject("message");
        return new Dialog(Integer.parseInt(object.getString("id")),
                Long.parseLong(object.getString("date")),
                Integer.parseInt(object.getString("user_id")),
                object.getString("read_state").equals("1"),
                object.getString("title"),
                object.getString("body"));
    }

    private Message getMessage(int index) throws JSONException {
        JSONObject messageJSON = this.messagesArray.getJSONObject(index);
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

    public List<Message> getMessagesList() throws JSONException {
        List<Message> result = new ArrayList<>();
        for (int i = 0; i < this.messagesArray.length(); i++) {
            result.add(this.getMessage(i));
        }
        return result;
    }

    public List<Dialog> getDialogsList() throws JSONException {
        List<Dialog> result = new ArrayList<>();
        for (int i = 0; i < this.messagesArray.length(); i++) {
            result.add(this.getDialog(i));
        }
        return result;
    }
}

