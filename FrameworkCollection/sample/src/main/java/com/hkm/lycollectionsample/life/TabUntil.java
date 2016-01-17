package com.hkm.lycollectionsample.life;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Created by hesk on 25/11/15.
 */
public class TabUntil {

    public static final int
            CELL_5 = 0,
            TABLET_5_INCHES = 2,
            TABLET_7_INCHES = 3,
            TABLET_10_INCHES = 3;

    public static int detectionDimension(Activity mactivity) {
        DisplayMetrics metrics = new DisplayMetrics();
        mactivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;
        float scaleFactor = metrics.density;
        float widthDp = widthPixels / scaleFactor;
        float heightDp = heightPixels / scaleFactor;

        float smallestWidth = Math.min(widthDp, heightDp);

        if (smallestWidth > 720) {
            //Device is a 10" tablet
            return TABLET_7_INCHES;
        } else if (smallestWidth > 600) {
            //Device is a 7" tablet
            return TABLET_7_INCHES;
        } else {
            return CELL_5;
        }

    }
}
