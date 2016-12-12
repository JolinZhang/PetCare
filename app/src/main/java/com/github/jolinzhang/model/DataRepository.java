package com.github.jolinzhang.model;

import android.content.Context;

import com.squareup.picasso.Callback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;
import io.realm.internal.Util;

/**
 * Created by Zengtai Qi - zxq150130 on 11/24/16.
 */

public class DataRepository implements IDataRepository {

    private static DataRepository instance = new DataRepository();
    private DataRepository() {}

    public static final String AUTH_URL = "http://" + "104.236.134.66" + ":9080/auth";
    public static final String REALM_URL = "realm://" + "104.236.134.66" + ":9080/~/petcare";

    /**
     * Zengtai Qi - zxq150130
     */
    public static DataRepository getInstance() { return instance; }

    private Boolean online = false;
    private List<SyncUser.Callback> callbacks = new ArrayList<>();
    private SyncUser user;

    /**
     * Zengtai Qi - zxq150130
     */
    public static void init(Context context) {
        Realm.init(context);
        getInstance().realm = Realm.getDefaultInstance();
        getInstance().login(null);
    }

    /**
     * Zengtai Qi - zxq150130
     */
    @Override
    public void login(final SyncUser.Callback callback) {

        /* If in the middle of logging in. */
        if (online == null) {
            if (callback != null) { callbacks.add(callback); }
        }
        /* If not online. */
        else if (online == false) {
            online = null;
            SyncUser.loginAsync(SyncCredentials.usernamePassword("diva@petcare.com", "can", false), AUTH_URL, new SyncUser.Callback() {
                @Override
                public void onSuccess(SyncUser user) {
                    SyncConfiguration defaultConfig = new SyncConfiguration.Builder(user, REALM_URL).build();
                    Realm.setDefaultConfiguration(defaultConfig);
                    getInstance().realm = Realm.getDefaultInstance();
                    getInstance().user = user;
                    if (callback != null) { callback.onSuccess(user); }
                    for (SyncUser.Callback thisCallback: callbacks) { thisCallback.onSuccess(user); }
                    online = true;
                }

                @Override
                public void onError(ObjectServerError error) {
                    if (callback != null) { callback.onError(error); }
                    for (SyncUser.Callback thisCallback: callbacks) { thisCallback.onError(error); }
                    online = false;
                }
            });
        }

        /* If online. */
        else if (online == true) {
            callback.onSuccess(user);
        }


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

    private RealmResults<Event> eventsOnThisDay;
    private List<RealmChangeListener<RealmResults<Event>>> eventsOnThisDayListeners = new ArrayList<>();

    /**
     * Zengtai Qi - zxq150130
     */
    private Pet getPet() {
        if (pet != null) { return pet; }
        return realm.where(Pet.class).equalTo("id", DataRepoConfig.getInstance().getCurrentPetId())
                .findFirstAsync();
    }

    /**
     * Zengtai Qi - zxq150130
     */
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

    /**
     * Zengtai Qi - zxq150130
     */
    private RealmResults<Event> getPastEvents() {
        if (pastEvents != null) { return pastEvents; }
        return realm.where(Event.class)
                .equalTo("owner.id", DataRepoConfig.getInstance().getCurrentPetId())
                .equalTo("isCompleted", true)
                .findAllSortedAsync("datetime", Sort.DESCENDING);
    }

    /**
     * Zengtai Qi - zxq150130
     */
    private RealmResults<Event> getPastEventsWithPicture() {
        if (pastEventsWithPicture != null) { return pastEventsWithPicture; }
        return realm.where(Event.class)
                .equalTo("owner.id", DataRepoConfig.getInstance().getCurrentPetId())
                .equalTo("isCompleted", true)
                .equalTo("hasPicture", true)
                .findAllSortedAsync("datetime", Sort.DESCENDING);
    }

    /**
     * Zengtai Qi - zxq150130
     */
    private RealmResults<Event> getFutureEvents() {
        if (futureEvents != null) { return futureEvents; }
        return realm.where(Event.class)
                .equalTo("owner.id", DataRepoConfig.getInstance().getCurrentPetId())
                .equalTo("isCompleted", false)
                .findAllSortedAsync("datetime", Sort.ASCENDING);
    }

    /**
     * Zengtai Qi - zxq150130
     */
    private RealmResults<Event> getEventsOnThisDay() {
        if (eventsOnThisDay != null) { return eventsOnThisDay; }
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        RealmResults<Event> onThisDay = realm.where(Event.class)
                .lessThan("datetime", date)
                .findAll();

        return onThisDay;
    }

    /**
     * Zengtai Qi - zxq150130
     */
    @Override
    public Pet getPet(String id) {
        try {
            return realm.copyFromRealm(realm.where(Pet.class).equalTo("id", id).findFirst());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Zengtai Qi - zxq150130
     */
    @Override
    public Event getEvent(String id) {
        try {
            return realm.copyFromRealm(realm.where(Event.class).equalTo("id", id).findFirst());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Zengtai Qi - zxq150130
     */
    @Override
    public void getPet(RealmChangeListener<Pet> listener) {
        petListeners.add(listener);
        getPet().addChangeListener(listener);
    }

    /**
     * Zengtai Qi - zxq150130
     */
    @Override
    public void getPets(RealmChangeListener<RealmResults<Pet>> listener) {
        petsListeners.add(listener);
        getPets().addChangeListener(listener);
    }

    /**
     * Zengtai Qi - zxq150130
     */
    @Override
    public void getPastEvents(RealmChangeListener<RealmResults<Event>> listener) {
        pastEventsListeners.add(listener);
        getPastEvents().addChangeListener(listener);
    }

    /**
     * Zengtai Qi - zxq150130
     */
    @Override
    public void getPastEventsWithPicture(RealmChangeListener<RealmResults<Event>> listener) {
        pastEventsWithPictureListeners.add(listener);
        getPastEventsWithPicture().addChangeListener(listener);
    }

    /**
     * Zengtai Qi - zxq150130
     */
    @Override
    public void getFutureEvents(RealmChangeListener<RealmResults<Event>> listener) {
        futureEventsListeners.add(listener);
        getFutureEvents().addChangeListener(listener);
    }

    /**
     * Zengtai Qi - zxq150130
     */
    @Override
    public void getEventsOnThisDay(RealmChangeListener<RealmResults<Event>> listener) {
        eventsOnThisDayListeners.add(listener);
        getEventsOnThisDay().addChangeListener(listener);
    }

    /**
     * Zengtai Qi - zxq150130
     */
    @Override
    public void createOrUpdatePet(Pet pet) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(pet);
        realm.commitTransaction();
        DataRepoConfig.getInstance().addPetId(pet.getId());
    }

    /**
     * Zengtai Qi - zxq150130
     */
    @Override
    public void createOrUpdateEvent(Event event) {
        Pet owner = realm.where(Pet.class).equalTo("id", DataRepoConfig.getInstance().getCurrentPetId()).findFirst();
        realm.beginTransaction();
        event.setOwner(owner);

        realm.copyToRealmOrUpdate(event);
        realm.commitTransaction();
    }

    /**
     * Zengtai Qi - zxq150130
     */
    @Override
    public void deleteEvent(String id) {
        realm.beginTransaction();
        realm.where(Event.class)
                .equalTo("id", id)
                .findAll()
                .deleteAllFromRealm();
        realm.commitTransaction();
    }

    /**
     * Zengtai Qi - zxq150130
     */
    void invalidAll() {

        invalidPets();

        invalidPet();

        invalidEvents();

    }

    /**
     * Zengtai Qi - zxq150130
     */
    void invalidPet() {
        pet = null;
        for (RealmChangeListener<Pet> listener: petListeners) {
            getPet().addChangeListener(listener);
        }
    }

    /**
     * Zengtai Qi - zxq150130
     */
    void invalidEvents() {
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

        eventsOnThisDay = null;
        for (RealmChangeListener<RealmResults<Event>> listener: eventsOnThisDayListeners) {
            getEventsOnThisDay().addChangeListener(listener);
        }
    }

    /**
     * Zengtai Qi - zxq150130
     */
    void invalidPets() {
        pets = null;
        for (RealmChangeListener<RealmResults<Pet>> listener: petsListeners) {
            getPets().addChangeListener(listener);
        }
    }

}
