package com.zohar.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {

    private static CrimeLab sCrimeLab;

    private List<Crime> mCrimes;


    private CrimeLab() {
        mCrimes = new ArrayList<>();

        // 生成数据
        for (int i = 0; i < 100; i++){
            Crime crime = new Crime();
            crime.setTitle("crime #" + i);
            crime.setSolved(i % 2== 0);
            mCrimes.add(crime);
        }
    }

    public static CrimeLab getInstance(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab();
        }

        return sCrimeLab;
    }

    public List<Crime> getCrimes(){
        return mCrimes;
    }

    public Crime getCrime(UUID id){
        for (Crime crime : mCrimes){
            if (crime.getId().equals(id)){
                return crime;
            }
        }
        return null;
    }

    /**
     * 添加
     *
     * @param crime
     */
    public void addCrime(Crime crime){
        mCrimes.add(crime);
    }

}
