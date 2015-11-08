package com.vkclient.views;

import android.content.Context;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.podkaifom.vkclient.R;
import com.squareup.picasso.Callback;
import com.vkclient.entities.Dialog;
import com.vkclient.supports.Logger;
import com.vkclient.supports.PhotoLoader;
import com.vkclient.supports.PhotoSaver;

import java.io.File;
import java.io.IOException;


public class DialogListItemView extends ListItemView {

    private static final String DATE_FORMAT = "dd.MM - HH:mm";
    private TextView text;
    private TextView date;
    private TextView name;
    private ImageView photo;
    private String photoUrl;

    public DialogListItemView(Context context) {
        this(context, null);
    }

    public DialogListItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DialogListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.dialogs_list_item, this);
        findViews();
    }

    private void findViews() {
        this.text = (TextView) findViewById(R.id.tvDialogText);
        this.photo = (ImageView) findViewById(R.id.ivDialogPhoto);
        this.date = (TextView) findViewById(R.id.tvDialogDate);
        this.name = (TextView) findViewById(R.id.tvDialogName);
    }

    public void setDialog(Dialog dialog) {
        this.text.setText(dialog.getBody());
        this.name.setText(dialog.getTitle().equals(" ... ") ? dialog.getUsername() : dialog.getTitle());
        this.date.setText(getParsedDate(dialog.getDate()).toString(this.DATE_FORMAT));
        this.text.setBackgroundColor(dialog.getBackgroundColor(getContext()));
        if ((!dialog.getUserPhotoLink().isEmpty())) {

            String folderToSave = Environment.getExternalStorageDirectory().toString();
            this.photoUrl = PhotoSaver.getBaseFileName(dialog.getUserPhotoLink());
            File photoFile = new File(folderToSave, this.photoUrl);
            if (photoFile.exists()) {
                PhotoLoader.loadPhotoFromFile(getContext(), photoFile, this.photo);
            } else {
                PhotoLoader.loadPhoto(getContext(), dialog.getUserPhotoLink(), this.photo, this.callBack);
                Logger.logDebug("Photo url as:", dialog.getUserPhotoLink());
                this.photoUrl = dialog.getUserPhotoLink();
            }
        }
    }

    private Callback callBack = new Callback() {
        @Override
        public void onSuccess() {
            try {
                PhotoSaver.SavePicture(getContext(), photo, photoUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError() {

        }
    };
}
