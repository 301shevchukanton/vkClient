package com.vkclient.database;

import com.vkclient.entities.Dialog;

import java.util.List;


public interface IDialogsRepository {
    void addDialog(Dialog dialog);

    void addAllDialogs(List<Dialog> dialogs);

    Dialog getDialog(int id);

    List<Dialog> getAllDialogs();

    int getDialogsCount();

    int updateDialog(Dialog dialog);

    void deleteDialog(Dialog dialog);

    void deleteAll();
}