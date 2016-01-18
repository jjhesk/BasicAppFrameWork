package com.hkm.dllocker.module;

import android.content.Intent;
import android.net.Uri;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zJJ on 1/18/2016.
 */
public final class DLUtil {

    public static String Log = "";

    public static void startFromSharing(Intent receivedIntent) {
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
                String container = "";
                determine_for_url(receivedText, container);
                if (!container.isEmpty()) {
                    Uri hparse = Uri.parse(container);
                    if (hparse.getAuthority().equalsIgnoreCase("soundcloud.com")) {
                        ProcessOrder po = new ProcessOrder(ProcessOrder.progcesstype.SOUNDCLOUD);
                        po.setRequest_url(container.trim());
                        EBus.getInstance().post(po);
                    } else if (hparse.getAuthority().equalsIgnoreCase("m.facebook.com")) {
                        ProcessOrder po = new ProcessOrder(ProcessOrder.progcesstype.FB_SHARE_VIDEO);
                        po.setRequest_url(container.trim());
                        EBus.getInstance().post(po);
                    }
                } else {

                }
            } else if (receivedType.startsWith("image/")) {
                //handle sent image

            }

        } else if (receivedAction.equals(Intent.ACTION_MAIN)) {
            //app has been launched directly, not from share list
        }
    }

    private static void determine_for_url(String receivedText, String container) {
        //final String find_login = "https://soundcloud.com/";
        final String get_link = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)";
        Pattern patternc = Pattern.compile(get_link);
        Matcher fm = patternc.matcher(receivedText);
        if (fm.find()) {
            // Log.d("hackResult", fm.group(0));
            String k_start = fm.group(0);
            if (!k_start.isEmpty()) {
                container = k_start;
            } else {
            }

        }
    }
}
