package com.zohar.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE = "answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "answer_shown";
    private static final String SHOWN_KEY = "shown_key";

    private boolean answerIsTrue;

    private TextView mAnswerTextView;
    private Button mShowAnswerButton;
    private boolean mWasAnswerShown;
    private TextView mAPILevelTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        answerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mAnswerTextView = findViewById(R.id.answer_text_view);

        mAPILevelTextView = findViewById(R.id.sdk_version_text_view);
        String apiLevel = "API Level " + Build.VERSION.SDK_INT;
        mAPILevelTextView.setText(apiLevel);

        if (savedInstanceState != null) {
            mWasAnswerShown = savedInstanceState.getBoolean(SHOWN_KEY);
            if (mWasAnswerShown) {
                isAnswerTextViewShow();
            }
        }


        mShowAnswerButton = findViewById(R.id.show_answer_button);
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAnswerTextViewShow();

                mWasAnswerShown = true;

                Intent intent = new Intent();
                intent.putExtra(EXTRA_ANSWER_SHOWN, mWasAnswerShown);
                setResult(RESULT_OK, intent);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int centerX = mShowAnswerButton.getWidth() / 2;
                    int centerY = mShowAnswerButton.getHeight() / 2;
                    float startRadius = mShowAnswerButton.getWidth();
                    float endRadius = 0;
                    Animator animator = ViewAnimationUtils.createCircularReveal(mShowAnswerButton, centerX, centerY, startRadius, endRadius);
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mShowAnswerButton.setVisibility(View.INVISIBLE);
                        }
                    });
                    animator.start();
                }else{
                    mShowAnswerButton.setVisibility(View.INVISIBLE);
                }

            }
        });



    }

    private void isAnswerTextViewShow() {
        if (answerIsTrue) {
            mAnswerTextView.setText(R.string.true_button);
        } else {
            mAnswerTextView.setText(R.string.false_button);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SHOWN_KEY, mWasAnswerShown);
    }

    /**
     * 返回到QuizActivity界面，用户调用了cheat界面
     *
     * @param result intent
     * @return 返回true/false
     */
    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }


    public static Intent newIntent(Context context, boolean answerIsTrue) {
        Intent intent = new Intent(context, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }
}
