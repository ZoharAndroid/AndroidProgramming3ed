package com.zohar.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.media.MediaCodecInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity {

    private static final String TAG = "CrimePagerActivity";

    private static final String EXTRA_CRIME_ID= "extra_crime_id";

    private ViewPager mViewPager;

    private Button mJumpFirstButton;
    private Button mJumpLastButton;

    private List<Crime> mCrimes;
    private int mCurrentIndex = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cirme_page);
        mViewPager = findViewById(R.id.activity_crime_pager_view_pager);
        mJumpFirstButton = findViewById(R.id.jump_to_first_button);
        mJumpLastButton = findViewById(R.id.jump_to_last_button);
        UUID crimeId = (UUID)getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        mCrimes = CrimeLab.getInstance(this).getCrimes();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int i) {
                Log.d(TAG, "setAdapter -> getItem : " + i);
                Crime crime = mCrimes.get(i);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });



        for (int i = 0; i < mCrimes.size(); i++){
            if (crimeId.equals(mCrimes.get(i).getId())){
                mCurrentIndex = i;
                mViewPager.setCurrentItem(i);
                break;
            }
        }

        Log.d(TAG, " mCurrentIndex : " + mCurrentIndex);

        mJumpFirstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = 0;
                mViewPager.setCurrentItem(mCurrentIndex);
                showFirstAndLastButton(mCurrentIndex);
            }
        });



        mJumpLastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = mCrimes.size() - 1;
                mViewPager.setCurrentItem(mCurrentIndex);
                showFirstAndLastButton(mCurrentIndex);
            }
        });

        showFirstAndLastButton(mCurrentIndex);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                Log.d(TAG, "OnPageChangeListener -> onPageSelected : " + i);
                showFirstAndLastButton(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    public static Intent newIntent(Context activityContext, UUID crimeId){
        Intent intent = new Intent(activityContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    private void showFirstAndLastButton(int position){
        if (position == 0){
            mJumpFirstButton.setVisibility(View.GONE);
            mJumpLastButton.setVisibility(View.VISIBLE);
        }else if (position == mCrimes.size() - 1){
            mJumpLastButton.setVisibility(View.GONE);
            mJumpFirstButton.setVisibility(View.VISIBLE);
        }else{
            mJumpLastButton.setVisibility(View.VISIBLE);
            mJumpFirstButton.setVisibility(View.VISIBLE);
        }
    }
}
