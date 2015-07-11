package com.vkclient.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.podkaifom.vkclient.R;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vkclient.entities.AbstractRequestListener;
import com.vkclient.entities.Message;
import com.vkclient.entities.RequestCreator;
import com.vkclient.entities.User;
import com.vkclient.parsers.UserParser;
import com.vkclient.supports.Logger;
import com.vkclient.supports.PhotoLoader;


public class SendMessageActivity extends VkSdkActivity {

    private VKRequest currentRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        profileId = getIntent().getStringExtra("id");
        Logger.logDebug("profid", "profileidSended " + profileId);
        startLoading();
        super.onCreate(savedInstanceState);
        VKUIHelper.onCreate(this);
        setContentView(R.layout.activity_send_message);
        super.onCreateDrawer();
        findViewById(R.id.btSendMessage).setOnClickListener(this.sendMessageClick);
    }

    private void startLoading() {
        if (this.currentRequest != null) {
            this.currentRequest.cancel();
        }
        Logger.logDebug("profid", "onComplete " + profileId);
        this.currentRequest = RequestCreator.getFullUserById(profileId);
        this.currentRequest.executeWithListener(this.sendMessageRequest);
    }

    private final AbstractRequestListener sendMessageRequest = new AbstractRequestListener() {
        @Override
        public void onComplete(VKResponse response) {
            super.onComplete(response);
            setUserInfo(response);
        }

        private void setUserInfo(VKResponse response) {
            User responseUser = new UserParser().parseUserName(response);
            ((TextView) findViewById(R.id.tvRecipientName)).setText(responseUser.getName());
            if (responseUser.getPhotoMax() != null)
                PhotoLoader.loadPhoto(getApplicationContext(), responseUser.getPhotoMax(), (ImageView) findViewById(R.id.ivMessagePhoto));
        }
    };
    private final View.OnClickListener sendMessageClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Message.sendMessage((TextView) findViewById(R.id.etMessageText), profileId);
        }
    };
}

