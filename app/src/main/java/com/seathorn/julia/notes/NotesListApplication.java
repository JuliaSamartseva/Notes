package com.seathorn.julia.notes;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

//initialisation of Realm
public class NotesListApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("notes.realm")
                .schemaVersion(0)
                .build();
        Realm.setDefaultConfiguration(realmConfig);
    }
}
