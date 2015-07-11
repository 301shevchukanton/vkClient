package com.vkclient.activities;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.podkaifom.vkclient.R;
import com.vk.sdk.VKUIHelper;
import com.vkclient.supports.PhotoLoader;

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
