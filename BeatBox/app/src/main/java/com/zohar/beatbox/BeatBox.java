package com.zohar.beatbox;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BeatBox {

    private static final String TAG = "BeatBox";

    // sound文件名
    private static final String SOUND_FOLDER = "sample_sound";
    private AssetManager mAssets;
    private List<Sound> mSounds = new ArrayList<>();

    public BeatBox(Context context){
        mAssets = context.getAssets();
        loadSound(); // 加载sound文件
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
                mSounds.add(sound);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }


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
