package com.hkm.downloadmedialocker.pages.catelog;

import android.os.Bundle;

/**
 * Created by hesk on 17/12/15.
 */
public class PageList {

    private String list_url;
    private String title;
    private Bundle mBundle;

    //the listing of the url
    public PageList(String unverialURL) {
        list_url = unverialURL;
    }

    public PageList(Bundle readyToUse) {
        mBundle = readyToUse;
    }

    public void setTitle(String title_name) {
        title = title_name;
    }

    public String getTitle() {
        return title;
    }

    public Bundle getTransferBundle() {
        return mBundle;
    }



}
