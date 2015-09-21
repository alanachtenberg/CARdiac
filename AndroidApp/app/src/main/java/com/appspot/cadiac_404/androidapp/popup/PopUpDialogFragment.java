package com.appspot.cadiac_404.androidapp.popup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Alan on 9/21/2015.
 */
public class PopUpDialogFragment extends DialogFragment{
    private static String DIALOGUE_TITLE="Hello?";
    private static String MESSAGE="A problem was detected, are you OK?\n" +
            "Press No to create emergency response.";
    private static String POS_BUTTON="YES";
    private static String NEG_BUTTON="NO";
    private static int seconds=5;

    private Dialog mDialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(DIALOGUE_TITLE);
        builder.setMessage(String.format(MESSAGE, seconds));

        builder.setPositiveButton(POS_BUTTON, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO user is okay
            }
        });

        builder.setNegativeButton(NEG_BUTTON, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();//close dialog
                //user said they were not okay
            }
        });

        mDialog=builder.create();
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mDialog.cancel();
                timer.cancel();
                //TODO user did not respond in time, create alert
            }
        } ,
                seconds*1000);

        return mDialog;
    }
}