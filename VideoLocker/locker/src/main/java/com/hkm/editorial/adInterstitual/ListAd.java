package com.hkm.editorial.adInterstitual;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.hkm.editorial.BuildConfig;
import com.marshalchen.ultimaterecyclerview.ui.AdGoogleDisplaySupport;

/**
 * Created by hesk on 21/12/15.
 */
public class ListAd {

    /**
     * in order to enable facebook adapter network ads
     * there are two things needs to be done
     * 1) adding library for compile 'com.facebook.android:audience-network-sdk:4.8.2'
     * 2) adding library from  https://developers.google.com/mobile-ads-sdk/docs/dfp/android/mediation-networks
     */
    public static final int LIST_AD_INTERVAL = 24;
    public static final double ADJUSTMENT_RATIO = 1.2d;

    public static synchronized RelativeLayout newAdView(Activity activity) {
        AdSize adSize = AdSize.SMART_BANNER;

        DisplayMetrics dm = activity.getResources().getDisplayMetrics();

        double density = dm.density * 160;
        double x = Math.pow(dm.widthPixels / density, 2);
        double y = Math.pow(dm.heightPixels / density, 2);
        double screenInches = Math.sqrt(x + y);

        adSize = AdSize.MEDIUM_RECTANGLE;
        final AdView mAdView = new AdView(activity);

        mAdView.setAdUnitId(BuildConfig.DFP_LIST_UNIT_ID);
        mAdView.setAdSize(adSize);
        // Create an ad request.
        AdRequest.Builder mRequestBuilder = new AdRequest.Builder();
        // Start loading the ad.
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        final RelativeLayout layout = AdGoogleDisplaySupport.initialSupport(activity, mDisplayMetrics);
        final double ratio = AdGoogleDisplaySupport.ratioMatching(mDisplayMetrics);
        final int ad_height = AdGoogleDisplaySupport.defaultHeight(mDisplayMetrics);
        AdGoogleDisplaySupport.panelAdjust(mAdView, (int) (ad_height * ratio));

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                int h = mAdView.getLayoutParams().height;
                AdGoogleDisplaySupport.scale(mAdView, ADJUSTMENT_RATIO);
                AdGoogleDisplaySupport.panelAdjust(mAdView, (int) (h * ADJUSTMENT_RATIO));
                //  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            }
        });
        layout.addView(mAdView);
        mAdView.loadAd(mRequestBuilder.build());
        return layout;
    }


}
