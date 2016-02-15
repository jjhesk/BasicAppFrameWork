package com.hkm.lycollectionsample.pages.featureList;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.hkm.lycollectionsample.R;
import com.hkm.lycollectionsample.adInterstitual.ListAd;
import com.hkm.lycollectionsample.life.Config;
import com.hkm.lycollectionsample.life.HBUtil;
import com.hkm.lycollectionsample.pages.adapters.LoadMoreGridLayoutManager;
import com.hypebeast.sdk.api.model.hbeditorial.ArticleData;
import com.marshalchen.ultimaterecyclerview.AdmobAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.quickAdapter.simpleAdmobAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by hesk on 13/1/16.
 */
public abstract class gridListBasic extends basicfeed {
    protected LoadMoreGridLayoutManager mLayoutManager;

    @Override
    protected void onClickItem(long routePID) {

    }

    @Override
    protected void onClickItem(String route) {

    }

    @Override
    protected void setUltimateRecyclerViewExtra(UltimateRecyclerView listview) {
        if (HBUtil.isTablet(getActivity())) {
            int colm = 2;
            if (HBUtil.isLandscape(getActivity())) {
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


    protected abstract void binddata(final videoListFragment.binder holder, final ArticleData data, int position);


    public class admobadp extends simpleAdmobAdapter<ArticleData, videoListFragment.binder, RelativeLayout> {

        public admobadp(RelativeLayout adview, boolean insertOnce, int setInterval, List<ArticleData> L, AdviewListener listener) {
            super(adview, insertOnce, setInterval, L, listener);
        }

        @Override
        protected void withBindHolder(videoListFragment.binder var1, ArticleData var2, int var3) {
            binddata(var1, var2, var3);
        }


        @Override
        protected int getNormalLayoutResId() {
            return normalLayoutResId();
        }

        @Override
        protected videoListFragment.binder newViewHolder(View view) {
            return new videoListFragment.binder(view);
        }
    }

    private admobadp adapter;

    protected admobadp getAdapter() {
        final List<ArticleData> list = new ArrayList<>();
        if (adapter == null) {
            adapter = new admobadp(
                    ListAd.newAdView(getActivity()),
                    false, ListAd.LIST_AD_INTERVAL,
                    list,
                    lsi);
        } else {
            return adapter;
        }
        if (list.size() < Config.setting.single_page_items) {
            listview_layout.disableLoadmore();
        }
        return adapter;
    }


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
