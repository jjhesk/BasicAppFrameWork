package com.hkm.dllocker.content;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hkm.advancedtoolbar.Util.ErrorMessage;
import com.hkm.dllocker.R;
import com.hkm.dllocker.basic.ListBaseLinear;
import com.hkm.dllocker.module.DLUtil;
import com.hkm.dllocker.module.EBus;
import com.hkm.dllocker.module.realm.RecordContainer;
import com.hkm.dllocker.module.realm.UriCap;
import com.hkm.layout.Module.easyAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.neopixl.pixlui.components.textview.TextView;

import java.util.List;

/**
 * Created by hesk on 18/1/16.
 */
public class RecentActivities extends ListBaseLinear {
    private final static String IMAGE_RES = "imageresid";
    private final static String SEARCH_QUERY = "query";
    private boolean hasrecentactivities;
    private RecordContainer rm_container;
    private datadaptr adp;

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

    public void notifylist() {
        adp = new datadaptr(rm_container.getAllRecords());
        listview_layout.setAdapter(adp);
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

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rm_container = RecordContainer.getInstnce(getActivity());
        return inflater.inflate(getLayoutListId(), container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        try {
            initBinding(view);
        } catch (Exception e) {
            ErrorMessage.alert(e.getMessage(), getChildFragmentManager());
        }
        setEmptyViewContent(view);
    }


    protected void setEmptyViewContent(View view) {
        if (getArguments() != null) {
            int resId = getArguments().getInt(IMAGE_RES);
            ImageView gi = (ImageView) view.findViewById(R.id.searchimage);
            if (gi == null) return;
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

    @Override
    protected void setUltimateRecyclerViewExtra(UltimateRecyclerView listview) {
        adp = new datadaptr(rm_container.getAllRecords());
        listview.setAdapter(adp);
        enableLoading(false);
    }

    private class datadaptr extends easyAdapter<UriCap, binder> {


        /**
         * dynamic object to start
         *
         * @param list the list source
         */
        public datadaptr(List<UriCap> list) {
            super(list);
        }

        @Override
        protected binder newViewHolder(View view) {
            return new binder(view);
        }

        @Override
        protected int getNormalLayoutResId() {
            return normalLayoutResId();
        }

        @Override
        protected void withBindHolder(binder holder, UriCap data, int position) {
            binddata(holder, data, position);
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

        }
    }

    @LayoutRes
    private int normalLayoutResId() {
        return R.layout.item_share_linear;
    }

    private void binddata(binder b, final UriCap u, final int t) {
        if (u.getMedia_type() == UriCap.SOUNDCLOUD) {
            b.big_image_single.setImageResource(R.drawable.ic_snd_cloud);
        }

        if (u.getMedia_type() == UriCap.FACEBOOK_VIDEO) {
            b.big_image_single.setImageResource(R.drawable.ic_fb);
        }

        b.tvtitle.setText(u.getMedia_title());
        b.tvtime.setText(DLUtil.getMoment(u.getDate()));
        //  if (b.touch_box == null) return;
      /*  b.touch_box.setLongClickable(true);
          b.touch_box.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });*/

        b.touch_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EBus.callMenu(u);
            }
        });
    }

    public static class binder extends UltimateRecyclerviewViewHolder {
        public final ImageView big_image_single;
        public final TextView tvtitle, tvtime;
        public final RelativeLayout touch_box;

        public binder(View itemView) {
            super(itemView);
            big_image_single = (ImageView) itemView.findViewById(R.id.ls_ft_icon);
            tvtitle = (TextView) itemView.findViewById(R.id.ls_title_top);
            tvtime = (TextView) itemView.findViewById(R.id.ls_title_article);
            touch_box = (RelativeLayout) itemView.findViewById(R.id.ls_row_button);
        }
    }


}
