package com.suji.ish.suji.utils;

import android.media.MediaPlayer;

import java.io.IOException;

public class AudioPlayer {
    private volatile static AudioPlayer instance = null;
    private MediaPlayer mediaPlayer;

    private AudioPlayer() {
        mediaPlayer = new MediaPlayer();
    }

    public static AudioPlayer getInstance() {
        if (instance == null) {
            synchronized (AudioPlayer.class) {
                if (instance == null) {
                    instance = new AudioPlayer();
                }
            }
        }
        return instance;
    }

    public void playAudio(String source) {
        try {
            if(mediaPlayer.isPlaying()){
                mediaPlayer.stop();
            }
            mediaPlayer.reset();

            //设置到播放的资源位置 path 可以是网络 路径 也可以是本地路径
            mediaPlayer.setDataSource(source);
            mediaPlayer.prepareAsync();
            //3.1 设置一个准备完成的监听
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
