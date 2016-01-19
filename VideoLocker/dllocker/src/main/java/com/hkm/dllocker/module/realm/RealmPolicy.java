package com.hkm.dllocker.module.realm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

import io.realm.Realm;
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

    public static void exportDatabase(Activity activity, RealmConfiguration configuration) {

        // init realm
        Realm realm = Realm.getInstance(configuration);

        File exportRealmFile = null;
        try {
            // get or create an "export.realm" file
            exportRealmFile = new File(activity.getExternalCacheDir(), "export.realm");

            // if "export.realm" already exists, delete
            exportRealmFile.delete();

            // copy current realm to "export.realm"
            realm.writeCopyTo(exportRealmFile);

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        realm.close();

        // init email intent and add export.realm as attachment
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, "YOUR MAIL");
        intent.putExtra(Intent.EXTRA_SUBJECT, "YOUR SUBJECT");
        intent.putExtra(Intent.EXTRA_TEXT, "YOUR TEXT");
        Uri u = Uri.fromFile(exportRealmFile);
        intent.putExtra(Intent.EXTRA_STREAM, u);

        // start email intent
        activity.startActivity(Intent.createChooser(intent, "Export Data"));
    }

}
