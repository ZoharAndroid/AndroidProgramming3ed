package com.zohar.geoquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_CHEAT = "cheat";
    private static final String KEY_ANSWERD = "answered";
    private static final int REQUEST_CODE_CHEAT = 1;

    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;
    private Button mCheatButton;
    private TextView mCheatCountTextView;

    private int mCorrectCount = 0; // 回答正确的分数
    private int mAnswerCount = 0; // 回答问题数
    private boolean mIsUserAnswered; // 用户是否打过这个题
    private int mCheatCount = 3; // 舞弊次数

    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)
    };

    private int mCurrentIndex = 0;
    private boolean mIsCheater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mCurrentIndex = (int) savedInstanceState.get(KEY_INDEX);
            mIsCheater = savedInstanceState.getBoolean(KEY_CHEAT);
            mIsUserAnswered = savedInstanceState.getBoolean(KEY_ANSWERD);
        }

        mQuestionBank[mCurrentIndex].setUserAnswered(mIsUserAnswered);

        setContentView(R.layout.activity_quiz);

        mCheatCountTextView = findViewById(R.id.cheat_count_text_view);
        mCheatCountTextView.setText(mCheatCount+"");
        mQuestionTextView = findViewById(R.id.text_question_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mTrueButton = findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsUserAnswered = true;
                checkAnswer(true);
            }
        });

        mFalseButton = findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsUserAnswered = true;
                checkAnswer(false);
            }
        });

        mNextButton = findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsCheater = false; // 重置一下
                updateQuestion();
            }
        });

        mPrevButton = findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 更新索引
                if (mCurrentIndex == 0) {
                    mCurrentIndex = mQuestionBank.length - 1;
                } else {
                    mCurrentIndex--;
                }
                updateQuestion();

            }
        });

        mCheatButton = findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CheatActivity.newIntent(QuizActivity.this, mQuestionBank[mCurrentIndex].isAnswerTrue());
                startActivityForResult(intent,REQUEST_CODE_CHEAT);
            }
        });

        updateQuestion();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != RESULT_OK){
            return;
        }

        if (requestCode == REQUEST_CODE_CHEAT){
            // 获取是否查看了
            if (data == null){
                return;
            }

            mIsCheater = CheatActivity.wasAnswerShown(data);
            if (mIsCheater){
                // 如果查看了
                mCheatCount--;
                if (mCheatCount == 0){
                    mCheatButton.setVisibility(View.INVISIBLE);
                    mCheatCountTextView.setVisibility(View.INVISIBLE);
                }
                mCheatCountTextView.setText(mCheatCount + "");

            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_INDEX, mCurrentIndex);
        outState.putBoolean(KEY_CHEAT, mIsCheater);
        outState.putBoolean(KEY_ANSWERD, mIsUserAnswered);
    }

    /**
     * 更新问题
     */
    private void updateQuestion() {
        mQuestionTextView.setText(mQuestionBank[mCurrentIndex].getTextResId());
        // 检测用户是否答过题
        checkUserAnswered();
    }

    /**
     * 检查用户是否答过这个题目
     */
    private void checkUserAnswered() {
        Question question = mQuestionBank[mCurrentIndex];
        mIsUserAnswered = question.isUserAnswered();
        showAnswerView(mIsUserAnswered);
    }

    /**
     * 是否显示回答界面
     *
     * @param isUserAnswered 判断用户是否回答回答过
     */
    private void showAnswerView(boolean isUserAnswered) {
        if (isUserAnswered){
            // 用户答过这个题目
            mTrueButton.setVisibility(View.INVISIBLE);
            mFalseButton.setVisibility(View.INVISIBLE);
        }else{
            mTrueButton.setVisibility(View.VISIBLE);
            mFalseButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 检查用户输入的答案是否正确
     *
     * @param userPressedTrue 用户按下去的选择
     */
    private void checkAnswer(boolean userPressedTrue) {
        int messageResId = 0;
        Question question = mQuestionBank[mCurrentIndex];
        mAnswerCount++;

        // 如果用户没有回答过
        if (!question.isUserAnswered()) {
            question.setUserAnswered(true);
            if (mIsCheater){
                messageResId = R.string.judgment_toast;
            }else {
                if (question.isAnswerTrue() == userPressedTrue) {
                    messageResId = R.string.correct_toast;
                    mCorrectCount++;
                } else {
                    messageResId = R.string.incorrect_toast;
                }
            }

        }

        // 用户回答过，就需要禁掉回答按钮
        mTrueButton.setVisibility(View.INVISIBLE);
        mFalseButton.setVisibility(View.INVISIBLE);

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();

        // 判断回答问题的个数是否答案
        if (mAnswerCount == mQuestionBank.length) {
            float temp = mCorrectCount + 0.0f;
            float goal = (temp / mAnswerCount) * 100;
            Toast.makeText(this, goal + "%", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

}
