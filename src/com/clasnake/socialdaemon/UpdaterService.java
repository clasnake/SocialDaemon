package com.clasnake.socialdaemon;

import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;

public class UpdaterService extends Service{

	public static final String NEW_STATUS_INTENT = "com.clasnake.socialdaemon.NEW_STATUS";
	public static final String NEW_STATUS_EXTRA_COUNT = "NEW_STATUS_EXTRA_COUNT";
	static final String TAG = "UpdaterService";
	static final int DELAY = 60000;
	private boolean runFlag = false;
	private Updater updater;
	private DaemonApplication daemon;
	SQLiteDatabase db;

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
		static final String RECEIVE_TIMELINE_NOTIFICATIONS = "com.clasnake.socialdaemon.RECEIVE_TIMELINE_NOTIFICATIONS";
		Intent intent;

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
//					DaemonApplication daemon = (DaemonApplication)updaterService.getApplication();
					int newUpdates = UpdaterService.this.daemon.fetchStatusUpdates();
					if(newUpdates > 0){
						Log.d(TAG, "We have a new status");
						intent = new Intent(NEW_STATUS_INTENT);
						intent.putExtra(NEW_STATUS_EXTRA_COUNT, newUpdates);
						updaterService.sendBroadcast(intent, RECEIVE_TIMELINE_NOTIFICATIONS);
						Log.d("TimelineReceiver", "onSent");
					}
					Thread.sleep(DELAY);
				}catch(InterruptedException e){
					updaterService.runFlag = false;
				}
			}
		}
		
	}

}
