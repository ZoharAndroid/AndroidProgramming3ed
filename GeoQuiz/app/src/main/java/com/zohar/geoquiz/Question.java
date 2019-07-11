package com.zohar.geoquiz;

public class Question {

    private int mTextResId;
    private boolean mAnswerTrue;
    // 用户是否回答了
    private boolean mUserAnswered;

    public boolean isUserAnswered() {
        return mUserAnswered;
    }

    public void setUserAnswered(boolean userAnswered) {
        mUserAnswered = userAnswered;
    }

    public Question(int textResId, boolean answerTrue) {
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
    }


    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }
}
