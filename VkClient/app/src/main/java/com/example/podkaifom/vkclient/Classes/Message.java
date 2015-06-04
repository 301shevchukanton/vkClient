package com.example.podkaifom.vkclient.Classes;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.joda.time.DateTime;

import java.util.TimeZone;

/**
 * Created by pod kaifom on 03.06.2015.
 */
public class Message {
    private final int id;
    private final int user_id;
    private final int from_id;
    private final long date;
    private final boolean readState;
    private final String body;
    private String userPhoto_200="";
    private String fromPhoto_200="";
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

    public DateTime getParsedDate()
    {
        DateTime dateTime = new DateTime( ( (long)this.date * 1000L + TimeZone.getDefault().getRawOffset()));

        return dateTime;
    }

    public void setFromname(String fromname) {
        this.fromname = fromname;
    }

    public String getFromname() {
        return fromname;
    }

    public void setFromPhoto_200(String fromPhoto_200) {
        this.fromPhoto_200 = fromPhoto_200;
    }

    public void setUserPhoto_200(String userPhoto_200) {
        this.userPhoto_200 = userPhoto_200;
    }

    public String getUserPhoto_200() {
        return userPhoto_200;
    }

    public String getFromPhoto_200() {
        return fromPhoto_200;
    }

    static public void sendMessage(final TextView msgView, String profileId){

        String msg = msgView.getText().toString();
       VKRequest currentRequest  = new VKRequest("messages.send", VKParameters.from(VKApiConst.USER_ID, profileId, VKApiConst.MESSAGE, msg), VKRequest.HttpMethod.GET);
        currentRequest.executeWithListener(new VKRequest.VKRequestListener() {
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
        });
    }

}
