package com.hkm.layout.Module;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.hkm.layout.R;

/**
 * Created by hesk on 21/8/15.
 */
public class Utils {

    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return toolbarHeight;
    }

    public static int getTabsHeight(Context context) {
        return (int) context.getResources().getDimension(R.dimen.tab_height_host);
    }

    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    public static void setMarginTop(View v, int top_margin) {
        setMargins(v, 0, top_margin, 0, 0);
    }

    public static boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    public static boolean isLandscape(Context context) {
        int ot = context.getResources().getConfiguration().orientation;
        switch (ot) {
            case Configuration.ORIENTATION_LANDSCAPE:
                Log.d("my orient", "ORIENTATION_LANDSCAPE");
                return true;
            case Configuration.ORIENTATION_PORTRAIT:
                Log.d("my orient", "ORIENTATION_PORTRAIT");
                return false;
            case Configuration.ORIENTATION_SQUARE:
                Log.d("my orient", "ORIENTATION_SQUARE");
                return false;
            case Configuration.ORIENTATION_UNDEFINED:
                Log.d("my orient", "ORIENTATION_UNDEFINED");
                return false;
            default:
                Log.d("my orient", "default val");
                return false;
        }
    }

}
