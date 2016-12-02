package com.github.jolinzhang.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Zengtai Qi - zxq150130 on 11/20/16.
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

    /**
     * Zengtai Qi - zxq150130
     */
    public void setNightMode(Boolean value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putBoolean(NIGHT_MODE, value);
        editor.commit();
    }

    /**
     * Zengtai Qi - zxq150130
     */
    public boolean getNightMode() {
        return sharedPreferences.getBoolean(NIGHT_MODE, false);
    }

    /**
     * Zengtai Qi - zxq150130
     */
    public void setNightModeAuto(Boolean value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putBoolean(NIGHT_MODE_AUTO, value);
        editor.commit();
    }

    /**
     * Zengtai Qi - zxq150130
     */
    public Boolean getNightModeAuto() {
        return sharedPreferences.getBoolean(NIGHT_MODE_AUTO, false);
    }

    /**
     * Zengtai Qi - zxq150130
     */
    private SharedPreferences.Editor getEditor() {
        return sharedPreferences.edit();
    }

}
