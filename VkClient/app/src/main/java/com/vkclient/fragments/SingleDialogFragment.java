package com.vkclient.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.podkaifom.vkclient.R;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKBatchRequest;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vkclient.adapters.MessagesListAdapter;
import com.vkclient.entities.Message;
import com.vkclient.entities.User;
import com.vkclient.listeners.AbstractRequestListener;
import com.vkclient.parsers.MessageParser;
import com.vkclient.parsers.UserParser;
import com.vkclient.supports.RequestCreator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SingleDialogFragment extends Fragment {
    public static final String PROFILE_EXTRA = "userid";
    public static final String PROFILE_CHAT_ID = "chatid";
    private static final String RESPONSE_PARAM = "response";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String PHOTO_SIZE = "photo_200";
    private static final String ZERO_CHAT_ID = "0";
    private final String MESSAGES_COUNT = "12";
    private ListView messagesList;
    private Button sendDialog;
    private TextView messageBody;
    private String profileId;
    private String chatId;
    private VKRequest currentRequest;
    private List<Message> messages = new ArrayList<>();
    private MessagesListAdapter listAdapter;
    private VKRequest ownRequest = null;
    private VKRequest fromRequest = null;

    @Override
    public void onResume() {
        super.onResume();
        VKUIHelper.onResume(getActivity());
        if (VKSdk.wakeUpSession()) {
            startLoading();
        }
    }

    public static SingleDialogFragment newInstance(int profileId, int chatId) {
        Bundle args = new Bundle();
        args.putString(PROFILE_EXTRA, String.valueOf(profileId));
        args.putString(PROFILE_CHAT_ID, String.valueOf(chatId));
        SingleDialogFragment fragment = new SingleDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewHierarchy = inflater.inflate(R.layout.fragment_single_dialog, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            profileId = bundle.getString(PROFILE_EXTRA, "30661428");
            chatId = bundle.getString(PROFILE_CHAT_ID, "30661428");
        } else {
            this.profileId = getActivity().getIntent().getStringExtra(DialogFriendListFragment.PROFILE_EXTRA);
            this.chatId = getActivity().getIntent().getStringExtra(DialogFriendListFragment.PROFILE_CHAT_ID);
        }
        super.onCreate(savedInstanceState);
        findViews(viewHierarchy);
        if (VKSdk.wakeUpSession()) {
            startLoading();
        }
        this.listAdapter = new MessagesListAdapter(getActivity(), this.messages);
        this.messagesList.setAdapter(this.listAdapter);
        this.sendDialog.setOnClickListener(this.singleDialogClickListener);
        return viewHierarchy;
    }

    private void findViews(View viewHierarchy) {
        this.sendDialog = (Button) viewHierarchy.findViewById(R.id.btSendDialogMessage);
        this.messagesList = (ListView) viewHierarchy.findViewById(R.id.lvSingleDialog);
        this.messageBody = (TextView) viewHierarchy.findViewById(R.id.etMessageText);
    }

    private void startLoading() {
        if (this.currentRequest != null) {
            this.currentRequest.cancel();
        }
        if (!chatId.equals(ZERO_CHAT_ID)) {
            this.currentRequest = RequestCreator.getHistoryById(this.MESSAGES_COUNT, this.chatId);
            this.currentRequest.executeWithListener(this.getHistoryByChatIdRequestListener);
        } else {
            this.currentRequest = RequestCreator.getHistory(this.profileId);
            this.currentRequest.executeWithListener(this.getHistoryRequest);
        }
    }

    final AbstractRequestListener getHistoryByChatIdRequestListener = new AbstractRequestListener() {
        @Override
        public void onComplete(final VKResponse response) {
            super.onComplete(response);
            ownRequest = null;
            messages.clear();
            messages.addAll(new MessageParser().getMessagesList(response));
            VKRequest[] requests = new VKRequest[messages.size()];
            for (int i = 0; i < messages.size(); i++) {
                requests[i] = RequestCreator.getUserById(String.valueOf(messages.get(i).getUser_id()));
            }
            VKBatchRequest batch = new VKBatchRequest(requests);
            batch.executeWithListener(singleDialogChatRequest);
            listAdapter.notifyDataSetChanged();
        }
    };
    final AbstractRequestListener getHistoryRequest = new AbstractRequestListener() {
        @Override
        public void onComplete(final VKResponse response) {
            super.onComplete(response);
            ownRequest = null;
            fromRequest = null;
            messages.clear();
            messages.addAll(new MessageParser().getMessagesList(response));
            for (int i = 0; i < messages.size(); i++) {
                ownRequest = RequestCreator.getUserById(String.valueOf(messages.get(i).getUser_id()));
                if (messages.get(i).getUser_id() != messages.get(i).getFromId()) {
                    fromRequest = RequestCreator.getUserById(String.valueOf(messages.get(i).getFromId()));
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

    private final VKBatchRequest.VKBatchRequestListener singleDialogChatRequest = new VKBatchRequest.VKBatchRequestListener() {
        @Override
        public void onComplete(VKResponse[] responses) {
            super.onComplete(responses);
            try {
                setMessageInfo(responses);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            listAdapter.notifyDataSetChanged();
        }

        @Override
        public void onError(VKError error) {
            super.onError(error);
            startLoading();
        }

        private void setMessageInfo(VKResponse[] responses) throws JSONException {
            for (int i = 0; i < responses.length; i++) {
                User responseUser = new UserParser().parseUserName(responses[i]);
                messages.get(i).setUsername(responseUser.getName());
                messages.get(i).setUserPhotoLink_200(responseUser.getPhoto());
                messages.get(i).setFromName(responseUser.getName());
                messages.get(i).setFromPhotoLink_200(responseUser.getPhoto());
            }
        }
    };

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
                listAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        private void setMessageInfo(VKResponse response) throws JSONException {
            JSONObject r = response.json.getJSONArray(RESPONSE_PARAM).getJSONObject(0);
            for (int i = 0; i < arrayLength; i++) {
                messages.get(i).setFromName(r.getString(FIRST_NAME) + " " + r.getString(LAST_NAME));
                messages.get(i).setFromPhotoLink_200(r.getString(PHOTO_SIZE));
            }
        }
    }

    public final View.OnClickListener singleDialogClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            if (view == sendDialog) {
                if (!chatId.equals(ZERO_CHAT_ID)) {
                    Message.sendChatMessage(messageBody, chatId);
                } else {
                    Message.sendMessage(messageBody, profileId);
                }
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
