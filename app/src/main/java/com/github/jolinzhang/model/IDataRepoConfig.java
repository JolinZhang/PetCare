package com.github.jolinzhang.model;

/**
 * Created by Zengtai Qi - zxq150130 on 11/24/16.
 */

public interface IDataRepoConfig {
    String getCurrentPetId();
    void setCurrentPetId(String id);
    void addPetId(String id);
    void removePetId(String id);

}
