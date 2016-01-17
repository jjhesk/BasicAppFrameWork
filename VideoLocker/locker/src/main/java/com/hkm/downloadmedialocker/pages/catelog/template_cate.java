package com.hkm.downloadmedialocker.pages.catelog;

import android.app.Fragment;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.hkm.downloadmedialocker.R;
import com.hkm.downloadmedialocker.life.EBus;
import com.hkm.layout.Module.easyAdapter;
import com.hypebeast.sdk.api.model.hbeditorial.Menuitem;
import com.hypebeast.sdk.api.model.hbeditorial.configbank;
import com.hypebeast.sdk.clients.HBEditorialClient;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;
import com.neopixl.pixlui.components.textview.TextView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class template_cate extends Fragment {
    public UltimateRecyclerView listview_layout;
    private LinearLayoutManager mLayoutManager;
    protected cateadapter mAdapter;
    protected Picasso mPicasso;
    protected String m_string_item;
    protected ProgressBar mProgress;
    public static String TAG = "template_cate";
    public static String universal = "url_universe";
    private configbank overhead_data;

    protected void onClickItem(String item_name) {
        m_string_item = item_name;
    }

    @LayoutRes
    protected int getFragmentResId() {
        return R.layout.jazz_cate;
    }

    @IdRes
    protected int getUltimate_recycler_viewResId() {
        return R.id.lylib_list_uv;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getFragmentResId(), container, false);
    }

    protected List<Menuitem> getSourceList() throws Exception {
        // return ConfigurationSync.getInstance().getFoundation().data.categories;

        List<Menuitem> item = new ArrayList<>();
        Iterator<Menuitem> m = overhead_data.cateitems.iterator();
        while (m.hasNext()) {
            Menuitem menuit = m.next();
            if (menuit.getDisplay() != null && menuit.getHref() != null) {
                item.add(menuit);
            }
        }
        return item;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            HBEditorialClient client = HBEditorialClient.getInstance(getActivity().getApplicationContext());
            overhead_data = com.hypebeast.sdk.application.hypebeast.ConfigurationSync.getInstance().getByLanguage(client.getLanguagePref());
            getProgressbar(view);
            renderviewlayout(view);
        } catch (Exception e) {
            //  Intent d = new Intent(MainHome.this, HBSplash.class);
            //  getActivity().finish();
            //  startActivity(d);
        }
    }


    protected void setUltimateRecyclerViewExtra(UltimateRecyclerView list, UltimateViewAdapter adapter) {


    }


    protected void renderviewlayout(View view) throws Exception {
        listview_layout = (UltimateRecyclerView) view.findViewById(getUltimate_recycler_viewResId());
        listview_layout.setHasFixedSize(true);
        listview_layout.setSaveEnabled(true);
        mPicasso = Picasso.with(getActivity());
        mAdapter = new cateadapter(getSourceList());
        listview_layout.setAdapter(mAdapter);
        setUltimateRecyclerViewExtra(listview_layout, mAdapter);
        if (mLayoutManager == null && getActivity() != null) {
            mLayoutManager = new LinearLayoutManager(getActivity());
            // listview_layout.setLayoutManager(mLayoutManager);
        }
        listview_layout.setLayoutManager(mLayoutManager);
        final HorizontalDividerItemDecoration decor = new HorizontalDividerItemDecoration
                .Builder(getActivity())
                .paint(get_solid())
                .showLastDivider()
                .build();
        listview_layout.addItemDecoration(decor);
        hideLoadingCircle();
    }

    protected Paint get_solid() {
        Paint paint = new Paint();
        int color_i = ContextCompat.getColor(getActivity(), R.color.divider);
        float fl = getResources().getDimension(R.dimen.divider_stroke_width);
        paint.setColor(color_i);
        paint.setStrokeWidth(fl);
        return paint;
    }

    public static class page_navigation {
        public String url, image, title;

        public page_navigation(String url, String image, String title) {
            this.url = url;
            this.image = image;
            this.title = title;
        }

        public Bundle getBundle() {
            return template_cate.con_full_path(url);
        }
    }

    public static Bundle con_full_path(String url) {
        final Bundle n = new Bundle();
        n.putString(universal, url);
        return n;
    }

    public static template_cate.page_navigation b(String url, String image, String title) {
        final template_cate.page_navigation page = new template_cate.page_navigation(url, image, title);
        return page;
    }


    public static class binder extends UltimateRecyclerviewViewHolder {
        public final ImageView im;
        public final TextView tvtitle;
        public final RelativeLayout cate_contain_hb;

        public binder(View itemView) {
            super(itemView);
            cate_contain_hb = (RelativeLayout) itemView.findViewById(R.id.cate_contain_hb);
            im = (ImageView) itemView.findViewById(R.id.background_image);
            tvtitle = (TextView) itemView.findViewById(R.id.text_title_field);
        }
    }

    public class cateadapter extends easyAdapter<Menuitem, binder> {

        /**
         * dynamic object to start
         *
         * @param list the list source
         */
        public cateadapter(List<Menuitem> list) {
            super(list);
        }

        /**
         * the layout id for the normal data
         *
         * @return the ID
         */
        @Override
        protected int getNormalLayoutResId() {
            return R.layout.item_cate;
        }

        @Override
        protected template_cate.binder newViewHolder(View view) {
            return new template_cate.binder(view);
        }

        @Override
        protected void withBindHolder(final binder holder, final Menuitem data, int position) {
            mPicasso.load(R.drawable.above_shadow)
                    .memoryPolicy(MemoryPolicy.NO_STORE, MemoryPolicy.NO_CACHE)
                    .into(holder.im);
            holder.cate_contain_hb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PageList pagelist = new PageList(template_general_list.general(data.getHref()));
                    pagelist.setTitle(data.getName());
                    EBus.getInstance().post(pagelist);
                }
            });
            holder.tvtitle.setText(data.getDisplay());
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

        }

        @Override
        public UltimateRecyclerviewViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
            return new UltimateRecyclerviewViewHolder(viewGroup);
        }

    }

    protected void getProgressbar(View view) {
        try {
            mProgress = (ProgressBar) view.findViewById(com.hkm.layout.R.id.lylib_ui_loading_circle);
        } catch (Exception e) {
            //unable to find loading progress bar
        }
    }

    protected void showLoadingCircle() {
        if (mProgress != null) {
            mProgress.setVisibility(View.VISIBLE);
            mProgress.animate().alpha(1f);
        }
    }

    protected void hideLoadingCircle() {
        if (mProgress != null && mProgress.getVisibility() == View.VISIBLE) {
            mProgress.animate().alpha(0f).withEndAction(new Runnable() {
                @Override
                public void run() {
                    mProgress.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

}
