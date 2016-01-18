package com.hkm.dllocker.content;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hkm.dllocker.R;
import com.neopixl.pixlui.components.textview.TextView;

/**
 * Created by hesk on 18/1/16.
 */
public class RecentActivities extends Fragment {
    private final static String IMAGE_RES = "imageresid";
    private final static String SEARCH_QUERY = "query";

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static RecentActivities newInstance(@DrawableRes int image) {
        RecentActivities nil = new RecentActivities();
        Bundle b = new Bundle();
        b.putInt(IMAGE_RES, image);
        nil.setArguments(b);
        nil.setAllowEnterTransitionOverlap(true);
        nil.setAllowReturnTransitionOverlap(true);
        return nil;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static RecentActivities newInstance(@DrawableRes int image, String search_q) {
        RecentActivities nil = new RecentActivities();
        Bundle b = new Bundle();
        b.putInt(IMAGE_RES, image);
        b.putString(SEARCH_QUERY, search_q);
        nil.setArguments(b);
        nil.setAllowEnterTransitionOverlap(true);
        nil.setAllowReturnTransitionOverlap(true);
        return nil;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.empty_notfound, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            int resId = getArguments().getInt(IMAGE_RES);
            ImageView gi = (ImageView) view.findViewById(R.id.searchimage);
            gi.setImageResource(resId);
            TextView tv = (TextView) view.findViewById(R.id.text);
            if (!getArguments().getString(SEARCH_QUERY, "").isEmpty()) {
                StringBuilder sb = new StringBuilder();
                sb.append(getArguments().getString(SEARCH_QUERY));
                sb.append(" is not found.");
                tv.setText(sb.toString());
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("There is no recent activities.");
                tv.setText(sb.toString());
            }
        }
    }
}
