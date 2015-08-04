package com.vkclient.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.podkaifom.vkclient.R;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKUIHelper;
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


public class SingleDialogActivity extends VkSdkActivity {
    private final String COUNT = "150";
    private ListView messagesList;
    private VKRequest currentRequest;
    private List<Message> messages = new ArrayList<>();
    private MessagesListAdapter listAdapter;
    private VKRequest ownRequest = null;
    private VKRequest fromRequest = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        profileId = getIntent().getStringExtra("userid");
        Logger.logDebug("profid", "ic_user id taked" + profileId);
        super.onCreate(savedInstanceState);
        this.messagesList = (ListView) findViewById(R.id.lvSingleDialog);
        if (VKSdk.wakeUpSession()) {
            startLoading();
        }
        Object items = getLastNonConfigurationInstance();
        if (items != null) {
            this.messages = ((ArrayList<Message>) items);
        }
        this.listAdapter = new MessagesListAdapter(this, this.messages);
        this.messagesList.setAdapter(this.listAdapter);
        findViewById(R.id.btSendDialogMessage).setOnClickListener(this.singleDialogClickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        VKUIHelper.onResume(this);
        if (VKSdk.wakeUpSession()) {
            startLoading();
        }
    }

    private void startLoading() {
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
            if (v == findViewById(R.id.btSendDialogMessage)) {
                Message.sendMessage((TextView) findViewById(R.id.etMessageText), profileId);
                try {
                    Thread.sleep(50, 0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startLoading();
            }
        }
    };

    @Override
    int getLayoutResource() {
        return R.layout.activity_single_dialog;
    }
}
