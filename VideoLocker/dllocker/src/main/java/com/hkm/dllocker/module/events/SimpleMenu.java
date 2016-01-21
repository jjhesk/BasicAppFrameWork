package com.hkm.dllocker.module.events;

import com.hkm.dllocker.module.realm.UriCap;

/**
 * Created by hesk on 21/1/16.
 */
public class SimpleMenu {
    private UriCap cc;

    public SimpleMenu(UriCap cc) {
        this.cc = cc;
    }

    public UriCap getCap() {
        return cc;
    }
}
