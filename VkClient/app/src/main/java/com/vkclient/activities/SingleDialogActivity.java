package com.vkclient.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.vkclient.entities.Message;
import com.vkclient.adapters.MessagesListAdapter;
import com.example.podkaifom.vkclient.R;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vkclient.entities.RequestCreator;
import com.vkclient.entities.AbstractRequestListener;
import com.vkclient.parsers.MessageParser;
import com.vkclient.supports.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SingleDialogActivity extends VkSdkActivity {
    private final String COUNT = "50";
    private ListView listView;
    private VKRequest currentRequest;
    private List<Message> messages = new ArrayList<>();
    private MessagesListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        profileId = getIntent().getStringExtra("userid");
        Logger.logDebug("profid", "ic_user id taked" + profileId);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_dialog);
        super.onCreateDrawer();
        this.listView = (ListView) findViewById(R.id.lvSingleDialog);
        VKUIHelper.onCreate(this);
        if (VKSdk.wakeUpSession()) {
            startLoading();
        }
        Object items = getLastNonConfigurationInstance();
        if (items != null) {
            this.messages = ((ArrayList<Message>) items);
        }
        this.listAdapter = new MessagesListAdapter(this, this.messages);
        this.listView.setAdapter(this.listAdapter);
        findViewById(R.id.btSendDialogMessage).setOnClickListener(new SingleDialogClickListener());
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
        this.currentRequest = new VKRequest("messages.getHistory", VKParameters.from(VKApiConst.COUNT, this.COUNT, VKApiConst.USER_ID, this.profileId), VKRequest.HttpMethod.GET);
        this.currentRequest.executeWithListener(new GetHistoryRequest());
    }

    final class GetHistoryRequest extends AbstractRequestListener {
        @Override
        public void onComplete(final VKResponse response) {
            super.onComplete(response);
            try {
                VKRequest ownRequest = null;
                VKRequest fromRequest = null;
                messages.addAll(new MessageParser(response.json).getMessagesList());
                for (int i = 0; i < messages.size(); i++) {
                    ownRequest = RequestCreator.getUserById(String.valueOf(messages.get(i).getUser_id()));
                    if (messages.get(i).getUser_id() != messages.get(i).getFrom_id()) {
                        fromRequest = RequestCreator.getUserById(String.valueOf(messages.get(i).getFrom_id()));
                    }
                }
                if (ownRequest != null) ownRequestExecution(ownRequest, messages.size());
                if (fromRequest != null) fromRequestExecution(fromRequest, messages.size());
            } catch (Exception e) {
            }
            listAdapter.notifyDataSetChanged();
        }

        private void ownRequestExecution(VKRequest request, final int arrayLength) {
            request.executeWithListener(new SingleDialogOwnerRequest(arrayLength));
        }

        private void fromRequestExecution(VKRequest request, final int arrayLength) {
            request.executeWithListener(new SingleDialogFromRequest(arrayLength));
        }
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
            listAdapter.notifyDataSetChanged();
        }

        private void setMessageInfo(VKResponse response) throws JSONException {
            JSONObject r = response.json.getJSONArray("response").getJSONObject(0);
            for (int i = 0; i < arrayLength; i++) {
                messages.get(i).setUsername(r.getString("first_name") + " " + r.getString("last_name"));
                messages.get(i).setUserPhotoLink_200(r.getString("photo_200"));
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

    public final class SingleDialogClickListener implements View.OnClickListener {
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
    }
}
