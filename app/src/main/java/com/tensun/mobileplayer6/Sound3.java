package com.tensun.mobileplayer6;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/4.
 */
public class Sound3 {

    private static MediaPlayer music;
    private static SoundPool soundPool;
    private static boolean musicSt = true; //音樂開關
    private static boolean soundSt = true; //音效開關
    private static Context context;
    private static int[] musicId = {R.raw.s_07};
    // private static final int[] musicId = {R.raw.ro};
    private static Map<Integer,Integer> soundMap; //音效資源id與加載過後的音源id的映射關係表

    /**
     * 初始化方法
     * @param c
     */
    public Sound3(Context c)
    {
        context = c;
        initMusic();
        initSound();
        Log.v("tensuncode", "Sound() - musicId: " + musicId);
    }

    //初始化音效播放器
    private static void initSound()
    {
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,100);
        soundMap = new HashMap<Integer,Integer>();
    }

    //初始化音樂播放器
    private static void initMusic()
    {
        music = MediaPlayer.create(context,R.raw.s_07);
        music.setLooping(true);
    }

    /**
     * 播放音效
     * @param resId 音效資源id
     */
    public static void playSound(int resId)
    {
        if(soundSt == false)
            return;

        Integer soundId = soundMap.get(resId);
        if(soundId != null)
            soundPool.play(soundId, 1, 1, 1, 0, 1);
    }

    /**
     * 切換一首音樂並播放
     */
    public static void changeAndPlayMusic()
    {
        if(music != null)
            music.release();
        initMusic();
        setMusicSt(true);
    }

    /**
     * 獲得音樂開關狀態
     * @return
     */
    public static boolean isMusicSt() {
        return musicSt;
    }

    /**
     * 設置音樂開關
     * @param musicSt
     */
    public static void setMusicSt(boolean musicSt) {
        Sound3.musicSt = musicSt;
        if(musicSt) {
            music.start();
            Log.v("tensuncode", "music.start()");
        } else {
            music.stop();
        }
    }

    /**
     * 獲得音效開關狀態
     * @return
     */
    public static boolean isSoundSt() {
        return soundSt;
    }

    /**
     * 設置音效開關
     * @param soundSt
     */
    public static void setSoundSt(boolean soundSt) {
        Sound3.soundSt = soundSt;
    }

    /**
     * 釋放資源
     */
    public void recyle()
    {
        music.release();
        soundPool.release();
        soundMap.clear();
    }
}
