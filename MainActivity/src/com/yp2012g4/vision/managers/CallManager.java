package com.yp2012g4.vision.managers;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;

/**
 * Call type container
 * 
 * @author Yaron
 * 
 */

// public class CallData {
// String name;
// String number;
// long date;
//
// public CallData(String name, String number, long date) {
// this.name = name;
// this.number = number;
// this.date = date;
// }
//
// /**
// * Get the formated hour of the call in a string
// *
// * @return the call hour string
// */
// public String getHour() {
// Calendar calendar = Calendar.getInstance();
// calendar.setTimeInMillis(date);
// return SpeakingClockActivity.parseTime(calendar);
// }
// }

public class CallManager {



	public static ArrayList<CallType> dood(Context c) {
		String[] strFields = { CallLog.Calls.NUMBER, CallLog.Calls.TYPE,
				CallLog.Calls.CACHED_NAME, CallLog.Calls.CACHED_NUMBER_TYPE,
				CallLog.Calls.DATE };
		String strOrder = android.provider.CallLog.Calls.DATE + " DESC";

		Cursor mCallCursor = c.getContentResolver().query(
				android.provider.CallLog.Calls.CONTENT_URI, strFields, null,
				null, strOrder);
		ArrayList<CallType> list = new ArrayList<CallType>();
		if (mCallCursor.moveToFirst()) {

			// loop through cursor
			do {
				list.add(new CallType(mCallCursor, c));
			} while (mCallCursor.moveToNext());

		}
		return list;
	}

	/**
	 * Get a list of all the missed calls with the date, name and phone number
	 * of each
	 * 
	 * @return list of missed calls
	 */
	public static ArrayList<CallType> getMissedCallsList(Context c) {
		String[] projection = { CallLog.Calls.CACHED_NAME,
				CallLog.Calls.NUMBER, CallLog.Calls.DATE };
		String where = CallLog.Calls.TYPE + "=" + CallLog.Calls.MISSED_TYPE
				+ " AND NEW = 1";
		Cursor cur;
		try {
			cur = c.getContentResolver().query(CallLog.Calls.CONTENT_URI,
					projection, where, null, null);
		} catch (Exception e) {
			return new ArrayList<CallType>();
		}
		ArrayList<CallType> al = new ArrayList<CallType>();
		while (cur.moveToNext()) {
			al.add(new CallType(cur, c));
		}
		return al;
	}

	/**
	 * Delete all the entries of a number from the missed call list - require
	 * WRITE_CONTACTS permission
	 * 
	 * @param c
	 * @param number
	 *            - string of the phone number in the call log
	 */
	static public void DeleteCallLogByNumber(Context c, String number) {
		try {
			c.getContentResolver().delete(CallLog.Calls.CONTENT_URI,
					CallLog.Calls.NUMBER + "=?", new String[] { number });
		} catch (Exception e) {
			e.getMessage();
		}
	}

	/**
	 * Get the number of missed calls
	 * 
	 * @return the missed calls number
	 */
	public static int getMissedCallsNum(Context c) {
		String where = CallLog.Calls.TYPE + "=" + CallLog.Calls.MISSED_TYPE
				+ " AND NEW = 1";
		Cursor cur;
		try {
			cur = c.getContentResolver().query(CallLog.Calls.CONTENT_URI, null,
					where, null, null);
		} catch (Exception e) {
			return 0;
		}
		return cur.getCount();
	}
	
	
}
