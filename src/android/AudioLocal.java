package com.feedhenry.phonegap.audio;

import android.media.AudioManager;
import android.media.MediaPlayer;

public class AudioLocal implements Audio {
    MediaPlayer player;
    String path;
    static AudioLocal instance;
    private boolean isPaused = false;

    public static AudioLocal getInstance() {
        if (instance == null)
            instance = new AudioLocal();
        return instance;
    }

    public void setURL(String path) {
        this.path = path;
    }

    public void setPlayer(MediaPlayer player) {
        
        this.player = player;
    }

    public AudioLocal() {

    }

    public void play() throws Exception {
        player.setDataSource(path);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.prepareAsync();
        

    }

    public void pause() throws Exception {
        if (player.isPlaying()){
            player.pause();
            isPaused = true;    
        }
    }

    public void stop() throws Exception {
        player.stop();

    }
    
    public void resume() throws Exception {
      if(!player.isPlaying() && isPaused ){
        player.start();
        isPaused = false;
      }
    }

}
