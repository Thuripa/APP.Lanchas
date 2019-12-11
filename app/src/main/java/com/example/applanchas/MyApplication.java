package com.example.applanchas;

import android.app.Application;

import com.example.applanchas.Modelos.Migration;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder().name("RealmData.realm")
                .schemaVersion(2)
                .migration(new Migration())
                .build();
        Realm.setDefaultConfiguration(configuration);

    }
}
