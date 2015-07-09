package com.vkclient.entities;

import android.util.Log;
import android.widget.TextView;

import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import java.util.ArrayList;
import java.util.List;

public class Message extends AbstractContentEntity {
    private final int from_id;
    private String fromPhotoLink_200 = "";
    private String fromname = "";
    private List<PhotoFeed> messagesPhotos = new ArrayList<>();

    public Message(int mId, int mUser_id, int mFrom_id, long mDate, boolean mReadState, String mBody, List<PhotoFeed> messagesPhotos) {
        id = mId;
        user_id = mUser_id;
        this.from_id = mFrom_id;
        date = mDate;
        readState = mReadState;
        body = mBody;
        this.messagesPhotos.addAll(messagesPhotos);
    }

    public int getFrom_id() {
        return this.from_id;
    }

    public void setFromname(String fromname) {
        this.fromname = fromname;
    }

    public String getFromname() {
        return this.fromname;
    }

    public void setFromPhotoLink_200(String fromPhotoLink_200) {
        this.fromPhotoLink_200 = fromPhotoLink_200;
    }

    public String getFromPhotoLink_200() {
        return this.fromPhotoLink_200;
    }

    static public void sendMessage(final TextView msgView, String profileId) {
        String msg = msgView.getText().toString();
        VKRequest currentRequest = new VKRequest("messages.send", VKParameters.from(VKApiConst.USER_ID, profileId, VKApiConst.MESSAGE, msg), VKRequest.HttpMethod.GET);
        currentRequest.executeWithListener(new SendMessageListener(msgView));
    }

    public void setMessagesPhotos(List<PhotoFeed> messagesPhotos) {
        this.messagesPhotos = messagesPhotos;
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
