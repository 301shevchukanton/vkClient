package com.vkclient.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.podkaifom.vkclient.R;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vkclient.adapters.NewsListAdapter;
import com.vkclient.entities.News;
import com.vkclient.listeners.AbstractRequestListener;
import com.vkclient.parsers.WallParser;
import com.vkclient.supports.Logger;
import com.vkclient.supports.RequestCreator;

import java.util.ArrayList;
import java.util.List;

public class WallFragment extends Fragment {
    private static final String ACTIVITY_EXTRA = "id";
    private static final String INSTANCE_STATE_KEY = "key";
    private VKRequest currentRequest;
    private ListView listView;
    private List<News> wall = new ArrayList<>();
    private NewsListAdapter listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View viewHierarchy = inflater.inflate(R.layout.fragment_news, container, false);
        VKUIHelper.onCreate(getActivity());
        this.listView = (ListView) viewHierarchy.findViewById(R.id.lvNews);
        if (savedInstanceState == null || !savedInstanceState.containsKey(INSTANCE_STATE_KEY)) {
            if (VKSdk.wakeUpSession()) {
                startLoading();
            }
        } else {
            this.wall = savedInstanceState.getParcelableArrayList("key");
        }
        this.listAdapter = new NewsListAdapter(getActivity(), this.wall);
        this.listView.setAdapter(this.listAdapter);
        return viewHierarchy;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("key", (ArrayList<? extends Parcelable>) wall);
        super.onSaveInstanceState(outState);
    }

    private void startLoading() {
        String ownerId = getActivity().getIntent().getStringExtra(ACTIVITY_EXTRA);
        this.currentRequest = RequestCreator.wallGet(ownerId);
        this.currentRequest.executeWithListener(this.getHistoryRequestListener);
    }

    private final AbstractRequestListener getHistoryRequestListener = new AbstractRequestListener() {
        @Override
        public void onComplete(final VKResponse response) {
            super.onComplete(response);
            wall.clear();
            wall.addAll(new WallParser().getNewsList(response));
            listAdapter.notifyDataSetChanged();
        }

        @Override
        public void onError(VKError error) {
            super.onError(error);
            Logger.logError("FUK IT", "onError: " + error);
        }
    };

}
