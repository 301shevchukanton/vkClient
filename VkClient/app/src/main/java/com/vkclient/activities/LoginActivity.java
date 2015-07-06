package com.vkclient.activities;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.podkaifom.vkclient.R;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.dialogs.VKCaptchaDialog;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKError;
import com.vk.sdk.util.VKUtil;
import com.vkclient.supports.AlertBuilder;
import com.vkclient.supports.Logger;

public class LoginActivity extends Activity {
    final String APPLICATION_ID = "4929437";
    private Button signIn;
    private Button forceAuth;
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
            if (v == forceAuth) VKSdk.authorize(vkPermissionScope, true, true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        VKUIHelper.onCreate(this);
        VKSdk.initialize(this.sdkListener, this.APPLICATION_ID);
        String[] fingerprint = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        Logger.logDebug("Fingerprint", fingerprint[0]);
        this.signIn = ((Button) findViewById(R.id.btSignIn));
        this.signIn.setOnClickListener(new LoginClickListener());
        this.forceAuth = ((Button) findViewById(R.id.btForceAuth));
        this.forceAuth.setOnClickListener(new LoginClickListener());
        if (VKSdk.wakeUpSession()) {
            startClientActivity();
            return;
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
            Toast.makeText(LoginActivity.this, "Token received", Toast.LENGTH_SHORT).show();
            startClientActivity();
        }

        @Override
        public void onAcceptUserToken(VKAccessToken token) {
            Toast.makeText(LoginActivity.this, "Token acepted", Toast.LENGTH_SHORT).show();
            startClientActivity();
        }
    };

    private void startClientActivity() {
        startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        VKUIHelper.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VKUIHelper.onDestroy(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        VKUIHelper.onActivityResult(this, requestCode, resultCode, data);
    }
}
