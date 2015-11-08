package com.vkclient.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.vkclient.entities.Dialog;
import com.vkclient.supports.Logger;

import java.util.ArrayList;
import java.util.List;

public class DialogsRepository extends SQLiteOpenHelper implements IDialogsRepository {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "dialogsManager";
    private static final String TABLE_DIALOGS = "dialogs";
    private static final String KEY_BODY = "text";
    private static final String KEY_DATE = "date";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_PHOTO_URL = "photo";
    private static final String KEY_NAME = "name";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_CHAT_ID = "chat_id";

    public DialogsRepository(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_DIALOGS_TABLE = "CREATE TABLE " + TABLE_DIALOGS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DATE + " INTEGER,"
                + KEY_USER_ID + " INTEGER," + KEY_TITLE + " TEXT ,"
                + KEY_BODY + " TEXT ," + KEY_CHAT_ID + " INTEGER ,"
                + KEY_NAME + " TEXT ," + KEY_PHOTO_URL + " TEXT" + ")";
        db.execSQL(CREATE_DIALOGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIALOGS);
        Logger.logDebug("DB:", "dropped");
        onCreate(db);
    }

    @Override
    public void addDialog(Dialog dialog) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DATE, dialog.getDate());
        values.put(KEY_USER_ID, dialog.getUser_id());
        values.put(KEY_TITLE, dialog.getTitle());
        values.put(KEY_BODY, dialog.getBody());
        values.put(KEY_CHAT_ID, dialog.getChatId());
        values.put(KEY_NAME, dialog.getUsername());
        values.put(KEY_PHOTO_URL, dialog.getUserPhotoLink());
        db.insert(TABLE_DIALOGS, null, values);
        db.close();
    }

    @Override
    public void addAllDialogs(List<Dialog> dialogs) {
        for (int i = 0; i < dialogs.size(); i++) {
            addDialog(dialogs.get(i));
        }
    }

    @Override
    public Dialog getDialog(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_DIALOGS, new String[]{
                        KEY_DATE, KEY_USER_ID, KEY_TITLE, KEY_BODY, KEY_CHAT_ID, KEY_NAME, KEY_PHOTO_URL}, KEY_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        Dialog dialog = new Dialog(Integer.parseInt(cursor.getString(0)),
                Long.parseLong(cursor.getString(1)),
                Integer.parseInt(cursor.getString(2)),
                true,
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5));

        dialog.setUsername(cursor.getString(6));
        dialog.setUserPhotoLink(cursor.getString(7));
        return dialog;
    }

    @Override
    public List<Dialog> getAllDialogs() {
        List<Dialog> dialogList = new ArrayList<Dialog>();
        String selectQuery = "SELECT  * FROM " + TABLE_DIALOGS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Dialog dialog = new Dialog(Integer.parseInt(cursor.getString(0)),
                        Long.parseLong(cursor.getString(1)),
                        Integer.parseInt(cursor.getString(2)),
                        true,
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5));
                dialog.setUsername(cursor.getString(6));
                if (!cursor.isNull(7)) {
                    dialog.setUserPhotoLink(cursor.getString(7));
                }
                dialogList.add(dialog);
            } while (cursor.moveToNext());
        }

        return dialogList;
    }

    @Override
    public int updateDialog(Dialog dialog) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DATE, dialog.getDate());
        values.put(KEY_USER_ID, dialog.getUser_id());
        values.put(KEY_TITLE, dialog.getTitle());
        values.put(KEY_BODY, dialog.getBody());
        values.put(KEY_CHAT_ID, dialog.getChatId());
        values.put(KEY_NAME, dialog.getUsername());
        values.put(KEY_PHOTO_URL, dialog.getUserPhotoLink());

        return db.update(TABLE_DIALOGS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(dialog.getId())});
    }

    @Override
    public void deleteDialog(Dialog dialog) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DIALOGS, KEY_BODY + " = ?", new String[]{String.valueOf(dialog.getId())});
        db.close();
    }

    @Override
    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DIALOGS, null, null);
        db.close();
    }

    @Override
    public int getDialogsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_DIALOGS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }
}
