package com.hkm.downloadmedialocker.adInterstitual;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.hkm.downloadmedialocker.BuildConfig;
import com.marshalchen.ultimaterecyclerview.ui.AdGoogleDisplaySupport;
import com.r0adkll.deadskunk.utils.Utils;

/**
 * Created by hesk on 12/11/15.
 */
public class BottomBar {
    private RelativeLayout ad_cell;
    private int movement_height, full_height, screen_h;
    private Context context;

    public static BottomBar with(@Nullable RelativeLayout advertisementCell) {

        final BottomBar bb = new BottomBar();
        bb.setViews(advertisementCell);
        bb.init();
        return bb;
    }

    public BottomBar() {
    }

    /**
     * this is the configuration of the Ad
     *
     * @return the AdView
     */
    private AdView prepareAd() {
        String adID = BuildConfig.DFP_BANNER_UNIT_ID;
        AdSize adSize = AdSize.BANNER;
        final AdView mAdView = new AdView(ad_cell.getContext());
        mAdView.setAdSize(adSize);
        mAdView.setAdUnitId(adID);
        return mAdView;
    }

    private void setViews(RelativeLayout adcell) {
        this.ad_cell = adcell;
    }

    private void init() {
        if (ad_cell == null) return;
        DisplayMetrics dm1 = ad_cell.getContext().getResources().getDisplayMetrics();
        screen_h = dm1.heightPixels;
        context = ad_cell.getContext();
        newBannerView();
    }


    private void newBannerView() {
        if (BuildConfig.DFP_BANNER_UNIT_ID == null) return;
        if (BuildConfig.DFP_BANNER_UNIT_ID.equalsIgnoreCase("")) return;

        DisplayMetrics dm = ad_cell.getContext().getResources().getDisplayMetrics();
        double density = dm.density * 160;
        double x = Math.pow(dm.widthPixels / density, 2);
        double y = Math.pow(dm.heightPixels / density, 2);
        double screenInches = Math.sqrt(x + y);
        final double ratio = 1.2d;
        final AdView mAdView = prepareAd();
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                int h = mAdView.getLayoutParams().height;
                AdGoogleDisplaySupport.scale(mAdView, ratio);
                AdGoogleDisplaySupport.panelAdjust(mAdView, (int) (h * ratio));
                ad_cell.setVisibility(View.VISIBLE);
            }
        });
        ad_cell.addView(mAdView);
        ad_cell.setVisibility(View.GONE);

        final AdRequest.Builder mRequestBuilder = new AdRequest.Builder();
        mAdView.loadAd(mRequestBuilder.build());
    }

    protected Animation transUp(Context context, final Runnable end) {
        TranslateAnimation a = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.ABSOLUTE, screen_h,
                Animation.ABSOLUTE, screen_h - Utils.dpToPx(context, 100)
        );
        a.setDuration(500);
        a.setFillAfter(true); //HERE
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(a);
        animationSet.setAnimationListener(new ListenerAnimation() {
            @Override
            public void onAnimationEnd(Animation animation) {
                end.run();
            }
        });
        return animationSet;
    }

    protected Animation transDown(Context context, final Runnable end) {
        TranslateAnimation a = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.ABSOLUTE, screen_h - Utils.dpToPx(context, 100),
                Animation.ABSOLUTE, screen_h
        );
        a.setDuration(500);
        a.setFillAfter(true);
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setAnimationListener(new ListenerAnimation() {
            @Override
            public void onAnimationEnd(Animation animation) {
                end.run();
            }
        });
        return animationSet;
    }

    private static void mayCancelAnimation(View anything) {
        if (anything.getAnimation() != null) {
            anything.getAnimation().cancel();
        }
    }

    private class enhancedAnimation extends ListenerAnimation {
        private View target;


        public enhancedAnimation(View target) {
            this.target = target;
        }


        @Override
        public void onAnimationEnd(Animation animation) {
            target.setVisibility(View.GONE);
        }
    }

    private class ListenerAnimation implements Animation.AnimationListener {
        /**
         * <p>Notifies the end of the animation. This callback is not invoked
         * for animations with repeat count set to INFINITE.</p>
         *
         * @param animation The animation which reached its end.
         */
        @Override
        public void onAnimationEnd(Animation animation) {

        }

        /**
         * <p>Notifies the repetition of the animation.</p>
         *
         * @param animation The animation which was repeated.
         */
        @Override
        public void onAnimationRepeat(Animation animation) {

        }

        /**
         * <p>Notifies the start of the animation.</p>
         *
         * @param animation The started animation.
         */
        @Override
        public void onAnimationStart(Animation animation) {

        }
    }
}
