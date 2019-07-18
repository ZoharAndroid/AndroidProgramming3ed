package com.zohar.sunset;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SunsetActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return SunsetFragment.newInstance();
    }

}
