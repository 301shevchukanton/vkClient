package com.vkclient.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.podkaifom.vkclient.R;
import com.vkclient.fragments.DialogsFragment;

public class DialogsActivity extends VkSdkActivity {
    private Fragment dialogsFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fragmentManager = getFragmentManager();
        dialogsFragment = fragmentManager.findFragmentById(R.id.container_dialogs);
        if (dialogsFragment == null) {
            dialogsFragment = new DialogsFragment();
            fragmentManager.beginTransaction().add(R.id.container_dialogs, dialogsFragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_messages, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_messages:
                ((DialogsFragment) dialogsFragment).startLoading();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    int getLayoutResource() {
        return R.layout.activity_dialogs;
    }
}
