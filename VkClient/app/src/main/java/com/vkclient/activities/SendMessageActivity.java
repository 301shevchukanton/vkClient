package com.vkclient.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

import com.example.podkaifom.vkclient.R;
import com.vkclient.fragments.SendMessageFragment;


public class SendMessageActivity extends VkSdkActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fragmentManager = getFragmentManager();
        Fragment sendMessageFragment = fragmentManager.findFragmentById(R.id.container_send_message);
        if (sendMessageFragment == null) {
            sendMessageFragment = new SendMessageFragment();
            fragmentManager.beginTransaction().add(R.id.container_send_message, sendMessageFragment).commit();
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_send_message;
    }
}

