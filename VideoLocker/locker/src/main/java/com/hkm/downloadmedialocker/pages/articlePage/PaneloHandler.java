package com.hkm.downloadmedialocker.pages.articlePage;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hkm.disqus.application.WiDisquscomment;
import com.hkm.downloadmedialocker.R;
import com.hkm.downloadmedialocker.pages.content.ShareArticle;
import com.hypebeast.sdk.api.model.hbeditorial.SingleArticle;
import com.neopixl.pixlui.components.textview.TextView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * Created by hesk on 29/4/15.
 */
public class PaneloHandler implements SlidingUpPanelLayout.PanelSlideListener {
    private SlidingUpPanelLayout mLayout;
    private final FrameLayout fml;
    public static final String TAG = "panelAMO";
    private final AppCompatActivity at;
    private int currenttag;
    public final static int SHARE = 0, COMMENT = 1;

    @IntDef({SHARE, COMMENT})
    public @interface page_design {
    }

    private FragmentManager fm;
    private final TextView commentcounttextview, barTitle;
    private RelativeLayout titlebarholder;
    private LinearLayout commentbuttonholder;
    private final ImageButton cross_close;
    private final ShareArticle bbShare;
    private WiDisquscomment discussion;
    private final SingleArticle the_post;

    public PaneloHandler(AppCompatActivity a, final SingleArticle post) {
        titlebarholder = (RelativeLayout) a.findViewById(R.id.bottomholder);
        cross_close = (ImageButton) a.findViewById(R.id.cross_close);
        commentbuttonholder = (LinearLayout) a.findViewById(R.id.topcommentbarholder);
        mLayout = (SlidingUpPanelLayout) a.findViewById(R.id.sliding_layout);
        commentcounttextview = (TextView) a.findViewById(R.id.comment_count);
        barTitle = (TextView) a.findViewById(R.id.barTitle);
        fml = (FrameLayout) a.findViewById(R.id.article_extra_frame);
        at = a;

        bbShare = new ShareArticle();
        bbShare.setBindShareObject(new ShareArticle.bindArticleData() {
            @Override
            public SingleArticle bind() {
                return post;
            }
        });


        the_post = post;
        currenttag = SHARE;
        try {
            Bundle transit_data = WiDisquscomment.B(
                    post._embedded.disqus_identifier,
                    post._links.self.getHref(),
                    "hypebeast");

            discussion = WiDisquscomment.newInstance(transit_data);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }

        setup();
    }


    private void setup() {
        fm = at.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.article_extra_frame, bbShare, "share")
                .addToBackStack(null).commit();
        mLayout.setPanelSlideListener(this);
        switcher(false);
    }

    /**
     * enabled the design of the switch is on
     *
     * @param tt the bool
     */
    private void switcher(final boolean tt) {
        if (tt) {
            //show the title only
            titlebarholder.setVisibility(View.VISIBLE);
            commentbuttonholder.setVisibility(View.GONE);
            cross_close.setEnabled(true);
            if (currenttag == COMMENT)
                barTitle.setText("Comments");
            else if (currenttag == SHARE) barTitle.setText("Share");
        } else {
            //show two buttons only
            titlebarholder.setVisibility(View.GONE);
            commentbuttonholder.setVisibility(View.VISIBLE);
            cross_close.setEnabled(false);
        }
    }

    private void showBarButtons() {
        switcher(false);
    }

    public PaneloHandler goto_share() {
        if (currenttag != SHARE) currenttag = SHARE;
        else return this;
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.article_extra_frame, bbShare, "share")
                .addToBackStack(null).commit();
        switcher(true);
        return this;
    }


    public PaneloHandler goto_comments() {
        if (currenttag != COMMENT) currenttag = COMMENT;
        else return this;
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.article_extra_frame, discussion, "comment_hb")
                .addToBackStack(null).commit();
        switcher(true);
        return this;
    }

    public void open() {
        if (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED)
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    public void toggle() {
        if (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED)
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        else if (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED)
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    public void commentCount(int n) {
        commentcounttextview.setText(n + "");
    }

    public boolean onBackPress() {
        if (mLayout != null && (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void onPanelSlide(View view, float v) {

    }

    @Override
    public void onPanelCollapsed(View view) {
        showBarButtons();
    }

    @Override
    public void onPanelExpanded(View view) {
        switcher(true);
    }

    @Override
    public void onPanelAnchored(View view) {

    }

    @Override
    public void onPanelHidden(View view) {

    }
}
