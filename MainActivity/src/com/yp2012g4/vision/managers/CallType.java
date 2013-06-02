package com.yp2012g4.vision.managers;

import java.util.Date;

import android.database.Cursor;
import android.text.format.DateFormat;

/**
 * Call type container
 * 
 * @author Yaron
 * 
 */
public class CallType {
  private String _number = "";
  private String _type = "";
  private String _name = "";
  private final Date _date;
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
  public CallType(final Cursor cur) {
    _number = cur.getString(cur.getColumnIndexOrThrow(android.provider.CallLog.Calls.NUMBER));
    final String mili = cur.getString(cur.getColumnIndexOrThrow(android.provider.CallLog.Calls.DATE));
    final long m = Long.parseLong(mili);
    _date = new Date((String) DateFormat.format("dd/MM/yy hh:mm:ss", m));
    try {
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
