package com.hkm.dllocker.module;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.hkm.dllocker.module.Dialog.BooDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zJJ on 1/18/2016.
 */
public final class DLUtil {

    public static String Log = "";

    public static boolean startFromSharing(@Nullable Intent receivedIntent, FragmentManager mg) {
        try {
            if (receivedIntent == null) return false;
            //find out what we are dealing with
            String receivedType = receivedIntent.getType();
            //get the action
            String receivedAction = receivedIntent.getAction();

            if (receivedType == null || receivedAction == null) return false;
            //make sure it's an action and type we can handle
            if (receivedAction.equals(Intent.ACTION_SEND)) {
                //content is being shared
                if (receivedType.startsWith("text/")) {
                    //handle sent text
                    String receivedText = receivedIntent.getStringExtra(Intent.EXTRA_TEXT);
                    determine_for_url(receivedText, mg);

                    return true;
                } else if (receivedType.startsWith("image/")) {
                    //handle sent image

                }

            } else if (receivedAction.equals(Intent.ACTION_MAIN)) {
                //app has been launched directly, not from share list
            }
        } catch (Exception e) {
            e.fillInStackTrace();
            return false;
        }
        return false;
    }

    public static void determine_for_url(@Nullable String receivedText, FragmentManager mg) {
        //DLUtil.slide_uri_check();
        if (receivedText == null || receivedText.isEmpty()) return;
        final String get_link = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)";
        Pattern patternc = Pattern.compile(get_link);
        Matcher fm = patternc.matcher(receivedText);
        if (fm.find()) {
            // Log.d("hackResult", fm.group(0));
            String k_start = fm.group(0);
            if (!k_start.isEmpty()) {

                Uri base = Uri.parse(k_start);
                if (base.getAuthority().equalsIgnoreCase("soundcloud.com")) {

                    if (base.getPathSegments().contains("you")) return;
                    if (base.getPathSegments().contains("pages")) return;
                    if (base.getPathSegments().contains("settings")) return;
                    if (base.getLastPathSegment().equalsIgnoreCase("stream")) return;
                    if (base.getLastPathSegment().equalsIgnoreCase("upload")) return;
                    if (base.getLastPathSegment().equalsIgnoreCase("people")) return;
                    if (base.getLastPathSegment().equalsIgnoreCase("settings")) return;

                    final ProcessOrder po = new ProcessOrder(ProcessOrder.progcesstype.SOUNDCLOUD);
                    po.setRequest_url(k_start.trim());

                    BooDialog.enquire("Soundcloud music is found, do you want to convert it now? ", mg, new Runnable() {
                        @Override
                        public void run() {
                            EBus.getInstance().post(po);
                        }
                    });

                } else if (base.getAuthority().equalsIgnoreCase("m.facebook.com")) {


                    final ProcessOrder po = new ProcessOrder(ProcessOrder.progcesstype.FB_SHARE_VIDEO);
                    po.setRequest_url(k_start.trim());
                    BooDialog.enquire("Facebook video is found, do you want to convert it now? ", mg, new Runnable() {
                        @Override
                        public void run() {
                            EBus.getInstance().post(po);
                        }
                    });
                }
            }
        }
    }


    public static String getMoment(String get_date) {
        //   PrettyTime p = new PrettyTime();
        //http://www.datameer.com/documentation/display/DAS20/Date+and+Time+Parse+Patterns
        String ISO_FORMAT1 = "yyyy-MM-dd'T'HH:mm:ss.SSS zzz";
        String ISO_FORMAT2 = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        String ISO_FORMAT3 = "yyyy-MM-dd HH:mm:ss z";
        String ISO_FORMAT4 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        String ISO_FORMAT5 = "yyyy-MM-dd'T'HH:mm:ssZ";
        String ISO_FORMAT6 = "yyyy-MM-dd HH:mm:ss.SSS";

        try {
            Date parsedTimeStamp = new SimpleDateFormat(ISO_FORMAT6, new Locale("en", "US")).parse(get_date);
            org.ocpsoft.prettytime.PrettyTime p = new org.ocpsoft.prettytime.PrettyTime();
            return p.format(parsedTimeStamp);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        //     return p.format(parsedTimeStamp);
        return get_date;
        //   return get_date;
    }


    private static String APP_INTENT_TITLE = "title";
    private static String APP_INTENT_URI = "uri";
    private static int RETURN_WITH_NEW_FEED_URL = 1022;


    /**
     * start the new activities
     *
     * @param packageName the package application id
     * @param url         the url to start from
     * @param title       the title to send as extra information
     */
    public static void startNewActivity(final String packageName, final String url, final String title, final Context activity) {
        Intent intent = activity.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null) {
        /* We found the activity now start the activity */
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Bundle b = new Bundle();
            b.putString(APP_INTENT_URI, url);
            b.putString(APP_INTENT_TITLE, title);
            intent.putExtras(b);
            activity.startActivity(intent);

        } else {
        /* Bring user to the market or let them choose an app? */
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + packageName));
            activity.startActivity(intent);
        }
    }

    /**
     * start the intent from the list view
     *
     * @param url      the url in full path
     * @param activity the activity
     */
    public static void startFeedList(final String url, final Activity activity) {
        Bundle conData = new Bundle();
        // conData.putString(MainScreen.KEYURL, url);
        Intent intent = new Intent();
        intent.putExtras(conData);
        activity.setResult(RETURN_WITH_NEW_FEED_URL, intent);
        activity.finish();
    }

    /**
     * start the new article by the single click
     *
     * @param url      in full path for url
     * @param activity the activity
     */

    public static void startNewArticle(final String url, final Activity activity) {
    /*    final Intent intent = new Intent(activity, Single.class);
        Bundle b = new Bundle();
        b.putString(Single.SURL, url);
        intent.putExtras(b);
        activity.startActivity(intent);*/
    }

    /**
     * start the application in browser to see the url or choose by other application to view this uri
     *
     * @param url      in full path for url
     * @param activity the activity
     */
    public static void openOtherUri(final String url, final Activity activity) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        activity.startActivity(intent);
    }


    public static int getMargin(final Context c) {
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, c.getResources()
                .getDisplayMetrics());

        return pageMargin;
    }

    public static void startToReveal(final RelativeLayout view, final int timeinit) {
        final Handler h = new Handler();
        view.setVisibility(View.VISIBLE);
        view.setAlpha(0f);
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.animate().alpha(1f);
            }
        }, timeinit);
    }

    public static void startToReveal(final ViewGroup view, final int timeinit) {
        final Handler h = new Handler();
        view.setVisibility(View.VISIBLE);
        view.setAlpha(0f);
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.animate().alpha(1f);
            }
        }, timeinit);
    }


    /**
     * Return a float value within the range.
     * This is just a wrapper for Math.min() and Math.max().
     * This may be useful if you feel it confusing ("Which is min and which is max?").
     *
     * @param value    the target value
     * @param minValue minimum value. If value is less than this, minValue will be returned
     * @param maxValue maximum value. If value is greater than this, maxValue will be returned
     * @return float value limited to the range
     */
    public static float getFloat(final float value, final float minValue, final float maxValue) {
        return Math.min(maxValue, Math.max(minValue, value));
    }

    /**
     * Create a color integer value with specified alpha.
     * This may be useful to change alpha value of background color.
     *
     * @param alpha     alpha value from 0.0f to 1.0f.
     * @param baseColor base color. alpha value will be ignored.
     * @return a color with alpha made from base color
     */
    public static int getColorWithAlpha(float alpha, int baseColor) {
        int a = Math.min(255, Math.max(0, (int) (alpha * 255))) << 24;
        int rgb = 0x00ffffff & baseColor;
        return a + rgb;
    }

    /**
     * Add an OnGlobalLayoutListener for the view.
     * This is just a convenience method for using {@code ViewTreeObserver.OnGlobalLayoutListener()}.
     * This also handles removing listener when onGlobalLayout is called.
     *
     * @param view     the target view to add global layout listener
     * @param runnable runnable to be executed after the view is laid out
     */
    public static void addOnGlobalLayoutListener(final View view, final Runnable runnable) {
        ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                runnable.run();
            }
        });
    }


    /**
     * Convert RGB color to CMYK color.
     *
     * @param rgbColor target color
     * @return CMYK array
     */
    public static float[] cmykFromRgb(int rgbColor) {
        int red = (0xff0000 & rgbColor) >> 16;
        int green = (0xff00 & rgbColor) >> 8;
        int blue = (0xff & rgbColor);
        float black = Math.min(1.0f - red / 255.0f, Math.min(1.0f - green / 255.0f, 1.0f - blue / 255.0f));
        float cyan = 1.0f;
        float magenta = 1.0f;
        float yellow = 1.0f;
        if (black != 1.0f) {
            // black 1.0 causes zero divide
            cyan = (1.0f - (red / 255.0f) - black) / (1.0f - black);
            magenta = (1.0f - (green / 255.0f) - black) / (1.0f - black);
            yellow = (1.0f - (blue / 255.0f) - black) / (1.0f - black);
        }
        return new float[]{cyan, magenta, yellow, black};
    }

    /**
     * Convert CYMK color to RGB color.
     * This method doesn't check f cmyk is not null or have 4 elements in array.
     *
     * @param cmyk target CYMK color. Each value should be between 0.0f to 1.0f,
     *             and should be set in this order: cyan, magenta, yellow, black.
     * @return ARGB color. Alpha is fixed value (255).
     */
    public static int rgbFromCmyk(float[] cmyk) {
        float cyan = cmyk[0];
        float magenta = cmyk[1];
        float yellow = cmyk[2];
        float black = cmyk[3];
        int red = (int) ((1.0f - Math.min(1.0f, cyan * (1.0f - black) + black)) * 255);
        int green = (int) ((1.0f - Math.min(1.0f, magenta * (1.0f - black) + black)) * 255);
        int blue = (int) ((1.0f - Math.min(1.0f, yellow * (1.0f - black) + black)) * 255);
        return ((0xff & red) << 16) + ((0xff & green) << 8) + (0xff & blue);
    }


    public static boolean isNetworkAvailable(Activity mActivity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void slide_uri_check(Uri in, Activity activity_text) {
        if (in.getScheme().equalsIgnoreCase("http") || in.getScheme().equalsIgnoreCase("https")) {
            if (in.getAuthority().endsWith("store.hypebeast.com")) {
                //assume we open the store app or open the play store to find the app
                startNewActivity("com.hypebeast.store", in.toString(), "shop in hypebeast", activity_text);
            } else if (in.getAuthority().endsWith("hypebeast.com")) {
                if (in.getPathSegments().contains("tags")) {
                    //assume this is the tags
                    startFeedList(in.toString(), activity_text);
                } else if (in.getPathSegments().contains("cate")) {
                    //assume this is the categories
                    startFeedList(in.toString(), activity_text);
                } else {
                    //assume this is the article
                    startNewArticle(in.toString(), activity_text);
                }
            } else {
                openOtherUri(in.toString(), activity_text);
            }
        } else {

        }
    }


    public static boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    public static boolean isLandscape(Context context) {
        int ot = context.getResources().getConfiguration().orientation;
        switch (ot) {
            case Configuration.ORIENTATION_LANDSCAPE:
                android.util.Log.d("my orient", "ORIENTATION_LANDSCAPE");
                return true;
            case Configuration.ORIENTATION_PORTRAIT:
                android.util.Log.d("my orient", "ORIENTATION_PORTRAIT");
                return false;
            case Configuration.ORIENTATION_SQUARE:
                android.util.Log.d("my orient", "ORIENTATION_SQUARE");
                return false;
            case Configuration.ORIENTATION_UNDEFINED:
                android.util.Log.d("my orient", "ORIENTATION_UNDEFINED");
                return false;
            default:
                android.util.Log.d("my orient", "default val");
                return false;
        }
    }

    private static Locale getLocale(String settings) {
        if (settings.equalsIgnoreCase("en")) {
            return Locale.ENGLISH;
        } else if (settings.equalsIgnoreCase("zh_CN")) {
            return Locale.SIMPLIFIED_CHINESE;
        } else if (settings.equalsIgnoreCase("zh_TW")) {
            return Locale.TRADITIONAL_CHINESE;
        } else if (settings.equalsIgnoreCase("ja")) {
            return Locale.JAPANESE;
        } else {
            return Locale.ENGLISH;
        }
    }


}
