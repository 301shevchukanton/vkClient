package com.vkclient.loaders;

import android.content.Context;

import com.vkclient.database.DialogsRepository;
import com.vkclient.entities.Dialog;
import com.vkclient.listeners.DialogsLoaderListener;

import java.util.ArrayList;
import java.util.List;


public class CacheDialogLoader implements DialogsLoader {
    DialogsLoader dialogsLoader;

    public CacheDialogLoader(DialogsLoader dialogsLoader) {
        this.dialogsLoader = dialogsLoader;
    }

    @Override
    public void load(DialogsLoaderListener dialogsLoaderListener, Context context) {
        List<Dialog> dialogs = new ArrayList<>();
        DialogsRepository db = new DialogsRepository(context);
        dialogs.clear();
        dialogs.addAll(db.getAllDialogs());
        dialogsLoaderListener.onLoad(dialogs);
        this.dialogsLoader.load(dialogsLoaderListener, context);
    }
}
