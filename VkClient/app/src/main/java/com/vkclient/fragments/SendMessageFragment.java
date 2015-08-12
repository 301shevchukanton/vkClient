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
            ((TextView) getView().findViewById(R.id.tvRecipientName)).setText(responseUser.getName());
            if (responseUser.getPhotoMax() != null)
                PhotoLoader.loadPhoto(getActivity(), responseUser.getPhotoMax(), (ImageView) getView().findViewById(R.id.ivMessagePhoto));
        }
    };

    private final View.OnClickListener sendMessageClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Message.sendMessage((TextView) getView().findViewById(R.id.etMessageText), profileId);
        }
    };


}
