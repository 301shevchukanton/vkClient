package com.example.podkaifom.vkclient.Activitys;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.podkaifom.vkclient.Adapters.SingleDialogListViewAdapter;
import com.example.podkaifom.vkclient.Classes.Message;
import com.example.podkaifom.vkclient.R;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SingleDialogActivity extends ListActivity {
    private String profileId;
    ListView listView;
    private VKRequest currentRequest;
    private List<Message> messages = new ArrayList<>();
    private SingleDialogListViewAdapter listAdapter;

    @Override
    public Object onRetainNonConfigurationInstance() {
        return this.listAdapter.getItems();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        profileId=getIntent().getStringExtra("userid");
        Log.d("profid", "user id taked" + profileId);
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
        listAdapter = new SingleDialogListViewAdapter(this,messages);
        setListAdapter(listAdapter);
        findViewById(R.id.single_sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message.sendMessage((TextView) findViewById(R.id.single_msgText), profileId);
                startLoading();
            }
        });
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_single_dialog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void startLoading() {
        if (currentRequest != null) {
            currentRequest.cancel();
        }
        currentRequest  = new VKRequest ("messages.getHistory", VKParameters.from(VKApiConst.COUNT, "50", VKApiConst.USER_ID, profileId), VKRequest.HttpMethod.GET);
        currentRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(final VKResponse response) {
                super.onComplete(response);
                Log.d("profid", response.responseString);
                messages.clear();
                try {
                    final JSONArray messagesArray =response.json.getJSONObject("response").getJSONArray("items");

                    VKRequest myrequest=null;
                    VKRequest fromrequest=null;
                    for (int i = 0; i<messagesArray.length(); i++ ){
                        messages.add(new Message(Integer.parseInt(messagesArray.getJSONObject(i).getString("id")),
                                Integer.parseInt(messagesArray.getJSONObject(i).getString("user_id")),
                                Integer.parseInt(messagesArray.getJSONObject(i).getString("from_id")),
                                Long.parseLong(messagesArray.getJSONObject(i).getString("date")),
                                (messagesArray.getJSONObject(i).getString("read_state").equals("1")),
                                messagesArray.getJSONObject(i).getString("body")));
                   myrequest = VKApi.users().get(VKParameters.from(VKApiConst.USER_IDS, String.valueOf(messagesArray.getJSONObject(i).getString("user_id")), VKApiConst.FIELDS,
                                "first_name,last_name,photo_200"));
                        if(Integer.parseInt(messagesArray.getJSONObject(i).getString("user_id"))!=
                                Integer.parseInt(messagesArray.getJSONObject(i).getString("from_id")))
                    fromrequest= VKApi.users().get(VKParameters.from(VKApiConst.USER_IDS, String.valueOf(messagesArray.getJSONObject(i).getString("from_id")), VKApiConst.FIELDS,
                                    "first_name,last_name,photo_200"));
                    }
                    if (myrequest != null) {
                        myrequest.executeWithListener(new VKRequest.VKRequestListener() {
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
                                for (int i = 0; i<messagesArray.length(); i++ ) {
                                    messages.get(i).setUsername(r.getString("first_name") + " " + r.getString("last_name"));
                                    messages.get(i).setUserPhoto_200(r.getString("photo_200"));
                                }
                            }
                            @Override
                            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                                super.attemptFailed(request, attemptNumber, totalAttempts);
                                Log.d("VkDemoApp", "attemptFailed " + request + " " + attemptNumber + " " + totalAttempts);
                            }

                            @Override
                            public void onError(VKError error) {
                                super.onError(error);
                                Log.d("VkDemoApp", "onError: " + error);
                            }

                            @Override
                            public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
                                super.onProgress(progressType, bytesLoaded, bytesTotal);
                                Log.d("VkDemoApp", "onProgress " + progressType + " " + bytesLoaded + " " + bytesTotal);
                            }
                        });
                    }
                    if(fromrequest != null) {
                        fromrequest.executeWithListener(new VKRequest.VKRequestListener() {
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
                                for (int i = 0; i < messagesArray.length(); i++) {
                                    messages.get(i).setFromname(r.getString("first_name") + " " + r.getString("last_name"));
                                    messages.get(i).setFromPhoto_200(r.getString("photo_200"));
                                }
                            }

                            @Override
                            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                                super.attemptFailed(request, attemptNumber, totalAttempts);
                                Log.d("VkDemoApp", "attemptFailed " + request + " " + attemptNumber + " " + totalAttempts);
                            }

                            @Override
                            public void onError(VKError error) {
                                super.onError(error);
                                Log.d("VkDemoApp", "onError: " + error);
                            }

                            @Override
                            public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
                                super.onProgress(progressType, bytesLoaded, bytesTotal);
                                Log.d("VkDemoApp", "onProgress " + progressType + " " + bytesLoaded + " " + bytesTotal);
                            }
                        });
                    }
                }
                catch (Exception e){
                }
                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                super.attemptFailed(request, attemptNumber, totalAttempts);
                Log.d("VkDemoApp", "attemptFailed " + request + " " + attemptNumber + " " + totalAttempts);
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                Log.d("VkDemoApp", "onError: " + error);
            }

            @Override
            public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
                super.onProgress(progressType, bytesLoaded, bytesTotal);
                Log.d("VkDemoApp", "onProgress " + progressType + " " + bytesLoaded + " " + bytesTotal);
            }
        });
    }
}
