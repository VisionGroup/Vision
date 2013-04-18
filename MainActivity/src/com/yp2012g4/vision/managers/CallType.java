package com.yp2012g4.vision.managers;

import android.content.Context;
import android.database.Cursor;
import android.text.format.DateFormat;

/**
 * Call type container
 * 
 * @author Yaron
 * 
 */
public class CallType {

	private String number = "";
	private String type = "";
	private String name = "";
	private String date = "";
	private String numberType = "";

	/**
	 * constructor with Cursor
	 * 
	 * @param cur
	 * @param context
	 */
	public CallType(Cursor cur, Context context) {
		number = cur
				.getString(
						cur.getColumnIndexOrThrow(android.provider.CallLog.Calls.NUMBER))
				.toString();
		String mili = cur.getString(
				cur.getColumnIndexOrThrow(android.provider.CallLog.Calls.DATE))
				.toString();
		Long m = Long.valueOf(mili);
		date = (String) DateFormat.format("dd/MM/yy", m.longValue());
		// protocol =
		// cur.getString(cur.getColumnIndexOrThrow("protocol")).toString(); can
		// cause exception
		try {
			name = cur
					.getString(
							cur.getColumnIndexOrThrow(android.provider.CallLog.Calls.CACHED_NAME))
					.toString();
		} catch (Exception e) {
			name = "";
		}
		try {
			numberType = cur
					.getString(
							cur.getColumnIndexOrThrow(android.provider.CallLog.Calls.CACHED_NUMBER_TYPE))
					.toString();
		} catch (Exception e) {
			numberType = "";
		}
		try {
			type = cur.getString(
					cur.getColumnIndexOrThrow(android.provider.CallLog.Calls.TYPE))
					.toString();
		} catch (Exception e) {
			type ="";
		}
		
		// subject = c.getString(c.getColumnIndexOrThrow("subject")).toString();
		// ContactManager cm = new ContactManager(context);

	}

	public synchronized String getNumber() {
		return number;
	}

	public synchronized String getNumberType() {
		return numberType;
	}

	public synchronized String getDate() {
		return date;
	}

	public synchronized String getType() {
		return type;
	}

	public synchronized String getName() {
		return name;
	}

}