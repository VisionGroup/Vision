package com.yp2012g4.vision.managers;

import java.util.Date;

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
	private Date date;
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
		Date q;
		try {
				q =new Date(cur.getLong(4));
				
		} catch (Exception e) {
			// TODO: handle exception
		}

		Long m = Long.valueOf(mili);
		
		date = new Date((String) DateFormat.format("dd/MM/yy hh:mm:ss", m.longValue()));
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
			type = cur
					.getString(
							cur.getColumnIndexOrThrow(android.provider.CallLog.Calls.TYPE))
					.toString();
		} catch (Exception e) {
			type = "";
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

	public synchronized Date getDate() {
		return date;
	}

	public synchronized String getType() {
		return type;
	}

	public synchronized String getName() {
		return name;
	}

}
