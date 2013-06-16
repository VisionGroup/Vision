package com.yp2012g4.vision.managers;

import android.content.Context;
import android.database.Cursor;
import android.text.format.DateFormat;

/**
 * SMS type container
 * 
 * @author Roman
 * 
 */
public class SmsType {
  public final String address;
  public final String person;
  public final String date;
  public final String body;
  
  /**
   * constructor with Cursor
   * 
   * @param cur
   * @param c
   */
  public SmsType(final Cursor cur, final Context c) {
    address = cur.getString(cur.getColumnIndexOrThrow("address"));
    final long miliseconds = Long.parseLong(cur.getString(cur.getColumnIndexOrThrow("date")));
    date = DateFormat.format("dd/MM/yy", miliseconds).toString();
    body = cur.getString(cur.getColumnIndexOrThrow("body"));
    person = new ContactManager(c).getNameFromPhone(address);
  }
}
