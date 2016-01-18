package com.hkm.dllocker.module;

/**
 * Created by zJJ on 1/18/2016.
 */
public class ProcessOrder {
    private String request_url;
    private progcesstype typeprocess;

    public enum progcesstype {
        SOUNDCLOUD,
        FB_SHARE_VIDEO
    }


    public ProcessOrder(progcesstype TYP) {
        typeprocess = TYP;
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
}
