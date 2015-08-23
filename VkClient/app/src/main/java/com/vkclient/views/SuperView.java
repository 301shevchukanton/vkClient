package com.vkclient.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.vkclient.activities.PhotoViewActivity;

import org.joda.time.DateTime;

import java.util.TimeZone;

public abstract class SuperView extends LinearLayout {
    public SuperView(Context context) {
        this(context, null);
    }

    public SuperView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuperView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected DateTime getParsedDate(long date) {
        return new DateTime(date * 1000L + TimeZone.getDefault().getRawOffset());
    }

    protected void photoViewCall(String photoUrl) {
        getContext().startActivity(PhotoViewActivity.getPhotoViewIntent(getContext(), photoUrl));
    }
}
