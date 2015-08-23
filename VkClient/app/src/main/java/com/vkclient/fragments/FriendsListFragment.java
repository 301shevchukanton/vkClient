package com.vkclient.fragments;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.podkaifom.vkclient.R;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKUsersArray;
import com.vkclient.activities.PhotoViewActivity;
import com.vkclient.activities.ProfileActivity;
import com.vkclient.adapters.FriendListAdapter;
import com.vkclient.entities.User;
import com.vkclient.listeners.AbstractRequestListener;
import com.vkclient.parsers.UserParser;
import com.vkclient.supports.Logger;
import com.vkclient.supports.RequestCreator;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

public class FriendsListFragment extends Fragment {

    private static final String BUNDLE_FILTER_TEXT = "BUNDLE_FILTER_TEXT";
    private static final String ACTIVITY_EXTRA = "id";

    private EditText filterText;
    private VKRequest currentRequest;
    private ListView friendsList;
    private String profileId;
    private List<User> users = new ArrayList<User>();
    private FriendListAdapter listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewHierarchy = inflater.inflate(R.layout.fragment_friends_list, container, false);
        findFriendsListViews(viewHierarchy);
        profileId = getActivity().getIntent().getStringExtra(ACTIVITY_EXTRA);
        return viewHierarchy;
    }

    private void findFriendsListViews(View view) {
        this.friendsList = (ListView) view.findViewById(R.id.lvFriends);
        this.filterText = (EditText) view.findViewById(R.id.etSearchFriends);
        this.filterText.addTextChangedListener(filterTextWatcher);
        startLoading();
        this.friendsList.setOnItemClickListener(this.friendClickListener);
        this.listAdapter = new FriendListAdapter(getActivity(), this.users);
        this.listAdapter.setOnPhotoClickListener(photoClickListener);
        this.friendsList.setAdapter(this.listAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BUNDLE_FILTER_TEXT, this.filterText.getText().toString());
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
            filterList(fullUsersArray, s.toString());
        }
    };

    private void filterList(List<User> fullUsersArray, String filterText) {
        List<User> temp = new ArrayList<User>();
        int textLength = filterText.length();
        for (int i = 0; i < fullUsersArray.size(); i++) {
            if (textLength <= fullUsersArray.get(i).getName().length()) {
                if (filterText.equalsIgnoreCase(
                        (String) fullUsersArray.get(i).getName().subSequence(0,
                                textLength))) {
                    temp.add(fullUsersArray.get(i));
                }
            }
        }
        users.clear();
        users.addAll(temp);
        listAdapter.notifyDataSetChanged();
    }

    private void startLoading() {
        if (this.currentRequest != null) {
            this.currentRequest.cancel();
        }
        this.currentRequest = RequestCreator.getFriends(profileId);
        this.currentRequest.executeWithListener(this.getFriendsRequestListener);
    }

    private void startUserApiCall(int id) {
        Intent i = new Intent(getActivity(), ProfileActivity.class);
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

                if (userFull.city != null) {
                    users.add(setUser(userFull, birthDate, format, userFull.city.title));
                } else {
                    users.add(setUser(userFull, birthDate, format));
                }
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

        private User setUser(VKApiUserFull userFull, DateTime birthDate, String format, String city) {
            User user = new User();
            user.setId(userFull.id);
            user.setName(userFull.toString());
            user.setBirthDate(birthDate);
            user.setPhoto(userFull.photo_200);
            user.setDateFormat(format);
            user.setCity(city);
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
        currentRequest = RequestCreator.getUserById(userId);
        currentRequest.executeWithListener(bigPhotoRequestListener);
    }

    private AbstractRequestListener bigPhotoRequestListener = new AbstractRequestListener() {
        @Override
        public void onComplete(VKResponse response) {
            super.onComplete(response);
            setPhoto(response);
        }

        private void setPhoto(VKResponse response) {
            startActivity(PhotoViewActivity.getPhotoViewIntent(getActivity(), new UserParser().parseUserName(response).getPhotoMax()));
        }
    };

}
