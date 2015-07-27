package com.vkclient.views;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.podkaifom.vkclient.R;

public class ProfileInfoView extends LinearLayout {
    private TextView caption;
    private TextView value;

    public ProfileInfoView(Context context) {
        this(context, null);
    }

    public ProfileInfoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProfileInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.profile_info_layout_view, this);
        findViews();
    }

    private void findViews() {
        this.caption = (TextView) findViewById(R.id.caption);
        this.value = (TextView) findViewById(R.id.value);
    }

    public void setCaption(String caption) {
        this.caption.setText(caption);
    }

    public void setValue(String value) {
        this.value.setText(value);
    }
}
