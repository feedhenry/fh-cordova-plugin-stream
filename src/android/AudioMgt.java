package com.feedhenry.phonegap.audio;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class AudioMgt extends CordovaPlugin implements OnBufferingUpdateListener,
		OnCompletionListener, OnPreparedListener, OnErrorListener {

    String url;
	boolean onPrepared = false;
	MediaPlayer player;
	int success = 0;
	int failure = 1;
	boolean isLocal = false;
	Audio audio;
	boolean isStart = false;
	float currentVolume;
	float rate = (float) 0.067;
	boolean paused = false;
	String[] act = { "play", "pause", "stop", "getvolume", "setvolume", "getbufferprogress", "seek", "getcurrentposition", "resume"};
	TelephonyManager telephonyMgr;
	PhoneStateListener listener;
	boolean phoneCall = false;
	int count = 0;
	boolean init = false;
	private int bufferedPercentage = 0;
	
	public AudioMgt() {
		player = new MediaPlayer();
	}

	public void init() {
		AudioManager audMgr = (AudioManager) this.cordova.getActivity().getSystemService(Context.AUDIO_SERVICE);
		currentVolume = (float) (audMgr.getStreamVolume(AudioManager.STREAM_MUSIC) * rate);
		player.setVolume(currentVolume, currentVolume);

		telephonyMgr = (TelephonyManager) this.cordova.getActivity().getSystemService(Context.TELEPHONY_SERVICE);
		telephonyMgr.listen(new phoneStates(), PhoneStateListener.LISTEN_CALL_STATE);
		init = true;

	}

	public void play(String url) {
		int tries = 0;
		int maxTries = 3;
		doPlay(url, tries, maxTries);
	}

	private void doPlay(String url, int count, int max) {
		try {
			this.url = url;
			audio = getPlayer(url);
			audio.play();
		} catch (Exception e) {
			if (count < max) {
				Log.w("audio", "Failed to Play Audio - Attempt " + count, e);
				doPlay(url, count++, max);
			} else {
				Log.e("audio", "Failed to Play Audio - Exiting", e);
				jsCallBack(failure, act[0], e.toString());
			}
		}
	}

	public void pause() {
		try {
			if (isStart) {
				if (!paused) {
				    audio.pause();
					paused = true;
					jsCallBack(success, act[1], "paused");

				}

			} else {

				jsCallBack(failure, act[1], "To start music first");
			}
		} catch (Exception e) {
			jsCallBack(failure, act[1], e.toString());
		}
	}
	
	public void resume(){
		try{
			if(isStart){
				if(paused){
					audio.resume();
					paused = false;
					jsCallBack(success, act[8], "resumed");
				}
			}else{
				jsCallBack(failure, act[8], "Not started");
			}
		}catch(Exception e){
			jsCallBack(failure, act[8], e.getMessage());
		}
	}


	public void stop() {
		try {
			if (isStart) {
				audio.stop();
				isStart = false;
				// paused = true;
				onPrepared = false;
				jsCallBack(success, act[2], "stopped");
			} else {
				jsCallBack(failure, act[2], "To start music first");
			}
		} catch (Exception e) {
			jsCallBack(failure, act[2], e.toString());
		}
	}

	public void onCompletionStop() {
		try {
			if (isStart) {
				audio.stop();
				isStart = false;
				// paused = true;
				onPrepared = false;
				jsCallBack(success, act[0], "stopped");
			} else {
				jsCallBack(failure, act[0], "To start music first");
			}
		} catch (Exception e) {
			jsCallBack(failure, act[0], e.toString());
		}

	}

	public void setVolume(String volume) {
		
		currentVolume = Float.valueOf(volume.trim()).floatValue();
		player.setVolume(currentVolume, currentVolume);
		jsCallBack(success, act[4], currentVolume + "");
	}

	public void getVolume() {
		jsCallBack(success, act[3], currentVolume + "");
	}

	public void release() {
		player.release();
		audio = null;
		player = null;

	}

	private void playback() {
		player.start();
		isStart = true;
		paused = false;
		count = 0;
		jsCallBack(success, act[0], "played");

	}

	public Audio getPlayer(String url) throws Exception {
		Audio audio = null;
		if (url.contains(":") && url.contains("//")) {
			audio = AudioStreaming.getInstance();
		} else {
			audio = AudioLocal.getInstance();
		}

		player.reset();
		player.setOnBufferingUpdateListener(this);
		player.setOnCompletionListener(this);
		player.setOnPreparedListener(this);
		audio.setPlayer(player);
		audio.setURL(url);

		return audio;

	}

	public void jsCallBack(int type, String act, String message) {
		switch (type) {
		case 0:
			this.webView.loadUrl("javascript: navigator.audio.success('" + act
					+ "','" + message + "')");
			break;
		case 1:
			this.webView.loadUrl("javascript: navigator.audio.failure('" + act
					+ "','" + message + "')");
			break;
		}
	}

	public void onPrepared(MediaPlayer arg0) {
		try {
			onPrepared = true;
			playback();
			Log.d("audio", "onPrepare " + onPrepared);
		} catch (Exception e) {
			jsCallBack(failure, act[0], e.toString());
		}

	}

	public void onCompletion(MediaPlayer mp) {
		if (onPrepared && isStart) {
			onCompletionStop();
		} else {
			if (count < 3) {
				play(url);
				count++;
				Log.d("audio", "OnCompletion error count:" + count);
			} else {
				player.reset();
				count = 0;
				jsCallBack(failure, act[0], "URL connection error");
			}
		}

	}

	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		try {
			Log.d("audio", "Buffering completed: " + percent + "%");
			bufferedPercentage = percent;
		} catch (Exception e) {
			jsCallBack(failure, act[0], e.toString());
		}

	}
	
	public void getBufferProgress(){
		jsCallBack(success, act[5], String.valueOf(bufferedPercentage));
    }

	public boolean onError(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void seek(String pPercent){
		  try{
		    if(isStart){
		      int percent = Integer.valueOf(pPercent);
		      //if(percent > bufferedPercentage){
		        //not buffered yet
		      //  percent = bufferedPercentage;
		      //}
		      int duration = player.getDuration();
		      int seekTime = Math.round(duration*((float)percent/100));
		      Log.d("audio", "Seek to time : " + seekTime);
		      player.seekTo(seekTime);
		    }
		  }catch(Exception e){
		    jsCallBack(failure, act[6], "Error when seek to position: " + pPercent);
		  }
    }
	
	public void onSeekComplete(MediaPlayer mp) {
		int currentPosition = mp.getCurrentPosition();
		int percent = Math.round(((float)currentPosition)/((float)mp.getDuration()) * 100);
		jsCallBack(success, act[6], String.valueOf(percent));
    }
		  
    public void getCurrentPosition(){
		int currentPosition = player.getCurrentPosition();
		int percent = Math.round(((float)currentPosition)/((float)player.getDuration()) * 100);
		jsCallBack(success, act[7], String.valueOf(percent));
    }
    
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
      if (!init) {
        init();
      }

      try {
        if (action.equals(act[0])) {
            this.play(args.getString(0));
        } else if (action.equals(act[1])) {
            this.pause();
        } else if (action.equals(act[2])) {
            this.stop();
        } else if (action.equals(act[3])) {
            this.getVolume();
        } else if (action.equals(act[4])) {             
            this.setVolume(args.getString(0));
        } else if (action.equals(act[5])) {
            this.getBufferProgress();
        } else if (action.equals(act[6])) {
            this.seek(args.getString(0));
        } else if (action.equals(act[7])) {
            this.getCurrentPosition();
        } else if (action.equals(act[8])) {
            this.resume();
        }
        
      } catch (JSONException e) {
        callbackContext.error("GAP not ready for this type of callback");
        return false;
      }

      callbackContext.success("GAP not ready for this type of callback");
      return true;
    }

	public boolean isSynch(String action) {
		return true;

	}
	
	public void onDestroy() {
		if (this != null) {
			this.release();
		}
		super.onDestroy();
	}
	
	private class phoneStates extends PhoneStateListener{
		
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {

			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:
				if ( isStart && paused && phoneCall) {
					resume();
					phoneCall=false;
				}
				Log.d("audio", "Media Player state Idle:"+paused+isStart);
				Log.d("audio", "TelephonyManager state: Idle");
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				if (isStart && !paused ) {
					pause();
					phoneCall=true;
				}
				Log.d("audio", "Media Player state Off Hook:"+paused+isStart);
				Log.d("audio", "TelephonyManager state: Off Hook");
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				if (isStart && !paused) {
					pause();
					phoneCall=true;
				}
				Log.d("audio", "Media Player state Ringing:"+paused+isStart);
				Log.d("audio", "TelephonyManager state: Ringing");
				break;
			}
		}
		
		
	}


}
