package com.vkclient.loaders;

import com.vk.sdk.api.VKBatchRequest;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vkclient.entities.Dialog;
import com.vkclient.listeners.AbstractRequestListener;
import com.vkclient.listeners.DialogsLoaderListener;
import com.vkclient.parsers.MessageParser;
import com.vkclient.parsers.UserParser;
import com.vkclient.supports.RequestCreator;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class NetworkDialogLoader implements DialogsLoader {

    final List<Dialog> oldDialogs = new ArrayList<>();
    final List<Dialog> dialogs = new ArrayList<>();

    @Override
    public void load(final DialogsLoaderListener dialogsLoaderListener) {
        final VKBatchRequest.VKBatchRequestListener batchListener = new VKBatchRequest.VKBatchRequestListener() {
            @Override
            public void onComplete(VKResponse[] responses) {
                super.onComplete(responses);
                try {
                    setUserInfo(responses);
                    oldDialogs.clear();
                    oldDialogs.addAll(dialogs);
                    dialogsLoaderListener.onLoad(dialogs);
                } catch (JSONException e) {
                    e.printStackTrace();
                    dialogsLoaderListener.onError();
                }
            }

            public void onError(VKError error) {
                dialogsLoaderListener.onError();
            }

            private void setUserInfo(VKResponse[] responses) throws JSONException {
                for (int i = 0; i < responses.length; i++) {
                    dialogs.get(i).setDialogUserInfo(new UserParser().parseUserName(responses[i]));
                }
            }
        };
        AbstractRequestListener getHistoryRequestListener = new AbstractRequestListener() {
            @Override
            public void onComplete(final VKResponse response) {
                super.onComplete(response);
                dialogs.clear();
                dialogs.addAll(new MessageParser().getDialogsList(response));
                VKRequest[] requests = new VKRequest[dialogs.size()];
                for (int i = 0; i < dialogs.size(); i++) {
                    requests[i] = RequestCreator.getUserById(String.valueOf(dialogs.get(i).getUser_id()));
                }
                new VKBatchRequest(requests).executeWithListener(batchListener);
            }

            public void onError(VKError error) {
                super.onError(error);
                dialogsLoaderListener.onError();
            }
        };
        VKRequest currentRequest = RequestCreator.getDialogs();
        currentRequest.executeWithListener(getHistoryRequestListener);
    }
}
