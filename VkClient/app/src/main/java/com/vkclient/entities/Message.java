package com.vkclient.entities;

import android.util.Log;
import android.widget.TextView;

import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vkclient.supports.RequestCreator;

import java.util.ArrayList;
import java.util.List;

public class Message extends AbstractContentEntity {
    private final int fromId;
    private String fromPhotoLink_200 = "";
    private String fromName = "";
    private List<PhotoFeed> messagesPhotos = new ArrayList<>();

    public Message(int mId, int mUser_id, int mFrom_id, long mDate, boolean mReadState, String mBody, List<PhotoFeed> messagesPhotos) {
        this.id = mId;
        this.user_id = mUser_id;
        this.fromId = mFrom_id;
        this.date = mDate;
        this.readState = mReadState;
        this.body = mBody;
        this.messagesPhotos.addAll(messagesPhotos);
    }

    public int getFromId() {
        return this.fromId;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getFromName() {
        return this.fromName;
    }

    public void setFromPhotoLink_200(String fromPhotoLink_200) {
        this.fromPhotoLink_200 = fromPhotoLink_200;
    }

    public String getFromPhotoLink_200() {
        return this.fromPhotoLink_200;
    }

    static public void sendMessage(final TextView messageView, String profileId) {
        String message = messageView.getText().toString();
        RequestCreator.sendMessageRequest(profileId, message).executeWithListener(new SendMessageListener(messageView));
    }

    static public void sendChatMessage(final TextView messageView, String chatId) {
        String message = messageView.getText().toString();
        RequestCreator.sendChatMessageRequest(chatId, message).executeWithListener(new SendMessageListener(messageView));
    }

    public List<PhotoFeed> getMessagesPhotos() {
        return messagesPhotos;
    }

    public static final class SendMessageListener extends VKRequest.VKRequestListener {
        private TextView msgView;

        public SendMessageListener(TextView msgView) {
            this.msgView = msgView;
        }

        @Override
        public void onComplete(VKResponse response) {
            super.onComplete(response);
            this.msgView.setText("");
        }

        @Override
        public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
            super.attemptFailed(request, attemptNumber, totalAttempts);
            Log.d("VkDemoApp", "attemptFailed " + request + " " + attemptNumber + " " + totalAttempts);
        }

        @Override
        public void onError(VKError error) {
            super.onError(error);
            Log.d("VkDemoApp", "onError: " + error);
        }

        @Override
        public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
            super.onProgress(progressType, bytesLoaded, bytesTotal);
            Log.d("VkDemoApp", "onProgress " + progressType + " " + bytesLoaded + " " + bytesTotal);
        }
    }


}
