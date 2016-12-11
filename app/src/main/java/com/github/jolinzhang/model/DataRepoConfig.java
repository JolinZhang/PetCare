package com.github.jolinzhang.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.ContactsContract;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Zengtai Qi - zxq150130 on 11/24/16.
 * The class stores config info.
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

    /**
     * Zengtai Qi - zxq150130
     */
    private SharedPreferences getSharePreferences() {
        return context.getSharedPreferences(DATA_REPO_CONFIG_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Zengtai Qi - zxq150130
     */
    private SharedPreferences.Editor getEditor() {
        return getSharePreferences().edit();
    }

    private String CURRENT_PET_ID = "CURRENT_PET_ID";

    /**
     * Zengtai Qi - zxq150130
     */
    public String getCurrentPetId() { return getSharePreferences().getString(CURRENT_PET_ID, ""); }

    private String PET_IDS = "PET_IDS";

    /**
     * Zengtai Qi - zxq150130
     */
    public Set<String> getPetIds() {
        Set<String> ids = new HashSet<>();
        return getSharePreferences().getStringSet(PET_IDS, ids);
    }

    /**
     * Zengtai Qi - zxq150130
     */
    @Override
    public void setCurrentPetId(String id) {
        SharedPreferences.Editor editor = getEditor();

        editor.putString(CURRENT_PET_ID, id);

        editor.commit();

        DataRepository.getInstance().invalidEvents();
        DataRepository.getInstance().invalidPet();
    }

    /**
     * Zengtai Qi - zxq150130
     */
    @Override
    public void addPetId(String id) {
        SharedPreferences.Editor editor = getEditor();

        Set<String> ids = getPetIds();
        Set<String> newIds = new HashSet<>(ids);
        newIds.add(id);
        editor.putStringSet(PET_IDS, newIds);
        editor.putString(CURRENT_PET_ID, id);

        editor.commit();

        DataRepository.getInstance().invalidAll();
    }

    /**
     * Zengtai Qi - zxq150130
     */
    @Override
    public void removePetId(String id) {
        SharedPreferences.Editor editor = getEditor();

        Set<String> ids = getPetIds();
        Set<String> newIds = new HashSet<>(ids);
        newIds.remove(id);
        editor.putStringSet(PET_IDS, newIds);

        if (newIds.size() > 0 ) {
            editor.putString(CURRENT_PET_ID, newIds.iterator().next());
        } else {
            editor.putString(CURRENT_PET_ID, "");
        }

        editor.commit();

        DataRepository.getInstance().invalidAll();
    }

}
