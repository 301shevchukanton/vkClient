package com.vkclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.vkclient.adapters.DialogsListViewAdapter;
import com.vkclient.entities.Dialog;
import com.example.podkaifom.vkclient.R;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKBatchRequest;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vkclient.entities.RequestCreator;
import com.vkclient.entities.AbstractRequestListener;
import com.vkclient.supports.JSONParser;
import com.vkclient.supports.Loger;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class DialogsActivity extends VkSdkActivity {
    private VKRequest currentRequest;
    private ListView listView;
    private List<Dialog> dialogs = new ArrayList<>();
    private DialogsListViewAdapter listAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VKUIHelper.onCreate(this);
        setContentView(R.layout.activity_dialogs);

        listView = (ListView)findViewById(R.id.lwDialogs);
        Object items = getLastNonConfigurationInstance();
        if (items != null) {
            this.dialogs =  ((List<Dialog>) items);
            this.listAdapter = new DialogsListViewAdapter(this,this.dialogs);
            this.listView.setAdapter(this.listAdapter);
            this.listAdapter.notifyDataSetChanged();
        }
        else if (VKSdk.wakeUpSession()) {
            startLoading();
        }

        this.listView.setOnItemClickListener(this.dialogClickListener);
        this.listAdapter = new DialogsListViewAdapter(this,this.dialogs);
        this.listView.setAdapter(this.listAdapter);
    }

    private void startSingleDialogApiCall(int user_id){
            Intent i = new Intent(this, SingleDialogActivity.class);
            i.putExtra("userid", String.valueOf(user_id));
            startActivity(i);
    }
    private void startLoading() {
        if (this.currentRequest != null) {
            this.currentRequest.cancel();
        }
            this.currentRequest  = RequestCreator.getDialogs();
            this.currentRequest.executeWithListener(this.getHistoryRequestListener);
    }
    private final AbstractRequestListener getHistoryRequestListener = new AbstractRequestListener() {
            @Override
        public void onComplete(final VKResponse response) {
            super.onComplete(response);
            Loger.log("profid", response.responseString);
            dialogs.clear();
            try {
                JSONParser history = new JSONParser(response.json);
                history.parseMessages();
                VKRequest[] myrequests = new VKRequest[history.length()];
                for (int i = 0; i<history.length(); i++ ){
                    dialogs.add(Dialog.parseDialog(history.getMessage(i)));
                    myrequests[i]= RequestCreator.getUserById(history.getUser(i));
                }
                VKBatchRequest batch = new VKBatchRequest(myrequests);
                batch.executeWithListener(batchListener);
            }
            catch (Exception e){
                Loger.log("profid", e.toString());
            }
            listAdapter.notifyDataSetChanged();
        }
    };
    private final VKBatchRequest.VKBatchRequestListener batchListener = new VKBatchRequest.VKBatchRequestListener(){
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
            for(int i=0;i<responses.length;i++) {
                JSONParser userParser = new JSONParser(responses[i].json);
                dialogs.get(i).setUsername(userParser.getUserName());
                if(userParser.photoAvailable()) dialogs.get(i).setPhoto(userParser.getPhoto());
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
            Loger.log("VkList", "id: " + id);
        }
    };
}
