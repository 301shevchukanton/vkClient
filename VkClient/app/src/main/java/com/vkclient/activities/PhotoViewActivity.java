package com.vkclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.podkaifom.vkclient.R;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vkclient.entities.RequestCreator;
import com.vkclient.entities.AbstractRequestListener;
import com.vkclient.supports.Logger;
import com.vkclient.supports.PhotoLoader;

import org.json.JSONException;
import org.json.JSONObject;

public class PhotoViewActivity extends VkSdkActivity {
    private static String photoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        super.onCreateDrawer();
        VKUIHelper.onCreate(this);
        this.photoUrl = getIntent().getStringExtra("photo");
        PhotoLoader.loadPhoto(getApplicationContext(), photoUrl, (ImageView) findViewById(R.id.ivPhoto));
    }

}
