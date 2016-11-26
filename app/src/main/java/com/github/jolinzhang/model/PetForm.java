package com.github.jolinzhang.model;

import java.util.Date;

/**
 * Created by Shadow on 11/25/16.
 */

public class PetForm {

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

    public Date getBirthday() {
        return birthday;
    }

    public String getChipCompany() {
        return chipCompany;
    }

    public String getChipId() {
        return chipId;
    }

    public String getMedications() {
        return medications;
    }

    public String getName() {
        return name;
    }

    public String getSpecies() {
        return species;
    }

    public String getVetName() {
        return vetName;
    }

    public String getVetPhone() {
        return vetPhone;
    }

    public boolean isFemale() {
        return isFemale;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setChipCompany(String chipCompany) {
        this.chipCompany = chipCompany;
    }

    public void setChipId(String chipId) {
        this.chipId = chipId;
    }

    public void setFemale(boolean female) {
        isFemale = female;
    }

    public void setMedications(String medications) {
        this.medications = medications;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public void setVetName(String vetName) {
        this.vetName = vetName;
    }

    public void setVetPhone(String vetPhone) {
        this.vetPhone = vetPhone;
    }
}
