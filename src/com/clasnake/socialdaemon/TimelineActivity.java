package com.clasnake.socialdaemon;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class TimelineActivity extends Activity{

	TextView textTimeline;
	Cursor cursor;
	StatusData statusData;
	DaemonApplication daemon;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.timeline_basic);
		daemon = (DaemonApplication)getApplication();
		Log.d("asd", daemon.getPrefs().getString("username", null));
		if(daemon.getPrefs().getString("username", null) == null){
			startActivity(new Intent(this, PrefsActivity.class));
			Toast.makeText(this, "Setup Prefs", Toast.LENGTH_LONG).show();
		}
		textTimeline = (TextView)findViewById(R.id.textTimeline);
		statusData = new StatusData(this);
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		cursor = statusData.getStatusUpdates();
		startManagingCursor(cursor);
		String user, text, output;
		while(cursor.moveToNext()){
			user = cursor.getString(cursor.getColumnIndex(StatusData.C_USER));
			text = cursor.getString(cursor.getColumnIndex(StatusData.C_TEXT));
			output = String.format("%s: %s\n", user, text);
			textTimeline.append(output);
		}
	}

}
