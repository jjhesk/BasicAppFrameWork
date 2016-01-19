package com.hkm.dllocker.content;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.hkm.advancedtoolbar.Util.ErrorMessage;
import com.hkm.dllocker.R;
import com.hkm.dllocker.ads.ListAd;
import com.hkm.dllocker.basic.ListBaseLinear;
import com.hkm.dllocker.module.DLUtil;
import com.hkm.dllocker.module.EBus;
import com.hkm.dllocker.module.realm.RecordContainer;
import com.hkm.dllocker.module.realm.UriCap;
import com.marshalchen.ultimaterecyclerview.AdmobAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.quickAdapter.simpleAdmobAdapter;
import com.neopixl.pixlui.components.textview.TextView;

import java.util.List;

import bolts.Task;

/**
 * Created by hesk on 18/1/16.
 */
public class RecentActivities extends ListBaseLinear {
    private final static String IMAGE_RES = "imageresid";
    private final static String SEARCH_QUERY = "query";
    private boolean hasrecentactivities;
    private RecordContainer rm_container;
    private admobadp adp;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static RecentActivities newInstance(@DrawableRes int empty_item_image) {
        RecentActivities nil = new RecentActivities();
        Bundle b = new Bundle();
        b.putInt(IMAGE_RES, empty_item_image);
        nil.setArguments(b);
        nil.setAllowEnterTransitionOverlap(true);
        nil.setAllowReturnTransitionOverlap(true);
        return nil;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static RecentActivities newInstance(@DrawableRes int empty_item_image, String search_q) {
        RecentActivities nil = new RecentActivities();
        Bundle b = new Bundle();
        b.putInt(IMAGE_RES, empty_item_image);
        b.putString(SEARCH_QUERY, search_q);
        nil.setArguments(b);
        nil.setAllowEnterTransitionOverlap(true);
        nil.setAllowReturnTransitionOverlap(true);
        return nil;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rm_container = RecordContainer.getInstnce(getActivity());
        return inflater.inflate(
                rm_container.getItemsCount() > 0 ?
                        R.layout.jazz_list : R.layout.empty_notfound,
                container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (rm_container.getItemsCount() > 0) {
            try {
                initBinding(view);
            } catch (Exception e) {
                ErrorMessage.alert(e.getMessage(), getChildFragmentManager());
            }
        } else {
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

    @Override
    protected void setUltimateRecyclerViewExtra(UltimateRecyclerView listview) {
        adp = new admobadp(
                ListAd.newAdView(getActivity()),
                false, 10,
                rm_container.getAllRecords(),
                new AdmobAdapter.AdviewListener() {
                    @Override
                    public ViewGroup onGenerateAdview() {
                        return ListAd.newAdView(getActivity());
                    }
                });
        listview.setAdapter(adp);
    }


    public class admobadp extends simpleAdmobAdapter<UriCap, binder, RelativeLayout> {

        public admobadp(RelativeLayout adview, boolean insertOnce, int setInterval, List<UriCap> L, AdviewListener listener) {
            super(adview, insertOnce, setInterval, L, listener);
        }

        @Override
        protected void withBindHolder(binder var1, UriCap var2, int var3) {
            binddata(var1, var2, var3);
        }


        @Override
        protected int getNormalLayoutResId() {
            return normalLayoutResId();
        }

        /**
         * create a new view holder for data binding
         *
         * @param mview the view layout with resource initialized
         * @return the view type
         */
        @Override
        protected binder newViewHolder(View mview) {
            return new binder(mview);
        }

    }

    @LayoutRes
    private int normalLayoutResId() {
        return R.layout.item_share_linear;
    }

    private void binddata(binder b, UriCap u, int t) {
        if (u.getMedia_type() == UriCap.SOUNDCLOUD) {
            b.big_image_single.setImageResource(R.drawable.ic_snd_cloud);
        }

        if (u.getMedia_type() == UriCap.FACEBOOK_VIDEO) {
            b.big_image_single.setImageResource(R.drawable.ic_fb);
        }

        b.tvtitle.setText(u.getMedia_title());
        b.tvtime.setText(DLUtil.getMoment(u.getDate()));

        b.click_detection.setLongClickable(true);
        b.click_detection.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                return false;
            }
        });
    }

    public static class binder extends UltimateRecyclerviewViewHolder {
        public final ImageView big_image_single;
        public final TextView tvtitle, tvtime;
        public final RelativeLayout click_detection;

        public binder(View itemView) {
            super(itemView);
            big_image_single = (ImageView) itemView.findViewById(R.id.ls_ft_icon);
            tvtitle = (TextView) itemView.findViewById(R.id.ls_title_top);
            tvtime = (TextView) itemView.findViewById(R.id.ls_title_article);
            //  comment_counts = (TextView) itemView.findViewById(R.id.comment);
            click_detection = (RelativeLayout) itemView.findViewById(R.id.click_detection);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        EBus.getInstance().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EBus.getInstance().unregister(this);
    }

}
