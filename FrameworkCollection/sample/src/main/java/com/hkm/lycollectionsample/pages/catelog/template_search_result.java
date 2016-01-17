package com.hkm.lycollectionsample.pages.catelog;


import android.os.Bundle;

import com.hkm.lycollectionsample.R;


/**
 * Created by hesk on 19/10/15.
 */
public class template_search_result extends template_general_list {
    public static template_search_result B(final Bundle b) {
        final template_search_result t = new template_search_result();
        t.setArguments(b);
        return t;
    }

    @Override
    protected int setEmptyListViewId() {
        return R.layout.emptyview;
    }
}
