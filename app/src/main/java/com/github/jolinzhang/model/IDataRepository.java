package com.github.jolinzhang.model;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.SyncUser;

/**
 * Created by Zengtai Qi - zxq150130 on 11/24/16.
 */

public interface IDataRepository {

    Pet getPet(String id);

    Event getEvent(String id);

    void createOrUpdatePet(Pet pet);

    void createOrUpdateEvent( Event event,
                                    Realm.Transaction.OnSuccess onSuccess,
                                    Realm.Transaction.OnError onError);

    void getPet(RealmChangeListener<Pet> listener);
    void getPets(RealmChangeListener<RealmResults<Pet>> listener);
    void getPastEvents(RealmChangeListener<RealmResults<Event>> listener);
    void getPastEventsWithPicture(RealmChangeListener<RealmResults<Event>> listener);
    void getFutureEvents(RealmChangeListener<RealmResults<Event>> listener);
    void getEventsOnThisDay(RealmChangeListener<RealmResults<Event>> listener);

    void deleteEvent(String id);

    void login(final SyncUser.Callback callback);

}
