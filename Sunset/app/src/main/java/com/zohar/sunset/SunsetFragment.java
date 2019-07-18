package com.zohar.sunset;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class SunsetFragment extends Fragment {

    private FrameLayout mSkyFrameLayout;
    private ImageView mSunImageView;
    private  View mSceneView;

    public static Fragment newInstance(){
        return new SunsetFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sunset, container, false);
        mSceneView = view;
        mSkyFrameLayout = view.findViewById(R.id.sky);
        mSunImageView = view.findViewById(R.id.sun);

        mSceneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimation();
            }
        });
        return view;
    }

    private void startAnimation(){
        float sunYstart = mSunImageView.getTop();
        float sunYend = mSkyFrameLayout.getHeight();

        ObjectAnimator heighAnimator = ObjectAnimator.ofFloat(mSunImageView, "y", sunYstart, sunYend).setDuration(3200);
        heighAnimator.start();
    }
}
