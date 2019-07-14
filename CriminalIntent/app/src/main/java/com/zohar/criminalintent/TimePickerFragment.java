package com.zohar.criminalintent;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.util.Date;

public class TimePickerFragment extends DialogFragment {


    private static final String ARGS_TIME = "args_time";

    public static final String EXTRA_TIME = "extra_time";

    private TimePicker mTimePicker;

    public static Fragment newInstance(Date date){
        Bundle args = new Bundle();
        args.putSerializable(ARGS_TIME,date);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_time, null);

        mTimePicker = view.findViewById(R.id.dialog_time_picker);

        final Date date = (Date)getArguments().getSerializable(ARGS_TIME);

        return  new AlertDialog.Builder(getContext()).setView(view).setTitle(R.string.time_picker_title).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int hour;
                int minute;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    hour = mTimePicker.getHour();
                    minute = mTimePicker.getMinute();
                }else{
                    hour = mTimePicker.getCurrentHour();
                    minute = mTimePicker.getCurrentMinute();
                }

                date.setHours(hour);
                date.setMinutes(minute);

                setResult(Activity.RESULT_OK,date);
            }
        }).create();
    }

    public void setResult(int resultCode, Date date){
        if (getTargetFragment() == null){
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME, date);

        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode, intent);
    }

}
