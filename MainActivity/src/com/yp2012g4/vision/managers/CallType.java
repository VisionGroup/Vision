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
  private String mNumber = "";
  private String mType = "";
  private String mName = "";
  private final Date mDate;
  private String mNumberType = "";
  
  public CallType(String number, String type, String name, Date date, String numberType) {
    mNumber = number;
    mType = type;
    mName = name;
    mDate = date;
    mNumberType = numberType;
  }
  
  /**
   * constructor with Cursor
   * 
   * @param cur
   * @param context
   */
  public CallType(Cursor cur, Context context) {
    mNumber = cur.getString(cur.getColumnIndexOrThrow(android.provider.CallLog.Calls.NUMBER)).toString();
    String mili = cur.getString(cur.getColumnIndexOrThrow(android.provider.CallLog.Calls.DATE)).toString();
    Long m = Long.valueOf(mili);
    mDate = new Date((String) DateFormat.format("dd/MM/yy hh:mm:ss", m.longValue()));
    try {
      mName = cur.getString(cur.getColumnIndexOrThrow(android.provider.CallLog.Calls.CACHED_NAME)).toString();
    } catch (Exception e) {
      mName = "";
    }
    try {
      mNumberType = cur.getString(cur.getColumnIndexOrThrow(android.provider.CallLog.Calls.CACHED_NUMBER_TYPE)).toString();
    } catch (Exception e) {
      mNumberType = "";
    }
    try {
      mType = cur.getString(cur.getColumnIndexOrThrow(android.provider.CallLog.Calls.TYPE)).toString();
    } catch (Exception e) {
      mType = "";
    }
  }
  
  public synchronized String getNumber() {
    return mNumber;
  }
  
  public synchronized String getNumberType() {
    return mNumberType;
  }
  
  public synchronized Date getDate() {
    return mDate;
  }
  
  public synchronized String getType() {
    return mType;
  }
  
  public synchronized String getName() {
    return mName;
  }
}
