package com.vkclient.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.podkaifom.vkclient.R;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vkclient.adapters.MessagesListAdapter;
import com.vkclient.entities.Message;
import com.vkclient.entities.User;
import com.vkclient.listeners.AbstractRequestListener;
import com.vkclient.parsers.MessageParser;
import com.vkclient.parsers.UserParser;
import com.vkclient.supports.Logger;
import com.vkclient.supports.RequestCreator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SingleDialogFragment extends Fragment {
    private final String COUNT = "150";
    private ListView messagesList;
    private Button sendDialog;
    private TextView messageBody;
    private String profileId;
    private VKRequest currentRequest;
    private List<Message> messages = new ArrayList<>();
    private MessagesListAdapter listAdapter;
    private VKRequest ownRequest = null;
    private VKRequest fromRequest = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewHierarchy = inflater.inflate(R.layout.fragment_single_dialog, container, false);
        this.profileId = getActivity().getIntent().getStringExtra("userid");
        Logger.logDebug("profid", "ic_user id taked" + profileId);
        super.onCreate(savedInstanceState);
        this.sendDialog = (Button) viewHierarchy.findViewById(R.id.btSendDialogMessage);
        this.messagesList = (ListView) viewHierarchy.findViewById(R.id.lvSingleDialog);
        this.messageBody = (TextView) viewHierarchy.findViewById(R.id.etMessageText);
        if (VKSdk.wakeUpSession()) {
            startLoading();
        }
        this.listAdapter = new MessagesListAdapter(getActivity(), this.messages);
        this.messagesList.setAdapter(this.listAdapter);
        this.sendDialog.setOnClickListener(this.singleDialogClickListener);
        return viewHierarchy;
    }

    public void startLoading() {
        if (this.currentRequest != null) {
            this.currentRequest.cancel();
        }
        this.messages.clear();
        this.currentRequest = RequestCreator.getHistory(this.COUNT, this.profileId);
        this.currentRequest.executeWithListener(this.getHistoryRequest);
    }

    final AbstractRequestListener getHistoryRequest = new AbstractRequestListener() {
        @Override
        public void onComplete(final VKResponse response) {
            super.onComplete(response);
            ownRequest = null;
            fromRequest = null;
            messages.addAll(new MessageParser().getMessagesList(response));
            for (int i = 0; i < messages.size(); i++) {
                ownRequest = RequestCreator.getUserById(String.valueOf(messages.get(i).getUser_id()));
                if (messages.get(i).getUser_id() != messages.get(i).getFrom_id()) {
                    fromRequest = RequestCreator.getUserById(String.valueOf(messages.get(i).getFrom_id()));
                }
            }
            if (ownRequest != null) ownRequestExecution(ownRequest, messages.size());
        }
    };

    private void ownRequestExecution(VKRequest request, final int arrayLength) {
        request.executeWithListener(new SingleDialogOwnerRequest(arrayLength));
    }

    private void fromRequestExecution(VKRequest request, final int arrayLength) {
        request.executeWithListener(new SingleDialogFromRequest(arrayLength));
    }

    final class SingleDialogOwnerRequest extends AbstractRequestListener {
        private int arrayLength;

        public SingleDialogOwnerRequest(int length) {
            this.arrayLength = length;
        }

        @Override
        public void onComplete(VKResponse response) {
            super.onComplete(response);
            try {
                setMessageInfo(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (fromRequest != null) fromRequestExecution(fromRequest, messages.size());
            else listAdapter.notifyDataSetChanged();
        }

        private void setMessageInfo(VKResponse response) throws JSONException {
            User responseUser = new UserParser().parseUserName(response);
            for (int i = 0; i < arrayLength; i++) {
                messages.get(i).setUsername(responseUser.getName());
                messages.get(i).setUserPhotoLink_200(responseUser.getPhoto());
            }
        }
    }

    final class SingleDialogFromRequest extends AbstractRequestListener {
        private int arrayLength;

        public SingleDialogFromRequest(int length) {
            this.arrayLength = length;
        }

        @Override
        public void onComplete(VKResponse response) {
            super.onComplete(response);
            try {
                setMessageInfo(response);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            listAdapter.notifyDataSetChanged();
        }

        private void setMessageInfo(VKResponse response) throws JSONException {
            JSONObject r = response.json.getJSONArray("response").getJSONObject(0);
            for (int i = 0; i < arrayLength; i++) {
                messages.get(i).setFromname(r.getString("first_name") + " " + r.getString("last_name"));
                messages.get(i).setFromPhotoLink_200(r.getString("photo_200"));
            }
        }
    }

    public final View.OnClickListener singleDialogClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            if (v == sendDialog) {
                Message.sendMessage(messageBody, profileId);
                try {
                    Thread.sleep(50, 0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startLoading();
            }
        }
    };

}
