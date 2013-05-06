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
  private String address = "";
  private String person = "";
  private String date = "";
  private final String protocol = "";
  private String read = "";
  private String status = "";
  private String type = "";
  private final String subject = "";
  private String body = "";
  
  /**
   * constructor with Cursor
   * 
   * @param cur
   * @param context
   */
  public SmsType(Cursor cur, Context context) {
    address = cur.getString(cur.getColumnIndexOrThrow("address"));
    String mili = cur.getString(cur.getColumnIndexOrThrow("date"));
    long m = Long.parseLong(mili);
    date = (String) DateFormat.format("dd/MM/yy", m);
    // protocol =
    // cur.getString(cur.getColumnIndexOrThrow("protocol")); can
    // cause exception
    read = cur.getString(cur.getColumnIndexOrThrow("read"));
    status = cur.getString(cur.getColumnIndexOrThrow("status"));
    type = cur.getString(cur.getColumnIndexOrThrow("type"));
    // subject = c.getString(c.getColumnIndexOrThrow("subject"));
    body = cur.getString(cur.getColumnIndexOrThrow("body"));
    ContactManager cm = new ContactManager(context);
    person = cm.getNameFromPhone(address);
  }
  
  public synchronized String getAddress() {
    return address;
  }
  
  public synchronized String getPerson() {
    return person;
  }
  
  public synchronized String getDate() {
    return date;
  }
  
  public synchronized String getProtocol() {
    return protocol;
  }
  
  public synchronized String getRead() {
    return read;
  }
  
  public synchronized String getStatus() {
    return status;
  }
  
  public synchronized String getType() {
    return type;
  }
  
  public synchronized String getSubject() {
    return subject;
  }
  
  public synchronized String getBody() {
    return body;
  }
}
