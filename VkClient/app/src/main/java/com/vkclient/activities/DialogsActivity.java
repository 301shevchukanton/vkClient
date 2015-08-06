package com.vkclient.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

import com.example.podkaifom.vkclient.R;
import com.vkclient.fragments.DialogsFragment;

public class DialogsActivity extends VkSdkActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fragmentManager = getFragmentManager();
        Fragment dialogsFragment = fragmentManager.findFragmentById(R.id.container_dialogs);
        if (dialogsFragment == null) {
            dialogsFragment = new DialogsFragment();
            fragmentManager.beginTransaction().add(R.id.container_dialogs, dialogsFragment).commit();
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_dialogs;
    }
}
