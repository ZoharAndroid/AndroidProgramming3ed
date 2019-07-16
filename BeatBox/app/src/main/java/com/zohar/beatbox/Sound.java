package com.zohar.beatbox;

public class Sound {

    private String name;
    private String assetPath;
    private Integer soundId;


    public Sound(String assetPath) {
        this.assetPath = assetPath;
        String[] components = assetPath.split("/");
        String filename = components[components.length - 1];
        name = filename.replace(".wav", "");// 把后缀名取掉
    }

    public String getName() {
        return name;
    }

    public String getAssetPath() {
        return assetPath;
    }

    public void setSoundId(Integer soundId){
        this.soundId = soundId;
    }

    public Integer getSoundId(){
        return soundId;
    }
}
