package com.hkm.layout.List;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;

import com.hkm.layout.Module.LoadMoreGridLayoutManager;
import com.hkm.layout.Module.Utils;
import com.hkm.layout.Module.easyAdapter;
import com.hkm.layout.R;
import com.hkm.layout.fragment.basicfeed;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;


/**
 * Created by hesk on 13/1/16.
 */
public abstract class gridListBasic<BINDER, DATA> extends v2 implements UltimateRecyclerView.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    protected LoadMoreGridLayoutManager mLayoutManager;

    protected Paint getsolid() {
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getActivity(), R.color.divider_v2);
        float fl = getResources().getDimension(R.dimen.divider_stroke_v2);
        paint.setColor(color);
        paint.setStrokeWidth(fl);
        return paint;
    }

    @Override
    protected void afterRenderViewLayout(final UltimateRecyclerView listview){
        listview.setOnLoadMoreListener(this);
        listview.setDefaultOnRefreshListener(this);
        listview.enableDefaultSwipeRefresh(false);
        setupColumns();
        if (Utils.isTablet(getActivity())) {
            int final_display_columns = Utils.isLandscape(getActivity()) ? landscape_common_colums : portrait_common_colums;
            mLayoutManager = new LoadMoreGridLayoutManager(getActivity(), final_display_columns, getAdapter());
        } else
            mLayoutManager = new LoadMoreGridLayoutManager(getActivity(), 1, getAdapter());

        listview.setLayoutManager(mLayoutManager);

        if (setEmptyListViewId() > 0)
            listview.setEmptyView(setEmptyListViewId());

        if (getActivity() != null) {
            final HorizontalDividerItemDecoration decor = new HorizontalDividerItemDecoration
                    .Builder(getActivity())
                    .paint(getsolid())
                            //.showLastDivider()
                    .build();

            listview.addItemDecoration(decor);
        }
    }


    protected patchHyprbid sw;


    protected abstract void binddata(final BINDER holder, final DATA data, int position);


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


    private int currentPage, totalPages, pagePerItems, landscape_common_colums = 4, portrait_common_colums = 2;

    private String tag_keyword, fullEndPoint, searchKeyword;
    private boolean enable_load_more, is_new_search;

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    public String getTag_keyword() {
        return tag_keyword;
    }

    public void setTag_keyword(String tag_keyword) {
        this.tag_keyword = tag_keyword;
    }

    public String getFullEndPoint() {
        return fullEndPoint;
    }

    public void setFullEndPoint(String fullEndPoint) {
        this.fullEndPoint = fullEndPoint;
    }

    public boolean is_new_search() {
        return is_new_search;
    }

    public void setIs_new_search(boolean is_new_search) {
        this.is_new_search = is_new_search;
    }

    protected int getCurrentPage() {
        return currentPage;
    }

    protected int getTotalPages() {
        return totalPages;
    }

    protected int getPagePerItems() {
        return pagePerItems;
    }

    protected void setCurrentPage(int n) {
        currentPage = n;
    }

    protected void nextPage() {
        if (currentPage < totalPages) {
            enable_load_more = true;
            currentPage++;
        }
    }

    protected void setTotalPages(int n) {
        totalPages = n;
        if (currentPage >= totalPages) {
            enable_load_more = false;
        } else {
            enable_load_more = true;
        }
    }

    protected boolean getEnabledLoadMore() {
        return enable_load_more;
    }

    public void triggerNewSearchKeyWord(String word) {
        setIs_new_search(true);
        setSearchKeyword(word);
    }

    /**
     * step 2:
     * this is the call for the loading the data stream externally
     */
    @Override
    protected void loadDataInitial() {
        currentPage = 1;
        totalPages = 1;
        pagePerItems = 12;
        enable_load_more = false;
        is_new_search = false;
    }

    @LayoutRes
    protected abstract int setEmptyListViewId();

    protected void setupTabletImplementation(int landscape, int portrait) {
        if (landscape > 0) {
            landscape_common_colums = landscape;
        }
        if (portrait > 0) {
            portrait_common_colums = portrait;
        }
    }

    /**
     * setup the custom columns in here
     */
    protected void setupColumns() {
    }

    /**
     * init the adapter in here
     *
     * @return the adapter for use
     */
    protected abstract easyAdapter getAdapter();
}
