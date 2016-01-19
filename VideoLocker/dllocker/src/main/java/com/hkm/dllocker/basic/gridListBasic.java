package com.hkm.dllocker.basic;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.hkm.dllocker.R;
import com.hkm.dllocker.ads.ListAd;
import com.hkm.dllocker.module.DLUtil;
import com.hkm.layout.Module.easyAdapter;
import com.hkm.layout.fragment.basicfeed;

import com.hkm.layout.fragment.patchHyprbid;
import com.marshalchen.ultimaterecyclerview.AdmobAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

/**
 * Created by hesk on 13/1/16.
 */
public abstract class gridListBasic<Binder, Data, adapter extends easyAdapter> extends basicfeed {
    protected LoadMoreGridLayoutManager mLayoutManager;

    @Override
    protected void onClickItem(long routePID) {

    }

    @Override
    protected void onClickItem(String route) {

    }

    @Override
    protected void setUltimateRecyclerViewExtra(UltimateRecyclerView listview) {
        if (DLUtil.isTablet(getActivity())) {
            int colm = 2;
            if (DLUtil.isLandscape(getActivity())) {
                colm = 4;
            } else {
                colm = 2;
            }
            mLayoutManager = new LoadMoreGridLayoutManager(getActivity(), colm, getAdapter());
        } else
            mLayoutManager = new LoadMoreGridLayoutManager(getActivity(), 1, getAdapter());

        listview_layout.setLayoutManager(mLayoutManager);


    }

    protected patchHyprbid sw;

    protected final AdmobAdapter.AdviewListener lsi = new AdmobAdapter.AdviewListener<RelativeLayout>() {
        @Override
        public RelativeLayout onGenerateAdview() {
            return ListAd.newAdView(getActivity());
        }
    };

    protected int normalLayoutResId() {
        return R.layout.item_video;
    }


    protected abstract void binddata(final Binder holder, final Data data, int position);


    protected abstract adapter getAdapter();


    /**
     * step 1:
     * takes the arguement form the intent bundle and determine if there is a need to queue a loading process. If that is a yes then we need to load up the data before displaying the list out.
     *
     * @param r and the data bundle
     * @return tells if  there is a loading process to be done before hand
     */
    @Override
    protected boolean onArguments(Bundle r) {
        return false;
    }

    /**
     * step 2:
     * this is the call for the loading the data stream externally
     */
    @Override
    protected void loadDataInitial() {

    }


}
