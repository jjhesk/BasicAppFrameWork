package com.hkm.dllocker.module.events;

import com.hkm.dllocker.module.realm.UriCap;

/**
 * Created by hesk on 21/1/16.
 */
public class menuCall {
    private UriCap cap;
    private int action;
    public static final int COPY = 1, VALIDATE = 2, SHARE = 3;

    public menuCall(UriCap capsual) {
        cap = capsual;
    }

    public UriCap getCap() {
        return cap;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
