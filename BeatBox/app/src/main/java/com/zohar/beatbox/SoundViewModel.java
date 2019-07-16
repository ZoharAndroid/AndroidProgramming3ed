package com.zohar.beatbox;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

public class SoundViewModel extends BaseObservable {

    private Sound mSound;
    private BeatBox mBeatBox;

    public SoundViewModel(BeatBox beatBox) {
        mBeatBox = beatBox;
    }

    public Sound getSound() {
        return mSound;
    }

    public void setSound(Sound sound) {
        mSound = sound;
        notifyChange(); // 通知绑定类，viewmodel已经发生变化，绑定类因此会更新视图数据
    }

    @Bindable
    public String getTitle(){
        return mSound.getName();
    }
}
