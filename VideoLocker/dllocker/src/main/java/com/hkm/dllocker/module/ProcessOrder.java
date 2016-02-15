package com.hkm.dllocker.module;

import android.content.Context;
import android.widget.Toast;

import com.hkm.dllocker.module.realm.RecordContainer;
import com.hkm.dllocker.module.realm.UriCap;
import com.hkm.vdlsdk.client.FBdownNet;
import com.hkm.vdlsdk.client.SoundCloud;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by zJJ on 1/18/2016.
 */
public class ProcessOrder {
    private CharSequence request_url;
    private progcesstype typeprocess;
    private boolean underProcessUrl = false;
    public LinkedHashMap<String, String> soundcloud_result;
    public String fb_video_result;
    private processNotification callback;
    private UriCap uriTarget;
    private RecordContainer container;

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
        SOUNDCLOUDRENEWAL,
        FB_SHARE_VIDEO,
        FB_SHARE_VIDEORENEWAL
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


    public void setrenewTarget(UriCap item) {
        uriTarget = item;
    }

    public progcesstype getTypeprocess() {
        return typeprocess;
    }

    public void setTypeprocess(progcesstype typeprocess) {
        this.typeprocess = typeprocess;
    }

    public String getRequest_url() {
        return request_url.toString();
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

    private void addResources(LinkedHashMap<String, String> result, int resourceType, final Context appcontext) {
        Iterator<Map.Entry<String, String>> iel = result.entrySet().iterator();
        int missed = 0;

        while (iel.hasNext()) {
            Map.Entry<String, String> el = iel.next();
            addMessage("track =========================");
            addMessage(el.getValue());
            if ((container.addNewRecord(getRequest_url(), el.getValue(), el.getKey(), UriCap.SOUNDCLOUD)
            )) {
                missed++;
            }
        }

        if (missed > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("There are ");
            sb.append(missed);
            sb.append(" duplicated resources!");
            showmsg(appcontext, sb.toString());
        } else {

            StringBuilder sb = new StringBuilder();
            sb.append("Successfully stored ");
            sb.append(result.size());
            sb.append(" resources!");
            showmsg(appcontext, sb.toString());
        }


    }

    private void showmsg(Context app, String text) {
        Toast.makeText(app, text, Toast.LENGTH_LONG).show();
    }

    public final void processStart(final Context appContext) {
        callback.start();
        container = RecordContainer.getInstnce(appContext);
        if (getTypeprocess() == ProcessOrder.progcesstype.SOUNDCLOUD) {
            final SoundCloud client = SoundCloud.newInstance(appContext);
            client.pullFromUrl(getRequest_url(), new SoundCloud.Callback() {

                @Override
                public void success(LinkedHashMap<String, String> result) {
                    addMessage("====success====");
                    addMessage("request has result of " + result.size());
                    addResources(result, UriCap.SOUNDCLOUD, appContext);
                    fb_video_result = null;
                    soundcloud_result = result;
                    // Util.EasySoundCloudListShare(appContext, result);
                    underProcessUrl = false;
                    showmsg(appContext, "Successfully converted the url resources");
                    callback.done();

                }

                @Override
                public void failture(String why) {
                    addMessage("========error=========");
                    addMessage(why);
                    underProcessUrl = false;
                    Toast.makeText(appContext, "Failure in conversion\n" + why.toString(), Toast.LENGTH_LONG).show();
                    callback.done();
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
                            underProcessUrl = false;
                            if ((container.addNewRecord(getRequest_url(), answer, "N/A", UriCap.FACEBOOK_VIDEO)
                            )) {
                                showmsg(appContext, "Successfully converted the url resources");
                            } else {
                                showmsg(appContext, "Duplicated resources!");
                            }
                            fb_video_result = answer;
                            soundcloud_result = null;
                            callback.done();
                        }

                        @Override
                        public void failture(String why) {
                            addMessage("========error=========");
                            addMessage(why);
                            underProcessUrl = false;
                            showmsg(appContext, "Failure in conversion\n" + why.toString());

                            callback.done();
                        }

                        @Override
                        public void loginfirst(String why) {
                            addMessage("========need to login first=========");
                            addMessage(why);
                            underProcessUrl = false;
                            showmsg(appContext, "Other issues from the facebook logins \n" + why.toString());
                            callback.done();

                        }
                    }
            );

        } else if (getTypeprocess() == progcesstype.FB_SHARE_VIDEORENEWAL) {
            if (uriTarget == null) return;
            final FBdownNet client = FBdownNet.getInstance(appContext);
            client.getVideoUrl(
                    uriTarget.getRaw_link(),
                    new FBdownNet.fbdownCB() {
                        @Override
                        public void success(String answer) {
                            addMessage("====success====");
                            addMessage(answer);
                            underProcessUrl = false;
                            container.updateItemCompatLink(uriTarget, answer);
                            showmsg(appContext, "The resource is renewed");
                            fb_video_result = answer;
                            soundcloud_result = null;
                            callback.done();
                        }

                        @Override
                        public void failture(String why) {
                            addMessage("========error=========");
                            addMessage(why);
                            underProcessUrl = false;
                            showmsg(appContext, "Failure in conversion\n" + why.toString());
                            callback.done();
                        }

                        @Override
                        public void loginfirst(String why) {
                            addMessage("========need to login first=========");
                            addMessage(why);
                            underProcessUrl = false;
                            showmsg(appContext, "Other issues from the facebook logins \n" + why.toString());
                            callback.done();
                        }
                    }
            );

        } else if (getTypeprocess() == progcesstype.SOUNDCLOUDRENEWAL) {
            if (uriTarget == null) return;
            final SoundCloud client = SoundCloud.newInstance(appContext);
            client.pullFromUrl(uriTarget.getRaw_link(), new SoundCloud.Callback() {

                @Override
                public void success(LinkedHashMap<String, String> result) {
                    addMessage("====success====");
                    addMessage("request has result of " + result.size());
                    addMessage("can only process this first resource on renewal");
                    Iterator<Map.Entry<String, String>> iel = result.entrySet().iterator();
                    while (iel.hasNext()) {
                        Map.Entry<String, String> el = iel.next();
                        addMessage("track =========================");
                        addMessage(el.getValue());
                        container.updateItemCompatLink(uriTarget, el.getValue());
                        break;
                    }
                    underProcessUrl = false;
                    showmsg(appContext, "Successfully renewed things");
                    callback.done();

                }

                @Override
                public void failture(String why) {
                    addMessage("========error=========");
                    addMessage(why);
                    underProcessUrl = false;
                    Toast.makeText(appContext, "Failure in conversion\n" + why.toString(), Toast.LENGTH_LONG).show();
                    callback.done();
                }
            });

        } else {
            underProcessUrl = false;
        }
    }
}
