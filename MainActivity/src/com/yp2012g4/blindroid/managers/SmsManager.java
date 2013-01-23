package com.yp2012g4.blindroid.managers;

import java.util.ArrayList;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class SmsManager {

	Context c;

	public SmsManager(Context c) {
		this.c = c;
	}

	public ArrayList<SmsType> getIncomingMessages() {

		ArrayList<SmsType> slist = new ArrayList<SmsType>();
		Uri uri = Uri.parse("content://sms/inbox");
		Cursor cur = c.getContentResolver().query(uri, null, null, null, null);

		if (cur.moveToFirst()) {
			do {

				try {
					slist.add(new SmsType(cur,c));
				} catch (IllegalArgumentException e) {
					// Ignore and go next
				}
			} while (cur.moveToNext());

		}

		return slist;

	}
}
