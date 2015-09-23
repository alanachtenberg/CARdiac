package com.appspot.cadiac_404.androidapp.popup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import java.security.InvalidParameterException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Alan on 9/21/2015.
 */
public class PopUpDialogFragment extends DialogFragment {
    public static String TIMEOUT_ARG_KEY="TIMEOUT";
    private static String DIALOGUE_TITLE = "Are you OK?         ";
    private static String MESSAGE = "A problem was detected\n" +
            "Press YES to confirm\n\n" +
            "!Press NO to create emergency response!";

    private static String POS_BUTTON_TEXT = "YES";
    private static String NEG_BUTTON_TEXT = "NO";
    private static int INTERVAL = 500; //Interval to update countdown
    private int mTimeout = 10 * 1000; //milliseconds
    private Dialog mDialog;
    private CountDownTimer mCountDownTimer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mCountDownTimer.start();//onCreateView runs last, before dialog is shown to user
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        final int timeout = args.getInt(TIMEOUT_ARG_KEY, 1);
        if (timeout < 1) {
            final String errorMsg = "Parameter timeout must be at least one second";
            Log.e("POP_UP_FRAGMENT", errorMsg);
            throw new InvalidParameterException(errorMsg);
        }
        this.mTimeout = timeout * 1000;

        TimePickerDialog.Builder builder = new TimePickerDialog.Builder(getActivity());
        builder.setTitle(DIALOGUE_TITLE);
        builder.setMessage(String.format(MESSAGE));

        builder.setPositiveButton(POS_BUTTON_TEXT, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO user is okay
                dialog.dismiss();
                mCountDownTimer.cancel();
            }
        });

        builder.setNeutralButton(NEG_BUTTON_TEXT, new DialogInterface.OnClickListener() {//using neutral button instead of neg to increase space between buttons(prevent accidental presses)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();//close dialog
                mCountDownTimer.cancel();
                //TODO user said they were not okay
            }
        });

        mDialog = builder.create();

        mCountDownTimer = new CountDownTimer(mTimeout, INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                mDialog.setTitle(String.format("%s %d", DIALOGUE_TITLE, seconds));
            }

            @Override
            public void onFinish() {
                mDialog.dismiss();
                //TODO user did not respond in time, create alert
            }
        };

        return mDialog;
    }

}

