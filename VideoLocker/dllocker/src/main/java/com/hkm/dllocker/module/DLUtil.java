package com.hkm.dllocker.module;

import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.hkm.advancedtoolbar.Util.ErrorMessage;
import com.hkm.dllocker.module.Dialog.BooDialog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zJJ on 1/18/2016.
 */
public final class DLUtil {

    public static String Log = "";

    public static void startFromSharing(@Nullable Intent receivedIntent, FragmentManager mg) {
        if (receivedIntent == null) return;
        //find out what we are dealing with
        String receivedType = receivedIntent.getType();
        //get the action
        String receivedAction = receivedIntent.getAction();

        if (receivedType == null || receivedAction == null) return;
        //make sure it's an action and type we can handle
        if (receivedAction.equals(Intent.ACTION_SEND)) {
            //content is being shared
            if (receivedType.startsWith("text/")) {
                //handle sent text
                String receivedText = receivedIntent.getStringExtra(Intent.EXTRA_TEXT);
                determine_for_url(receivedText, mg);
            } else if (receivedType.startsWith("image/")) {
                //handle sent image

            }

        } else if (receivedAction.equals(Intent.ACTION_MAIN)) {
            //app has been launched directly, not from share list
        }
    }

    public static void determine_for_url(@Nullable String receivedText, FragmentManager mg) {
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


            } else {
            }

        }
    }
}
