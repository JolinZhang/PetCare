package com.github.jolinzhang.model;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by Shadow on 11/24/16.
 */

public class DataRepoConfig implements IDataRepoConfig {

    private Context context;

    private DataRepoConfig() {}

    private static DataRepoConfig instance = new DataRepoConfig();

    public static IDataRepoConfig getInstance() {
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
    }

    String getCurrentPetId() { return getSharePreferences().getString(CURRENT_PET_ID, null); }

    private String PET_IDS = "PET_IDS";

    Set<String> getPetIds() {
        return getSharePreferences().getStringSet(PET_IDS, null);
    }

    public void addPetId(String id) {
        SharedPreferences.Editor editor = getEditor();
        Set<String> ids = getPetIds();
        ids.add(id);
        editor.putStringSet(PET_IDS, ids);
    }

    public void removePetId(String id) {
        SharedPreferences.Editor editor = getEditor();
        Set<String> ids = getPetIds();
        ids.remove(id);
        editor.putStringSet(PET_IDS, ids);
    }

}