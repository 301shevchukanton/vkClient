package com.vkclient.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.podkaifom.vkclient.R;
import com.vkclient.entities.Dialog;
import com.vkclient.parsers.UserParser;
import com.vkclient.supports.PhotoLoader;


public class DialogListItemView extends LinearLayout {

    private TextView text;
    private TextView date;
    private TextView name;
    private ImageView photo;

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
        this.name.setText(dialog.getUsername());
        this.date.setText(UserParser.getParsedDate(dialog.getDate()).toString("dd.MM - HH:mm"));
        this.text.setBackgroundColor(!dialog.getReadState() ? Color.LTGRAY : Color.TRANSPARENT);
        if ((!dialog.getUserPhotoLink_200().isEmpty()) && dialog.getUserPhotoLink_200() != null) {
            PhotoLoader.loadPhoto(getContext(), dialog.getUserPhotoLink_200(), this.photo);
        }

    }

}