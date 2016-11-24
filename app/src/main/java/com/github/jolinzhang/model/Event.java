package com.github.jolinzhang.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Shadow on 11/21/16.
 */

public class Event extends RealmObject {
    @PrimaryKey
    private String id;

    private String title;
    private String description;
    private double longitude;
    private double latitude;
    private Date datetime;
    private boolean isCompleted;
    private Pet owner;

    public Date getDatetime() {
        return datetime;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Pet getOwner() {
        return owner;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setOwner(Pet owner) {
        this.owner = owner;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
