package com.github.jolinzhang.model;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by Shadow on 11/24/16.
 */

public interface IDataRepository {

    Pet getPet(String id);

    void createOrUpdatePet(Pet pet);

    void createOrUpdateEvent(Event event);

    void getPet(RealmChangeListener<Pet> listener);
    void getPets(RealmChangeListener<RealmResults<Pet>> listener);
    void getPastEvents(RealmChangeListener<RealmResults<Event>> listener);
    void getPastEventsWithPicture(RealmChangeListener<RealmResults<Event>> listener);
    void getFutureEvents(RealmChangeListener<RealmResults<Event>> listener);

}
