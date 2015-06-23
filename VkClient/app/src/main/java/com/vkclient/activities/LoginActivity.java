package com.vkclient.activities;

import android.os.Bundle;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;
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
import com.vkclient.supports.Loger;

public class LoginActivity extends VkSdkActivity {
    final String APPLICATION_ID = "4929437";
    private static final String[] vkPermissionScope = new String[] {
            VKScope.FRIENDS,
            VKScope.WALL,
            VKScope.PHOTOS,
            VKScope.NOHTTPS,
            VKScope.MESSAGES,
            VKScope.GROUPS
    };
    public final class LoginClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(final View v)
        {
            if(v==findViewById(R.id.btSignIn))      VKSdk.authorize(vkPermissionScope, true, true);
            if(v==findViewById(R.id.btForceAuth))  VKSdk.authorize(vkPermissionScope, true, true);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        VKUIHelper.onCreate(this);
        VKSdk.initialize(sdkListener, APPLICATION_ID);
        String[] fingerprint = VKUtil.getCertificateFingerprint(this, this.getPackageName());
         Loger.log("Fingerprint", fingerprint[0]);
        findViewById(R.id.btSignIn).setOnClickListener(new LoginClickListener());
        findViewById(R.id.btForceAuth).setOnClickListener(new LoginClickListener());
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
            new AlertDialog.Builder(VKUIHelper.getTopActivity())
                    .setMessage(authorizationError.toString())
                    .show();
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
        Intent intent = new Intent(LoginActivity.this, ClientActivity.class);
        startActivity(intent);
    }
}
