package com.github.jolinzhang.model;

import io.realm.RealmResults;

/**
 * Created by Shadow on 11/24/16.
 */

public interface IDataRepository {

    Pet getPet();

    RealmResults<Pet> getPets();

    RealmResults<Event> getPastEvents();

    RealmResults<Event> getPastEventsWithPicture();

    RealmResults<Event> getFutureEvents();

}
