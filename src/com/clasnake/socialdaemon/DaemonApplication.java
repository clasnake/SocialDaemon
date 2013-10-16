package com.clasnake.socialdaemon;

import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.Twitter.Status;
import android.app.Application;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

public class DaemonApplication extends Application implements OnSharedPreferenceChangeListener{
	private static final String TAG = DaemonApplication.class.getSimpleName();
	public Twitter twitter;
	private SharedPreferences prefs;
	private boolean serviceRunning;
	private StatusData statusData;
	
	public SharedPreferences getPrefs() {
		return prefs;
	}

	public void setPrefs(SharedPreferences prefs) {
		this.prefs = prefs;
	}

	public void setStatusData(StatusData statusData) {
		this.statusData = statusData;
	}

	@Override
	public void onCreate(){
		super.onCreate();
		this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
		this.prefs.registerOnSharedPreferenceChangeListener(this);
		this.statusData = new StatusData(this);
		Log.i(TAG, "onCreated");
	}
	
	@Override
	public void onTerminate(){
		super.onTerminate();
		Log.i(TAG, "onTerminated");
	}
	
	public synchronized Twitter getTwitter(){
		if(this.twitter == null){
			String username, password, apiRoot;
			username = prefs.getString("username", "");
			password = prefs.getString("password", "");
			apiRoot = prefs.getString("urlroot", "http://yamba.marakana.com/api");
			
			if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(apiRoot)){
				this.twitter = new Twitter(username, password);
				this.twitter.setAPIRootUrl(apiRoot);
			}
			
		}
		return this.twitter;
		
	}
	
	@Override
	public synchronized void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
		this.twitter = null;
		
	}
	
	public boolean isServiceRunning(){
		return serviceRunning;
	}
	
	public void setServiceRunning(boolean serviceRunning){
		this.serviceRunning = serviceRunning;
	}
	
	public StatusData getStatusData(){
		return statusData;
	}
	
	public synchronized int fetchStatusUpdates() { 
	    Log.d(TAG, "Fetching status updates");
	    Twitter twitter = this.getTwitter();
	    if (twitter == null) {
	      Log.d(TAG, "Twitter connection info not initialized");
	      return 0;
	    }
	    try {
	      List<Status> statusUpdates = twitter.getFriendsTimeline();
	      Log.d(TAG, "0");
	      long latestStatusCreatedAtTime;
	      Log.d(TAG, "1");
	      Log.d(TAG, this.getStatusData().toString());
	      latestStatusCreatedAtTime = this.getStatusData().getLatestStatusCreatedAtTime();
	      Log.d(TAG, "2");
	      int count = 0;
	      ContentValues values = new ContentValues();
	      for (Status status : statusUpdates) {
	        values.put(StatusData.C_ID, status.getId());
	        long createdAt = status.getCreatedAt().getTime();
	        values.put(StatusData.C_CREATED_AT, createdAt);
	        values.put(StatusData.C_TEXT, status.getText());
	        values.put(StatusData.C_USER, status.getUser().getName());
	        Log.d(TAG, "Got update with id " + status.getId() + ". Saving");
	        this.getStatusData().insertOrIgnore(values);
	        if (latestStatusCreatedAtTime < createdAt) {
	          count++;
	        }
	      }
	      Log.d(TAG, count > 0 ? "Got " + count + " status updates"
	          : "No new status updates");
	      return count;
	    } catch (RuntimeException e) {
	      Log.e(TAG, "Failed to fetch status updates", e);
	      return 0;
	    }
	  }

}
