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
import com.vk.sdk.api.VKBatchRequest;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vkclient.adapters.DialogsListAdapter;
import com.vkclient.entities.Dialog;
import com.vkclient.listeners.AbstractRequestListener;
import com.vkclient.parsers.MessageParser;
import com.vkclient.parsers.UserParser;
import com.vkclient.supports.Logger;
import com.vkclient.supports.RequestCreator;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class DialogFriendListFragment extends Fragment {

    public static final String PROFILE_EXTRA = "userid";
    public static final String PROFILE_CHAT_ID = "chatid";
    private VKRequest currentRequest;
    private ListView listView;
    private List<Dialog> dialogs = new ArrayList<>();
    private DialogsListAdapter listAdapter;
    private Callbacks callbacks;

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
        if (this.currentRequest != null) {
            this.currentRequest.cancel();
        }
        this.currentRequest = RequestCreator.getDialogs();
        this.currentRequest.executeWithListener(this.getHistoryRequestListener);
    }

    private final AbstractRequestListener getHistoryRequestListener = new AbstractRequestListener() {
        @Override
        public void onComplete(final VKResponse response) {
            super.onComplete(response);
            Logger.logDebug("profid", response.responseString);
            dialogs.clear();
            dialogs.addAll(new MessageParser().getDialogsList(response));
            VKRequest[] requests = new VKRequest[dialogs.size()];
            for (int i = 0; i < dialogs.size(); i++) {
                requests[i] = RequestCreator.getUserById(String.valueOf(dialogs.get(i).getUser_id()));
            }
            VKBatchRequest batch = new VKBatchRequest(requests);
            batch.executeWithListener(batchListener);
            listAdapter.notifyDataSetChanged();
        }
    };
    private final VKBatchRequest.VKBatchRequestListener batchListener = new VKBatchRequest.VKBatchRequestListener() {
        @Override
        public void onComplete(VKResponse[] responses) {
            super.onComplete(responses);
            try {
                setUserInfo(responses);
                listAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public void onError(VKError error) {
            super.onError(error);
            startLoading();
        }

        private void setUserInfo(VKResponse[] responses) throws JSONException {
            for (int i = 0; i < responses.length; i++) {
                dialogs.get(i).setDialogUserInfo(new UserParser().parseUserName(responses[i]));
            }
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

    public interface Callbacks {
        void onDialogSelected(Dialog dialog);
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
}
