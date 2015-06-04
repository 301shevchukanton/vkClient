package com.example.podkaifom.vkclient.Activitys;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.podkaifom.vkclient.Adapters.FriendListViewAdapter;
import com.example.podkaifom.vkclient.R;
import com.example.podkaifom.vkclient.Classes.User;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKUsersArray;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;


public class FriendsListActivity extends ListActivity {
    private EditText filterText = null;
    private VKRequest currentRequest;
    ListView listView;
    private String profileId;
    private List<User> users = new ArrayList<User>();
    private FriendListViewAdapter listAdapter;

    @Override
    public Object onRetainNonConfigurationInstance() {
        return this.listAdapter.getItems();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            profileId=getIntent().getStringExtra("id");
        Log.d("profid", "profile id taked" + profileId);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);
        listView = (ListView)findViewById(android.R.id.list);

        filterText = (EditText) findViewById(R.id.searchView);
        filterText.addTextChangedListener(filterTextWatcher);
        VKUIHelper.onCreate(this);
        if (VKSdk.wakeUpSession()) {
            startLoading();
        }
        Object items = getLastNonConfigurationInstance();
        if (items != null) {
            users =  ((ArrayList<User>) items);
        }

        listAdapter = new FriendListViewAdapter(this,users);

        filterText = (EditText) findViewById(R.id.searchView);
        filterText.addTextChangedListener(filterTextWatcher);
        ((ListView) findViewById(android.R.id.list)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).getName() == ((TextView) view.findViewById(R.id.text1)).getText()) {
                        Log.d("VkList", "od_photo: " + users.get(i).getPhoto());
                        startUserApiCall(users.get(i).getId());
                        break;
                    }
                }
                Log.d("VkList", "id: " + id);
            }
        });
        setListAdapter(listAdapter);
    }
    private TextWatcher filterTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            List<User> temp = new ArrayList<User>();
            filterText = (EditText) findViewById(R.id.searchView);
            int textlength = filterText.getText().length();
            temp.clear();
            for (int i = 0; i < users.size(); i++)
            {
                if (textlength <= users.get(i).getName().length())
                {
                    if(filterText.getText().toString().equalsIgnoreCase(
                            (String)
                                    users.get(i).getName().subSequence(0,
                                            textlength)))
                    {
                        temp.add(new User(users.get(i).getId(),users.get(i).getName(),users.get(i).getBirthDate(),users.get(i).getPhoto(),"dd.MM"));
                    }
                }
            }
            listView.setAdapter(listAdapter = new FriendListViewAdapter(FriendsListActivity.this,temp));
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        VKUIHelper.onResume(this);
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
        filterText.removeTextChangedListener(filterTextWatcher);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friends_list, menu);
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
        Log.d("profid", "onCompleteFriends " + profileId);

        currentRequest = VKApi.friends().get(VKParameters.from(VKApiConst.USER_ID, profileId, "order", "hints", VKApiConst.COUNT, "222",  VKApiConst.FIELDS, "id,first_name,last_name,bdate,photo_200"));
        currentRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                VKUsersArray usersArray = (VKUsersArray) response.parsedModel;
                users.clear();
                final String[] formats = new String[]{"dd.MM.yyyy", "dd.MM"};

                for (VKApiUserFull userFull : usersArray) {
                    DateTime birthDate = null;
                    String format = null;

                    if (!TextUtils.isEmpty(userFull.bdate)) {
                        for (int i = 0; i < formats.length; i++) {
                            format = formats[i];
                            try {
                                birthDate = DateTimeFormat.forPattern(format).parseDateTime(userFull.bdate);
                            } catch (Exception ignored) {
                            }
                            if (birthDate != null) {
                                break;
                            }
                        }
                    }
                    users.add(new User(userFull.id, userFull.toString(), birthDate, userFull.photo_200, format));
                    Log.d("Vkphoto", "photo 200: " + userFull.photo_200);
                    Log.d("Vkphoto", "id: " + userFull.id);
                    Log.d("Vkphoto", "name: " + userFull.last_name + " " + userFull.first_name);
                    //listAdapter.notifyDataSetChanged();
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

    private void startUserApiCall(int id) {
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("id", String.valueOf(id));
        startActivity(i);
    }
}
