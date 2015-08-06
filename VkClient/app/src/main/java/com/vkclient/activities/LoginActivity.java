package com.vkclient.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

import com.example.podkaifom.vkclient.R;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.util.VKUtil;
import com.vkclient.fragments.LoginFragment;
import com.vkclient.supports.Logger;

public class LoginActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        VKUIHelper.onCreate(this);
        String[] fingerprint = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        Logger.logDebug("Fingerprint", fingerprint[0]);
        FragmentManager fragmentManager = getFragmentManager();
        Fragment loginFragment = fragmentManager.findFragmentById(R.id.container_login);
        if (loginFragment == null) {
            loginFragment = new LoginFragment();
            fragmentManager.beginTransaction().add(R.id.container_login, loginFragment).commit();
        }
    }
}
