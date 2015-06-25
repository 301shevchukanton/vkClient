package com.vkclient.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vkclient.entities.Message;
import com.example.podkaifom.vkclient.R;
import com.squareup.picasso.Picasso;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vkclient.entities.RequestCreator;
import com.vkclient.entities.AbstractRequestListener;
import com.vkclient.supports.Loger;

import org.json.JSONException;
import org.json.JSONObject;


public class SendMessageActivity extends VkSdkActivity {

    private VKRequest currentRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        profileId=getIntent().getStringExtra("id");
        Loger.log("profid", "profileidSended " + profileId);
        startLoading();
        super.onCreate(savedInstanceState);
        VKUIHelper.onCreate(this);
        setContentView(R.layout.activity_send_message);
        findViewById(R.id.btSendMessage).setOnClickListener(this.sendMessageClick);
    }
    private void startLoading() {
        if (this.currentRequest != null) {
            this.currentRequest.cancel();
        }
        Loger.log("profid", "onComplete " + profileId);
        this.currentRequest = RequestCreator.getFullUserById(profileId);
        this.currentRequest.executeWithListener(this.sendMessageRequest);
    }
    private final AbstractRequestListener sendMessageRequest = new AbstractRequestListener(){
        @Override
        public void onComplete(VKResponse response) {
            super.onComplete(response);
            Loger.log("profid", "onComplete " + response);
            setUserInfo(response);
        }

        private void setUserInfo(VKResponse response) {
            try {
                Loger.log("profid", "seting inf " + profileId);
                JSONObject r = response.json.getJSONArray("response").getJSONObject(0);
                if (r.getString("last_name") != null && r.getString("first_name") != null)
                    ((TextView) findViewById(R.id.tvRecipientName)).setText(r.getString("first_name") + " " + r.getString("last_name"));
                if(r.getString("photo_200")!=null) Picasso.with(getApplicationContext())
                        .load(r.getString("photo_200"))
                        .into((ImageView)findViewById(R.id.ivMessagePhoto));
            } catch (JSONException e) {
                Log.e(e.getMessage(), e.toString());
            }
        }
    };
    private final View.OnClickListener sendMessageClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Message.sendMessage((TextView) findViewById(R.id.etMessageText), profileId);
        }
    };
}

