package com.zohar.criminalintent;

import android.content.Intent;
import android.support.v4.app.Fragment;

public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.Callback, CrimeFragment.Callbacks {

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_master_detail;
    }

    @Override
    public void onCrimeSelect(Crime crime) {
        if (findViewById(R.id.detail_fragment_container) == null){
            // 如果没有，那么就
            Intent intent = CrimePagerActivity.newIntent(CrimeListActivity.this, crime.getId());
            startActivity(intent);
        }else {
            Fragment detailFragment = CrimeFragment.newInstance(crime.getId());
            getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment_container, detailFragment).commit();
        }
    }

    @Override
    public void onCrimeUpdated(Crime crime) {
        CrimeListFragment crimeListFragment = (CrimeListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        crimeListFragment.updateUI();
    }
}
