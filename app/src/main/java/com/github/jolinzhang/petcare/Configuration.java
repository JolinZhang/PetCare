package com.github.jolinzhang.petcare;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by Shadow on 11/20/16.
 */

public class Configuration {

    private static Configuration instance = new Configuration();
    private Configuration() {}
    public static Configuration getInstance() { return instance; }

    private String CONFIGURATION_NAME = "CONFIGURATION_NAME";

    private String NIGHT_MODE = "NIGHT_MODE";
    private String NIGHT_MODE_AUTO = "NIGHT_MODE_AUTO";
    private String PET_IDS = "PET_IDS";

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

    private SharedPreferences getSharePreferences() {
        return ThisApplication.instance.getSharedPreferences(CONFIGURATION_NAME, Context.MODE_PRIVATE);
    }

    private SharedPreferences.Editor getEditor() {
        return ThisApplication.instance.getSharedPreferences(CONFIGURATION_NAME, Context.MODE_PRIVATE).edit();
    }

}
