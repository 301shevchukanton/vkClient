package com.vkclient.entities;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.TimeZone;

public class Dialog {
    private final int id;
    private final long date;
    private final int user_id;
    private final boolean readState;
    private final String title;
    private final String body;
    private String photoLink_200 ="";
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
        return this.id;
    }

    public long getDate() {
        return this.date;
    }

    public int getUser_id() {
        return this.user_id;
    }

    public boolean getReadState() {
        return this.readState;
    }

    public String getTitle() {
        return this.title;
    }

    public String getBody() {
        return this.body;
    }
    public  DateTime getParsedDate()
    {
        DateTime dateTime = new DateTime( ( this.date * 1000L + TimeZone.getDefault().getRawOffset()));

        return dateTime;
    }
    public void setUsername(String name){
        this.username=name;
    }
    public String getUsername(){
       return this.username;
    }
    public void setPhoto(String photo){
        this.photoLink_200 =photo;
    }
    public String getGetPhoto(){
        return this.photoLink_200;
    }
    public static Dialog parseDialog(JSONObject object){
        Dialog result=null;
        try{
                result = new Dialog(Integer.parseInt(object.getString("id")),
                        Long.parseLong(object.getString("date")),
                        Integer.parseInt(object.getString("user_id")),
                        (object.getString("read_state").equals("1")),
                        object.getString("title"),
                        object.getString("body"));
            } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}
