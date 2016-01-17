package com.hkm.downloadmedialocker.pages.catelog;

import android.support.annotation.LayoutRes;

import com.hkm.downloadmedialocker.R;
import com.hkm.downloadmedialocker.life.EBus;
import com.squareup.otto.Subscribe;

/**
 * Created by hesk on 7/1/16.
 */
public class template_home_list extends template_general_list {

    @Subscribe
    public void eventRefresh(EBus.refresh re) {
        super.eventRefresh(re);
    }

    @LayoutRes
    protected int setMainLayoutId() {
        return R.layout.jazz_list_home;
    }
}
