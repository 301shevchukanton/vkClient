package com.vkclient.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.vkclient.adapters.FriendListViewAdapter;
import com.example.podkaifom.vkclient.R;
import com.vkclient.entities.User;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKUsersArray;
import com.vkclient.entities.RequestCreator;
import com.vkclient.supports.Loger;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;


public class FriendsListActivity extends ListActivity {
    private EditText filterText = null;
    private VKRequest currentRequest;
    ListView friendsList;
    private String profileId;
    private List<User> users = new ArrayList<User>();
    private FriendListViewAdapter listAdapter;
    public final class GetFriendsRequestListener extends VKRequest.VKRequestListener
    {
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
    }
    @Override
    public Object onRetainNonConfigurationInstance() {
        return this.listAdapter.getItems();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            profileId=getIntent().getStringExtra("id");
         Loger.log("profid", "profile id taked" + profileId);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);
        friendsList = (ListView)findViewById(android.R.id.list);
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
                    if (users.get(i).getName().equals(((TextView) view.findViewById(R.id.text1)).getText())) {
                        startUserApiCall(users.get(i).getId());
                        break;
                    }
                }
                 Loger.log("VkList", "id: " + id);
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
            friendsList.setAdapter(listAdapter = new FriendListViewAdapter(FriendsListActivity.this, temp));
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        VKUIHelper.onResume(this);
        startLoading();
        listAdapter.notifyDataSetChanged();
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
        getMenuInflater().inflate(R.menu.menu_friends_list, menu);
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
        currentRequest = RequestCreator.getFriends(profileId);
        currentRequest.executeWithListener(new GetFriendsRequestListener());
    }

    private void startUserApiCall(int id) {
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("id", String.valueOf(id));
        startActivity(i);
    }
}
