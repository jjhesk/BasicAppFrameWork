package com.hkm.editorial.pages.featureList;

import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.RelativeLayout;

import com.hkm.editorial.R;
import com.hkm.editorial.adInterstitual.ListAd;
import com.hkm.editorial.life.Config;
import com.hkm.editorial.life.EBus;
import com.hkm.editorial.life.HBUtil;
import com.hypebeast.sdk.api.exception.ApiException;
import com.marshalchen.ultimaterecyclerview.AdmobAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;
import com.marshalchen.ultimaterecyclerview.quickAdapter.BiAdAdapterSwitcher;

import java.util.Random;

/**
 * Created by hesk on 24/7/15.
 */
public abstract class skeleton extends basicfeed {
    protected patchHyprbid sw;
    protected final Handler h = new Handler();
    protected boolean adIncluded = false;
    protected String adapter_url, cate_title, slugtag;
    protected int requestType;

    /**
     * enable the external slider support in here
     */
    private boolean isRecyclerTouched = false;

    public static Bundle con_latest(final @StringRes int title) {
        final Bundle n = new Bundle();
        n.putInt(basicfeed.FRAGMENTTITLE_RESID, title);
        n.putInt(basicfeed.REQUEST_TYPE, basicfeed.LATEST);
        return n;
    }

    public static Bundle general(final String _end_point) {
        final Bundle n = new Bundle();
        //   nd.putInt(basicfeed.FRAGMENTTITLE_RESID, title);
        n.putInt(basicfeed.REQUEST_TYPE, basicfeed.GENERAL);
        n.putString(basicfeed.URL, _end_point);
        return n;
    }

    public static Bundle con_cate(final @StringRes int title, final String cate_item) {
        final Bundle n = new Bundle();
        n.putInt(basicfeed.FRAGMENTTITLE_RESID, title);
        n.putInt(basicfeed.REQUEST_TYPE, basicfeed.CATE);
        n.putString(basicfeed.SLUG, cate_item);
        return n;
    }

    public static Bundle con_cate(final String cate_item) {
        final Bundle n = new Bundle();
        n.putInt(basicfeed.REQUEST_TYPE, basicfeed.CATE);
        n.putString(basicfeed.SLUG, cate_item);
        return n;
    }

    public static Bundle con_major(final String major_tab) {
        final Bundle n = new Bundle();
        n.putInt(basicfeed.REQUEST_TYPE, basicfeed.MAJOR);
        n.putString(basicfeed.SLUG, major_tab);
        return n;
    }

    public static Bundle conSearch(final String search) {
        final Bundle n = new Bundle();
        n.putInt(basicfeed.REQUEST_TYPE, basicfeed.SEARCH);
        n.putString(basicfeed.SEARCH_WORD, search);
        return n;
    }

    protected final AdmobAdapter.AdviewListener lsi = new AdmobAdapter.AdviewListener<RelativeLayout>() {
        @Override
        public RelativeLayout onGenerateAdview() {
            return ListAd.newAdView(getActivity());
        }
    };

    protected final patchHyprbid.onLoadMore request_url_start = new patchHyprbid.onLoadMore() {
        @Override
        public boolean request_start(int current_page_no, int itemsCount, int maxLastVisiblePosition, final BiAdAdapterSwitcher bimod, final boolean reset) {
            try {
                onLoadMore(requestType, current_page_no, Config.setting.single_page_items);
                return true;
            } catch (ApiException e) {
                Log.d("print_error", e.getMessage());
                return true;
            }
        }
    };

    @Override
    protected boolean onArguments(Bundle r) {
        requestType = r.getInt(REQUEST_TYPE, UNSET);
        adapter_url = r.getString(URL, "");
        slugtag = r.getString(SLUG, "");
        cate_title = r.getString(FRAGMENTTITLE, "");
        adIncluded = r.getBoolean(LIST_WITH_AD, true);
        final int resid = r.getInt(FRAGMENTTITLE_RESID, -1);
        if (resid != -1) {
            cate_title = getActivity().getResources().getString(resid);
        }
        return !adapter_url.equalsIgnoreCase("") || requestType != UNSET;
    }

    protected void additionalHypbridAdapter(final patchHyprbid otheroptions) {

        otheroptions
                .setExternalCallback(new patchHyprbid.externalcb() {
                    @Override
                    public void onrefresh() {
                        setRefreshInitial();
                    }
                }).onEnableRefresh(100);

    }


    protected void afterInitiateHyprbidAdapter() {
        if (sw == null) return;
        //todo: refresh disable for some layouts - drag top layout.
        additionalHypbridAdapter(sw);
        sw.EnableAutoDisableLoadMoreByMaxPages()
                .onEnableLoadmore(R.layout.custom_bottom_progressbar, 2000, request_url_start);
        final HorizontalDividerItemDecoration decor = new HorizontalDividerItemDecoration
                .Builder(getActivity())
                .paint(getlinestyle())
                        //.showLastDivider()
                .build();
        listview_layout.addItemDecoration(decor);
        sw.init(is_list_with_ad_enabled());
    }

    protected patchHyprbid getSwitcherBi() {
        return sw;
    }


    @Override
    protected void setUltimateRecyclerViewExtra(final UltimateRecyclerView listview) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            listview.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.common_background));
        } else {
            listview.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.common_background));
        }
        listview.setClipToPadding(false);
        listview.mRecyclerView.setOnTouchListener(new EBus.Scrolling.SyncTouchEvent());
        listview.addOnScrollListener(new EBus.Scrolling.ScrollEvent());
    }


    protected boolean is_list_with_ad_enabled() {
        return false;
    }


    protected abstract void onLoadMore(final int requestType, final int currentpage, final int posts_per_page) throws ApiException;


    protected Paint getsolid() {
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getActivity(), R.color.divider);
        float fl = getResources().getDimension(R.dimen.divider_stroke_width);
        paint.setColor(color);
        paint.setStrokeWidth(fl);
        return paint;
    }


    protected Paint getlinestyle() {
        return getsolid();
    }


}
