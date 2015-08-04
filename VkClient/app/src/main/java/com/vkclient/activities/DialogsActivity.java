package com.vkclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.podkaifom.vkclient.R;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKBatchRequest;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vkclient.adapters.DialogsListAdapter;
import com.vkclient.entities.Dialog;
import com.vkclient.listeners.AbstractRequestListener;
import com.vkclient.parsers.MessageParser;
import com.vkclient.parsers.UserParser;
import com.vkclient.supports.Logger;
import com.vkclient.supports.RequestCreator;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class DialogsActivity extends VkSdkActivity {
    private VKRequest currentRequest;
    private ListView listView;
    private List<Dialog> dialogs = new ArrayList<>();
    private DialogsListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listView = (ListView) findViewById(R.id.lvDialogs);
        if (VKSdk.wakeUpSession()) {
            startLoading();
        }
        this.listView.setOnItemClickListener(this.dialogClickListener);
        this.listAdapter = new DialogsListAdapter(this, this.dialogs);
        this.listView.setAdapter(this.listAdapter);
    }

    private void startSingleDialogApiCall(int user_id) {
        Intent i = new Intent(this, SingleDialogActivity.class);
        i.putExtra("userid", String.valueOf(user_id));
        startActivity(i);
    }

    private void startLoading() {
        if (this.currentRequest != null) {
            this.currentRequest.cancel();
        }
        this.currentRequest = RequestCreator.getDialogs();
        this.currentRequest.executeWithListener(this.getHistoryRequestListener);
    }

    private final AbstractRequestListener getHistoryRequestListener = new AbstractRequestListener() {
        @Override
        public void onComplete(final VKResponse response) {
            super.onComplete(response);
            Logger.logDebug("profid", response.responseString);
            dialogs.clear();
            dialogs.addAll(new MessageParser().getDialogsList(response));
            VKRequest[] requests = new VKRequest[dialogs.size()];
            for (int i = 0; i < dialogs.size(); i++) {
                requests[i] = RequestCreator.getUserById(String.valueOf(dialogs.get(i).getUser_id()));
            }
            VKBatchRequest batch = new VKBatchRequest(requests);
            batch.executeWithListener(batchListener);
        }
    };
    private final VKBatchRequest.VKBatchRequestListener batchListener = new VKBatchRequest.VKBatchRequestListener() {
        @Override
        public void onComplete(VKResponse[] responses) {
            super.onComplete(responses);
            try {
                setUserInfo(responses);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void setUserInfo(VKResponse[] responses) throws JSONException {
            for (int i = 0; i < responses.length; i++) {
                dialogs.get(i).setDialogUserInfo(new UserParser().parseUserName(responses[i]));
            }
            listAdapter.notifyDataSetChanged();
        }
    };
    private final AdapterView.OnItemClickListener dialogClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {
            ((TextView) view.findViewById(R.id.tvDialogName)).getText();
            for (int i = 0; i < dialogs.size(); i++) {
                if (dialogs.get(i).getUsername() == ((TextView) view.findViewById(R.id.tvDialogName)).getText()) {
                    startSingleDialogApiCall(dialogs.get(i).getUser_id());
                    break;
                }
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_messages, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_messages:
                startLoading();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    int getLayoutResource() {
        return R.layout.activity_dialogs;
    }
}
