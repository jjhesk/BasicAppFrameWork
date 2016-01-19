package com.hkm.dllocker.module.Dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

/**
 * Created by hesk on 19/1/16.
 */
public class BooDialog extends DialogFragment {
    private static Runnable onclickrun;
    private static final String type_dialog = "#PROPMTQUESETION", message_channel = "MESSAGE_DIALOG";
    private static final String positive_button = "YES", negative_button = "NO";

    public static void enquire(final String message, FragmentManager manager) {
        BooDialog.message(message).show(manager, type_dialog);
    }

    public static void enquire(final String message, FragmentManager manager, Runnable positive_response) {
        BooDialog.onclickrun = positive_response;
        BooDialog.message(message).show(manager, type_dialog);
    }

    private static Bundle getMessageBundle(final String mes) {
        Bundle h = new Bundle();
        h.putString(message_channel, mes);
        return h;
    }

    private static BooDialog message(final String mes) {
        Bundle h = new Bundle();
        h.putString(message_channel, mes);
        BooDialog e = new BooDialog();
        e.setArguments(h);
        return e;
    }


    protected void triggerPositive(DialogInterface dialog, int id, String original_message) {
        dialog.dismiss();
        if (BooDialog.onclickrun != null) {
            BooDialog.onclickrun.run();
            BooDialog.onclickrun = null;
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getArguments().getString(message_channel))
                .setPositiveButton(positive_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        triggerPositive(dialog, id, getArguments().getString(message_channel));
                    }
                })
                .setNegativeButton(negative_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
        ;
        // Create the AlertDialog object and return it
        return builder.create();
    }

}
