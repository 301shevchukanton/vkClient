package com.vkclient.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.podkaifom.vkclient.R;
import com.squareup.picasso.Picasso;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKAttachments;
import com.vk.sdk.api.model.VKPhotoArray;
import com.vk.sdk.api.model.VKWallPostResult;
import com.vkclient.entities.RequestCreator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;


public class WallPostActivity extends VkSdkActivity
{
    private VKRequest currentRequest;
     Bitmap photo;
    Bitmap selectedBitmap=null;
    public final class WallPostRequestListener extends VKRequest.VKRequestListener
    {
        @Override
        public void onComplete(VKResponse response) {
            super.onComplete(response);
        }

        @Override
        public void onError(VKError error) {
            showError(error.apiError != null ? error.apiError : error);
        }
    }
    public class WallPostClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(final View v)
        {
          if(v==findViewById(R.id.addPhotoButton)) pickPhoto();
            if(v==findViewById(R.id.wallPostButton))   post();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        profileId=getIntent().getStringExtra("id");
        startLoading();
        super.onCreate(savedInstanceState);
        VKUIHelper.onCreate(this);
        setContentView(R.layout.activity_wall_post);
        findViewById(R.id.addPhotoButton).setOnClickListener(new WallPostClickListener());
        findViewById(R.id.wallPostButton).setOnClickListener(new WallPostClickListener());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        VKUIHelper.onActivityResult(this, requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    InputStream imageStream;
                    try {
                        imageStream = getContentResolver().openInputStream(selectedImage);
                        selectedBitmap = BitmapFactory.decodeStream(imageStream);
                       selectPhoto();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }
    }
    private void makePost(VKAttachments attachments) {
        makePost(attachments, null);
    }
    private void makePost(String message) {
        VKRequest post = VKApi.wall().post(VKParameters.from(VKApiConst.OWNER_ID, profileId, VKApiConst.MESSAGE, message));
        post.setModelClass(VKWallPostResult.class);
        post.executeWithListener(new WallPostRequestListener());
    }
    private void makePost(VKAttachments attachments, String message) {
        VKRequest post = VKApi.wall().post(VKParameters.from(VKApiConst.OWNER_ID, profileId, VKApiConst.ATTACHMENTS, attachments, VKApiConst.MESSAGE, message));
        post.setModelClass(VKWallPostResult.class);
        post.executeWithListener(new WallPostRequestListener());
    }
    private void showError(VKError error) {
        new AlertDialog.Builder(WallPostActivity.this)
                .setMessage(error.errorMessage)
                .setPositiveButton("OK", null)
                .show();

        if (error.httpError != null) {
            Log.w("Test", "Error in request or upload", error.httpError);
        }
    }
    private void startLoading() {
        if (currentRequest != null) {
            currentRequest.cancel();
        }
        Log.d("profid", "onComplete " + profileId);
        currentRequest = RequestCreator.getFullUserById(profileId);
        currentRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                Log.d("profid", "onComplete " + response);
                setUserInfo(response);
            }

            private void setUserInfo(VKResponse response) {
                try {
                    Log.d("profid", "seting inf " + profileId);
                    JSONObject r = response.json.getJSONArray("response").getJSONObject(0);
                    if (r.getString("last_name") != null && r.getString("first_name") != null)
                        ((TextView) findViewById(R.id.post_name)).setText(r.getString("first_name") + " " + r.getString("last_name"));
                    try {
                        Picasso.with(getApplicationContext())
                                .load(r.getString("photo_200"))
                                .into((ImageView) findViewById(R.id.post_photo));
                    } catch (Exception e) {
                    }
                } catch (JSONException e) {
                    Log.e(e.getMessage(), e.toString());
                }
            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                super.attemptFailed(request, attemptNumber, totalAttempts);
                Log.d("VkDemoApp", "attemptFailed " + request + " " + attemptNumber + " " + totalAttempts);
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                Log.d("VkDemoApp", "onError: " + error);
            }

            @Override
            public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
                super.onProgress(progressType, bytesLoaded, bytesTotal);
                Log.d("VkDemoApp", "onProgress " + progressType + " " + bytesLoaded + " " + bytesTotal);
            }
        });
    }
    private void pickPhoto(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);
    }
    private void selectPhoto() {
        photo = selectedBitmap;
    }
    private void post(){
        final String msg = ((TextView) findViewById(R.id.post)).getText().toString();
        Toast.makeText(getApplicationContext(), "posted successful", Toast.LENGTH_LONG).show();
        ((TextView) findViewById(R.id.post)).setText("");
        if(photo!=null){
        VKRequest request = RequestCreator.uploadPhotoToUser(profileId, photo);
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                photo.recycle();
                VKApiPhoto photoModel = ((VKPhotoArray) response.parsedModel).get(0);
                makePost(new VKAttachments(photoModel), msg);
            }
            @Override
            public void onError(VKError error) {
                showError(error);
            }
        });}
        else{
            makePost(msg);
        }
    }

}
