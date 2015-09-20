package com.vkclient.loaders;

import com.vkclient.listeners.DialogsLoaderListener;


public class CacheDialogLoader implements DialogsLoader {
    DialogsLoader dialogsLoader;

    public CacheDialogLoader(DialogsLoader dialogsLoader) {
        this.dialogsLoader = dialogsLoader;
    }

    @Override
    public void load(DialogsLoaderListener dialogsLoaderListener) {
        this.dialogsLoader.load(dialogsLoaderListener);
    }
}
