package com.hkm.editorial.pages.content;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

import com.hkm.editorial.R;
import com.hkm.editorial.life.HBUtil;
import com.hkm.editorial.pages.catelog.template_general_list;
import com.hkm.editorial.pages.catelog.template_home_list;
import com.hkm.editorial.pages.featureList.skeleton;
import com.hkm.editorial.viewpagerfix.FixedSpeedScroller;
import com.hkm.slider.Animations.DescriptionAnimation;
import com.hkm.slider.LoyalUtil;
import com.hkm.slider.SliderLayout;
import com.hkm.slider.SliderTypes.AdvancedTextSliderView;
import com.hkm.slider.SliderTypes.BaseSliderView;
import com.hkm.slider.SliderTypes.CompactFrameSliderView;
import com.hkm.slider.TransformerL;
import com.hypebeast.sdk.api.model.hbeditorial.Slide;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v13.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v13.FragmentPagerItems;

import java.util.Iterator;
import java.util.List;

import github.chenupt.dragtoplayout.DragTopLayout;

/**
 * Created by hesk on 15/6/15.
 */
public class home_slider extends homeBase implements BaseSliderView.OnSliderClickListener {
    private SmartTabLayout mTab;
    protected SliderLayout mSlider;
    protected RelativeLayout rlayout;
    protected DragTopLayout mDragLayout;

    protected int getIdLayout() {
        //getArguments().getBoolean(HAS_AD, false) ? R.layout.homepage_ad : R.layout.homepage
        //return retention.overConfigurationFile == null ? R.layout.homepage : R.layout.homepage_v2;
        return R.layout.homepage_v30;
    }

    public static home_slider withAd(boolean bool) {
        final home_slider t = new home_slider();
        Bundle b = new Bundle();
        b.putBoolean(homeBase.HAS_AD, bool);
        t.setArguments(b);
        return t;
    }


    @Override
    @SuppressLint("ResourceAsColor")
    protected void constructTabs(View v) {
        mTab = (SmartTabLayout) v.findViewById(R.id.materialTabHost);
       /* if (mTab != null) {
            mTab.setBorderReferenceColor(1, R.color.divider_press);
            mTab.setCustomBackground(R.drawable.tab_host_bottom_line);
            mTab.addTab(mTab.createCustomTextTab(R.layout.item_tab, getResources().getString(R.string.recent), false).setTabListener(this));
            mTab.addTab(mTab.createCustomTextTab(R.layout.item_tab, getResources().getString(R.string.popular), false).setTabListener(this));
        }*/
        pager.setAdapter(newAdapter());
        pager.setOffscreenPageLimit(2);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //pagerChanged(position);
            }

            @Override
            public void onPageSelected(int position) {
                // pagerChanged(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTab.setViewPager(pager);
        FixedSpeedScroller.setSmoothScroller(pager, getActivity());
    }

    public FragmentPagerItemAdapter newAdapter() {
        FragmentPagerItemAdapter adp = new FragmentPagerItemAdapter(
                getChildFragmentManager(), FragmentPagerItems.with(getActivity())
                /**
                 * adding the fragment of the list template into the tabs
                 */
                .add(R.string.recent, template_home_list.class, skeleton.con_latest(R.string.recent))
                .add(R.string.popular, template_home_list.class, skeleton.con_cate("popular"))
                .create());
        return adp;
    }

    @Override
    protected void constructAfter(View v, ViewPager vp) {
        mDragLayout = (DragTopLayout) v.findViewById(R.id.drag_layout);
    }

/*
    @Override
    protected void onVPSelected(final int p) {
        // when user do a swipe the selected tab change
        mTab.setSelectedNavigationItem(p);
    }*/

    /**
     * an API for have the view detected
     *
     * @param v slider
     */
    @Override
    protected void constructOthers(View v) {
        rlayout = (RelativeLayout) v.findViewById(R.id.sliderHolder);
        final SliderLayout mslider = (SliderLayout) v.findViewById(R.id.slider);
        if (mslider != null) {
            loadSlider(mslider);
        }
    }

    @SuppressLint("ResourceAsColor")
    protected void loadSlider(final SliderLayout vslid) {
        vslid.setOffscreenPageLimit(1);
        vslid.setSliderTransformDuration(500, new LinearOutSlowInInterpolator());
        vslid.setPresetTransformer(TransformerL.Default);
        vslid.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        //  vslid.setVisibility(View.INVISIBLE);
        vslid.getPagerIndicator().setDefaultIndicatorColor(R.color.indicator_scheme_1_s, R.color.indicator_scheme_1_u);
        vslid.stopAutoCycle();
        // mslider.setCustomAnimation(new DescriptionAnimation());
    }


    static class SlideFrameHolder extends AdvancedTextSliderView {

        public SlideFrameHolder(Context context) {
            super(context);
        }

        @Override
        protected int renderedLayoutTextBanner() {
            return R.layout.feature_banner;
        }
    }

    public void onSliderClick(BaseSliderView b) {
        if (LoyalUtil.getUri(b) != null) {
            HBUtil.slide_uri_check(LoyalUtil.getUri(b), getActivity());
        }
    }

    protected void setup_double_faces(final SliderLayout mslide, final List<Slide> list) throws Exception {
        Iterator<Slide> itb = list.iterator();
        mslide.setCustomAnimation(new DescriptionAnimation(250, new DecelerateInterpolator()));
        while (itb.hasNext()) {
            final Slide first = itb.next();
            final boolean hasSecond = itb.hasNext();
            if (hasSecond) {
                final Slide second = itb.next();
                CompactFrameSliderView textSliderView = new CompactFrameSliderView(getActivity(), 2);
                textSliderView
                        .setDescriptions(
                                new String[]{
                                        first.text,
                                        second.text
                                }
                        )
                        .setDisplayOnlyImageUrls(
                                new String[]{
                                        first.image,
                                        second.image
                                }
                        )
                        .setLinksOnEach(
                                new String[]{
                                        first.href,
                                        second.href
                                }
                        )
                        .enableImageLocalStorage()
                        .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                        .setOnSliderClickListener(this);
                //add your extra information
                mslide.addSlider(textSliderView);
            } else {
                SlideFrameHolder textSliderView = new SlideFrameHolder(getActivity());
                textSliderView
                        .description(first.text)
                        .setUri(Uri.parse(first.href))
                        .image(first.image)
                        .enableImageLocalStorage()
                        .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                        .setOnSliderClickListener(this);
                //add your extra information
                mslide.addSlider(textSliderView);
            }
        }
        HBUtil.startToReveal(rlayout, 1000);
    }


    protected void setup_gallery(final SliderLayout mslider, final List<Slide> list) throws Exception {
        Iterator<Slide> itb = list.iterator();
        while (itb.hasNext()) {
            final Slide listItem = itb.next();
            SlideFrameHolder textSliderView = new SlideFrameHolder(getActivity());
            textSliderView
                    .description(listItem.text)
                    .setUri(Uri.parse(listItem.href))
                    .enableImageLocalStorage()
                    .image(listItem.image)
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                    .setOnSliderClickListener(this);
            //add your extra information
            mslider.addSlider(textSliderView);
        }
        HBUtil.startToReveal(rlayout, 1000);
    }


    public void triggerPullDown() {
        if (mDragLayout.getState() == DragTopLayout.PanelState.COLLAPSED) {
            mDragLayout.toggleTopView();
        }
    }
}