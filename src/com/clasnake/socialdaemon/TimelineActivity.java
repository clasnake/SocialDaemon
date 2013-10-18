package com.clasnake.socialdaemon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

public class TimelineActivity extends BaseActivity{

	static final String SEND_TIMELINE_NOTIFICATIONS = "com.clasnake.socialdaemon.SEND_TIMELINE_NOTIFICATIONS";
	Cursor cursor;
	StatusData statusData;
	DaemonApplication daemon;
	ListView listTimeline;
	TimelineAdapter adapter;
	TimelineReceiver receiver;
	IntentFilter filter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.timeline);
		daemon = (DaemonApplication)getApplication();
		Log.d("asd", daemon.getPrefs().getString("username", null));
		if(daemon.getPrefs().getString("username", null) == null){
			startActivity(new Intent(this, PrefsActivity.class));
			Toast.makeText(this, "Setup Prefs", Toast.LENGTH_LONG).show();
		}
		listTimeline = (ListView)findViewById(R.id.listTimeline);
		statusData = new StatusData(this);
		filter = new IntentFilter(UpdaterService.NEW_STATUS_INTENT);
		receiver = new TimelineReceiver();
	}


	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		cursor = statusData.getStatusUpdates();
		startManagingCursor(cursor);
		adapter = new TimelineAdapter(this, cursor);
		Log.d("asd", adapter.toString());
		listTimeline.setAdapter(adapter);

		registerReceiver(receiver, filter, SEND_TIMELINE_NOTIFICATIONS, null);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		try{
			unregisterReceiver(receiver);
		}catch(Exception e){
			Log.d("ReceiverError", e+"");
		}
	}

	class TimelineReceiver extends BroadcastReceiver{

		@SuppressWarnings("deprecation")
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			cursor.requery();
			adapter.notifyDataSetChanged();
			Log.d("TimelineReceiver", "OnReceived");
		}
		
	}

}
