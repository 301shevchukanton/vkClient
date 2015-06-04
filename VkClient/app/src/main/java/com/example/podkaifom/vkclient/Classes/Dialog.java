package com.example.podkaifom.vkclient.Classes;

import org.joda.time.DateTime;

import java.util.TimeZone;

/**
 * Created by pod kaifom on 02.06.2015.
 */
public class Dialog {
    private final int id;
    private final long date;
    private final int user_id;
    private final boolean readState;
    private final String title;
    private final String body;
    private String photo_200;
    private  String username="";
public Dialog(int id, long date, int user_id, boolean readState, String title, String body){
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

    public long getDate() {
        return date;
    }

    public int getUser_id() {
        return user_id;
    }

    public boolean getReadState() {
        return readState;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
    public  DateTime getParsedDate()
    {
        DateTime dateTime = new DateTime( ( (long)this.date * 1000L + TimeZone.getDefault().getRawOffset()));

        return dateTime;
    }
    public void setUsername(String name){
        this.username=name;
    }
    public String getUsername(){
       return this.username;
    }
    public void setPhoto(String photo){
        this.photo_200=photo;
    }
    public String getGetphoto(){
        return this.photo_200;
    }
}
