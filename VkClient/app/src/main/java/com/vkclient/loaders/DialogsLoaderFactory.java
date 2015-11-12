package com.vkclient.loaders;

public class DialogsLoaderFactory {
    public DialogsLoader create() {
        return new CacheDialogLoader(new NetworkDialogLoader());

    }
}

