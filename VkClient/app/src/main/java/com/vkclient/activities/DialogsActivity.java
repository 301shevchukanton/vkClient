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
import com.vkclient.entities.RequestListenerMaster;
import com.vkclient.supports.Loger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DialogsActivity extends VkSdkActivity {
    private VKRequest currentRequest;
    ListView listView;
    private List<Dialog> dialogs = new ArrayList<>();
    private DialogsListViewAdapter listAdapter;

    public final class GetHistoryRequestListener extends RequestListenerMaster {
        @Override
        public void onComplete(final VKResponse response) {
            super.onComplete(response);
            Loger.log("profid", response.responseString);
            dialogs.clear();
            try {
                JSONArray messagesArray =response.json.getJSONObject("response").getJSONArray("items");
                VKRequest[] myrequests = new VKRequest[messagesArray.length()];
                for (int i = 0; i<messagesArray.length(); i++ ){
                    dialogs.add(Dialog.parseDialog(messagesArray.getJSONObject(i).getJSONObject("message")));
                    myrequests[i]= RequestCreator.getUserById(String.valueOf(messagesArray.getJSONObject(i).getJSONObject("message").getString("user_id")));
                }
                VKBatchRequest batch = new VKBatchRequest(myrequests);
                batch.executeWithListener(batchListener);
            }
            catch (Exception e){
                Loger.log("profid", e.toString());
            }
            listAdapter.notifyDataSetChanged();
        }
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
                    JSONObject r = responses[i].json.getJSONArray("response").getJSONObject(0);
                    dialogs.get(i).setUsername(r.getString("first_name") + " " + r.getString("last_name"));
                    if(!r.getString("photo_200").isEmpty()) dialogs.get(i).setPhoto(r.getString("photo_200"));

                }
                listAdapter.notifyDataSetChanged();
            }
        };

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VKUIHelper.onCreate(this);
        setContentView(R.layout.activity_dialogs);

        listView = (ListView)findViewById(R.id.lwDialogs);
        Object items = getLastNonConfigurationInstance();
        if (items != null) {
            dialogs =  ((List<Dialog>) items);
            listAdapter = new DialogsListViewAdapter(this,dialogs);
            listView.setAdapter(listAdapter);
            listAdapter.notifyDataSetChanged();
        }
        else if (VKSdk.wakeUpSession()) {
            startLoading();
        }

        ((ListView) findViewById(R.id.lwDialogs)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        });
        listAdapter = new DialogsListViewAdapter(this,dialogs);
        listView.setAdapter(listAdapter);
    }
    private void startSingleDialogApiCall(int user_id){
            Intent i = new Intent(this, SingleDialogActivity.class);
            i.putExtra("userid", String.valueOf(user_id));
            startActivity(i);
    }
    private void startLoading() {
        if (currentRequest != null) {
            currentRequest.cancel();
        }
            currentRequest  = RequestCreator.getDialogs();
            currentRequest.executeWithListener(new GetHistoryRequestListener());
    }
}
