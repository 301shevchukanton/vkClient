package com.vkclient.listeners;

import com.vkclient.entities.Dialog;

import java.util.List;

public interface DialogsLoaderListener {

    void onLoad(List<Dialog> dialogs);

    void onError();
}
