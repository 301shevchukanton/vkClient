package com.vkclient.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.podkaifom.vkclient.R;
import com.vk.sdk.VKSdk;
import com.vkclient.adapters.DialogsListAdapter;
import com.vkclient.entities.Dialog;
import com.vkclient.listeners.DialogsLoaderListener;
import com.vkclient.loaders.DialogsLoader;
import com.vkclient.loaders.DialogsLoaderFactory;

import java.util.ArrayList;
import java.util.List;


public class DialogFriendListFragment extends Fragment {

    public static final String PROFILE_EXTRA = "userid";
    public static final String PROFILE_CHAT_ID = "chatid";
    private ListView listView;
    private List<Dialog> dialogs = new ArrayList<>();
    private DialogsListAdapter listAdapter;
    private Callbacks callbacks;

    public interface Callbacks {
        void onDialogSelected(Dialog dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewHierarchy = inflater.inflate(R.layout.fragment_dialogs, container, false);
        this.listView = (ListView) viewHierarchy.findViewById(R.id.lvDialogs);
        this.listView.setOnItemClickListener(this.dialogClickListener);
        this.listAdapter = new DialogsListAdapter(getActivity(), this.dialogs);
        this.listView.setAdapter(this.listAdapter);
        setHasOptionsMenu(true);
        if (VKSdk.wakeUpSession()) {
            startLoading();
        }
        return viewHierarchy;
    }

    private void startLoading() {
        DialogsLoader loader = new DialogsLoaderFactory().create();
        loader.load(loaderListener, getActivity());
    }

    private DialogsLoaderListener loaderListener = new DialogsLoaderListener() {
        @Override
        public void onLoad(List<Dialog> loadedDialogs) {
            dialogs.clear();
            dialogs.addAll(loadedDialogs);
            listAdapter.notifyDataSetChanged();
        }

        @Override
        public void onError() {
            listAdapter.notifyDataSetChanged();
        }
    };
    private final AdapterView.OnItemClickListener dialogClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {
            ((TextView) view.findViewById(R.id.tvDialogName)).getText();
            for (int i = 0; i < dialogs.size(); i++) {
                if (dialogs.get(i).getChatId() != 0 && dialogs.get(i).getTitle() == ((TextView) view.findViewById(R.id.tvDialogName)).getText()) {
                    callbacks.onDialogSelected(dialogs.get(i));
                    break;
                } else if (dialogs.get(i).getUsername() == ((TextView) view.findViewById(R.id.tvDialogName)).getText()) {
                    callbacks.onDialogSelected(dialogs.get(i));
                    break;
                }
            }
        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_messages, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_messages:
                startLoading();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.callbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.callbacks = null;
    }
}
