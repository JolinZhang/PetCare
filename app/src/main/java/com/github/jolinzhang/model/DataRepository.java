package com.github.jolinzhang.model;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.RealmChangeListener;
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

    public static DataRepository getInstance() { return instance; }

    public static void init(Context context) {
        Realm.init(context);
        final DataRepository repo = instance;
        final Context thisContext = context;
        repo.realm = Realm.getDefaultInstance();
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
            }
        });
    }

    private Realm realm;

    private Pet pet;
    private List<RealmChangeListener<Pet>> petListeners = new ArrayList<>();

    private RealmResults<Pet> pets;
    private List<RealmChangeListener<RealmResults<Pet>>> petsListeners = new ArrayList<>();

    private RealmResults<Event> pastEvents;
    private List<RealmChangeListener<RealmResults<Event>>> pastEventsListeners = new ArrayList<>();

    private RealmResults<Event> pastEventsWithPicture;
    private List<RealmChangeListener<RealmResults<Event>>> pastEventsWithPictureListeners = new ArrayList<>();

    private RealmResults<Event> futureEvents;
    private List<RealmChangeListener<RealmResults<Event>>> futureEventsListeners = new ArrayList<>();

    private Pet getPet() {
        if (pet != null) { return pet; }
        return realm.where(Pet.class).equalTo("id", DataRepoConfig.getInstance().getCurrentPetId())
                .findFirst();
    }

    private RealmResults<Pet> getPets() {
        if (pets != null) { return pets; }
        Set<String> idsSet = DataRepoConfig.getInstance().getPetIds();
        String[] idsArray;
        if (idsSet.size() == 0) { idsArray = new String[] {""}; }
        else { idsArray = idsSet.toArray(new String[idsSet.size()]); }
        return realm.where(Pet.class)
                .in("id", idsArray)
                .findAllSortedAsync("id");
    }

    private RealmResults<Event> getPastEvents() {
        if (pastEvents != null) { return pastEvents; }
        return realm.where(Event.class)
                .equalTo("owner.id", DataRepoConfig.getInstance().getCurrentPetId())
                .equalTo("isCompleted", true)
                .findAllSortedAsync("datetime");
    }

    private RealmResults<Event> getPastEventsWithPicture() {
        if (pastEventsWithPicture != null) { return pastEventsWithPicture; }
        return realm.where(Event.class)
                .equalTo("owner.id", DataRepoConfig.getInstance().getCurrentPetId())
                .equalTo("isCompleted", true)
                .equalTo("hasPicture", true)
                .findAllSortedAsync("datetime");
    }

    private RealmResults<Event> getFutureEvents() {
        if (futureEvents != null) { return futureEvents; }
        return realm.where(Event.class)
                .equalTo("owner.id", DataRepoConfig.getInstance().getCurrentPetId())
                .equalTo("isCompleted", false)
                .findAllSortedAsync("datetime");
    }

    @Override
    public Pet getPet(String id) {
        try {
           return realm.copyFromRealm(realm.where(Pet.class).equalTo("id", id).findFirst());
        } catch (IllegalArgumentException e) {
           return null;
        }
    }

    @Override
    public Event getEvent(String id) {
        try {
            return realm.copyFromRealm(realm.where(Event.class).equalTo("id", id).findFirst());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public void createOrUpdatePet(Pet pet) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(pet);
        realm.commitTransaction();
        DataRepoConfig.getInstance().addPetId(pet.getId());
        invalidPetIds();
    }

    @Override
    public void createOrUpdateEvent(Event event) {
        Pet owner = getPet();
        realm.beginTransaction();
        event.setOwner(owner);
        realm.copyToRealmOrUpdate(event);
        realm.commitTransaction();
    }

    @Override
    public void getPet(RealmChangeListener<Pet> listener) {
        petListeners.add(listener);
        getPet().addChangeListener(listener);
    }

    @Override
    public void getPets(RealmChangeListener<RealmResults<Pet>> listener) {
        petsListeners.add(listener);
        getPets().addChangeListener(listener);
    }

    @Override
    public void getPastEvents(RealmChangeListener<RealmResults<Event>> listener) {
        pastEventsListeners.add(listener);
        getPastEvents().addChangeListener(listener);
    }

    @Override
    public void getPastEventsWithPicture(RealmChangeListener<RealmResults<Event>> listener) {
        pastEventsWithPictureListeners.add(listener);
        getPastEventsWithPicture().addChangeListener(listener);
    }

    @Override
    public void getFutureEvents(RealmChangeListener<RealmResults<Event>> listener) {
        futureEventsListeners.add(listener);
        getFutureEvents().addChangeListener(listener);
    }

    void invalid() {
        pet = null;
        for (RealmChangeListener<Pet> listener: petListeners) {
            getPet().addChangeListener(listener);
        }

        pastEvents = null;
        for (RealmChangeListener<RealmResults<Event>> listener: pastEventsListeners) {
            getPastEvents().addChangeListener(listener);
        }

        pastEventsWithPicture = null;
        for (RealmChangeListener<RealmResults<Event>> listener: pastEventsWithPictureListeners) {
            getPastEventsWithPicture().addChangeListener(listener);
        }

        futureEvents = null;
        for (RealmChangeListener<RealmResults<Event>> listener: futureEventsListeners) {
            getFutureEvents().addChangeListener(listener);
        }
    }

    void invalidPetIds() {
        pets = null;
        for (RealmChangeListener<RealmResults<Pet>> listener: petsListeners) {
            getPets().addChangeListener(listener);
        }
    }



}
