package com.zohar.sunset;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class SunsetFragment extends Fragment {

    private FrameLayout mSkyFrameLayout;
    private ImageView mSunImageView;
    private View mSceneView;

    public static Fragment newInstance() {
        return new SunsetFragment();
    }

    private boolean flag = false;

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
                flag = !flag;
                if (flag) {
                    startAnimation();
                } else {
                    upAnimation();
                }
            }
        });
        return view;
    }

    private void upAnimation() {
        float sunYstart = mSkyFrameLayout.getHeight();
        float sunYend = mSunImageView.getTop();

        // 高度动画
        ObjectAnimator hightAnimator = ObjectAnimator.ofFloat(mSunImageView, "y", sunYstart, sunYend).setDuration(3200);
        hightAnimator.setInterpolator(new DecelerateInterpolator());

        // 对sky黄昏到天亮
        ObjectAnimator skyAnimator = ObjectAnimator.ofInt(mSkyFrameLayout, "backgroundColor", getResources().getColor(R.color.sunset_sky), getResources().getColor(R.color.blue_sky)).setDuration(3000);
        skyAnimator.setEvaluator(new ArgbEvaluator());

        // 夜晚到黄昏
        ObjectAnimator nightAnimator = ObjectAnimator.ofInt(mSkyFrameLayout, "backgroundColor", getResources().getColor(R.color.night_sky), getResources().getColor(R.color.sunset_sky)).setDuration(1500);
        nightAnimator.setEvaluator(new ArgbEvaluator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(hightAnimator).with(skyAnimator).after(nightAnimator);
        animatorSet.start();

    }

    private void startAnimation() {
        float sunYstart = mSunImageView.getTop();
        float sunYend = mSkyFrameLayout.getHeight();

        // 对sun进行设置
        ObjectAnimator highAnimator = ObjectAnimator.ofFloat(mSunImageView, "y", sunYstart, sunYend).setDuration(3200);
        // 添加加速插值器
        highAnimator.setInterpolator(new AccelerateInterpolator());

        // 对sky进行颜色处理
        ObjectAnimator sunsetSkyAnimator = ObjectAnimator.ofInt(mSkyFrameLayout, "backgroundColor", getResources().getColor(R.color.blue_sky), getResources().getColor(R.color.sunset_sky))
                .setDuration(3000);
        // 设置颜色渐变程度
        sunsetSkyAnimator.setEvaluator(new ArgbEvaluator());

        // 夜晚天空变化动画
        ObjectAnimator nightSkyAnimator = ObjectAnimator.ofInt(mSkyFrameLayout, "backgroundColor", getResources().getColor(R.color.sunset_sky), getResources().getColor(R.color.night_sky)).setDuration(1500);
        nightSkyAnimator.setEvaluator(new ArgbEvaluator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(highAnimator).with(sunsetSkyAnimator).before(nightSkyAnimator);
        animatorSet.start();


        highAnimator.start();
        sunsetSkyAnimator.start();


    }
}
