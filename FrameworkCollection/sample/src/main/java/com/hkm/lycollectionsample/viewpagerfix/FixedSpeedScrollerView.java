package com.hkm.lycollectionsample.viewpagerfix;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Created by hesk on 9/6/15.
 */
public class FixedSpeedScrollerView extends Scroller {

    private int mDuration = 5000;

    public FixedSpeedScrollerView(Context context) {
        super(context);
    }

    public FixedSpeedScrollerView(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public FixedSpeedScrollerView(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    public void setFixedDuration(int dur) {
        mDuration = dur;
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        // Ignore received duration, use fixed one instead
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        // Ignore received duration, use fixed one instead
        super.startScroll(startX, startY, dx, dy, mDuration);
    }
}