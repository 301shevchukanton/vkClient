package com.vkclient.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.podkaifom.vkclient.R;
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
import com.vkclient.entities.User;
import com.vkclient.listeners.AbstractRequestListener;
import com.vkclient.listeners.ConcreteImageLoaderListener;
import com.vkclient.loaders.ImageLoaderFactory;
import com.vkclient.parsers.UserParser;
import com.vkclient.supports.AlertBuilder;
import com.vkclient.supports.Logger;
import com.vkclient.supports.RequestCreator;

import java.io.FileNotFoundException;

public class WallPostFragment extends Fragment {
    private static final int OK_REQUEST_CODE = 1;
    private Bitmap photo;
    private String profileId;
    private TextView postText;
    private TextView postName;
    private ImageView postPhoto;
    private ImageButton addPhoto;
    private Button sendPost;
    protected String message;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewHierarchy = inflater.inflate(R.layout.fragment_wall_post, container, false);
        findViews(viewHierarchy);
        startLoading();
        setViewsListeners();
        return viewHierarchy;
    }

    private void setViewsListeners() {
        addPhoto.setOnClickListener(this.wallPostClickListener);
        sendPost.setOnClickListener(this.wallPostClickListener);
    }

    private void findViews(View viewHierarchy) {
        profileId = getActivity().getIntent().getStringExtra("id");
        postText = (TextView) viewHierarchy.findViewById(R.id.post);
        postName = (TextView) viewHierarchy.findViewById(R.id.tvPostName);
        postPhoto = (ImageView) viewHierarchy.findViewById(R.id.ivPostPhoto);
        addPhoto = (ImageButton) viewHierarchy.findViewById(R.id.ibAddPhoto);
        sendPost = (Button) viewHierarchy.findViewById(R.id.btWallPost);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == OK_REQUEST_CODE) {
            selectPhoto(data.getData());
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
        AlertBuilder.showErrorMessage(getActivity(), error.errorMessage);
        if (error.httpError != null) {
            Logger.logError("Test", getString(R.string.request_error) + error.httpError);
        }
    }

    private void startLoading() {
        RequestCreator.getFullUserById(profileId).executeWithListener(this.userFullRequestListener);
    }

    private void pickPhoto() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);
    }

    private void selectPhoto(Uri selectedImage) {
        try {
            this.photo = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(selectedImage));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void post() {
        this.message = postText.getText().toString();
        postText.setText("");
        if (this.photo != null) {
            VKRequest request = RequestCreator.uploadPhotoToUser(profileId, this.photo);
            request.executeWithListener(this.uploadPhotoRequestListener);
        } else {
            makePost(this.message);
        }
    }

    private AbstractRequestListener uploadPhotoRequestListener = new AbstractRequestListener() {
        @Override
        public void onComplete(VKResponse response) {
            photo.recycle();
            VKApiPhoto photoModel = ((VKPhotoArray) response.parsedModel).get(0);
            makePost(new VKAttachments(photoModel), message);
        }

        @Override
        public void onError(VKError error) {
            showError(error);
        }
    };

    private AbstractRequestListener userFullRequestListener = new AbstractRequestListener() {
        @Override
        public void onComplete(VKResponse response) {
            super.onComplete(response);
            setUserInfo(response);
        }

        private void setUserInfo(VKResponse response) {
            User user = new UserParser().parse(response);
            postName.setText(user.getName());
            new ImageLoaderFactory().create()
                    .load(getActivity(), new ConcreteImageLoaderListener(), user.getPhoto(),
                            postPhoto);

        }
    };
    private VKRequest.VKRequestListener wallPostRequestListener = new VKRequest.VKRequestListener() {
        @Override
        public void onComplete(VKResponse response) {
            super.onComplete(response);
            Toast.makeText(getActivity(), getString(R.string.posted_successful), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onError(VKError error) {
            showError(error.apiError != null ? error.apiError : error);
        }
    };
    private View.OnClickListener wallPostClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            switch (v.getId()) {
                case R.id.ibAddPhoto: {
                    pickPhoto();
                    break;
                }
                case R.id.btWallPost: {
                    post();
                    break;
                }
            }
        }
    };

}
