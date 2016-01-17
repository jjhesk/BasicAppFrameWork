package com.hkm.downloadmedialocker.pages.content;

import android.app.Activity;
import android.os.Bundle;

import com.hkm.downloadmedialocker.life.EBus;
import com.hkm.downloadmedialocker.life.TabUntil;
import com.hkm.slider.SliderLayout;
import com.hypebeast.sdk.api.model.hbeditorial.configbank;
import com.hypebeast.sdk.application.hypebeast.ConfigurationSync;
import com.hypebeast.sdk.clients.HBEditorialClient;
import com.squareup.otto.Subscribe;

/**
 * Created by hesk on 15/6/15.
 */
public class HomePagePullDownList extends home_slider {
    public static HomePagePullDownList withAd(boolean bool) {
        HomePagePullDownList t = new HomePagePullDownList();
        Bundle b = new Bundle();
        b.putBoolean(HAS_AD, bool);
        t.setArguments(b);
        return t;
    }

    @Override
    protected void loadSlider(SliderLayout vslid) {
        super.loadSlider(vslid);
        try {
            HBEditorialClient client = HBEditorialClient.getInstance(getActivity().getApplicationContext());
            configbank mConfiguration =
                    ConfigurationSync
                            .getInstance()
                            .getByLanguage(client.getLanguagePref());

            if (TabUntil.detectionDimension(getActivity()) == TabUntil.CELL_5) {
                setup_gallery(vslid, mConfiguration.featurebanner);
            } else {
                setup_double_faces(vslid, mConfiguration.featurebanner);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        EBus.getInstance().register(this);
    }

    /**
     * Called when the Fragment is no longer started.  This is generally
     * tied to {@link Activity#onStop() Activity.onStop} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onStop() {
        super.onStop();
        EBus.getInstance().unregister(this);
    }

    @Subscribe
    public void onEvent(EBus.Scrolling vb) {
        if (mDragLayout != null) {
            boolean first = vb.getEnabled();
            boolean v = vb.getDirectionUpFreeHand();
            if (v && first) {
                triggerPullDown();
            }
            mDragLayout.setTouchMode(first);
        }
    }
}
