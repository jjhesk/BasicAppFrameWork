package com.hkm.dllocker.module.realm;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by hesk on 19/1/16.
 */
public class UriCap extends RealmObject {

    public final static int SOUNDCLOUD = 1, FACEBOOK_VIDEO = 2;
    @Index
    @PrimaryKey
    private long id;
    private String media_title;
    @Required
    private String compatible_link;
    @Required
    private String raw_link;
    @Required
    private String date;

    private int media_type;

    public String getCompatible_link() {
        return compatible_link;
    }

    public void setCompatible_link(String compatible_link) {
        this.compatible_link = compatible_link;
    }

    public String getRaw_link() {
        return raw_link;
    }

    public void setRaw_link(String raw_link) {
        this.raw_link = raw_link;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMedia_title() {
        return media_title;
    }

    public void setMedia_title(String media_title) {
        this.media_title = media_title;
    }

    public int getMedia_type() {
        return media_type;
    }

    public void setMedia_type(int media_type) {
        this.media_type = media_type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
