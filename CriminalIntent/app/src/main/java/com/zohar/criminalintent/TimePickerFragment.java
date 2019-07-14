package com.zohar.criminalintent;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import java.util.Date;

public class TimePickerFragment extends DialogFragment {

    private static final String ARGS_TIME = "args_time";

    public static Fragment newInstance(Date date){
        Bundle args = new Bundle();
        args.putSerializable(ARGS_TIME,date);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        return  new AlertDialog.Builder(getContext()).setView(R.layout.dialog_time).setTitle(R.string.time_picker_title).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create();
    }

    public void setResult(){

    }

}
