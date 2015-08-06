package com.vkclient.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.podkaifom.vkclient.R;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKError;
import com.vk.sdk.dialogs.VKCaptchaDialog;
import com.vkclient.activities.ProfileActivity;
import com.vkclient.supports.AlertBuilder;

public class LoginFragment extends Fragment {
    final String APPLICATION_ID = "4929437";
    private Button signIn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View viewHierarchy = inflater.inflate(R.layout.fragment_login, container, false);
        VKSdk.initialize(this.sdkListener, this.APPLICATION_ID);
        this.signIn = ((Button) viewHierarchy.findViewById(R.id.btSignIn));
        this.signIn.setOnClickListener(new LoginClickListener());
        if (VKSdk.wakeUpSession()) {
            startClientActivity();
        }
        return viewHierarchy;
    }

    private static final String[] vkPermissionScope = new String[]{
            VKScope.FRIENDS,
            VKScope.WALL,
            VKScope.PHOTOS,
            VKScope.NOHTTPS,
            VKScope.MESSAGES,
            VKScope.GROUPS
    };

    public final class LoginClickListener implements View.OnClickListener {
        @Override
        public void onClick(final View v) {
            if (v == signIn) VKSdk.authorize(vkPermissionScope, true, true);
        }
    }

    private final VKSdkListener sdkListener = new VKSdkListener() {
        @Override
        public void onCaptchaError(VKError captchaError) {
            new VKCaptchaDialog(captchaError).show();
        }

        @Override
        public void onTokenExpired(VKAccessToken expiredToken) {
            VKSdk.authorize(vkPermissionScope);
        }

        @Override
        public void onAccessDenied(final VKError authorizationError) {
            AlertBuilder.showErrorMessage(VKUIHelper.getTopActivity(), authorizationError.toString());
        }

        @Override
        public void onReceiveNewToken(VKAccessToken newToken) {
            Toast.makeText(getActivity(), "Token received", Toast.LENGTH_SHORT).show();
            startClientActivity();
        }

        @Override
        public void onAcceptUserToken(VKAccessToken token) {
            Toast.makeText(getActivity(), "Token acepted", Toast.LENGTH_SHORT).show();
            startClientActivity();
        }
    };

    private void startClientActivity() {
        startActivity(new Intent(getActivity(), ProfileActivity.class));
    }

    @Override
    public void onResume() {
        super.onResume();
        VKUIHelper.onResume(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VKUIHelper.onDestroy(getActivity());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        VKUIHelper.onActivityResult(getActivity(), requestCode, resultCode, data);
    }
}
