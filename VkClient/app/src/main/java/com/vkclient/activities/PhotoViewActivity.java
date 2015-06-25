package com.vkclient.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.podkaifom.vkclient.R;
import com.squareup.picasso.Picasso;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vkclient.entities.RequestCreator;
import com.vkclient.entities.AbstractRequestListener;
import com.vkclient.supports.Loger;
import com.vkclient.supports.PhotoLoader;

import org.json.JSONException;
import org.json.JSONObject;

public class PhotoViewActivity extends VkSdkActivity {

    private VKRequest currentRequest;
    private String photoUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        VKUIHelper.onCreate(this);
        this.photoUrl=getIntent().getStringExtra("photo");
        startLoading();

    }
    private void startLoading() {
        if (this.currentRequest != null) {
            this.currentRequest.cancel();
        }
        Loger.log("profid", "onComplete " + profileId);
        this.currentRequest = RequestCreator.getBigUserPhoto(this.photoUrl);
        this.currentRequest.executeWithListener(new bigPhotoRequestListener());
    }
    public final class bigPhotoRequestListener extends AbstractRequestListener {
        @Override
        public void onComplete(VKResponse response) {
            super.onComplete(response);
            Loger.log("profid", "onComplete " + response);
            setPhoto(response);
        }
        private void setPhoto(VKResponse response) {
            try {
                JSONObject r = response.json.getJSONArray("response").getJSONObject(0);
                if(r.getString("photo_max_orig")!=null) {
                    PhotoLoader.loadPhoto(getApplicationContext(),r.getString("photo_max_orig"),(ImageView) findViewById(R.id.ivPhoto));
                }
            } catch (JSONException e) {
                Log.e(e.getMessage(), e.toString());
            }
        }
    }
}
