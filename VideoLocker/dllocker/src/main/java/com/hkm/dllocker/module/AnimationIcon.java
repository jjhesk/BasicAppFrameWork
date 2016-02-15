package com.hkm.dllocker.module;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;

import com.hkm.dllocker.module.realm.UriCap;
import com.hkm.vdlsdk.client.ValidationChecker;
import com.neopixl.pixlui.components.textview.TextView;

/**
 * Created by zJJ on 1/31/2016.
 */
public class AnimationIcon {
    private TextView mv;
    private UriCap data;

    public static AnimationIcon load(TextView tv, UriCap data, Operation op) {
        AnimationIcon h = new AnimationIcon(tv);
        h.setData(data);
        h.changeAnimation(op);
        return h;
    }

    private void setData(UriCap d) {
        this.data = d;
    }

    public void operate(Operation g) {
        changeAnimation(g);
    }

    public AnimationIcon(TextView view) {
        mv = view;
    }

    public enum Operation {
        START, STOP
    }

    private void check_start() {
        ValidationChecker.redirection_link_finder(data.getCompatible_link(), new ValidationChecker.check_cb() {
            @Override
            public void success(String delivered_product) {
                mv.setText("Ready");
                changeAnimation(Operation.STOP);
            }

            @Override
            public void failture(String failure) {
                mv.setText("Invalid");
                changeAnimation(Operation.STOP);
            }
        });
    }

    private void changeAnimation(Operation operation) {
        Drawable[] drawables = mv.getCompoundDrawables();
        for (Drawable drawable : drawables) {
            if (drawable != null && drawable instanceof Animatable) {
                Animatable animatable = ((Animatable) drawable);
                switch (operation) {
                    case START:
                        animatable.start();
                        check_start();
                        break;
                    case STOP:
                        animatable.stop();
                        break;
                }
            }
        }
    }


}
