package com.hkm.dllocker.module;

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

}
