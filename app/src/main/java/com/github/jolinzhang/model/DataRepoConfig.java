package com.github.jolinzhang.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.ArraySet;

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
    }

    String getCurrentPetId() { return getSharePreferences().getString(CURRENT_PET_ID, null); }

    private String PET_IDS = "PET_IDS";

    Set<String> getPetIds() {
        Set<String> ids = new HashSet<>();
        ids.add("b5f8c5f9-2dad-4ffb-97da-ee9b2e6ec8bb");
        ids.add("3bcc6392-3886-4cd9-857a-d720c5e19c07");
//        return getSharePreferences().getStringSet(PET_IDS, ids);
        return ids;
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
