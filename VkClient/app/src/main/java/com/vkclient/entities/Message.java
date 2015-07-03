package com.vkclient.entities;

import android.util.Log;
import android.widget.TextView;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.TimeZone;

public class Message {
    private final int id;
    private final int user_id;
    private final int from_id;
    private final long date;
    private final boolean readState;
    private final String body;
    private String userPhotoLink_200 ="";
    private String fromPhotoLink_200 ="";
    private  String username="";
    private  String fromname="";
    public Message(int id, int user_id, int from_id, long date, boolean readState, String body) {
        this.id = id;
        this.user_id = user_id;
        this.from_id = from_id;
        this.date = date;
        this.readState = readState;
        this.body = body;
    }

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getFrom_id() {
        return from_id;
    }

    public long getDate() {
        return date;
    }

    public boolean isReadState() {
        return readState;
    }

    public String getBody() {
        return body;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFromname(String fromname) {
        this.fromname = fromname;
    }

    public String getFromname() {
        return fromname;
    }

    public void setFromPhotoLink_200(String fromPhotoLink_200) {
        this.fromPhotoLink_200 = fromPhotoLink_200;
    }

    public void setUserPhotoLink_200(String userPhotoLink_200) {
        this.userPhotoLink_200 = userPhotoLink_200;
    }

    public String getUserPhotoLink_200() {
        return userPhotoLink_200;
    }

    public String getFromPhotoLink_200() {
        return fromPhotoLink_200;
    }

    static public void sendMessage(final TextView msgView, String profileId){
        String msg = msgView.getText().toString();
        VKRequest currentRequest  = new VKRequest("messages.send", VKParameters.from(VKApiConst.USER_ID, profileId, VKApiConst.MESSAGE, msg), VKRequest.HttpMethod.GET);
        currentRequest.executeWithListener(new SendMessageListener(msgView));
    }
    public static final class SendMessageListener extends VKRequest.VKRequestListener
    {
        private TextView msgView;
        public SendMessageListener(TextView msgView)
        {
            this.msgView=msgView;
        }
        @Override
        public void onComplete(VKResponse response) {
            super.onComplete(response);
            msgView.setText("");
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
