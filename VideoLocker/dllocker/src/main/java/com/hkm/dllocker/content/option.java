package com.hkm.dllocker.content;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hkm.dllocker.R;
import com.hkm.dllocker.module.EBus;
import com.hkm.dllocker.module.events.menuCall;
import com.hkm.dllocker.module.realm.UriCap;
import com.hkm.slider.Layouts.MaterialRippleLayout;

/**
 * Created by hesk on 21/1/16.
 */
public class option extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return View.inflate(getActivity(), R.layout.tabs_options, container);
    }

    private LinearLayout container;
    private Object temp_stored;
    private MaterialRippleLayout b1, b2, b3;

    public void setObject(Object t) {
        temp_stored = t;
    }

    private void share() {
        if (temp_stored instanceof UriCap) {
            EBus.newItemMenu((UriCap) temp_stored, menuCall.SHARE);
        }
    }

    private void validate() {
        if (temp_stored instanceof UriCap) {
            EBus.newItemMenu((UriCap) temp_stored, menuCall.VALIDATE);
        }

    }

    private void copy() {
        if (temp_stored instanceof UriCap) {
            EBus.newItemMenu((UriCap) temp_stored, menuCall.COPY);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        container = (LinearLayout) view.findViewById(R.id.lylib_menu_container);

        b1 = (MaterialRippleLayout) view.findViewById(R.id.b1);
        b2 = (MaterialRippleLayout) view.findViewById(R.id.b2);
        b3 = (MaterialRippleLayout) view.findViewById(R.id.b3);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copy();
            }
        });
    }
}
