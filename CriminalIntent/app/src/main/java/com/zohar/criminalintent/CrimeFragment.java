package com.zohar.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;


public class CrimeFragment extends Fragment {
    private static final String TAG = "CrimeFragment";
    private final String DIALOG_SHOW = "dialog_show";
    private final String DIALOG_SHOW_TIME = "dialog_show_time";

    private static final String ARGS_CRIME_ID = "crime_id";

    private static final int REQUEST_CODE = 1;
    private static final int REQUEST_CODE_TIME = 2;
    private Crime mCrime;

    private EditText mTitleField;
    private Button mDateButton;
    private Button mTimeButton;
    private CheckBox mSolvedCheckBox;

    public static Fragment newInstance(UUID crimeId){
        Bundle args = new Bundle();
        args.putSerializable(ARGS_CRIME_ID ,crimeId);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID)getArguments().getSerializable(ARGS_CRIME_ID);
        mCrime = CrimeLab.getInstance(getContext()).getCrime(crimeId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cirme, container, false);

        mTitleField = view.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG, "beforeTextChanged -> " + s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged -> " + s.toString());
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "afterTextChanged -> " + s.toString());
            }
        });

        mDateButton = view.findViewById(R.id.crime_date);
        updateDate();

        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = (DatePickerFragment)DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_CODE);
                dialog.show(manager, DIALOG_SHOW);
            }
        });

        mTimeButton = view.findViewById(R.id.crime_time);
        updateTime();
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                TimePickerFragment timeDiaolg = (TimePickerFragment)TimePickerFragment.newInstance(mCrime.getDate());
                timeDiaolg.setTargetFragment(CrimeFragment.this, REQUEST_CODE_TIME);
                timeDiaolg.show(manager, DIALOG_SHOW_TIME);
            }
        });

        mSolvedCheckBox = view.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_CODE){
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        }

        if (requestCode == REQUEST_CODE_TIME){
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mCrime.setDate(date);
            updateTime();
        }
    }

    /**
     * 更新日期
     *
     */
    private void updateDate() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd E", Locale.CHINA);
        String date = format.format(mCrime.getDate());
        mDateButton.setText(date);
    }

    /**
     * 更新时间
     */
    private void updateTime(){
        DateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
        String time = format.format(mCrime.getDate());
        mTimeButton.setText(time);
    }
}
