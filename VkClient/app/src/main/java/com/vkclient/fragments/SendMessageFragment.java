package com.vkclient.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.podkaifom.vkclient.R;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vkclient.entities.Message;
import com.vkclient.entities.User;
import com.vkclient.listeners.AbstractRequestListener;
import com.vkclient.parsers.UserParser;
import com.vkclient.supports.Logger;
import com.vkclient.supports.PhotoLoader;
import com.vkclient.supports.RequestCreator;

public class SendMessageFragment extends Fragment {
    private VKRequest currentRequest;
    private String profileId;
    private TextView recipientName;
    private TextView messageText;
    private ImageView messagePhoto;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewHierarchy = inflater.inflate(R.layout.fragment_send_message, container, false);
        VKUIHelper.onCreate(getActivity());
        profileId = getActivity().getIntent().getStringExtra("id");
        Logger.logDebug("profid", "profileidSended " + profileId);
        startLoading();
        super.onCreate(savedInstanceState);
        viewHierarchy.findViewById(R.id.btSendMessage).setOnClickListener(this.sendMessageClick);
        this.recipientName = (TextView) viewHierarchy.findViewById(R.id.tvRecipientName);
        this.messageText = (TextView) viewHierarchy.findViewById(R.id.etMessageText);
        this.messagePhoto = (ImageView) viewHierarchy.findViewById(R.id.ivMessagePhoto);
        return viewHierarchy;
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
            recipientName.setText(responseUser.getName());
            if (responseUser.getPhotoMax() != null)
                PhotoLoader.loadPhoto(getActivity(), responseUser.getPhotoMax(), messagePhoto);
        }
    };

    private final View.OnClickListener sendMessageClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Message.sendMessage(messageText, profileId);
        }
    };


}
