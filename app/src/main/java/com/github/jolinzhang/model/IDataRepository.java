package com.github.jolinzhang.model;

import io.realm.RealmResults;

/**
 * Created by Shadow on 11/24/16.
 */

public interface IDataRepository {

    Pet getPet();

    Pet getPet(String id);

    RealmResults<Pet> getPets();

    RealmResults<Event> getPastEvents();

    RealmResults<Event> getPastEventsWithPicture();

    RealmResults<Event> getFutureEvents();

    void createOrUpdatePet(Pet pet);

    void createOrUpdateEvent(Event event);

}
