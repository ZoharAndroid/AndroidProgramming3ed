package com.zohar.nerdlauncher;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public abstract class SingleFragmentActivity extends AppCompatActivity {


    public abstract Fragment createFragment();

    public int getLayoutResId(){
        return R.layout.activity_fragment;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        FragmentManager manager = getSupportFragmentManager();
        // 通过manager找到fragment
        Fragment fragment = manager.findFragmentById(R.id.fragment_container);
        // 如果没有找到fragment
        if (fragment == null) {
            // 那么新建fragment
            fragment = createFragment();
            // 然后通过manager把当前的fragment加入到managner之中进行管理
            manager.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }
}
