package com.hkm.dllocker.module;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hkm.dllocker.R;
import com.hkm.dllocker.content.option;
import com.hkm.layout.Dialog.BottomSheetDialogFragment;

/**
 * Created by hesk on 21/1/16.
 */
public class ActionMenu implements BottomSheetDialogFragment.onCallBack {

    private boolean isInProgress = false;
    private boolean initialize = false;
    private BottomSheetDialogFragment mBottomSheetDialog;
    private AppCompatActivity activity;

    public static ActionMenu with(AppCompatActivity x) {
        final ActionMenu filter = new ActionMenu(x);
        return filter;
    }

    public ActionMenu(AppCompatActivity activityBase) {
        this.activity = activityBase;
        mBottomSheetDialog = attachBottomSheet();
    }


    public BottomSheetDialogFragment attachBottomSheet() {
        final Bundle bloop = new Bundle();
        bloop.putInt(BottomSheetDialogFragment.MEASUREMENT_HEIGHT, (int) activity.getResources().getDimension(R.dimen.dialog_height));
        final BottomSheetDialogFragment instance = BottomSheetDialogFragment.newInstace(bloop);
        return instance;
    }

    public void displayButtonUpSheet() {
        mBottomSheetDialog.show(activity.getFragmentManager().beginTransaction(), "bottomSheetFragment");
        mBottomSheetDialog.setRenderCallback(this);
    }

    @Override
    public void onFragmentRender(Dialog dialog, boolean isFirstTimeRender) {
        initialize = true;
   /*     thecontroller = null;
        if (selection_memory != null && selection_memory.size() > 0) {
            request_continue_filter();
        } else
            request_new_filter();*/

        mBottomSheetDialog.setFragment(new option());
        //  mAdapter.updateHome(mPager, menu_data);
        initialize = false;

    }
}
