package com.hkm.downloadmedialocker;

import android.content.Intent;

import com.hkm.advancedtoolbar.Util.ErrorMessage;
import com.hkm.downloadmedialocker.life.HBUtil;
import com.hypebeast.sdk.api.model.hbeditorial.Foundation;
import com.hypebeast.sdk.application.Splash;
import com.hypebeast.sdk.application.hypebeast.ConfigurationSync;
import com.hypebeast.sdk.application.hypebeast.sync;

/**
 * Created by hesk on 2/9/15.
 */
public class HBSplash extends Splash {

    @Override
    protected void onPermissionGranted() {
        synchronizeData();
    }

    @Override
    protected void onPermissionDenied() {
        ErrorMessage.alert("There is no permission granted.", getFragmentManager(), new Runnable() {
            @Override
            public void run() {
                finish();
            }
        });
    }


    @Override
    protected void synchronizeData() {
        ConfigurationSync.with(getApplication(), new sync() {
            @Override
            public void initFailure(String message) {
                ErrorMessage.alert(message, getFragmentManager(), new Runnable() {
                    @Override
                    public void run() {
                        synchronizeData();
                    }
                });
            }

            @Override
            public void syncDone(ConfigurationSync conf, Foundation data) {
                String langpreference = conf.getInstanceHBClient().getLanguagePref();
                conf.switchToLanguage(langpreference);
                HBUtil.setApplicationLanguageBase(langpreference, getApplication(), conf.getInstanceHBClient());
                Intent d = new Intent(HBSplash.this, MainHome.class);
                d.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(d);
                finish();
            }
        });
    }


    @Override
    protected int getLayoutId() {
        return R.layout.splash_screen;
    }


}
