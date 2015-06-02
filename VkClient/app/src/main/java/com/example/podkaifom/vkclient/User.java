package com.example.podkaifom.vkclient;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import org.joda.time.DateTime;

import java.util.List;

public class User {
    public final static String[] relationshipStatus = {"nonset", "single","in a relationship","engaged","married","married","actively searching","in love"};
    private final int id;
    private final String name;
    private final DateTime birthDate;
    private final String dateFormat;
    private final String photo;

    public User(int id,String name, DateTime birthDate, String dateFormat) {
        this.name = name;
        this.birthDate = birthDate;
        this.dateFormat = dateFormat;
        this.photo=null;
        this.id=id;
    }

    public User(int id,String name, DateTime birthDate, String photo, String dateFormat ) {
        this.name = name;
        this.birthDate = birthDate;
        this.dateFormat = dateFormat;
        this.photo=photo;
        this.id=id;
    }
    public DateTime getBirthDate() {
        return birthDate;
    }

    public String getName() {
        return name;
    }
    public int getId() {
        return id;
    }
    public String getDateFormat() {
        return dateFormat;
    }
    public String getPhoto() {
        return photo;
    }

}
