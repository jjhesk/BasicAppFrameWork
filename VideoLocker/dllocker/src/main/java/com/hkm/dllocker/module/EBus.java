package com.hkm.dllocker.module;

import com.hkm.dllocker.module.events.SimpleMenu;
import com.hkm.dllocker.module.events.menuCall;
import com.hkm.dllocker.module.realm.UriCap;
import com.squareup.otto.Bus;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zJJ on 1/19/2016.
 */
public class EBus {
    public static final AtomicInteger atomicInteger = new AtomicInteger(110);
    public static final String HASH = "mHASH";
    private static final Bus BUS = new Bus();

    public static Bus getInstance() {
        return BUS;
    }

    private EBus() {

    }

    public static void post(Object k) {
        getInstance().post(k);
    }

    public static void newItemMenu(UriCap cc, int menuOption) {
        menuCall c = new menuCall(cc);
        c.setAction(menuOption);
        getInstance().post(c);
    }

    public static void callMenu(UriCap cc) {
        SimpleMenu c = new SimpleMenu(cc);
        getInstance().post(c);
    }

}
