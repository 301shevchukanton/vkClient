package com.vkclient.parsers;


import android.support.annotation.NonNull;

import com.vkclient.entities.Dialog;
import com.vkclient.entities.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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
        return new Message(Integer.parseInt(messageJSON.getString("id")),
                Integer.parseInt(messageJSON.getString("user_id")),
                Integer.parseInt(messageJSON.getString("from_id")),
                Long.parseLong(messageJSON.getString("date")),
                messageJSON.getString("read_state").equals("1"),
                messageJSON.getString("body"));
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

