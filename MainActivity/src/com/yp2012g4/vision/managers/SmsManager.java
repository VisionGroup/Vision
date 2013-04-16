package com.yp2012g4.vision.managers;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class SmsManager {
//	Context c;
//
//	public SmsManager(Context c) {
//		this.c = c;
//	}

	public static ArrayList<SmsType> getIncomingMessages(Context c) {
		ArrayList<SmsType> slist = new ArrayList<SmsType>();
		Uri uri = Uri.parse("content://sms/inbox");
		Cursor cur = c.getContentResolver().query(uri, null, null, null, null);
		if (cur.moveToFirst())
			do
				try {
					slist.add(new SmsType(cur, c));
				} catch (IllegalArgumentException e) {
					// Ignore and go next
				}
			while (cur.moveToNext());
		return slist;
	}

	static public void markMessageRead(Context context, String number,
			String body) {
		Uri uri = Uri.parse("content://sms/inbox");
		Cursor cursor = context.getContentResolver().query(uri, null, null,
				null, null);
		try {
			while (cursor.moveToNext())
				if (cursor.getString(cursor.getColumnIndex("address")).equals(
						number)
						&& cursor.getInt(cursor.getColumnIndex("read")) == 0)
					if (cursor.getString(cursor.getColumnIndex("body"))
							.startsWith(body)) {
						String SmsMessageId = cursor.getString(cursor
								.getColumnIndex("_id"));
						ContentValues values = new ContentValues();
						values.put("read", Boolean.TRUE);
						context.getContentResolver().update(
								Uri.parse("content://sms/inbox"), values,
								"_id=" + SmsMessageId, null);
						return;
					}
		} catch (Exception e) {
			Log.e("Mark Read", "Error in Read: " + e.toString());
		}
	}

	/**
	 * Get the number of SMS messages the user didn't read yet
	 * 
	 * @return the unread SMS number
	 */
	public static int getUnreadSMS(Context c) {
		final Uri SMS_INBOX = Uri.parse("content://sms/inbox");
		Cursor cur = c.getContentResolver().query(SMS_INBOX, null, "read = 0",
				null, null);
		return cur.getCount();
	}

}
