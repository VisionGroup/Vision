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
  private String _mNumber = "";
  private String _mType = "";
  private String _mName = "";
  private final Date _mDate;
  private String _mNumberType = "";
  
  public CallType(String number, String type, String name, Date date, String numberType) {
    _mNumber = number;
    _mType = type;
    _mName = name;
    _mDate = date;
    _mNumberType = numberType;
  }
  
  /**
   * constructor with Cursor
   * 
   * @param cur
   * @param c
   */
  public CallType(Cursor cur) {
    _mNumber = cur.getString(cur.getColumnIndexOrThrow(android.provider.CallLog.Calls.NUMBER));
    String mili = cur.getString(cur.getColumnIndexOrThrow(android.provider.CallLog.Calls.DATE));
    long m = Long.parseLong(mili);
    _mDate = new Date((String) DateFormat.format("dd/MM/yy hh:mm:ss", m));
    try {
      _mName = cur.getString(cur.getColumnIndexOrThrow(android.provider.CallLog.Calls.CACHED_NAME));
    } catch (Exception e) {
      _mName = "";
    }
    try {
      _mNumberType = cur.getString(cur.getColumnIndexOrThrow(android.provider.CallLog.Calls.CACHED_NUMBER_TYPE));
    } catch (Exception e) {
      _mNumberType = "";
    }
    try {
      _mType = cur.getString(cur.getColumnIndexOrThrow(android.provider.CallLog.Calls.TYPE));
    } catch (Exception e) {
      _mType = "";
    }
  }
  
  public synchronized String getNumber() {
    return _mNumber;
  }
  
  public synchronized String getNumberType() {
    return _mNumberType;
  }
  
  public synchronized Date getDate() {
    return _mDate;
  }
  
  public synchronized String getType() {
    return _mType;
  }
  
  public synchronized String getName() {
    return _mName;
  }
}
