package com.vkclient.views;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.podkaifom.vkclient.R;

public class ProfileInfoLayoutView extends LinearLayout {
    private TextView caption;
    private TextView value;

    public ProfileInfoLayoutView(Context context) {
        this(context, null);
    }

    public ProfileInfoLayoutView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProfileInfoLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.profile_info_layout_view, this);
        findViews();
    }

    private void findViews() {
        this.caption = (TextView) findViewById(R.id.caption);
        this.value = (TextView) findViewById(R.id.value);
    }

    public void setCaption(String captionText) {
        this.caption.setText(captionText);
    }

    public void setValue(String valueText) {
        this.value.setText(valueText);
    }

    public String getCaption() {
        return this.caption.getText().toString();
    }

    public String getValue() {
        return this.value.getText().toString();
    }
}
