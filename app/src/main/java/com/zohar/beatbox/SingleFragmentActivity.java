package com.zohar.beatbox;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public abstract class SingleFragmentActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();

    @LayoutRes
    protected int getLayoutResId(){
        return R.layout.activity_fragment;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        FragmentManager manager = getSupportFragmentManager();
        // 找到fragment
        Fragment fragment = manager.findFragmentById(R.id.fragment_container);
        if (fragment == null){
            fragment = createFragment();
            manager.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }
}
