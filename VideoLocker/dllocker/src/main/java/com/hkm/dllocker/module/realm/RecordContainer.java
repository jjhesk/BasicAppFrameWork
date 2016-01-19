package com.hkm.dllocker.module.realm;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.exceptions.RealmError;
import io.realm.exceptions.RealmMigrationNeededException;

/**
 * Created by hesk on 10/12/15.
 */
public class RecordContainer {
    private static final int LIMIT = 10;
    private int totalpages = 1;
    private int current_page = 1;
    private int skipped_upstream_items = 0;
    private String error_message;
    private syncResult message_channel;
    private ArrayList<Long> remove_list = new ArrayList<>();
    private List<UriCap> local_list = new ArrayList<UriCap>();
    private Context context;
    private int worker_status;
    private final RealmConfiguration conf;
    private static RecordContainer instance;
    public static final int
            STATUS_IDEAL = 1,
            STATUS_DOWN_STREAM = 2,
            STATUS_UP_STREAM = 3;

    @IntDef({STATUS_IDEAL, STATUS_DOWN_STREAM, STATUS_UP_STREAM})
    public @interface WishlistSyncStatus {

    }

    public static RecordContainer getInstnce(Context c) {
        if (instance == null) {
            instance = new RecordContainer(c);
        }

        return instance;
    }

    public RecordContainer(Context _c) {
        context = _c;
        worker_status = STATUS_IDEAL;
        conf = RealmPolicy.realmCfg(_c);
    }

    @WishlistSyncStatus
    public int getStatus() {
        return worker_status;
    }

    /**
     * locally add to the storeschema list
     *
     * @param copyproduct the client interface
     * @return bool result of the wishlist being added.
     */
    public boolean addNewRecord(UriCap copyproduct) {
        Realm realm = Realm.getInstance(conf);
        //  if (check_duplicated(realm, copyproduct)) return false;
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(copyproduct);
        realm.commitTransaction();
        return true;
    }

    public void flushRecords() {
        Realm realm = Realm.getInstance(conf);
        RealmResults<UriCap> copies = realm.where(UriCap.class).findAll();
        realm.beginTransaction();
        copies.clear();
        realm.commitTransaction();
    }

    public void removeItem(long product_id) {
        Realm realm = Realm.getInstance(conf);
        RealmResults<UriCap> copies = realm.where(UriCap.class).equalTo("id", product_id).findAll();
        realm.beginTransaction();
        copies.clear();
        realm.commitTransaction();
    }


    public List<UriCap> getAllRecords() {
        Realm realm = Realm.getInstance(conf);
        RealmResults<UriCap> copies = realm.where(UriCap.class).findAll();
        Iterator<UriCap> is = copies.iterator();
        List<UriCap> list = new ArrayList<>();
        while (is.hasNext()) {
            UriCap cap = is.next();
            list.add(cap);
        }
       /* if (copies.size() == 0) {
            return new RealmResults<UriCap>(copies);
        }*/
        return list;
    }

    public boolean check_duplicated(Realm realm, UriCap product) {
        RealmQuery<UriCap> query = realm.where(UriCap.class);
        query.equalTo("id", product.getId());
        // Execute the query:
        RealmResults<UriCap> result = query.findAll();
        return result.size() > 0;
    }

    private void errorTrigger() {
        if (message_channel != null) message_channel.failure(error_message);
    }

    public final int getItemsCount() {
        try {
            Realm realm = Realm.getInstance(conf);
            return realm.where(UriCap.class).findAll().size();
        } catch (RealmMigrationNeededException e) {
            e.fillInStackTrace();
            return 0;
        } catch (RealmError e) {
            e.fillInStackTrace();
            return 0;
        } catch (Exception e) {
            e.fillInStackTrace();
            return 0;
        }
    }

    /**
     * the callback interface for the wishlist sync
     */
    public interface syncResult {
        void showListAll(final List<UriCap> wistlist);

        void failure(String error_message_out);
    }


    public static UriCap newCap(String raw, String product, @Nullable String media_title, int mediaType) {
        UriCap cap = new UriCap();
        cap.setCompatible_link(product);
        cap.setRaw_link(raw);
        cap.setMedia_type(mediaType);
        Date date = new Date();
        Timestamp now = new Timestamp(date.getTime());
        cap.setDate(now.toString());

        if (media_title == null) {
            cap.setMedia_title("N/A");
        } else {
            cap.setMedia_title(media_title);
        }
        return cap;
    }
}
