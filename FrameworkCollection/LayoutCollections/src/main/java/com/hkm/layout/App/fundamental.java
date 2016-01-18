package com.hkm.layout.App;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.AnimatorRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.hkm.layout.R;

/**
 * Created by hesk on 21/8/15.
 */
public abstract class fundamental<F> extends AppCompatActivity {


    /**
     * there are some presets for the main layout configurations
     * - noactionbar : there is consist of layout resource IDs of +id(main_frame_body)
     * - actionbar : there is consist of layout resource IDs of +id(main_frame_body) and +id(mxtoolbar)
     * - overlayactionbar : there is consist of layout resource IDs of +id(main_frame_body) and +id(mxtoolbar)
     */
    public enum BODY_LAYOUT {
        weixin(R.layout.template_wei_xin, true),
        weixindual(R.layout.template_wei_xin_dual, true),
        weixindualtabhot_trans_status(R.layout.template_wx_host_status_translucence, true),
        weixindualtabhot_solid_status(R.layout.template_wx_host_solid, true),
        bottomslideupmenuoverlay(R.layout.template_bottom_menu_slide_up, true),
        bottommenuoverlay(R.layout.template_bottom_menu_overlaytb, true),
        bottommenu(R.layout.template_bottom_menu, true),
        singelsimple(R.layout.template_plane_basic, true);

        private int id;
        private boolean mhastoolbar;

        BODY_LAYOUT(@LayoutRes int layoutId, boolean hasToolBar) {
            id = layoutId;
            mhastoolbar = hasToolBar;
        }

        public boolean hasToolBarInside() {
            return mhastoolbar;
        }

        @LayoutRes
        public int getResID() {
            return id;
        }

        public static boolean compareSymbol(final int Ordinal, final BODY_LAYOUT symbol) {
            return symbol.ordinal() == Ordinal;
        }

        public static BODY_LAYOUT fromLayoutId(final @LayoutRes int id) throws Exception {
            final int e = BODY_LAYOUT.values().length;
            for (int i = 0; i < e; i++) {
                final BODY_LAYOUT h = BODY_LAYOUT.values()[i];
                if (h.getResID() == id) {
                    return h;
                }
            }
            throw new Exception("not found from this layout Id");
        }

        public static boolean isToolbarOn(final @LayoutRes int id) throws Exception {
            return BODY_LAYOUT.fromLayoutId(id).hasToolBarInside();
        }
    }

    private boolean enable_custom_transition = false;
    protected F currentFragmentNow;

    /**
     * setting the default main activity layout ID and this is normally had presented in the library and no need change unless there is a customization need for different layout ID
     *
     * @return resource id
     */
    @LayoutRes
    protected abstract int getDefaultMainActivityLayoutId();

    /**
     * @return when @link{getDefaultMainActivityLayoutId} is using user specified layout and such layout contains custom action bar or custom action tool bar then this function must return TRUE to enable the configuration of the tool bar
     */
    protected boolean forceConfigureToolBar() {
        return false;
    }

    /**
     * setting the first initial fragment at the beginning
     *
     * @return generic type fragment
     */
    protected F getInitFragment() {
        return null;
    }

    @IdRes
    protected int targetHomeFrame() {
        return R.id.lylib_main_frame_body;
    }

    public void removeFragment(F fragment) {
        if (fragment instanceof Fragment) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (fragment != null) {
                transaction.remove((Fragment) fragment);
                transaction.commit();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                fragment = null;
            }
        } else if (fragment instanceof android.support.v4.app.Fragment) {
            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (fragment != null) {
                transaction.remove((android.support.v4.app.Fragment) fragment);
                transaction.commit();
                transaction.setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                fragment = null;
            }
        }
    }

    @AnimRes
    private int transition_enter, transition_exit;

    protected void enableFragmentTransition() {
        enable_custom_transition = true;
    }

    protected void enableFragmentTransition(final @AnimRes int enter, final @AnimRes int out) {
        enable_custom_transition = true;
        transition_enter = enter;
        transition_exit = out;
    }

    @AnimRes
    private int enter() {
        if (transition_enter != 0)
            return transition_enter;
        else
            return R.anim.back_in_from_right;
    }


    @AnimRes
    private int out() {
        if (transition_exit != 0)
            return transition_exit;
        else
            return R.anim.back_out_to_right;
    }

    /**
     * require android-support-v4 import and the regular android fragment
     *
     * @param fragment    the unknown typed fragment
     * @param title       the string in title
     * @param oldFragment the previous fragment
     * @param closeDrawer if it needs to close the drawer after the new fragment has been rendered
     */
    public void setFragment(F fragment, String title, @Nullable F oldFragment, boolean closeDrawer) {
        currentFragmentNow = fragment;
        setTitle(title);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            // before honeycomb there is not android.app.Fragment
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (oldFragment != null && oldFragment != fragment)
                ft.remove((android.support.v4.app.Fragment) oldFragment);

            ft.replace(targetHomeFrame(), (android.support.v4.app.Fragment) fragment);

            if (enable_custom_transition) {
                ft.setCustomAnimations(enter(), out());
            }

            ft.commit();
        } else if (fragment instanceof Fragment) {
            if (oldFragment instanceof android.support.v4.app.Fragment)
                throw new RuntimeException("You should use only one type of Fragment");

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (oldFragment != null && fragment != oldFragment)
                ft.remove((Fragment) oldFragment);

            ft.replace(targetHomeFrame(), (Fragment) fragment);

            if (enable_custom_transition) {
                ft.setCustomAnimations(enter(), out());
            }

            ft.commit();
        } else if (fragment instanceof android.support.v4.app.Fragment) {
            if (oldFragment instanceof Fragment)
                throw new RuntimeException("You should use only one type of Fragment");

            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (oldFragment != null && oldFragment != fragment)
                ft.remove((android.support.v4.app.Fragment) oldFragment);
            ft.replace(targetHomeFrame(), (android.support.v4.app.Fragment) fragment);

            if (enable_custom_transition) {
                ft.setCustomAnimations(enter(), out());
            }

            ft.commit();

        } else
            throw new RuntimeException("Fragment must be android.app.Fragment or android.support.v4.app.Fragment");

        //if (closeDrawer)
        // getSlidingMenu().toggle(true);

        notifyOnBodyFragmentChange(currentFragmentNow);
    }

    private F getOldFragment(F fragment, @IdRes int frame_location) throws Exception {
        if (fragment instanceof Fragment) {
            return (F) this.getFragmentManager().findFragmentById(frame_location);
        } else if (fragment instanceof android.support.v4.app.Fragment) {
            return (F) this.getSupportFragmentManager().findFragmentById(frame_location);
        } else {
            throw new Exception("The input fragment is not a valid Fragment. ");
        }
    }

    private void initToolBar(final @LayoutRes int resId) throws Exception {
        if (BODY_LAYOUT.isToolbarOn(resId) || forceConfigureToolBar()) {
            final Toolbar widgetToolBar = (Toolbar) findViewById(R.id.lylib_toolbar);
            configToolBar(widgetToolBar);
            setSupportActionBar(widgetToolBar);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getRmenu() > -1) {
            getMenuInflater().inflate(getRmenu(), menu);
        }
        return true;
    }

    public void setinternalChangeNoToggle(F section, String title) {
        setinternalChange(section, title, currentFragmentNow, false);
    }


    public void setinternalChange(F section, String title) {
        setFragment(section, title);
    }

    public void setinternalChange(F section, String title, F previousFragment, boolean closedrawer) {
        setFragment(section, title, previousFragment, closedrawer);
    }

    public void setinternalChange(F section, String title, F previousFragment) {
        setFragment(section, title, previousFragment);
    }

    public void setFragment(F fragment, String title, F old_fragment) {
        setFragment(fragment, title, old_fragment, true);
    }


    public void setFragment(F fragment, String title) {
        if (currentFragmentNow == null) {
            setFragment(fragment, title, null, true);
        } else {
            setinternalChange(fragment, title, currentFragmentNow);
        }
        ;
    }


    /**
     * the location to setup and configure the toolbar widget under AppCompat V7
     *
     * @param mxToolBarV7 Toolbar object
     */
    protected void configToolBar(final Toolbar mxToolBarV7) throws NullPointerException {
        mxToolBarV7.setNavigationIcon(R.drawable.ic_action_menu_drawer);
        mxToolBarV7.setTitle(getTitle());
    }

    protected Toolbar getToolBar() {
        return (Toolbar) findViewById(R.id.lylib_toolbar);
    }

    /**
     * when the fragment is changed now and it will notify the function for user specific operations
     *
     * @param new_fragment_change_now the generic fragment type
     */
    protected void notifyOnBodyFragmentChange(F new_fragment_change_now) {

    }

    /**
     * to produce the menu by layout inflation
     *
     * @return int with resource id
     */
    protected int getRmenu() {
        return -1;
    }

    protected void afterInitContentViewToolBar() {

    }

    protected void initMainContentFragment(F fragment, Bundle savestate) throws Exception {
        if (fragment == null) throw new Exception("there is a null input from the fragment.");
        if (savestate == null) {
            setFragment(fragment, getTitle().toString(), null, false);
        } else {
            setFragment(fragment, getTitle().toString(), getOldFragment(fragment, targetHomeFrame()));
        }
    }


    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        try {
            setContentView(getDefaultMainActivityLayoutId());
            initToolBar(getDefaultMainActivityLayoutId());
            afterInitContentViewToolBar();
            initMainContentFragment(getInitFragment(), savedInstance);
        } catch (NullPointerException e) {
            Log.d("not work", e.getMessage() + "you got some problem here. try different layout namings. ");
            errorFromOnCreate(e);
        } catch (Exception e) {
            Log.d("not work", e.getMessage());
            errorFromOnCreate(e);
        }

    }

    protected void errorFromOnCreate(Exception e) {

    }

}
