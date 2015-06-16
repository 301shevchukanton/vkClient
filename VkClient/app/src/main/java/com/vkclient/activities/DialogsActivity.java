package com.vkclient.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vkclient.entities.RequestCreator;
import com.vkclient.supports.Loger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DialogsActivity extends ListActivity {
    private VKRequest currentRequest;
    ListView listView;
    private List<Dialog> dialogs = new ArrayList<>();
    private DialogsListViewAdapter listAdapter;

    public final class GetHistoryRequestListener extends VKRequest.VKRequestListener {
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
        @Override
        public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
            super.attemptFailed(request, attemptNumber, totalAttempts);
            Loger.log("VkDemoApp", "attemptFailed " + request + " " + attemptNumber + " " + totalAttempts);
        }

        @Override
        public void onError(VKError error) {
            super.onError(error);
            Loger.log("VkDemoApp", "onError: " + error);
        }

        @Override
        public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
            super.onProgress(progressType, bytesLoaded, bytesTotal);
            Loger.log("VkDemoApp", "onProgress " + progressType + " " + bytesLoaded + " " + bytesTotal);
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
        listView = (ListView)findViewById(android.R.id.list);
        Object items = getLastNonConfigurationInstance();
        if (items != null) {
            dialogs =  ((List<Dialog>) items);
            listAdapter = new DialogsListViewAdapter(this,dialogs);
            setListAdapter(listAdapter);
            listAdapter.notifyDataSetChanged();
        }
        else if (VKSdk.wakeUpSession()) {
            startLoading();
        }

        ((ListView) findViewById(android.R.id.list)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ((TextView) view.findViewById(R.id.dialog_name)).getText();
                for (int i = 0; i < dialogs.size(); i++) {
                    if (dialogs.get(i).getUsername() == ((TextView) view.findViewById(R.id.dialog_name)).getText()) {
                        startSingleDialogApiCall(dialogs.get(i).getUser_id());
                        break;
                    }
                }
                Loger.log("VkList", "id: " + id);
            }
        });
        listAdapter = new DialogsListViewAdapter(this,dialogs);
        setListAdapter(listAdapter);
    }
    private void startSingleDialogApiCall(int user_id){
            Intent i = new Intent(this, SingleDialogActivity.class);
            i.putExtra("userid", String.valueOf(user_id));
            startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        VKUIHelper.onResume(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        VKUIHelper.onDestroy(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        VKUIHelper.onActivityResult(this, requestCode, resultCode, data);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dialogs, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startLoading() {
        if (currentRequest != null) {
            currentRequest.cancel();
        }
            currentRequest  = RequestCreator.getDialogs();
            currentRequest.executeWithListener(new GetHistoryRequestListener());
    }
}