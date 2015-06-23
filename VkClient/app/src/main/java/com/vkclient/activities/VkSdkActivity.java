package com.vkclient.activities;

import android.app.Activity;
import android.content.Intent;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKApiConst;

public abstract class VkSdkActivity extends Activity {
    protected String profileId= VKApiConst.OWNER_ID;
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
