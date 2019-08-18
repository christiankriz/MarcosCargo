package com.macoscargo.mobileparcels.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.macoscargo.mobileparcels.login.Login;

import io.realm.Realm;
import io.realm.RealmConfiguration;


/**
 * Created by christiannhlabano on 2019/07/16.
 */

public class EasyCargo extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(getApplicationContext());
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name("EasyCargo")
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
        init();
        Intent i = new Intent(getBaseContext(), Login.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);

    }

    private  void init(){

    }
}
