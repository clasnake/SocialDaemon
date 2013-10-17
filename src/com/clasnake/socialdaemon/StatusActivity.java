package com.clasnake.socialdaemon;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class StatusActivity extends BaseActivity implements OnClickListener, TextWatcher{
	private static final String TAG = "StatusActivity";
	EditText editText;
	Button updateButton;
	Twitter twitter;
	TextView textCount;
	SharedPreferences prefs;
	
	class PostToTwitter extends AsyncTask<String, Integer, String>{
		@Override
		protected String doInBackground(String... statuses){
			try{
				DaemonApplication daemon = (DaemonApplication) getApplication();
				Twitter.Status status = daemon.getTwitter().updateStatus(statuses[0]);
				return status.text;
			}catch(TwitterException e){
				Log.e(TAG, e.toString());
				e.printStackTrace();
				return "Failed to post";
			}
		}
		
		@Override
		protected void onProgressUpdate(Integer... values){
			super.onProgressUpdate(values);
		}
		
		@Override
		protected void onPostExecute(String result){
			Toast.makeText(StatusActivity.this, result, Toast.LENGTH_LONG).show();
		}
	}
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        
        editText = (EditText)findViewById(R.id.editText);
        updateButton = (Button)findViewById(R.id.buttonUpdate);
        
        textCount = (TextView)findViewById(R.id.textCount);
        textCount.setText(Integer.toString(140));
        textCount.setTextColor(Color.GREEN);
        editText.addTextChangedListener(this);
        
        updateButton.setOnClickListener(this);
        
    }
	
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//    	MenuInflater inflater = getMenuInflater();
//    	inflater.inflate(R.menu.menu, menu);
//    	return true;
//    }
//    
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item){
//    	switch(item.getItemId()){
//    		case R.id.itemPrefs:
//    			startActivity(new Intent(this, PrefsActivity.class));
//    			break;
//    		case R.id.itemServiceStart:
//    			startService(new Intent(this, UpdaterService.class));
//    			break;
//    		case R.id.itemServiceStop:
//    			stopService(new Intent(this, UpdaterService.class));
//    			break;
//    	}
//    	return true;
//    }

	@Override
	public void onClick(View view) {
		String status = editText.getText().toString();
		new PostToTwitter().execute(status);
		Log.d(TAG, "onClicked");
	}

	@Override
	public void afterTextChanged(Editable statusText) {
		int count = 140 - statusText.length();
		textCount.setText(Integer.toString(count));
		textCount.setTextColor(Color.GREEN);
		if(count < 10){
			textCount.setTextColor(Color.YELLOW);
		}
		if(count < 0){
			textCount.setTextColor(Color.RED);
		}
	}


	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}
    
}
