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
  private static int MINIMAL_SIZE_FOR_PHONE_NUMBER = 3;
  public final String number;
  private String _name = "";
  public final Date date;
  public final String numberType;
  
  public CallType(final String num, final String newName, final Date d, final String numType) {
    number = num;
    _name = newName;
    date = d;
    numberType = numType;
  }
  
  /**
   * constructor with Cursor
   * 
   * @param cur
   * @param c
   */
  public CallType(final Context c, final Cursor cur) {
    Date d;
    try {
      d = new Date(DateFormat.format("dd/MM/yy hh:mm", cur.getLong(cur.getColumnIndexOrThrow(android.provider.CallLog.Calls.DATE)))
          .toString());
    } catch (final Exception e1) {
      d = new Date();
      e1.printStackTrace();
    }
    date = d;
    String num = "";
    try {
      num = cur.getString(cur.getColumnIndexOrThrow(android.provider.CallLog.Calls.NUMBER));
      if (num.length() < MINIMAL_SIZE_FOR_PHONE_NUMBER)
        num = _name = c.getString(com.yp2012g4.vision.R.string.incoming_call_from_private_number);
      else
        _name = cur.getString(cur.getColumnIndexOrThrow(android.provider.CallLog.Calls.CACHED_NAME));
      if (_name == null)
        _name = " ";
    } catch (final Exception e) {
      _name = " ";
    }
    number = num;
    numberType = getValueFromCursor(cur, android.provider.CallLog.Calls.CACHED_NUMBER_TYPE);
  }
  
  static String getValueFromCursor(final Cursor cur, final String colValue) {
    try {
      return cur.getString(cur.getColumnIndexOrThrow(colValue));
    } catch (final Exception e) {
      return "";
    }
  }
  
  public synchronized String getName() {
    return _name;
  }
}
