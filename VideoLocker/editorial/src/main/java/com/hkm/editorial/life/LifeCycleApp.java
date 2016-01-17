package com.hkm.editorial.life;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.hkm.disqus.api.ApiClient;
import com.hkm.disqus.api.ApiConfig;
import com.hkm.editorial.BuildConfig;
import com.hkm.editorial.R;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.PushService;
import com.parse.SaveCallback;
import com.r0adkll.deadskunk.utils.Utils;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrListener;
import com.r0adkll.slidr.model.SlidrPosition;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import retrofit.RestAdapter;

/**
 * Created by hesk on 2/2/15.
 */
public class LifeCycleApp extends Application {
    protected ApiConfig disqusConfigurations;
    protected ApiClient disqusClient;
    protected GoogleAnalytics googleAnalytics;
    protected Tracker ga_tracker;

    @Override
    public void onCreate() {
        super.onCreate();
        // Enable Crash Reporting
        // ParseCrashReporting.enable(this);
        // ENABLE PARSE IN HERE
        Parse.enableLocalDatastore(this);
        // Enable and initialize the parse application
        Parse.initialize(this, BuildConfig.PARSE_APPLICATION_ID, BuildConfig.PARSE_CLIENT_KEY);
        //CookieHandler.setDefault(cookieManager);
        // Save the current Installation to Parse.

        ParseInstallation.getCurrentInstallation().saveInBackground();
        // When users indicate they are Giants fans, we subscribe them to that channel.
        //  ParsePush.subscribeInBackground("");
        initializeDisQus();
        buildPicasso();
    }
       private static void saveInstallation(){
           ParseInstallation install = ParseInstallation.getCurrentInstallation();

       }
    public static void subscribeWithEmail(String email) {
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("email", email);
        installation.saveInBackground();
    }

    public synchronized Tracker getGA() {
        if (ga_tracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            ga_tracker = analytics.newTracker(BuildConfig.GOOGLE_TRACKER);
        }
        return ga_tracker;
    }

    protected void buildPicasso() {
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
        //built.setIndicatorsEnabled(true);
        //built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);

      /*  int maxSize = 1024 * 1024 * 30;
        Downloader downloader = new OkHttpDownloader(this, Integer.MAX_VALUE);
        // Create memory cache
        Cache memoryCache = new LruCache(maxSize);
        Picasso picasso = new Picasso.Builder(this)
                .downloader(downloader)
                .memoryCache(memoryCache)
                .build();*/
    }

    public static final String base_en = "http://hypebeast.com";
    private void initializeDisQus() {
        try {
            disqusConfigurations = new ApiConfig(BuildConfig.DISQUS_API_KEY, RestAdapter.LogLevel.BASIC);
            disqusConfigurations.setApiSecret(BuildConfig.DISQUS_SECRET);
            disqusConfigurations.setRedirectUri(base_en);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*
        public AuthMgr getManager() {
            return authmanager;
        }

        public ApiClient getDisqusClient() {
            if (disqusClient == null) {
                disqusClient = new ApiClient(disqusConfigurations);
                authmanager = disqusClient.createAuthenticationManager(this);
            }
            return disqusClient;
        }
    */

    public ApiConfig getConfiguration() {
        return disqusConfigurations;
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public SlidrConfig getSlidrConfig(final SlidrListener m) {
        SlidrConfig config = new SlidrConfig.Builder()
                .primaryColor(ContextCompat.getColor(getApplicationContext(), R.color.transparent))
                .secondaryColor(ContextCompat.getColor(getApplicationContext(), R.color.main_background))
                .position(SlidrPosition.LEFT)
                .sensitivity(0.4f)
                .scrimColor(Color.BLACK)
                .scrimStartAlpha(0.8f)
                .scrimEndAlpha(0f)
                .velocityThreshold(50f)
                .distanceThreshold(0.2f)
                .edge(true)
                .touchSize(Utils.dpToPx(this, 32))
                .listener(m)
                .build();
        return config;
    }
}
