package com.vkclient.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.vkclient.entities.Message;
import com.vkclient.adapter.MessagesAdapter;
import com.example.podkaifom.vkclient.R;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vkclient.entities.RequestCreator;
import com.vkclient.supports.Loger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SingleDialogActivity extends ListActivity {
    private final String COUNT="50";

    private String profileId;
    ListView listView;
    private VKRequest currentRequest;
    private List<Message> messages = new ArrayList<>();
    private MessagesAdapter listAdapter;
    public final class SingleDialogClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(final View v)
        {
            if(v==findViewById(R.id.single_sendButton))
                {
                    Message.sendMessage((TextView) findViewById(R.id.single_msgText), profileId);
                    try {
                        Thread.sleep(50,0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    startLoading();
                }
            }
        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        profileId=getIntent().getStringExtra("userid");
         Loger.log("profid", "ic_user id taked" + profileId);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_dialog);
        listView = (ListView)findViewById(android.R.id.list);
        VKUIHelper.onCreate(this);
        if (VKSdk.wakeUpSession()) {
            startLoading();
        }
        Object items = getLastNonConfigurationInstance();
        if (items != null) {
            messages =  ((ArrayList<Message>) items);
        }
        listAdapter = new MessagesAdapter(this,messages);
        setListAdapter(listAdapter);
        findViewById(R.id.single_sendButton).setOnClickListener(new SingleDialogClickListener());
    }
    protected void onResume() {
        super.onResume();
        VKUIHelper.onResume(this);
        if (VKSdk.wakeUpSession()) {
            startLoading();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        VKUIHelper.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VKUIHelper.onDestroy(this);
        if (currentRequest != null) {
            currentRequest.cancel();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_single_dialog, menu);
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
        messages.clear();
        currentRequest  = new VKRequest ("messages.getHistory", VKParameters.from(VKApiConst.COUNT, this.COUNT, VKApiConst.USER_ID, this.profileId), VKRequest.HttpMethod.GET);
        currentRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(final VKResponse response) {
                super.onComplete(response);
                try {
                    final JSONArray messagesArray =response.json.getJSONObject("response").getJSONArray("items");

                    VKRequest ownRequest=null;
                    VKRequest fromRequest=null;
                    for (int i = 0; i<messagesArray.length(); i++ ){
                        messages.add(new Message(Integer.parseInt(messagesArray.getJSONObject(i).getString("id")),
                                Integer.parseInt(messagesArray.getJSONObject(i).getString("user_id")),
                                Integer.parseInt(messagesArray.getJSONObject(i).getString("from_id")),
                                Long.parseLong(messagesArray.getJSONObject(i).getString("date")),
                                (messagesArray.getJSONObject(i).getString("read_state").equals("1")),
                                messagesArray.getJSONObject(i).getString("body")));
                    ownRequest = RequestCreator.getUserById(messagesArray.getJSONObject(i).getString("user_id"));
                        if(Integer.parseInt(messagesArray.getJSONObject(i).getString("user_id"))!=
                                Integer.parseInt(messagesArray.getJSONObject(i).getString("from_id")))
                    fromRequest = RequestCreator.getUserById(messagesArray.getJSONObject(i).getString("from_id"));
                    }
                    if (ownRequest != null) ownRequestExecution(ownRequest, messagesArray.length());
                    if(fromRequest != null) fromRequestExecution(fromRequest,messagesArray.length());
                }
                catch (Exception e){
                }
                listAdapter.notifyDataSetChanged();
            }

            private void ownRequestExecution(VKRequest request, final int arrayLength)
            {
                request.executeWithListener(new VKRequest.VKRequestListener() {
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
                            messages.get(i).setUserPhoto_200(r.getString("photo_200"));
                        }
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
                });
            }
            private void fromRequestExecution(VKRequest request, final int arrayLength)
            {
                request.executeWithListener(new VKRequest.VKRequestListener() {
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
                        for (int i = 0; i <arrayLength; i++) {
                            messages.get(i).setFromname(r.getString("first_name") + " " + r.getString("last_name"));
                            messages.get(i).setFromPhoto_200(r.getString("photo_200"));
                        }
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
                });
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
        });
    }
}
