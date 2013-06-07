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
  private String _number = "";
  private String _type = "";
  private String _name = "";
  private Date _date;
  private String _numberType = "";
  
  public CallType(final String number, final String type, final String name, final Date date, final String numberType) {
    _number = number;
    _type = type;
    _name = name;
    _date = date;
    _numberType = numberType;
  }
  
  /**
   * constructor with Cursor
   * 
   * @param cur
   * @param c
   */
  public CallType(final Context c, final Cursor cur) {
    try {
      _date = new Date((String) DateFormat.format("dd/MM/yy hh:mm",
          cur.getLong(cur.getColumnIndexOrThrow(android.provider.CallLog.Calls.DATE))));
    } catch (final Exception e1) {
      _date = new Date();
      e1.printStackTrace();
    }
    try {
      _number = cur.getString(cur.getColumnIndexOrThrow(android.provider.CallLog.Calls.NUMBER));
      if (_number.length() < MINIMAL_SIZE_FOR_PHONE_NUMBER)
        _number = _name = c.getString(com.yp2012g4.vision.R.string.incoming_call_from_private_number);
      else
        _name = cur.getString(cur.getColumnIndexOrThrow(android.provider.CallLog.Calls.CACHED_NAME));
    } catch (final Exception e) {
      _name = "";
    }
    try {
      _numberType = cur.getString(cur.getColumnIndexOrThrow(android.provider.CallLog.Calls.CACHED_NUMBER_TYPE));
    } catch (final Exception e) {
      _numberType = "";
    }
    try {
      _type = cur.getString(cur.getColumnIndexOrThrow(android.provider.CallLog.Calls.TYPE));
    } catch (final Exception e) {
      _type = "";
    }
  }
  
  public synchronized String getNumber() {
    return _number;
  }
  
  public synchronized String getNumberType() {
    return _numberType;
  }
  
  public synchronized Date getDate() {
    return _date;
  }
  
  public synchronized String getType() {
    return _type;
  }
  
  public synchronized String getName() {
    return _name;
  }
}
