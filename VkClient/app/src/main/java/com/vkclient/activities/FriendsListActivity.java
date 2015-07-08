package com.vkclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vkclient.adapters.DialogsListAdapter;
import com.vkclient.adapters.FriendListAdapter;
import com.example.podkaifom.vkclient.R;
import com.vkclient.entities.AbstractRequestListener;
import com.vkclient.entities.Dialog;
import com.vkclient.entities.User;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKUsersArray;
import com.vkclient.entities.RequestCreator;
import com.vkclient.supports.Logger;
import com.vkclient.supports.PhotoLoader;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FriendsListActivity extends VkSdkActivity {
    private EditText filterText = null;
    private VKRequest currentRequest;
    private String photoUrl = "";
    private ListView friendsList;
    private List<User> users = new ArrayList<User>();
    private FriendListAdapter listAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        profileId = getIntent().getStringExtra("id");
        Logger.logDebug("profid", "profile id taked" + profileId);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);
        super.onCreateDrawer();
        this.friendsList = (ListView) findViewById(R.id.lvFriends);
        this.filterText = (EditText) findViewById(R.id.etSearchFriends);
        this.filterText.addTextChangedListener(filterTextWatcher);
        VKUIHelper.onCreate(this);
        Object items = getLastNonConfigurationInstance();
        if (items != null) {
            this.users = ((List<User>) items);
            this.listAdapter.notifyDataSetChanged();
        } else if (VKSdk.wakeUpSession()) {
            startLoading();
        }
        this.friendsList.setOnItemClickListener(this.friendClickListener);
        this.listAdapter = new FriendListAdapter(this, this.users);
        listAdapter.setOnPhotoClickListener(photoClickListener);
        this.friendsList.setAdapter(this.listAdapter);
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {
        List<User> fullUsersArray = new ArrayList<User>();

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            if (fullUsersArray.isEmpty()) fullUsersArray.addAll(users);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            List<User> temp = new ArrayList<User>();
            int textLength = filterText.getText().length();
            for (int i = 0; i < fullUsersArray.size(); i++) {
                if (textLength <= fullUsersArray.get(i).getName().length()) {
                    if (filterText.getText().toString().equalsIgnoreCase(
                            (String) fullUsersArray.get(i).getName().subSequence(0,
                                    textLength))) {
                        temp.add(fullUsersArray.get(i));
                    }
                }
            }
            listAdapter.notifyDataSetChanged();
            users.clear();
            users.addAll(temp);
        }
    };

    private void startLoading() {
        if (this.currentRequest != null) {
            this.currentRequest.cancel();
        }
        this.currentRequest = RequestCreator.getFriends(profileId);
        this.currentRequest.executeWithListener(this.getFriendsRequestListener);

    }

    private void startUserApiCall(int id) {
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("id", String.valueOf(id));
        startActivity(i);
    }

    private final AdapterView.OnItemClickListener friendClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getName().equals(((TextView) view.findViewById(R.id.tvFriendName)).getText())) {
                    startUserApiCall(users.get(i).getId());
                    break;
                }
            }
            Logger.logDebug("VkList", "id: " + id);
        }
    };
    private final AbstractRequestListener getFriendsRequestListener = new AbstractRequestListener() {
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
                users.add(setUser(userFull, birthDate, format));
            }
            listAdapter.notifyDataSetChanged();
        }

        private User setUser(VKApiUserFull userFull, DateTime birthDate, String format) {
            User user = new User();
            user.setId(userFull.id);
            user.setName(userFull.toString());
            user.setBirthDate(birthDate);
            user.setPhoto(userFull.photo_200);
            user.setDateFormat(format);
            return user;
        }
    };


    private final FriendListAdapter.OnPhotoClickListener photoClickListener = new FriendListAdapter.OnPhotoClickListener() {
        @Override
        public void onClick(String userId) {
            startPhotoViewCall(userId);
        }
    };

    public void startPhotoViewCall(String userId) {

        if (currentRequest != null) {
            currentRequest.cancel();
        }
        currentRequest = RequestCreator.getBigUserPhoto(userId);
        currentRequest.executeWithListener(bigPhotoRequestListener);

    }

    private AbstractRequestListener bigPhotoRequestListener = new AbstractRequestListener() {
        @Override
        public void onComplete(VKResponse response) {
            super.onComplete(response);
            setPhoto(response);
        }

        private void setPhoto(VKResponse response) {
            try {
                JSONObject r = response.json.getJSONArray("response").getJSONObject(0);
                if (r.getString("photo_max_orig") != null) {
                    photoUrl = r.getString("photo_max_orig");
                    Intent i = new Intent(getApplicationContext(), PhotoViewActivity.class);
                    i.putExtra("photo", photoUrl);
                    startActivity(i);
                }
            } catch (JSONException e) {
                Log.e(e.getMessage(), e.toString());
            }
        }
    };

}
