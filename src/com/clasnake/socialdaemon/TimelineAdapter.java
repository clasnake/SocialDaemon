package com.clasnake.socialdaemon;

import android.content.Context;
import android.database.Cursor;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class TimelineAdapter extends SimpleCursorAdapter{

	static final String[] FROM = {StatusData.C_CREATED_AT, StatusData.C_USER, StatusData.C_TEXT};
	static final int[] TO = {R.id.textCreatedAt, R.id.textUser, R.id.textText};
	
	@SuppressWarnings("deprecation")
	public TimelineAdapter(Context context, Cursor cursor) {
		super(context, R.layout.row, cursor, FROM, TO);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		super.bindView(view, context, cursor);
		Long timestamp = cursor.getLong(cursor.getColumnIndex(StatusData.C_CREATED_AT));
		TextView textCreatedAt = (TextView)view.findViewById(R.id.textCreatedAt);
		textCreatedAt.setText(DateUtils.getRelativeTimeSpanString(timestamp));
	}

}
