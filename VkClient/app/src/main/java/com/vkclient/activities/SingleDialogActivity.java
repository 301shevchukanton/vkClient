package com.vkclient.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

import com.example.podkaifom.vkclient.R;
import com.vkclient.fragments.SingleDialogFragment;
import com.vkclient.supports.Logger;


public class SingleDialogActivity extends VkSdkActivity {

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
    int getLayoutResource() {
        return R.layout.activity_single_dialog;
    }
}
