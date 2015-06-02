package com.example.podkaifom.vkclient;

import org.joda.time.DateTime;

/**
 * Created by pod kaifom on 02.06.2015.
 */
public class Dialog {
    private final int id;
    private final DateTime date;
    private final int user_id;
    private final int readState;
    private final String title;
    private final String body;

public Dialog(int id, DateTime date, int user_id, int readState, String title, String body){
    this.id = id;
    this.date=date;
    this.user_id=user_id;
    this.readState=readState;
    this.title=title;
    this.body=body;
}
    public int getId() {
        return id;
    }

    public DateTime getDate() {
        return date;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getReadState() {
        return readState;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}
