package com.github.jolinzhang.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Shadow on 11/21/16.
 */

public class Pet extends RealmObject {

    @PrimaryKey
    private String id;

    private String name;
    private String species;
    private Date birthday;
    private boolean isFemale;
    private String vetName;
    private String vetPhone;
    private String chipId;
    private String chipCompany;
    private String medications;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSpecies() {
        return species;
    }

    public Date getBirthday() {
        return birthday;
    }

    public boolean isFemale() {
        return isFemale;
    }

    public String getVetName() {
        return vetName;
    }

    public String getVetPhone() {
        return vetPhone;
    }

    public String getChipId() {
        return chipId;
    }

    public String getChipCompany() {
        return chipCompany;
    }

    public String getMedications() {
        return medications;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setFemale(boolean female) {
        isFemale = female;
    }

    public void setVetName(String vetName) {
        this.vetName = vetName;
    }

    public void setVetPhone(String vetPhone) {
        this.vetPhone = vetPhone;
    }

    public void setChipId(String chipId) {
        this.chipId = chipId;
    }

    public void setChipCompany(String chipCompany) {
        this.chipCompany = chipCompany;
    }

    public void setMedications(String medications) {
        this.medications = medications;
    }

    public void setId(String id) {
        this.id = id;
    }
}
