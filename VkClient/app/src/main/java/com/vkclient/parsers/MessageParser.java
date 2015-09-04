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

    private static final String RESPONSE_PARAM = "response";
    private static final String ITEMS_PARAM = "items";
    private static final String MESSAGE = "message";
    private static final String ID_PARAM = "id";
    private static final String DATE_PARAM = "date";
    private static final String USER_ID = "user_id";
    private static final String READ_STATE_PARAM = "read_state";
    private static final String VK_TRUE_PARAM = "1";
    private static final String TITLE_PARAM = "title";
    private static final String BODY = "body";
    private static final String CHAT_ID_PARAM = "chat_id";
    private static final String MESSAGE_PARSER_ERROR_STRING = "MessageParserError:";
    private static final String ATTACHMENTS_PARAM = "attachments";
    private static final String PHOTO = "photo";
    private static final String ALBUM_ID = "album_id";
    private static final String OWNER_ID = "owner_id";
    private static final String PHOTO_SMALL = "photo_75";
    private static final String PHOTO_LARGE = "photo_604";
    private static final String TEXT = "text";
    private static final String EMPTY_PARAM = "";
    private static final String FROM_ID = "from_id";

    private Dialog getDialog(JSONObject object, int index) {
        try {
            JSONObject dialogObject = object.getJSONObject(RESPONSE_PARAM).getJSONArray(ITEMS_PARAM).getJSONObject(index).getJSONObject(MESSAGE);
            return new Dialog(Integer.parseInt(dialogObject.getString(ID_PARAM)),
                    Long.parseLong(dialogObject.getString(DATE_PARAM)),
                    Integer.parseInt(dialogObject.getString(USER_ID)),
                    dialogObject.getString(READ_STATE_PARAM).equals(VK_TRUE_PARAM),
                    dialogObject.getString(TITLE_PARAM),
                    dialogObject.getString(BODY), dialogObject.has(CHAT_ID_PARAM) ? dialogObject.getString(CHAT_ID_PARAM) : EMPTY_PARAM);
        } catch (JSONException e) {
            Logger.logError(MESSAGE_PARSER_ERROR_STRING, e.toString());
            return null;
        }
    }

    private void parseMessagePhotos(JSONArray attachmentsJSON, List<PhotoFeed> messagesPhotos, int position) {
        try {
            if (attachmentsJSON.getJSONObject(position).has(PHOTO)) {
                JSONObject photoJson = attachmentsJSON.getJSONObject(position).getJSONObject(PHOTO);
                messagesPhotos.add(new PhotoFeed(
                        photoJson.getString(ID_PARAM),
                        photoJson.getString(ALBUM_ID),
                        photoJson.getString(OWNER_ID),
                        EMPTY_PARAM,
                        photoJson.getString(PHOTO_SMALL),
                        photoJson.getString(PHOTO_LARGE),
                        photoJson.getString(TEXT),
                        EMPTY_PARAM));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private Message getMessage(JSONObject object, int index) {
        try {
            JSONObject messageJSON = object.getJSONObject(RESPONSE_PARAM).getJSONArray(ITEMS_PARAM).getJSONObject(index);
            List<PhotoFeed> messagesPhotos = new ArrayList<>();
            if (messageJSON.has(ATTACHMENTS_PARAM)) {
                JSONArray attachmentsJSON = messageJSON.getJSONArray(ATTACHMENTS_PARAM);
                for (int i = 0; i < attachmentsJSON.length(); i++) {
                    parseMessagePhotos(attachmentsJSON, messagesPhotos, i);
                }
            }

            return new Message(Integer.parseInt(messageJSON.getString(ID_PARAM)),
                    Integer.parseInt(messageJSON.getString(USER_ID)),
                    Integer.parseInt(messageJSON.getString(FROM_ID)),
                    Long.parseLong(messageJSON.getString(DATE_PARAM)),
                    messageJSON.getString(READ_STATE_PARAM).equals(VK_TRUE_PARAM),
                    messageJSON.getString(BODY), messagesPhotos
            );
        } catch (JSONException e) {
            Logger.logError(MESSAGE_PARSER_ERROR_STRING, e.toString());
            return null;
        }
    }

    public List<Message> getMessagesList(VKResponse response) {
        try {
            JSONObject object = response.json;
            List<Message> result = new ArrayList<>();
            for (int i = 0; i < object.getJSONObject(RESPONSE_PARAM).getJSONArray(ITEMS_PARAM).length(); i++) {
                result.add(this.getMessage(object, i));
            }
            return result;
        } catch (JSONException e) {
            Logger.logError(MESSAGE_PARSER_ERROR_STRING, e.toString());
            return null;
        }
    }

    public List<Dialog> getDialogsList(VKResponse response) {
        JSONObject object = response.json;
        List<Dialog> result = new ArrayList<>();
        try {
            for (int i = 0; i < object.getJSONObject(RESPONSE_PARAM).getJSONArray(ITEMS_PARAM).length(); i++) {
                result.add(this.getDialog(object, i));
            }
        } catch (JSONException e) {
            Logger.logError(MESSAGE_PARSER_ERROR_STRING, e.toString());
        }
        return result;
    }
}

