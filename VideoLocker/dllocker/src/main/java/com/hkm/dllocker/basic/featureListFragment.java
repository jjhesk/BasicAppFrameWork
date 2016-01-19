package com.hkm.dllocker.basic;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hkm.editorial.R;
import com.hkm.editorial.life.HBUtil;
import com.hypebeast.sdk.api.exception.ApiException;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.quickAdapter.easyRegularAdapter;
import com.marshalchen.ultimaterecyclerview.quickAdapter.simpleAdmobAdapter;
import com.neopixl.pixlui.components.textview.TextView;

import java.util.List;

/**
 * Created by hesk on 3/7/15.
 */
public abstract class featureListFragment<T> extends skeleton {

    protected String adapter_url, cate_title, slugtag;
    protected int requestType;

    @Override
    protected boolean onArguments(Bundle r) {
        requestType = r.getInt(REQUEST_TYPE, UNSET);
        adapter_url = r.getString(URL, "");
        slugtag = r.getString(SLUG, "");
        cate_title = r.getString(FRAGMENTTITLE, "");
        final int resid = r.getInt(FRAGMENTTITLE_RESID, -1);
        if (resid != -1) {
            cate_title = getString(resid);
        }
        if (requestType == SEARCH) {
            slugtag = r.getString(SEARCH_WORD, "");
        }
        return !adapter_url.equalsIgnoreCase("") || requestType != UNSET;
    }

    @Override
    protected void onClickItem(final long post_id) {
        Log.d(TAG, post_id + " now");
        //   PBUtil.routeSinglePage(post_id, getActivity());
        //   HBUtil.startNewArticle(post_id, getActivity());
    }

    @Override
    protected void onClickItem(final String route) {
        Log.d(TAG, route + " now");
        HBUtil.startNewArticle(route, getActivity());
    }


    protected abstract void onLoadMore(final int requestType, final int currentpage, final int pagelimit) throws ApiException;


    protected Paint getsolid() {
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getActivity(), R.color.divider);
        float fl = getResources().getDimension(R.dimen.divider_stroke_width);
        paint.setColor(color);
        paint.setStrokeWidth(fl);
        return paint;
    }


    protected Paint getlinestyle() {
        return getsolid();
    }


    public static class binder extends UltimateRecyclerviewViewHolder {
        public final ImageView big_image_single;
        public final TextView tvtitle, tvtime, comment_counts;
        public final LinearLayout click_detection;

        public binder(View itemView) {
            super(itemView);
          /*   big_image_single = (ImageView) itemView.findViewById(R.id.big_image_single);
            tvtitle = (TextView) itemView.findViewById(R.id.articletopic);
            tvtime = (TextView) itemView.findViewById(R.id.time_line);
            cate_name = (TextView) itemView.findViewById(R.id.cate_name);
            click_detection = (LinearLayout) itemView.findViewById(R.id.click_detection);*/


            big_image_single = (ImageView) itemView.findViewById(R.id.big_image_single);
            tvtitle = (TextView) itemView.findViewById(R.id.articletopic);
            tvtime = (TextView) itemView.findViewById(R.id.nd_line);
            comment_counts = (TextView) itemView.findViewById(R.id.comment);
            // cate_name = (TextView) itemView.findViewById(R.id.cate_name);
            click_detection = (LinearLayout) itemView.findViewById(R.id.click_detection);
        }
    }

    public class admobadp extends simpleAdmobAdapter<T, binder, RelativeLayout> {

        public admobadp(RelativeLayout adview, boolean insertOnce, int setInterval, List<T> L, AdviewListener listener) {
            super(adview, insertOnce, setInterval, L, listener);
        }

        @Override
        protected void withBindHolder(featureListFragment.binder var1, T var2, int var3) {
            binddata(var1, var2, var3);
        }


        @Override
        protected int getNormalLayoutResId() {
            return normalLayoutResId();
        }

        @Override
        protected featureListFragment.binder newViewHolder(View view) {
            return new featureListFragment.binder(view);
        }
    }

    public class cateadapter extends easyRegularAdapter<T, binder> {

        /**
         * dynamic object to start
         *
         * @param list the list source
         */
        public cateadapter(List<T> list) {
            super(list);
        }

        /**
         * the layout id for the normal data
         *
         * @return the ID
         */
        @Override
        protected int getNormalLayoutResId() {
            return normalLayoutResId();
        }

        @Override
        protected featureListFragment.binder newViewHolder(View view) {
            return new featureListFragment.binder(view);
        }

        @Override
        protected void withBindHolder(final featureListFragment.binder holder, final T data, int position) {
            binddata(holder, data, position);
        }

    }

    protected abstract void binddata(final featureListFragment.binder holder, final T data, int position);

    protected int normalLayoutResId() {
        return R.layout.item_general;
    }
}
