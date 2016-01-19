package com.hkm.dllocker.module.realm;

import android.content.Context;

import io.realm.RealmConfiguration;

/**
 * Created by hesk on 19/1/16.
 */
public class RealmPolicy {
    public static RealmConfiguration realmCfg(Context app) {
        return new RealmConfiguration.Builder(app)
                .deleteRealmIfMigrationNeeded()
                .build();
    }
}
