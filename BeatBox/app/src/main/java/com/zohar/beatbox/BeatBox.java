package com.zohar.beatbox;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BeatBox {

    private static final String TAG = "BeatBox";

    // sound文件名
    private static final String SOUND_FOLDER = "sample_sound";
    private static final int MAX_SOUNDS = 5;

    private AssetManager mAssets;
    private List<Sound> mSounds = new ArrayList<>();
    private SoundPool mSoundPool;

    public BeatBox(Context context){
        mAssets = context.getAssets();
        loadSound(); // 加载sound文件
        mSoundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);
    }

    /**
     * 加载assets资源文件中的内容
     */
    private void loadSound(){
        String[] soundNames;
        try {
            soundNames = mAssets.list(SOUND_FOLDER);
            Log.d(TAG, "发现 " + soundNames.length + "个声音文件！");

            for (String soundName : soundNames){
                Sound sound = new Sound(SOUND_FOLDER + "/" + soundName);
                load(sound);
                mSounds.add(sound);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

    }

    public void play(Sound sound){
        Integer soundId = sound.getSoundId();
        if (soundId == null){
            return;
        }

        mSoundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    /**
     * 通过AssetFileDescriptor加载sound
     *
     * @param sound
     * @throws IOException
     */
    private void load(Sound sound) throws IOException {
        AssetFileDescriptor fd = mAssets.openFd(sound.getAssetPath());
        int soundId = mSoundPool.load(fd, 1);
        sound.setSoundId(soundId);
    }

    /**
     * 获取所有sound文件
     *
     * @return
     */
    public List<Sound> getSounds(){
        return mSounds;
    }

}
