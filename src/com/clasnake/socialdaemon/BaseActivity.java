package com.clasnake.socialdaemon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class BaseActivity extends Activity{
	DaemonApplication daemon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		daemon = (DaemonApplication) getApplication();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.menu1, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case R.id.itemPrefs:
			startActivity(new Intent(this, PrefsActivity.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
			break;
		case R.id.itemToggleService:
			if(daemon.isServiceRunning()){
				stopService(new Intent(this, UpdaterService.class));
			}
			else{
				startService(new Intent(this, UpdaterService.class));
			}
			break;
		case R.id.itemPurge:
//			((DaemonApplication) getApplication()).getStatusData().delete();
			Toast.makeText(this, "Data purged", Toast.LENGTH_LONG).show();
			break;
		case R.id.itemTimeline:
			startActivity(new Intent(this, TimelineActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
			break;
		case R.id.itemStatus:
			startActivity(new Intent(this, StatusActivity.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
			break;
		}
		return true;
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		// TODO Auto-generated method stub
		MenuItem toggleItem = menu.findItem(R.id.itemToggleService);
		if(daemon.isServiceRunning()){
			toggleItem.setTitle(R.string.title_service_stop);
		}
		else{
			toggleItem.setTitle(R.string.title_service_start);
		}
		return true;
	}
}
