package com.hkm.dllocker;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hkm.advancedtoolbar.Util.ErrorMessage;
import com.hkm.advancedtoolbar.V5.BeastBar;
import com.hkm.dllocker.content.RecentActivities;
import com.hkm.dllocker.content.ViewLog;
import com.hkm.dllocker.module.Clipboardmanager;
import com.hkm.dllocker.module.DLUtil;
import com.hkm.dllocker.module.EBus;
import com.hkm.dllocker.module.ProcessOrder;
import com.hkm.dllocker.module.UI;
import com.hkm.dllocker.module.events.SimpleMenu;
import com.hkm.dllocker.module.events.menuCall;
import com.hkm.dllocker.module.realm.UriCap;
import com.hkm.layout.App.WeiXinHost;
import com.hkm.layout.Dialog.ExitDialog;
import com.hkm.layout.Menu.TabIconView;
import com.hkm.layout.WeiXinTabHost;
import com.hkm.vdlsdk.Util;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hesk on 7/1/16.
 */
public class MainHome extends WeiXinHost<Fragment> {
    public final static int SINGLE_ARTICLE = 9019;
    public final static int SETTING_INTENT = 1001;
    public final static int RETURN_WITH_NEW_FEED_URL = 102;
    public final static int RETURN_WITH_NOTHING = 100;
    public final static String KEYURL = "feed_list_url";
    private BeastBar mBeastWorker;
    private int tab_position = 0;
    private boolean tabLock = false, enable_back_button = false;
    private Runnable back_button_event;


    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            if (!ExitDialog.Tap(getFragmentManager(), new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }
            )) {
                super.onBackPressed();
            }
        }
    }

    private void fromintentdata(Intent data) {
        try {
            Bundle b = data.getExtras();
            final String url_endpoint = b.getString(KEYURL);
            //  setFragment(template_general_list.B(skeleton.general(url_endpoint)), "Tags#");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        switch (requestCode) {
            case SETTING_INTENT:
                switch (resultCode) {
                    case RESULT_OK:
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                        break;
                    case RETURN_WITH_NOTHING:
                        break;
                }
                break;
            case SINGLE_ARTICLE:
                switch (resultCode) {
                    case RETURN_WITH_NEW_FEED_URL:
                        fromintentdata(data);
                        break;
                    case RETURN_WITH_NOTHING:
                        break;
                }
                break;

        }

    }

    private RecentActivities rezHome() {
        // final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final RecentActivities mHome = RecentActivities.newInstance(R.drawable.ic_get_pocket);
        return mHome;
    }


    @Override
    protected void afterInitContentViewToolBar() {
        try {
            boolean fromIntent = DLUtil.startFromSharing(getIntent(), getFragmentManager());
            if (!fromIntent) start_clipboard_detection();
            setFragment(rezHome(), "home");
        } catch (Exception error) {
            // to handle out of memory issue
            ErrorMessage.alert(error.getMessage(), getFragmentManager(), new Runnable() {
                @Override
                public void run() {
                    Intent d = new Intent(MainHome.this, HBSplash.class);
                    startActivity(d);
                    finish();
                }
            });
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void configToolBar(Toolbar mxToolBarV7) {
        BeastBar.Builder bb = new BeastBar.Builder();
        bb.search(R.mipmap.ic_action_find);
        bb.companyIcon(R.drawable.actionbar_bg_hb_logo);
        bb.background(R.drawable.actionbar_bg_hb_white);
        bb.back(R.drawable.ic_find_replace_24dp);
        mBeastWorker = BeastBar.withToolbar(this, mxToolBarV7, bb);
        mBeastWorker.setFindIconFunc(new Runnable() {
            @Override
            public void run() {
                searchView.showSearch();
            }
        });
        mBeastWorker.setBackIconFunc(new Runnable() {
            @Override
            public void run() {
                start_clipboard_detection();
            }
        });
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        super.configToolBar(mxToolBarV7);
    }

    private void renderOriginalTabByPosition(final int position) {
        //  mBeastWorker.setBackIconFunc(null);
        //    enable_back_button = false;
       /* Menuitem chosen = overhead_data.nav_bar.get(position);
        if (chosen.getName().equalsIgnoreCase("newsfeed")) {
            mBeastWorker.showMainLogo();
            setFragment(rezHome(), "#newsfeed");
        } else if (chosen.getName().equalsIgnoreCase("categories")) {
            template_cate cate = new template_cate();
            mBeastWorker.setActionTitle(getString(R.string.action_categories));
            setFragment(cate, "#cate");
        } else if (chosen.getName().equalsIgnoreCase("tv")) {
            template_video_list girlo = template_video_list.B(template_video_list.general(chosen.getHref()));
            setFragment(girlo, "#video");
            mBeastWorker.setActionTitle(getString(R.string.action_tv));
        } else if (chosen.getName().equalsIgnoreCase("settings")) {
            userpreference h = new userpreference();
            setFragment(h, "#preference");
            mBeastWorker.setActionTitle(getString(R.string.action_settings));
        }*/

        if (position == 0) {
            RecentActivities cate = new RecentActivities();
            setFragment(cate, "#newsfeed");
            switchFindIconFunc(true);
        }

        if (position == 2) {
            ViewLog cate = new ViewLog();
            setFragment(cate, "#newsfeed");
            switchFindIconFunc(false);
        }
    }

    private void switchFindIconFunc(boolean b) {
        if (b) {
            mBeastWorker.setFindIconFunc(new Runnable() {
                @Override
                public void run() {
                    searchView.showSearch();
                }
            });
        } else {
            mBeastWorker.setFindIconFunc(null);
        }
    }

    protected void headerPosition(int ktab) {
        if (ktab > 0)
            //tool_bar_fall_down();
            tab_position = ktab;
    }
/*
    @Override
    protected List<TabIconView.Icon> getCustomTabItems() {
          Iterator<Menuitem> m = overhead_data.nav_bar.iterator();
        List<TabIconView.Icon> item = new ArrayList<>();
        while (m.hasNext()) {
            Menuitem menu = m.next();
            item.add(ic_button(menu.getName(), menu.getDisplay()));
        }
        return item;
    }*/

    @Override
    protected List<TabIconView.Icon> getCustomTabItems() {
        List<TabIconView.Icon> item = new ArrayList<>();
        for (int u = 0; u < 4; u++) {
            item.add(UI.ic_button(u));
        }
        return item;
    }


    private void make_menu_back_button_goto_main() {
        back_button_event = new Runnable() {
            @Override
            public void run() {
                renderOriginalTabByPosition(tab_position);
                mBeastWorker.setBackIconFunc(null);
                enable_back_button = false;
            }
        };
        enable_back_button = true;

        mBeastWorker.setBackIconFunc(back_button_event);
    }


    @Override
    protected WeiXinTabHost.ReTouchListener setHostRetouchListner() {
        return new WeiXinTabHost.ReTouchListener() {
            @Override
            public void againTouch(int position) {
                headerPosition(position);
            }

            @Override
            public void firstTouch(int position) {
                renderOriginalTabByPosition(position);
                headerPosition(position);
            }
        };
    }

    ProgressDialog progress;

    @Subscribe
    public void process_url(ProcessOrder order) {
        order.setOnProcessNotification(new ProcessOrder.processNotification() {
            @Override
            public void start() {
                progress = ProgressDialog.show(
                        MainHome.this,
                        "Processing",
                        "wait for the progress...",
                        false);

               /* runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (progress != null) {
                            progress = null;
                        }
                        progress = ProgressDialog.show(
                                getApplicationContext(),
                                "Processing",
                                "wait for the progress...",
                                false);
                    }
                });*/

            }

            @Override
            public void done() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                        if (currentFragmentNow instanceof RecentActivities) {
                            RecentActivities c = (RecentActivities) currentFragmentNow;
                            c.notifylist();
                        }
                    }
                });
            }
        });
        order.processStart(getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();
        EBus.getInstance().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EBus.getInstance().unregister(this);
    }

    private void start_clipboard_detection() {
        try {
            String board_text = Clipboardmanager.readFromClipboard(getApplication());
            DLUtil.determine_for_url(board_text, getFragmentManager());
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    @Subscribe
    public void eMenuCall(menuCall call) {
        if (call.getAction() == menuCall.COPY) {
            Clipboardmanager.copyToClipboard(this, call.getCap().getCompatible_link());
            Toast.makeText(this, "link is copied", Toast.LENGTH_LONG).show();
        }
        if (call.getAction() == menuCall.VALIDATE) {
            Clipboardmanager.copyToClipboard(this, call.getCap().getCompatible_link());
            Toast.makeText(this, "link is copied", Toast.LENGTH_LONG).show();
        }
        if (call.getAction() == menuCall.SHARE) {
            Clipboardmanager.copyToClipboard(this, call.getCap().getCompatible_link());
            Toast.makeText(this, "link is copied", Toast.LENGTH_LONG).show();
        }
    }


    @Subscribe
    public void itemTriggered(SimpleMenu sim) {
        openBottomSheet(sim.getCap());
    }

    public void openBottomSheet(final UriCap temp_stored) {
        View view = getLayoutInflater().inflate(R.layout.tabs_options, null);
        final TextView c_share = (TextView) view.findViewById(R.id.c_share);
        final TextView c_copy = (TextView) view.findViewById(R.id.c_copy);
        final TextView c_validate = (TextView) view.findViewById(R.id.c_validate);
        // final TextView txtUninstall = (TextView) view.findViewById(R.id.txt_uninstall);

        final Dialog mBottomSheetDialog = new Dialog(MainHome.this, R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();

        c_share.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // EBus.newItemMenu((UriCap) temp_stored, menuCall.SHARE);
                Toast.makeText(MainHome.this, "Clicked Backup", Toast.LENGTH_SHORT).show();
                mBottomSheetDialog.dismiss();
                Util.EasyVideoMessageShare(MainHome.this, "I just found out this title as " + temp_stored.getMedia_title(), temp_stored.getCompatible_link());
            }
        });

        c_copy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // EBus.newItemMenu((UriCap) temp_stored, menuCall.SHARE);
                mBottomSheetDialog.dismiss();
                Clipboardmanager.copyToClipboard(MainHome.this, temp_stored.getCompatible_link());
                Toast.makeText(MainHome.this, "link is copied", Toast.LENGTH_LONG).show();
            }
        });

        c_validate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //  EBus.newItemMenu((UriCap) temp_stored, menuCall.SHARE);
                Toast.makeText(MainHome.this, "Validation..", Toast.LENGTH_SHORT).show();
                mBottomSheetDialog.dismiss();
                // DLUtil..LinkConfirmer(temp_stored.getCompatible_link())
            }
        });

     /*   txtUninstall.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EBus.newItemMenu((UriCap) temp_stored, menuCall.SHARE);
                Toast.makeText(MainHome.this, "Clicked Uninstall", Toast.LENGTH_SHORT).show();
                mBottomSheetDialog.dismiss();

            }
        });*/

    }
}
