package com.github.jolinzhang.model;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Shadow on 11/24/16.
 */

public class DataRepoConfig implements IDataRepoConfig {

    private Context context;

    private DataRepoConfig() {}

    private static DataRepoConfig instance = new DataRepoConfig();

    public static DataRepoConfig getInstance() {
        return instance;
    }

    public static void init(Context context) {
        instance.context = context;
    }

    private String DATA_REPO_CONFIG_NAME = "DATA_REPO_CONFIG_NAME";

    private SharedPreferences getSharePreferences() {
        return context.getSharedPreferences(DATA_REPO_CONFIG_NAME, Context.MODE_PRIVATE);
    }

    private SharedPreferences.Editor getEditor() {
        return getSharePreferences().edit();
    }

    private String CURRENT_PET_ID = "CURRENT_PET_ID";

    public void setCurrentPetId(String id) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(CURRENT_PET_ID, id);
        editor.commit();
        DataRepository.getInstance().invalid();
    }

    String getCurrentPetId() { return getSharePreferences().getString(CURRENT_PET_ID, ""); }

    private String PET_IDS = "PET_IDS";

    Set<String> getPetIds() {
        Set<String> ids = new HashSet<>();
        return getSharePreferences().getStringSet(PET_IDS, ids);
    }

    public void addPetId(String id) {
        SharedPreferences.Editor editor = getEditor();
        Set<String> ids = getPetIds();
        Set<String> newIds = new HashSet<>(ids);
        newIds.add(id);
        editor.putStringSet(PET_IDS, newIds);
        editor.commit();
        DataRepository.getInstance().invalidPetIds();
    }

    public void removePetId(String id) {
        SharedPreferences.Editor editor = getEditor();
        Set<String> ids = getPetIds();
        Set<String> newIds = new HashSet<>(ids);
        newIds.remove(id);
        editor.putStringSet(PET_IDS, newIds);
        editor.commit();
        Set<String> petIds = getPetIds();
        if (petIds.size() > 0 ) {
            setCurrentPetId(petIds.iterator().next());
        } else {
            setCurrentPetId("");
        }
    }

}
