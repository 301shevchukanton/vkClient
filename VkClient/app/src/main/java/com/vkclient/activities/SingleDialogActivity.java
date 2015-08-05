package com.vkclient.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.widget.ListView;

import com.example.podkaifom.vkclient.R;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKRequest;
import com.vkclient.adapters.MessagesListAdapter;
import com.vkclient.entities.Message;
import com.vkclient.fragments.SingleDialogFragment;
import com.vkclient.supports.Logger;

import java.util.ArrayList;
import java.util.List;


public class SingleDialogActivity extends VkSdkActivity {
    private final String COUNT = "150";
    private ListView messagesList;
    private VKRequest currentRequest;
    private List<Message> messages = new ArrayList<>();
    private MessagesListAdapter listAdapter;
    private VKRequest ownRequest = null;
    private VKRequest fromRequest = null;
    private Fragment singleDialogFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        profileId = getIntent().getStringExtra("userid");
        Logger.logDebug("profid", "ic_user id taked" + profileId);
        super.onCreate(savedInstanceState);
        FragmentManager fragmentManager = getFragmentManager();
        this.singleDialogFragment = fragmentManager.findFragmentById(R.id.container_single_dialog);
        if (singleDialogFragment == null) {
            singleDialogFragment = new SingleDialogFragment();
            fragmentManager.beginTransaction().add(R.id.container_single_dialog, singleDialogFragment).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        VKUIHelper.onResume(this);
        if (VKSdk.wakeUpSession()) {
            ((SingleDialogFragment) singleDialogFragment).startLoading();
        }
    }

    @Override
    int getLayoutResource() {
        return R.layout.activity_single_dialog;
    }
}
