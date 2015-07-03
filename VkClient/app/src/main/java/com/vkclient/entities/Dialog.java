package com.vkclient.entities;

public class Dialog extends AbstractContentEntity{
    private final String title;
    public Dialog(int id, long date, int user_id, boolean readState, String title, String body){
        this.id = id;
        this.date=date;
        this.user_id=user_id;
        this.readState=readState;
        this.title=title;
        this.body=body;
    }

    public String getTitle() {
        return this.title;
    }
}
