package com.vkclient.entities;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.DateTime;

public class User implements Parcelable {
    public final static String[] RELATIONSHIP_STATUS = {"nonset", "single", "in a relationship", "engaged", "married", "married", "actively searching", "in love"};
    private int id;
    private String name;
    private String status;
    private DateTime birthDate;
    private String bdDateString;
    private String city;
    private String univers;
    private String relationship;
    private String dateFormat;
    private String photo;
    private String photoMax;
    private String langs;

    public User() {
        this.name = null;
        this.birthDate = null;
        this.dateFormat = null;
        this.photo = null;
        this.photoMax = null;
        this.id = 1;
    }

    private User(Parcel in) {
        this.name = in.readString();
        this.birthDate = (DateTime) in.readValue(null);
        this.dateFormat = in.readString();
        this.photo = in.readString();
        this.photoMax = in.readString();
        this.id = in.readInt();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhoto() {
        return photo;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setBdDateString(String bdDateString) {
        this.bdDateString = bdDateString;
    }

    public String getBdDateString() {
        return bdDateString;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setUnivers(String univers) {
        this.univers = univers;
    }

    public String getUnivers() {
        return univers;
    }

    public void setLangs(String langs) {
        this.langs = langs;
    }

    public String getLangs() {
        return langs;
    }

    public void setPhotoMax(String photoMax) {
        this.photoMax = photoMax;
    }

    public String getPhotoMax() {
        return photoMax;
    }

    public void setBirthDate(DateTime birthDate) {
        this.birthDate = birthDate;
    }

    public DateTime getBirthDate() {
        return birthDate;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.name);
        out.writeValue(this.birthDate);
        out.writeString(this.dateFormat);
        out.writeString(this.photo);
        out.writeString(this.photoMax);
        out.writeInt(this.id);
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
