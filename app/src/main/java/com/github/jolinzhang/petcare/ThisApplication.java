package com.github.jolinzhang.petcare;

/**
 * Created by Shadow on 11/21/16.
 */

import android.app.Application;
import android.widget.Toast;

//import com.github.jolinzhang.model.Event;
//import com.github.jolinzhang.model.Pet;

import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

public class ThisApplication extends Application implements SyncUser.Callback {

    public static final String AUTH_URL = "http://" + "138.68.55.252" + ":9080/auth";
    public static final String REALM_URL = "realm://" + "138.68.55.252" + ":9080/~/petcare";

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        SyncUser.loginAsync(SyncCredentials.usernamePassword("diva@petcare.com", "can", false), ThisApplication.AUTH_URL, this);
    }

    @Override
    public void onSuccess(SyncUser user) {
        SyncConfiguration defaultConfig = new SyncConfiguration.Builder(user, ThisApplication.REALM_URL).build();
        Realm.setDefaultConfiguration(defaultConfig);
    }

    @Override
    public void onError(ObjectServerError error) {
        Toast.makeText(this, "Failed connecting to database.", Toast.LENGTH_LONG).show();
    }
}
