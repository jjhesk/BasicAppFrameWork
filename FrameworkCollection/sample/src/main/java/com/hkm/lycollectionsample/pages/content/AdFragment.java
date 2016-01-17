package com.hkm.lycollectionsample.pages.content;

import android.app.Fragment;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.doubleclick.AppEventListener;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.hkm.lycollectionsample.R;


/**
 * Created by hesk on 6/5/15.
 */
public class AdFragment extends Fragment implements AppEventListener {
    private PublisherAdView mAdView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        measureDisplay();
        return inflater.inflate(R.layout.fragment_ad, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle s) {
        super.onViewCreated(view, s);
        //  resizeFragment(this, screensize.x, screensize.y);
      //  RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(screensize.x, screensize.y);
      //  view.setLayoutParams(p);
        //  view.requestLayout();
    }

    final Point screensize = new Point();
    private AdSize as;

    protected void measureDisplay() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        int ratioHeight = (int) (width * 50 / 500);
        screensize.set(width, ratioHeight);
        as = new AdSize(width, ratioHeight);

    }

    private void resizeFragment(Fragment f, int newWidth, int newHeight) {
        if (f != null) {
            View view = f.getView();
            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(newWidth, newHeight);
            view.setLayoutParams(p);
            view.requestLayout();
        }
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        mAdView = (PublisherAdView) getView().findViewById(R.id.adView);
        final PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
        mAdView.setAdSizes(as, AdSize.SMART_BANNER, AdSize.BANNER);
        mAdView.loadAd(adRequest);
        mAdView.setAppEventListener(this);
    }

    /**
     * Called when leaving the activity
     */
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    /**
     * Called when returning to the activity
     */
    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    /**
     * Called before the activity is destroyed
     */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onAppEvent(String s, String s1) {
        Log.d("appevent", s + s1);
    }
}