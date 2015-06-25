package com.vkclient.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.vkclient.entities.AbstractRequestListener;
import com.vkclient.supports.AlertBuilder;
import com.vkclient.supports.Loger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;


public class WallPostActivity extends VkSdkActivity
{
    private VKRequest currentRequest;
    private Bitmap photo;
    private Bitmap selectedBitmap=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        profileId=getIntent().getStringExtra("id");
        startLoading();
        super.onCreate(savedInstanceState);
        VKUIHelper.onCreate(this);
        setContentView(R.layout.activity_wall_post);
        findViewById(R.id.ibAddPhoto).setOnClickListener(this.wallPostClickListener);
        findViewById(R.id.btWallPost).setOnClickListener(this.wallPostClickListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if(resultCode == RESULT_OK&&requestCode==1){
                Uri selectedImage = data.getData();
                selectPhoto(selectedImage);
            }
    }
    private void makePost(String message) {
        VKRequest post = VKApi.wall().post(VKParameters.from(VKApiConst.OWNER_ID, profileId, VKApiConst.MESSAGE, message));
        post.setModelClass(VKWallPostResult.class);
        post.executeWithListener(this.wallPostRequestListener);
    }
    private void makePost(VKAttachments attachments, String message) {
        VKRequest post = VKApi.wall().post(VKParameters.from(VKApiConst.OWNER_ID, profileId, VKApiConst.ATTACHMENTS, attachments, VKApiConst.MESSAGE, message));
        post.setModelClass(VKWallPostResult.class);
        post.executeWithListener(this.wallPostRequestListener);
    }
    private void showError(VKError error) {
        AlertBuilder.showErrorMessage(WallPostActivity.this,error.errorMessage);
        if (error.httpError != null) {
            Log.w("Test", "Error in request or upload", error.httpError);
        }
    }
    private void startLoading() {
        if (this.currentRequest != null) {
            this.currentRequest.cancel();
        }
         Loger.log("profid", "onComplete " + profileId);
        this.currentRequest = RequestCreator.getFullUserById(profileId);
        this.currentRequest.executeWithListener(new AbstractRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                 Loger.log("profid", "onComplete " + response);
                setUserInfo(response);
            }

            private void setUserInfo(VKResponse response) {
                try {
                     Loger.log("profid", "seting inf " + profileId);
                    JSONObject r = response.json.getJSONArray("response").getJSONObject(0);
                    if (r.getString("last_name") != null && r.getString("first_name") != null)
                        ((TextView) findViewById(R.id.tvPostName)).setText(r.getString("first_name") + " " + r.getString("last_name"));
                    try {
                        Picasso.with(getApplicationContext())
                                .load(r.getString("photo_200"))
                                .into((ImageView) findViewById(R.id.ivPostPhoto));
                    } catch (Exception e) {
                    }
                } catch (JSONException e) {
                    Log.e(e.getMessage(), e.toString());
                }
            }
        });
    }
    private void pickPhoto(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);
    }
    private void selectPhoto(Uri selectedImage) {
        InputStream imageStream;
        try {
            imageStream = getContentResolver().openInputStream(selectedImage);
            this.selectedBitmap = BitmapFactory.decodeStream(imageStream);
            this.photo = this.selectedBitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void post(){
        final String msg = ((TextView) findViewById(R.id.post)).getText().toString();
        Toast.makeText(getApplicationContext(), "posted successful", Toast.LENGTH_LONG).show();
        ((TextView) findViewById(R.id.post)).setText("");
        if(this.photo!=null){
        VKRequest request = RequestCreator.uploadPhotoToUser(profileId, this.photo);
        request.executeWithListener(new AbstractRequestListener() {
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
    private VKRequest.VKRequestListener wallPostRequestListener = new VKRequest.VKRequestListener() {
        @Override
        public void onComplete(VKResponse response) {
            super.onComplete(response);
        }

        @Override
        public void onError(VKError error) {
            showError(error.apiError != null ? error.apiError : error);
        }
    };
    private View.OnClickListener wallPostClickListener  = new View.OnClickListener() {
        @Override
        public void onClick(final View v)
        {
            switch (v.getId()){
                case R.id.ibAddPhoto: {
                    pickPhoto();
                    break;
                }
                case R.id.btWallPost:{
                    post();
                    break;
                }
            }
        }
    };
}
