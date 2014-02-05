package com.feedhenry.phonegap.audio;

import android.media.MediaPlayer;

public interface Audio {
	
	public void play() throws Exception;
	public void pause() throws Exception;
	public void stop() throws Exception;
	public void setPlayer(MediaPlayer player);
	public void setURL(String url);
	public void resume() throws Exception;

}
