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
  private final String _address;
  private final String _person;
  private final String _date;
  private final String _protocol = "";
  private final String _read;
  private final String _status;
  private final String _type;
  private final String _subject = "";
  private final String _body;
  
  /**
   * constructor with Cursor
   * 
   * @param cur
   * @param c
   */
  public SmsType(final Cursor cur, final Context c) {
    _address = cur.getString(cur.getColumnIndexOrThrow("address"));
    final String ds = cur.getString(cur.getColumnIndexOrThrow("date"));
    final long m = Long.parseLong(ds);
    _date = (String) DateFormat.format("dd/MM/yy", m);
    _read = cur.getString(cur.getColumnIndexOrThrow("read"));
    _status = cur.getString(cur.getColumnIndexOrThrow("status"));
    _type = cur.getString(cur.getColumnIndexOrThrow("type"));
    _body = cur.getString(cur.getColumnIndexOrThrow("body"));
    final ContactManager cm = new ContactManager(c);
    _person = cm.getNameFromPhone(_address);
  }
  
  public synchronized String getAddress() {
    return _address;
  }
  
  public synchronized String getPerson() {
    return _person;
  }
  
  public synchronized String getDate() {
    return _date;
  }
  
  public synchronized String getProtocol() {
    return _protocol;
  }
  
  public synchronized String getRead() {
    return _read;
  }
  
  public synchronized String getStatus() {
    return _status;
  }
  
  public synchronized String getType() {
    return _type;
  }
  
  public synchronized String getSubject() {
    return _subject;
  }
  
  public synchronized String getBody() {
    return _body;
  }
}
