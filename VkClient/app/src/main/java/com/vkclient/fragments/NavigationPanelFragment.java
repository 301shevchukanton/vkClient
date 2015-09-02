package com.vkclient.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.podkaifom.vkclient.R;
import com.vk.sdk.VKUIHelper;
import com.vkclient.activities.DialogsActivity;
import com.vkclient.activities.FriendsListActivity;
import com.vkclient.activities.NewsActivity;
import com.vkclient.activities.ProfileActivity;
import com.vkclient.adapters.DrawerAdapter;
import com.vkclient.entities.DrawerMenuItem;

import java.util.ArrayList;
import java.util.List;

public class NavigationPanelFragment extends Fragment {

    private List<DrawerMenuItem> drawerItems = new ArrayList<>();
    private ListView drawerList;
    private DrawerAdapter drawerListAdapter;
    private Callbacks callbacks;

    public enum NavigationItem {
        PROFILE_INFO,
        NEWS,
        DIALOGS,
        FRIENDS_LIST
    }

    public interface Callbacks {
        void onDrawerItemSelected();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        VKUIHelper.onCreate(getActivity());
        View viewHierarchy = inflater.inflate(R.layout.fragment_drawer, container, false);
        this.drawerList = (ListView) viewHierarchy.findViewById(R.id.left_drawer);
        onCreateDrawer();
        return viewHierarchy;
    }

    protected void onCreateDrawer() {
        this.drawerItems.add(new DrawerMenuItem(getString(R.string.drawer_item_profile), R.drawable.ic_home, NavigationItem.PROFILE_INFO));
        this.drawerItems.add(new DrawerMenuItem(getString(R.string.drawer_item_friends), R.drawable.ic_friends, NavigationItem.FRIENDS_LIST));
        this.drawerItems.add(new DrawerMenuItem(getString(R.string.drawer_item_messages), R.drawable.ic_messages, NavigationItem.DIALOGS));
        this.drawerItems.add(new DrawerMenuItem(getString(R.string.drawer_item_news), R.drawable.ic_news, NavigationItem.NEWS));

        this.drawerListAdapter = new DrawerAdapter(getActivity(), this.drawerItems);
        this.drawerList.setAdapter(this.drawerListAdapter);
        this.drawerList.setOnItemClickListener(this.drawerListener);
    }

    private ListView.OnItemClickListener drawerListener = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            callbacks.onDrawerItemSelected();
            switch (drawerListAdapter.getItem(position).getKey()) {
                case PROFILE_INFO: {
                    startApiCall(ProfileActivity.class);
                    break;
                }
                case NEWS: {
                    startApiCall(NewsActivity.class);
                    break;
                }
                case DIALOGS: {
                    startApiCall(DialogsActivity.class);
                    break;
                }
                case FRIENDS_LIST: {
                    startApiCall(FriendsListActivity.class);
                    break;
                }
            }
        }
    };

    private void startApiCall(Class<?> cls) {
        Intent i = new Intent(getActivity(), cls);
        startActivity(i);
    }

}
