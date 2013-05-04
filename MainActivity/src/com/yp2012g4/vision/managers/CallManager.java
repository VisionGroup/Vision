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

public class CallManager {

	private Cursor cur = null;

	/**
	 * Get a list of all the missed calls with the date, name and phone number
	 * of each
	 * 
	 * @return list of missed calls
	 */
	public static ArrayList<CallType> getMissedCallsList(Context c) {
		final String[] projection = { CallLog.Calls.CACHED_NAME,
				CallLog.Calls.NUMBER, CallLog.Calls.DATE };
		final String where = CallLog.Calls.TYPE + "="
				+ CallLog.Calls.MISSED_TYPE + " AND NEW = 1";
		Cursor cur;
		try {
			cur = c.getContentResolver().query(CallLog.Calls.CONTENT_URI,
					projection, where, null, null);
		} catch (final Exception e) {
			return new ArrayList<CallType>();
		}
		final ArrayList<CallType> al = new ArrayList<CallType>();
		while (cur.moveToNext())
			al.add(new CallType(cur, c));
		return al;
	}

	/**
	 * Delete all the entries of a number from the missed call list - require
	 * WRITE_CONTACTS permission
	 * 
	 * @param c
	 * @param number - string of the phone number in the call log
	 */
	static public void DeleteCallLogByNumber(Context c, String number) {
		try {
			c.getContentResolver().delete(CallLog.Calls.CONTENT_URI,
					CallLog.Calls.NUMBER + "=?", new String[] { number });
		} catch (final Exception e) {
			e.getMessage();
		}
	}

	/**
	 * Get a list of all the missed calls with the date, name and phone number
	 * of each
	 * 
	 * @param c
	 *            Context
	 * @param number
	 *            how may call to get back
	 * @return list of missed calls
	 */
	public CallType getLastOutgoingCall(Context c) {
		String[] projection = { CallLog.Calls.CACHED_NAME,
				CallLog.Calls.NUMBER, CallLog.Calls.DATE };

		try {
			cur = c.getContentResolver().query(CallLog.Calls.CONTENT_URI,
					projection, null, null, null);
		} catch (Exception e) {
			return null;
		}

		if (cur.moveToNext())
			return new CallType(cur, c);
		else
			return null;
	}

	/**
	 * Get a list of all the missed calls with the date, name and phone number
	 * of each
	 * 
	 * @param c Context
	 * @param number how may call to get back
	 * @return list of missed calls
	 */
	public ArrayList<CallType> getNextMissedCallsList(Context c, int number) {
		String[] projection = { CallLog.Calls.CACHED_NAME,
				CallLog.Calls.NUMBER, CallLog.Calls.DATE };
		String where = CallLog.Calls.TYPE + "=" + CallLog.Calls.MISSED_TYPE
				+ " AND NEW = 1";

		try {
			cur = c.getContentResolver().query(CallLog.Calls.CONTENT_URI,
					projection, where, null, null);
		} catch (Exception e) {
			return new ArrayList<CallType>();
		}
		ArrayList<CallType> al = new ArrayList<CallType>();
		for (int i = 0; (i < number) && (cur.moveToNext()); i++) {
			al.add(new CallType(cur, c));
		}
		return al;
	}

	/**
	 * Get the number of missed calls
	 * 
	 * @return the missed calls number
	 */
	public static int getMissedCallsNum(Context c) {
		final String where = CallLog.Calls.TYPE + "="
				+ CallLog.Calls.MISSED_TYPE + " AND NEW = 1";
		Cursor cur;
		try {
			cur = c.getContentResolver().query(CallLog.Calls.CONTENT_URI, null,
					where, null, null);
		} catch (final Exception e) {
			return 0;

		}
		return cur.getCount();
	}


}
