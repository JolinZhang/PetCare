package com.github.jolinzhang.model;

/**
 * Created by Shadow on 11/24/16.
 */

public interface IDataRepoConfig {

    void setCurrentPetId(String id);
    void addPetId(String id);
    void removePetId(String id);

}
