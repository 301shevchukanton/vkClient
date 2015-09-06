package com.vkclient.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

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
import com.vkclient.supports.RequestCreator;
import com.vkclient.views.FriendsListItemView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

public class FriendsListFragment extends Fragment {

    private static final String ACTIVITY_EXTRA = "id";
    private static final String FULL_DATE = "dd.MM.yyyy";
    private static final String PART_DAY = "dd.MM";
    private static final String FULL_USERS_INSTANCE_KEY = "full_users_list";
    private static final String USERS_INSTANCE_KEY = "users_list";
    private ParcelableTextWatcher filterTextWatcher;
    private EditText filterText;
    private VKRequest currentRequest;
    private ListView friendsList;
    private String profileId;
    private List<User> users = new ArrayList<User>();
    private FriendListAdapter listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.profileId = getActivity().getIntent().getStringExtra(ACTIVITY_EXTRA);
        View viewHierarchy = inflater.inflate(R.layout.fragment_friends_list, container, false);
        findFriendsListViews(viewHierarchy);
        setupWatcher(savedInstanceState);
        setupViews();
        return viewHierarchy;
    }

    private void setupWatcher(Bundle savedInstanceState) {
        filterTextWatcher = new ParcelableTextWatcher();
        this.filterText.addTextChangedListener(filterTextWatcher);
        if (savedInstanceState != null && savedInstanceState.containsKey(FULL_USERS_INSTANCE_KEY)) {
            filterTextWatcher.fullUsersArray = savedInstanceState.getParcelableArrayList(FULL_USERS_INSTANCE_KEY);
            users = savedInstanceState.getParcelableArrayList(USERS_INSTANCE_KEY);
        } else {
            startLoading();
        }
    }

    private void setupViews() {
        this.friendsList.setOnItemClickListener(this.friendClickListener);
        this.listAdapter = new FriendListAdapter(getActivity(), this.users);
        this.listAdapter.setOnPhotoClickListener(photoClickListener);
        this.friendsList.setAdapter(this.listAdapter);
    }

    private void findFriendsListViews(View view) {
        this.friendsList = (ListView) view.findViewById(R.id.lvFriends);
        this.filterText = (EditText) view.findViewById(R.id.etSearchFriends);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(FULL_USERS_INSTANCE_KEY, (ArrayList<? extends Parcelable>) filterTextWatcher.fullUsersArray);
        outState.putParcelableArrayList(USERS_INSTANCE_KEY, (ArrayList<? extends Parcelable>) users);
        super.onSaveInstanceState(outState);
    }

    private class ParcelableTextWatcher implements TextWatcher {
        public List<User> fullUsersArray = new ArrayList<User>();

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            if (this.fullUsersArray.isEmpty()) {
                this.fullUsersArray.addAll(users);
            }

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            filterList(this.fullUsersArray, s.toString());
        }
    }


    private void filterList(List<User> fullUsersArray, String filterText) {
        List<User> temp = new ArrayList<User>();
        int textLength = filterText.length();
        for (int i = 0; i < fullUsersArray.size(); i++) {
            addFilteredUser(fullUsersArray, filterText, temp, textLength, i);
        }
        this.users.clear();
        this.users.addAll(temp);
        this.listAdapter.notifyDataSetChanged();
    }

    private void addFilteredUser(List<User> fullUsersArray, String filterText, List<User> temp, int textLength, int i) {
        if (textLength <= fullUsersArray.get(i).getName().length()) {
            if (userFound(fullUsersArray, filterText, textLength, i)) {
                temp.add(fullUsersArray.get(i));
            }
        }
    }

    private boolean userFound(List<User> fullUsersArray, String filterText, int textLength, int i) {
        return filterText.equalsIgnoreCase(
                (String) fullUsersArray.get(i).getName().subSequence(0,
                        textLength));
    }

    private void startLoading() {
        this.currentRequest = RequestCreator.getFriends(profileId);
        this.currentRequest.executeWithListener(this.getFriendsRequestListener);
    }

    private void startUserApiCall(int id) {
        Intent i = new Intent(getActivity(), ProfileActivity.class);
        i.putExtra(ACTIVITY_EXTRA, String.valueOf(id));
        startActivity(i);
    }

    private final AdapterView.OnItemClickListener friendClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getName().equals(((FriendsListItemView) view).getFriendName())) {
                    startUserApiCall(users.get(i).getId());
                    break;
                }
            }
        }
    };
    private final AbstractRequestListener getFriendsRequestListener = new AbstractRequestListener() {
        @Override
        public void onComplete(VKResponse response) {
            super.onComplete(response);
            VKUsersArray usersArray = (VKUsersArray) response.parsedModel;
            users.clear();
            addUsersInfo(usersArray);
            listAdapter.notifyDataSetChanged();
        }

        private void addUsersInfo(VKUsersArray usersArray) {
            final String[] formats = new String[]{FULL_DATE, PART_DAY};
            for (VKApiUserFull userFull : usersArray) {
                addUserInfo(formats, userFull);
            }
        }

        private void addUserInfo(String[] formats, VKApiUserFull userFull) {
            DateTime birthDate = null;
            String format = null;
            if (!TextUtils.isEmpty(userFull.bdate)) {
                for (int i = 0; i < formats.length; i++) {
                    format = formats[i];
                    birthDate = getDateTime(userFull, birthDate, format);
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

        private DateTime getDateTime(VKApiUserFull userFull, DateTime birthDate, String format) {
            try {
                birthDate = DateTimeFormat.forPattern(format).parseDateTime(userFull.bdate);
            } catch (Exception ignored) {
            }
            return birthDate;
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
        this.currentRequest = RequestCreator.getUserById(userId);
        this.currentRequest.executeWithListener(this.bigPhotoRequestListener);
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
