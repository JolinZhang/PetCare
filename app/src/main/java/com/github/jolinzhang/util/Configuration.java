package com.github.jolinzhang.util;

import android.content.Context;
import android.content.SharedPreferences;

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

    public void setNightMode(Boolean value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putBoolean(NIGHT_MODE, value);
        editor.commit();
    }

    public boolean getNightMode() {
        return sharedPreferences.getBoolean(NIGHT_MODE, false);
    }

    public void setNightModeAuto(Boolean value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putBoolean(NIGHT_MODE_AUTO, value);
        editor.commit();
    }

    public Boolean getNightModeAuto() {
        return sharedPreferences.getBoolean(NIGHT_MODE_AUTO, false);
    }

    private SharedPreferences.Editor getEditor() {
        return sharedPreferences.edit();
    }

}
