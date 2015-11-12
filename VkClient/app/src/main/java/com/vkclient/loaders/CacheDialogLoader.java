package com.vkclient.loaders;

import android.content.Context;

import com.vkclient.database.DialogsRepository;
import com.vkclient.entities.Dialog;
import com.vkclient.listeners.DialogsLoaderListener;

import java.util.List;


public class CacheDialogLoader implements DialogsLoader {
    private DialogsLoader dialogsLoader;
    private Context context;
    private DialogsLoaderListener dialogsLoaderListener;

    public CacheDialogLoader(DialogsLoader dialogsLoader) {
        this.dialogsLoader = dialogsLoader;
    }

    private DialogsLoaderListener dialogsCacheLoaderListener = new DialogsLoaderListener() {
        @Override
        public void onLoad(List<Dialog> dialogs) {
            DialogsRepository db = new DialogsRepository(context);
            db.deleteAll();
            db.addAllDialogs(dialogs);
            dialogsLoader.load(dialogsLoaderListener, context);
        }

        @Override
        public void onError() {
            dialogsLoaderListener.onLoad(new DialogsRepository(context).getAllDialogs());
        }
    };

    @Override
    public void load(DialogsLoaderListener dialogsLoaderListener, Context context) {
        this.context = context;
        this.dialogsLoaderListener = dialogsLoaderListener;
        this.dialogsLoader.load(dialogsCacheLoaderListener, context);
    }
}
