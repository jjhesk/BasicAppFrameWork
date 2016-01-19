package com.hkm.dllocker.basic;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

import com.hkm.layout.Module.easyAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

/**
 * Created by hesk on 13/1/16.
 */
public class LoadMoreGridLayoutManager extends GridLayoutManager {
    private final easyAdapter mAdapter;

    protected class VIEW_TYPES {
        public static final int NORMAL = 0;
        public static final int HEADER = 1;
        public static final int FOOTER = 2;
        public static final int CHANGED_FOOTER = 3;
    }

    private GridLayoutManager.SpanSizeLookup m = new GridLayoutManager.SpanSizeLookup() {
        @Override
        public int getSpanSize(int position) {
            return calSpanSize(mAdapter, position);
        }
    };

    protected int calSpanSize(easyAdapter baseAdapter, final int basePosition) {
        /*if (mAdapter instanceof easyAdapter) {
            easyAdapter sw = (easyAdapter) mAdapter;
            if (sw.getItemViewType(basePosition) == VIEW_TYPES.FOOTER) {
                return getSpanCount();
            } else if (sw.getItemViewType(basePosition) == VIEW_TYPES.HEADER) {
                return getSpanCount();
            } else if (sw.getItemViewType(basePosition) == VIEW_TYPES.NORMAL) {
                return 1;
            }
        }
*/


        if (baseAdapter.getItemViewType(basePosition) == VIEW_TYPES.FOOTER) {
            return getSpanCount();
        } else if (baseAdapter.getItemViewType(basePosition) == VIEW_TYPES.HEADER) {
            return getSpanCount();
        } else if (baseAdapter.getItemViewType(basePosition) == VIEW_TYPES.NORMAL) {
            return 1;
        }

        return 1;
    }

    public LoadMoreGridLayoutManager(Context context, int spanCount, final easyAdapter mAdapter) {
        super(context, spanCount);
        this.mAdapter = mAdapter;
        setSpanSizeLookup(m);
    }


    public LoadMoreGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout,
                                     final easyAdapter mAdapter) {
        super(context, spanCount, orientation, reverseLayout);
        this.mAdapter = mAdapter;
        setSpanSizeLookup(m);
    }
}
