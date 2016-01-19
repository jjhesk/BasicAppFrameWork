package com.hkm.dllocker.module;

import android.content.ClipboardManager;
import android.content.Context;
import android.widget.ProgressBar;

import com.hkm.vdlsdk.Util;
import com.hkm.vdlsdk.client.FBdownNet;
import com.hkm.vdlsdk.client.SoundCloud;

import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * Created by zJJ on 1/18/2016.
 */
public class ProcessOrder {
    private String request_url;
    private progcesstype typeprocess;
    private boolean underProcessUrl = false;
    public LinkedHashMap<String, String> soundcloud_result;
    public String fb_video_result;
    private processNotification callback;

    public interface processNotification {
        void start();

        void done();
    }

    public final ProcessOrder setOnProcessNotification(final processNotification cb) {
        callback = cb;
        return this;
    }
    public enum progcesstype {
        SOUNDCLOUD,
        FB_SHARE_VIDEO
    }


    public ProcessOrder(progcesstype TYP) {
        setTypeprocess(TYP);
        callback = new processNotification() {

            @Override
            public void start() {

            }

            @Override
            public void done() {

            }
        };
    }

    public progcesstype getTypeprocess() {
        return typeprocess;
    }

    public void setTypeprocess(progcesstype typeprocess) {
        this.typeprocess = typeprocess;
    }

    public String getRequest_url() {
        return request_url;
    }

    public void setRequest_url(String request_url) {
        this.request_url = request_url;
    }

    private void addMessage(String g) {
        if (DLUtil.Log.isEmpty()) {
            DLUtil.Log = g;
        } else {
            DLUtil.Log += "\n" + g;
        }
    }


    public final void processStart(final Context appContext) {
        if (getTypeprocess() == ProcessOrder.progcesstype.SOUNDCLOUD) {
            final SoundCloud client = SoundCloud.newInstance(appContext);
            client.pullFromUrl(getRequest_url(), new SoundCloud.Callback() {
                @Override
                public void success(LinkedHashMap<String, String> result) {
                    addMessage("====success====");
                    addMessage("resquest has result of " + result.size());
                    Iterator<String> iel = result.values().iterator();
                    while (iel.hasNext()) {
                        String el = iel.next();
                        addMessage("track =========================");
                        addMessage(el);
                    }
                    // enableall();
                    fb_video_result = null;
                    soundcloud_result = result;
                    Util.EasySoundCloudListShare(appContext, result);
                    underProcessUrl = false;
                }

                @Override
                public void failture(String why) {
                    addMessage("========error=========");
                    addMessage(why);
                    //  enableall();
                    underProcessUrl = false;
                }
            });

        } else if (getTypeprocess() == ProcessOrder.progcesstype.FB_SHARE_VIDEO) {

            final FBdownNet client = FBdownNet.getInstance(appContext);

            client.getVideoUrl(
                    getRequest_url(),
                    new FBdownNet.fbdownCB() {
                        @Override
                        public void success(String answer) {
                            addMessage("====success====");
                            addMessage(answer);
                            //  setClip(answer);
                            //  enableall();
                            Util.EasyVideoMessageShare(appContext, null, answer);
                            fb_video_result = answer;
                            soundcloud_result = null;
                            underProcessUrl = false;
                        }

                        @Override
                        public void failture(String why) {
                            addMessage("========error=========");
                            addMessage(why);
                            //      enableall();
                            underProcessUrl = false;
                        }

                        @Override
                        public void loginfirst(String why) {
                            addMessage("========need to login first=========");
                            addMessage(why);
                            //     enableall();
                            underProcessUrl = false;
                        }
                    }
            );

        } else {
            underProcessUrl = false;
        }
    }
}
