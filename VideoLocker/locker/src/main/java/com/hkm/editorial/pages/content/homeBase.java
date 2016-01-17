package com.hkm.editorial.pages.content;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import com.hkm.editorial.R;
import com.hkm.editorial.viewpagerfix.FixedSpeedScrollerView;

import java.lang.reflect.Field;

/**
 * Created by hesk on 11/6/15.
 */
public abstract class homeBase extends Fragment {
    protected Application app;

    /**
     * Called when a fragment is first attached to its activity.
     * {@link #onCreate(Bundle)} will be called after this.
     *
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        app = activity.getApplication();

    }

    protected ViewPager pager;
  //  private TabVPAdapter pagerAdapter;
    private FragmentManager mChildFragmentManager;

    public static final String HAS_AD = "hasAd";
    public static final String TAG = "mainpagefragment";

    /**
     * an API for have the view detected
     *
     * @param v
     */
    protected abstract void constructOthers(View v);

    protected abstract int getIdLayout();

    protected Interpolator getSlideBehave() {
        return new DecelerateInterpolator();
    }

    protected abstract void constructTabs(View v);

    protected abstract void constructAfter(View v, ViewPager vp);


    // protected abstract void onVPSelected(int position);

    /*{
        //getArguments().getBoolean(HAS_AD, false) ? R.layout.homepage_ad : R.layout.homepage
        return R.layout.homepage_v2;
    }
*/
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getIdLayout(), container, false);
    }

    public void onDestroyView() {
        super.onDestroyView();
        try {
            Fragment fragment = (getFragmentManager().findFragmentById(R.id.adFragment));
            if (fragment != null) {
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.remove(fragment);
                ft.commit();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    final Handler handler = new Handler();

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onViewCreated(View v, Bundle b) {

        pager = (ViewPager) v.findViewById(R.id.viewpager);
        constructTabs(v);
        constructOthers(v);
        // init view pager
     /*   pagerAdapter = new TabVPAdapter(mChildFragmentManager = getChildFragmentManager(), getActivity());
        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                onVPSelected(position);
            }
        });*/
        constructAfter(v, pager);
        try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScrollerView scroller = new FixedSpeedScrollerView(getActivity(), getSlideBehave());
            scroller.setFixedDuration(500);
            mScroller.set(pager, scroller);
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
    }

    protected ViewPager getPager() {
        return pager;
    }

    protected void selectPageAt(final int page_num) {
        pager.setCurrentItem(page_num, true);
    }

    /**
     * made for external command
     */
    public void invalidateAdapterFragments() {
       // if (pagerAdapter != null) {
       //     pagerAdapter.refreshAll();
       // }
    }


    /**
     * fixed the bug on V19 or below when switching fragment between activities
     */
    @Override
    public void onDetach() {
        super.onDetach();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT_WATCH) {
            try {
                Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
                childFragmentManager.setAccessible(true);
                childFragmentManager.set(this, null);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
