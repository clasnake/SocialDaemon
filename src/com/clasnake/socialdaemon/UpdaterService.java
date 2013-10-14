package com.clasnake.socialdaemon;

import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class UpdaterService extends Service{

	static final String TAG = "UpdaterService";
	static final int DELAY = 60000;
	private boolean runFlag = false;
	private Updater updater;
	private DaemonApplication daemon;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		this.daemon = (DaemonApplication) getApplication();
		this.updater = new Updater();
		Log.d(TAG, "onCreated");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		this.runFlag = false;
		this.updater.interrupt();
		this.updater = null;
		this.daemon.setServiceRunning(false);
		Log.d(TAG, "onDestroyed");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		super.onStartCommand(intent, flags, startId);
		this.runFlag = true;
		this.updater.start();
		this.daemon.setServiceRunning(true);
		Log.d(TAG, "onStarted");
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private class Updater extends Thread{
		List<Twitter.Status> timeline;
		public Updater(){
			super("UpdaterService-Updater");
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			UpdaterService updaterService = UpdaterService.this;
			while(updaterService.runFlag){
				Log.d(TAG, "Updater running");
				try{
					try{
						timeline = daemon.getTwitter().getFriendsTimeline();
					}catch(TwitterException e){
						Log.e(TAG, "Failed to connect to twitter service");
					}
					for(Twitter.Status status : timeline){
						Log.d(TAG, String.format("%s: %s", status.user.name, status.text));
					}
					Log.d(TAG, "Updater ran");
					Thread.sleep(DELAY);
				}catch(InterruptedException e){
					updaterService.runFlag = false;
				}
			}
		}
		
	}

}
