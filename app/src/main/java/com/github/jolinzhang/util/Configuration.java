package com.github.jolinzhang.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.github.jolinzhang.petcare.ThisApplication;

import java.util.Set;

/**
 * Created by Shadow on 11/20/16.
 */

public class Configuration {

    private static Configuration instance = new Configuration();
    private Configuration() {}
    public static Configuration getInstance() { return instance; }

    private SharedPreferences sharedPreferences;

    public static void init(Context context) {
        instance.sharedPreferences = context.getSharedPreferences(CONFIGURATION_NAME, Context.MODE_PRIVATE);
    }

    private static final String CONFIGURATION_NAME = "CONFIGURATION_NAME";

    private static final String NIGHT_MODE = "NIGHT_MODE";
    private static final String NIGHT_MODE_AUTO = "NIGHT_MODE_AUTO";
    private static final String PET_IDS = "PET_IDS";
    private static final String CURRENT_PET_ID = "CURRENT_PET_ID";

    public void setNightMode(Boolean value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putBoolean(NIGHT_MODE, value);
        editor.commit();
    }

    public boolean getNightMode() {
        return getSharePreferences().getBoolean(NIGHT_MODE, false);
    }

    public void setNightModeAuto(Boolean value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putBoolean(NIGHT_MODE_AUTO, value);
        editor.commit();
    }

    public Boolean getNightModeAuto() {
        return getSharePreferences().getBoolean(NIGHT_MODE_AUTO, false);
    }

    public void addPet(String petId) {
        SharedPreferences.Editor editor = getEditor();
        Set<String> ids = getPetIds();
        ids.add(petId);
        editor.putStringSet(PET_IDS, ids);
    }

    public void removePet(String petId) {
        SharedPreferences.Editor editor = getEditor();
        Set<String> ids = getPetIds();
        ids.remove(petId);
        editor.putStringSet(PET_IDS, ids);
    }

    public Set<String> getPetIds() {
        return getSharePreferences().getStringSet(PET_IDS, null);
    }

    public String getCurrentPetId() {
        return getSharePreferences().getString(CURRENT_PET_ID, null);
    }

    public void setCurrentPetId(String id) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(CURRENT_PET_ID, id);
    }

    private SharedPreferences getSharePreferences() {
        return sharedPreferences;
    }

    private SharedPreferences.Editor getEditor() {
        return sharedPreferences.edit();
    }

}
