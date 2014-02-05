package com.feedhenry.phonegap.audio;

import android.media.AudioManager;
import android.media.MediaPlayer;

public class AudioStreaming implements Audio {
    MediaPlayer player;
    private String url;
    static AudioStreaming instance;
    boolean pause = false;

    public AudioStreaming() {

    }

    public void setPlayer(MediaPlayer player) {
        this.player = player;
    }

    public void setURL(String url) {
        this.url = url;
    }

    public static AudioStreaming getInstance() {
        if (instance == null)
            instance = new AudioStreaming();
        return instance;
    }

    public void play() throws Exception {
        player.setDataSource(url);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.prepareAsync();
        pause = false;

    }

    public void pause() throws Exception {
      if (player.isPlaying()){
        player.pause();
        pause = true;
      }
    }

    public void stop() throws Exception {
        player.stop();
        pause = true;

    }
    
    public void resume() throws Exception {
      if(!player.isPlaying() && pause){
        player.start();
        pause = false;
      }
    }

}
