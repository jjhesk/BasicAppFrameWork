package com.hkm.editorial.pages.adapters;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

import com.hkm.editorial.pages.featureList.gridListBasic;
import com.hkm.layout.Module.easyAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

/**
 * Created by hesk on 13/1/16.
 */
public class LoadMoreGridLayoutManager extends GridLayoutManager {
    private final UltimateViewAdapter mAdapter;

    protected class VIEW_TYPES {
        public static final int NORMAL = 0;
        public static final int HEADER = 1;
        public static final int FOOTER = 2;
        public static final int CHANGED_FOOTER = 3;
    }

    private GridLayoutManager.SpanSizeLookup m = new GridLayoutManager.SpanSizeLookup() {
        @Override
        public int getSpanSize(int position) {
            if (mAdapter instanceof easyAdapter) {
                easyAdapter sw = (easyAdapter) mAdapter;
                if (sw.getItemViewType(position) == VIEW_TYPES.FOOTER) {
                    return getSpanCount();
                } else if (sw.getItemViewType(position) == VIEW_TYPES.HEADER) {
                    return getSpanCount();
                } else if (sw.getItemViewType(position) == VIEW_TYPES.NORMAL) {
                    return 1;
                }
            }

            if (mAdapter instanceof gridListBasic.admobadp) {
                gridListBasic.admobadp sw = (gridListBasic.admobadp) mAdapter;
                if (sw.getItemViewType(position) == VIEW_TYPES.FOOTER) {
                    return getSpanCount();
                } else if (sw.getItemViewType(position) == VIEW_TYPES.HEADER) {
                    return getSpanCount();
                } else if (sw.getItemViewType(position) == VIEW_TYPES.NORMAL) {
                    return 1;
                }
            }

            return 1;
        }
    };

    public LoadMoreGridLayoutManager(Context context, int spanCount, easyAdapter mAdapter) {
        super(context, spanCount);
        this.mAdapter = mAdapter;
        setSpanSizeLookup(m);
    }

    public LoadMoreGridLayoutManager(Context context, int spanCount, gridListBasic.admobadp mAdapter) {
        super(context, spanCount);
        this.mAdapter = mAdapter;
        setSpanSizeLookup(m);
    }


    public LoadMoreGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout, easyAdapter mAdapter) {
        super(context, spanCount, orientation, reverseLayout);
        this.mAdapter = mAdapter;
        setSpanSizeLookup(m);
    }
}
