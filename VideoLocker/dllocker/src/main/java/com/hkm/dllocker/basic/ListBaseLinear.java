package com.hkm.dllocker.basic;

import android.app.Fragment;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ProgressBar;

import com.hkm.dllocker.R;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.uiUtils.ScrollSmoothLineaerLayoutManager;

/**
 * Created by hesk on 19/1/16.
 */
public abstract class ListBaseLinear extends Fragment {
    protected Handler hlder = new Handler();
    private ProgressBar loadingbar;
    protected UltimateRecyclerView listview_layout;
    protected ScrollSmoothLineaerLayoutManager mLayoutManager;

    protected int getSmoothDuration() {
        return 300;
    }

    private void renderviewlayout(View view) throws Exception {
        listview_layout = (UltimateRecyclerView) view.findViewById(R.id.lylib_list_uv);
        loadingbar = (ProgressBar) view.findViewById(R.id.lylib_ui_loading_circle);
        listview_layout.setLayoutManager(constructLayoutManager());
        listview_layout.setHasFixedSize(false);
        listview_layout.setSaveEnabled(false);
        if (setEmptyListViewId() != 0) {
            listview_layout.setEmptyView(setEmptyListViewId());
        }
        setUltimateRecyclerViewExtra(listview_layout);
    }

    @LayoutRes
    protected int setEmptyListViewId() {
        return R.layout.empty_notfound;
    }

    @LayoutRes
    protected int getLayoutListId() {
        return R.layout.jazz_list;
    }

    protected abstract void setUltimateRecyclerViewExtra(final UltimateRecyclerView listview);

    protected LinearLayoutManager constructLayoutManager() {
        mLayoutManager = new ScrollSmoothLineaerLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false, getSmoothDuration());
        return mLayoutManager;
    }

    protected void initBinding(View view) throws Exception {
        renderviewlayout(view);
    }

    protected void enableLoading(boolean b) {
        if (b) {
            loadingbar.animate().alpha(1f);
        } else {
            loadingbar.animate().alpha(0f);
        }
    }
}
