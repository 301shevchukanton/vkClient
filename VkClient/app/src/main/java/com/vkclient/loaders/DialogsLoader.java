package com.vkclient.loaders;

import android.content.Context;

import com.vkclient.listeners.DialogsLoaderListener;

public interface DialogsLoader {
    void load(DialogsLoaderListener dialogsLoaderListener, Context context);
}
