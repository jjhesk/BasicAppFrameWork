package com.hkm.downloadmedialocker.pages.articlePage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.hkm.advancedtoolbar.Util.ErrorMessage;
import com.hkm.downloadmedialocker.R;
import com.hkm.downloadmedialocker.adInterstitual.BottomBar;
import com.hkm.downloadmedialocker.life.HBUtil;
import com.hkm.downloadmedialocker.life.LifeCycleApp;
import com.hkm.ezwebview.Util.Fx9C;
import com.hkm.ezwebview.Util.In32;
import com.hkm.ezwebview.webviewclients.HClient;
import com.hkm.ezwebview.webviewleakfix.NonLeakingWebView;
import com.hkm.slider.Animations.DescriptionAnimation;
import com.hkm.slider.Indicators.PagerIndicator;
import com.hkm.slider.SliderLayout;
import com.hkm.slider.SliderTypes.BaseSliderView;
import com.hkm.slider.SliderTypes.DefaultSliderView;
import com.hkm.slider.TransformerL;
import com.hypebeast.sdk.api.exception.ApiException;
import com.hypebeast.sdk.api.model.hbeditorial.Attachment;
import com.hypebeast.sdk.api.model.hbeditorial.EmbedPayload;
import com.hypebeast.sdk.api.model.hbeditorial.ResponseSingle;
import com.hypebeast.sdk.api.model.hbeditorial.SingleArticle;
import com.hypebeast.sdk.application.hypebeast.ConfigurationSync;
import com.hypebeast.sdk.clients.HBEditorialClient;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.neopixl.pixlui.components.textview.TextView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.hkm.ezwebview.Util.In32.*;

/**
 * Created by hesk on 27/5/15.
 */
public abstract class ArticlePageSetup extends AppCompatActivity implements DialogInterface.OnClickListener, View.OnClickListener {
    private String url, stored_content;
    public static String SURL = "theurl", TAG = "article_tag", TITLEARTICLE = "title_tag";
    private TextView line1, line2, block_tv;
    private SliderLayout mslider;
    private ProgressBar mprogressbar;
    private ImageView imageview;
    private NonLeakingWebView mVideo, block;
    private Picasso pica;
    private PagerIndicator pagerIndicator;
    private RelativeLayout sliderframe, video_frame, content_article_frame;
    protected PaneloHandler _panelamo;
    protected HBEditorialClient client;

    /**
     * find out the switch
     *
     * @return bool
     */
    private boolean withAdOn() {
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        return sp.getBoolean("ad_display", true);
    }

    /**
     * remove the ad
     */
    private void adControl() {
        if (withAdOn()) {
            BottomBar.with(((RelativeLayout) findViewById(R.id.ad_fragment)));
        } else {
            ((RelativeLayout) findViewById(R.id.ad_fragment)).setVisibility(View.GONE);
        }

    }


    /**
     * Save all appropriate fragment state.
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (stored_content != null) {
            outState.putString("html", stored_content);
        }
        super.onSaveInstanceState(outState);
    }

    /**
     * This method is called after {@link #onStart} when the activity is
     * being re-initialized from a previously saved state, given here in
     * <var>savedInstanceState</var>.  Most implementations will simply use {@link #onCreate}
     * to restore their state, but it is sometimes convenient to do it here
     * after all of the initialization has been done or to allow subclasses to
     * decide whether to use your default implementation.  The default
     * implementation of this method performs a restore of any view state that
     * had previously been frozen by {@link #onSaveInstanceState}.
     * <p/>
     * <p>This method is called between {@link #onStart} and
     * {@link #onPostCreate}.
     *
     * @param savedInstanceState the data most recently supplied in {@link #onSaveInstanceState}.
     * @see #onCreate
     * @see #onPostCreate
     * @see #onResume
     * @see #onSaveInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        stored_content = savedInstanceState.getString("html");
    }

    protected int getLayoutContentId() {
        return R.layout.sliderlayout_act_single_article;
    }

    @Override
    protected void onCreate(Bundle saved) {
        //    getActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(saved);
        final Bundle b = getIntent().getExtras();
        setContentView(getLayoutContentId());
        line1 = (TextView) findViewById(R.id.article_title);
        line2 = (TextView) findViewById(R.id.subline);
        block = (NonLeakingWebView) findViewById(R.id.content_block);
        block_tv = (TextView) findViewById(R.id.content_block_text);
        mslider = (SliderLayout) findViewById(R.id.slider);
        presetGallery(mslider);
        //  pagerIndicator = (PagerIndicator) findViewById(R.id.custom_indicator);
        mprogressbar = (ProgressBar) findViewById(R.id.progressc);
        mVideo = (NonLeakingWebView) findViewById(R.id.videoplayer);
        imageview = (ImageView) findViewById(R.id.image_view);
        video_frame = (RelativeLayout) findViewById(R.id.framevideoplayer);
        sliderframe = (RelativeLayout) findViewById(R.id.sliderFrame);
        content_article_frame = (RelativeLayout) findViewById(R.id.content_article_frame);
        pica = Picasso.with(this);
        ((ImageButton) findViewById(R.id.share_button_ssn)).setOnClickListener(this);
        ((ImageButton) findViewById(R.id.open_comment)).setOnClickListener(this);
        final Toolbar t = (Toolbar) findViewById(R.id.toolbar_actionbar);
        controlNagivation(t);
        setSupportActionBar(t);
        url = b.getString(SURL);
        client = HBEditorialClient.getInstance(getApplicationContext());
        if (url != null) {
            try {
                client.createAPIUniversal(url).getSingleArticle(article_response);
            } catch (ApiException e) {
                ErrorMessage.alert(e.getMessage(), getFragmentManager());
            }
        }
    }

    protected String getMoment(String date) throws ParseException {
        PrettyTime p = new PrettyTime();
        String ISO_FORMAT5 = "yyyy-MM-dd\'T\'HH:mm:ssZ";
        Date parsedTimeStamp = (new SimpleDateFormat(ISO_FORMAT5, new Locale("en", "US"))).parse(date);
        return p.format(parsedTimeStamp);
    }

    private Callback<ResponseSingle> article_response = new Callback<ResponseSingle>() {

        /**
         * Successful HTTP response.
         *
         * @param thePost     the return object
         * @param response          the response object
         */
        @Override
        public void success(final ResponseSingle thePost, final Response response) {
            adControl();

            if (thePost.post._embedded.video_embed_code != null) {
                setup_video(thePost.post._embedded.video_embed_code);
            } else if (thePost.post._embedded.getAttachments() != null) {
                setup_gallery(thePost.post._embedded.getAttachments());
            } else {
                setup_imageView(thePost.post._embedded);
                mVideo.setVisibility(View.GONE);
                video_frame.setVisibility(View.GONE);
                sliderframe.setVisibility(View.GONE);
            }
            googleTrack(thePost.post);
            setTitle(thePost.post._links.getCategories().get(0).getName());
            try {
                line1.setText(thePost.post.single_article_title);
                line2.setText(getMoment(thePost.post.single_article_date) + " by " + thePost.post._embedded.author);
                stored_content = thePost.post.single_article_content;
                loadFromLocalFileText(
                        ConfigurationSync.folder_name_local,
                        ConfigurationSync.local_css_file_name,
                        new In32.cssFileListenr() {
                            @Override
                            public void readFile(String html_css) {
                                try {
                                    Fx9C.setup_content_block_custom_css(
                                            ArticlePageSetup.this,
                                            content_article_frame,
                                            block,
                                            html_css,
                                            stored_content,
                                            true,
                                            default_webview_cb,
                                            null
                                    );
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                );
                updateNumberOnCommentIcon();
            } catch (IOException e) {
                error_exit(e.getMessage());
            } catch (ParseException e) {
                error_exit(e.getMessage());
            } catch (Exception e) {
                error_exit(e.getMessage());
            }


            mprogressbar.setVisibility(View.GONE);
            _panelamo = new PaneloHandler(ArticlePageSetup.this, thePost.post);


            final SharedPreferences spf = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String freq = spf.getString("languagecode", "0");

            if (freq.equals("0")) {
                triggerLoadComments(thePost.post._embedded);
            } else if (freq.equals("1")) {
                removecomment();
            } else if (freq.equals("2")) {
                triggerLoadComments(thePost.post._embedded);
            } else if (freq.equals("3")) {
                removecomment();
            } else {
                triggerLoadComments(thePost.post._embedded);
            }


        }

        /**
         * Unsuccessful HTTP response due to network failure, non-2XX status code, or unexpected
         * exception.
         *
         * @param error    error message from the http connection
         */
        @Override
        public void failure(RetrofitError error) {
            ErrorMessage.alert(error.getMessage(), getFragmentManager(), new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            });
        }
    };

    @SuppressLint("ResourceAsColor")
    protected void presetGallery(final SliderLayout vslid) {
        vslid.stopAutoCycle();
        vslid.setPresetTransformer(TransformerL.Default);
        vslid.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        vslid.setCustomAnimation(new DescriptionAnimation());
        vslid.getPagerIndicator().setDefaultIndicatorColor(R.color.indicator_scheme_2_s, R.color.indicator_scheme_2_u);
        vslid.setSliderTransformDuration(500, new LinearOutSlowInInterpolator());
        vslid.setOffscreenPageLimit(3);
    }

    protected abstract void controlNagivation(Toolbar ab);

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_button_ssn:

                try {
                    _panelamo.goto_share().open();
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }

                break;

            case R.id.open_comment:
                try {
                   /* Intent intent = AuthorizeUtils.createIntent(this, BuildConfig.API_KEY,  BuildConfig.SCOPES, BuildConfig.REDIRECT_URI);
                    startActivityForResult(intent, 0);*/
                    _panelamo.goto_comments().open();
                } catch (Exception e) {
                }
                break;
        }
    }

    /**
     * This method will be invoked when a button in the dialog is clicked.
     *
     * @param dialog The dialog that received the click.
     * @param which  The button that was clicked (e.g.
     *               {@link android.content.DialogInterface#BUTTON1}) or the position
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case R.id.share:
                break;
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


    protected void setup_imageView(EmbedPayload wrapped_items) {
        try {
            imageview.setVisibility(View.VISIBLE);
            pica
                    .load(wrapped_items.getAttachments().get(0).getFull())
                    .memoryPolicy(MemoryPolicy.NO_STORE, MemoryPolicy.NO_CACHE)
                    .into(imageview);
        } catch (Exception e) {
            imageview.setVisibility(View.GONE);
        }
    }

    private Handler h = new Handler();

    protected void setup_gallery(ArrayList<Attachment> list) {

        for (Attachment entry : list) {
            DefaultSliderView textSliderView = new DefaultSliderView(this);
            textSliderView
                    .image(entry.getFull())
                    .enableImageLocalStorage()
                    .enableSaveImageByLongClick(getFragmentManager())
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop);

            mslider.addSlider(textSliderView);
        }
        HBUtil.startToReveal(sliderframe, 2000);

    }


    private void googleTrack(SingleArticle single) {
        // Get tracker from google.
        Tracker t = ((LifeCycleApp) getApplication()).getGA();
        // Build and send an Event.
        t.setScreenName("Article:" + single._links.getSelf());
        t.send(new HitBuilders.EventBuilder()
                .setCategory("_trackEvent")
                .setAction("click view post")
                .setLabel(single._links.toString())
                .build());
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        GoogleAnalytics.getInstance(ArticlePageSetup.this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        GoogleAnalytics.getInstance(ArticlePageSetup.this).reportActivityStop(this);
    }

    protected void setTitle(final String title) {
        getSupportActionBar().setTitle(title);
    }

    private HClient.Callback default_webview_cb = new HClient.Callback() {


        @Override
        public void retrieveCookie(String s) {

        }

        @Override
        public boolean overridedefaultlogic(String s, Activity activity) {
            HBUtil.slide_uri_check(Uri.parse(s), activity);
            return true;
        }
    };

    protected void setup_video(final String video_embed_code_webvoew) {
        try {
            Fx9C.setup_web_video(
                    this,
                    video_frame,
                    mVideo,
                    (CircleProgressBar) this.findViewById(R.id.progressloadingbarpx),
                    video_embed_code_webvoew,
                    getResources().getInteger(R.integer.video_height),
                    1000,
                    default_webview_cb,
                    null);
        } catch (Exception e) {
            error_exit(e.getMessage());
        }
    }


    private void removecomment() {
        RelativeLayout r = (RelativeLayout) findViewById(R.id.comment_enabled);
        r.setVisibility(View.GONE);
        RelativeLayout s = (RelativeLayout) findViewById(R.id.share_articles);

        s.setLayoutParams(new
                LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f));

    }


    @SuppressLint("LongLogTag")
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //int orientation = this.getResources().getConfiguration().orientation;
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            getSupportActionBar().show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportActionBar().hide();
        }
        if (block.getVisibility() == View.INVISIBLE || block.getAlpha() < 1.0f && block.getVisibility() == View.VISIBLE) {

            try {
                Fx9C.setup_content_block_wb(
                        this,
                        content_article_frame,
                        block,
                        stored_content == null ? "" : stored_content,
                        default_webview_cb);
            } catch (Exception e) {
                error_exit(e.getMessage());
            }


        }
    }

    @Override
    public void onPause() {
        Fx9C.clearVideo(video_frame, mVideo);
        super.onPause();
    }

    @Override
    public void finish() {
        Fx9C.clearVideo(video_frame, mVideo);
        super.finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }

    protected abstract void updateNumberOnCommentIcon();

    protected abstract void updateNumberOnCommentIcon(int n);

    protected abstract void triggerLoadComments(EmbedPayload embed);


    protected void error_exit(String error) {
        ErrorMessage.alert(error, getFragmentManager(), new Runnable() {
            @Override
            public void run() {
                finish();
            }
        });
    }
}

