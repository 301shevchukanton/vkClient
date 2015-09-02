package com.vkclient.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.podkaifom.vkclient.R;
import com.vkclient.entities.Dialog;
import com.vkclient.fragments.DialogFriendListFragment;
import com.vkclient.fragments.SingleDialogFragment;

public class DialogsActivity extends VkSdkActivity implements DialogFriendListFragment.Callbacks {
    public static final String PROFILE_EXTRA = "userid";
    public static final String PROFILE_CHAT_ID = "chatid";

    @Override
    protected Fragment createFragment() {
        return new DialogFriendListFragment();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_navigation;
    }

    public void onDialogSelected(Dialog dialog) {
        if (findViewById(R.id.detailFragmentContainer) == null) {
            Intent i = new Intent(this, SingleDialogActivity.class);
            i.putExtra(PROFILE_EXTRA, String.valueOf(dialog.getUser_id()));
            i.putExtra(PROFILE_CHAT_ID, String.valueOf(dialog.getChatId()));
            startActivity(i);
        } else {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment oldDetail = fm.findFragmentById(R.id.detailFragmentContainer);
            Fragment newDetail = SingleDialogFragment.newInstance(dialog.getUser_id(), dialog.getChatId());
            if (oldDetail != null) {
                ft.remove(oldDetail);
            }
            ft.add(R.id.detailFragmentContainer, newDetail);
            ft.commit();
        }
    }
}
