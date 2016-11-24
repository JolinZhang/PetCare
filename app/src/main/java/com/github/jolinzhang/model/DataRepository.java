package com.github.jolinzhang.model;

import android.content.Context;
import android.widget.Toast;

import com.github.jolinzhang.util.Configuration;

import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

/**
 * Created by Shadow on 11/24/16.
 */

public class DataRepository implements IDataRepository {

    private static DataRepository instance = new DataRepository();
    private DataRepository() {}

    public static final String AUTH_URL = "http://" + "138.68.55.252" + ":9080/auth";
    public static final String REALM_URL = "realm://" + "138.68.55.252" + ":9080/~/petcare";

    public static IDataRepository getInstance() { return instance; }

    public static void init(Context context) {
        Realm.init(context);
        final DataRepository repo = instance;
        final Context thisContext = context;
        SyncUser.loginAsync(SyncCredentials.usernamePassword("diva@petcare.com", "can", false), AUTH_URL, new SyncUser.Callback() {
            @Override
            public void onSuccess(SyncUser user) {
                SyncConfiguration defaultConfig = new SyncConfiguration.Builder(user, REALM_URL).build();
                Realm.setDefaultConfiguration(defaultConfig);
                repo.realm = Realm.getDefaultInstance();
            }

            @Override
            public void onError(ObjectServerError error) {
                Toast.makeText(thisContext, "Failed connecting to database.", Toast.LENGTH_LONG).show();
                repo.realm = Realm.getDefaultInstance();
            }
        });
    }

    private Realm realm;

    private RealmResults<Pet> pets;
    private RealmResults<Event> pastEvents;
    private RealmResults<Event> pastEventsWithPicture;
    private RealmResults<Event> futureEvents;

    @Override
    public Pet getPet() {
        return realm.where(Pet.class).equalTo("id", Configuration.getInstance().getCurrentPetId())
                .findFirstAsync();
    }

    @Override
    public RealmResults<Pet> getPets() {
        if (pets != null) { return pets; }
        return realm.where(Pet.class)
                .in("id", (String[]) Configuration.getInstance().getPetIds().toArray())
                .findAllSortedAsync("id");
    }

    @Override
    public RealmResults<Event> getPastEvents() {
        if (pastEvents != null) { return pastEvents; }
        return realm.where(Event.class)
                .equalTo("owner.id", Configuration.getInstance().getCurrentPetId())
                .equalTo("isCompleted", true)
                .findAllSortedAsync("datetime");
    }

    @Override
    public RealmResults<Event> getPastEventsWithPicture() {
        if (pastEventsWithPicture != null) { return pastEventsWithPicture; }
        return realm.where(Event.class)
                .equalTo("owner.id", Configuration.getInstance().getCurrentPetId())
                .equalTo("isCompleted", true)
                .equalTo("hasPicture", true)
                .findAllSortedAsync("datetime");
    }

    @Override
    public RealmResults<Event> getFutureEvents() {
        if (futureEvents != null) { return futureEvents; }
        return realm.where(Event.class)
                .equalTo("owner.id", Configuration.getInstance().getCurrentPetId())
                .equalTo("isCompleted", false)
                .findAllSortedAsync("datetime");
    }
}